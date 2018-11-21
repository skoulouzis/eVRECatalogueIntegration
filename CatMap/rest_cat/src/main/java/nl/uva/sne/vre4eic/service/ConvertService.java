/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.service;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import gr.forth.ics.isl.exporter.CatalogueExporter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import nl.uva.sne.vre4eic.model.ExportDocTask;
import nl.uva.sne.vre4eic.model.ProcessingStatus;
import nl.uva.sne.vre4eic.util.Util;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

@Service
public class ConvertService {

    @Autowired
    ThreadPoolTaskExecutor exec;

    @Autowired
    CachingConnectionFactory connectionFactory;

    @Autowired
    MetricsEndpoint endpoint;
//
    @Autowired
    MeterRegistry meterRegistry;

    Map<String, Future<String>> taskMap = new HashMap<>();

    public ProcessingStatus doProcess(String catalogueURL, String mappingURL, String generatorURL, int limit, String exportID) throws MalformedURLException, IOException, FileNotFoundException, InterruptedException, Exception {
        String taskID = catalogueURL + exportID;
        Future<String> convertTask = taskMap.get(taskID);

        if (convertTask == null) {
//            String path = new URL(mappingURL).getPath();
//            String queueName = path.substring(path.lastIndexOf('/') + 1);
            String queueName = "metadata_records";
            ExportDocTask task = new ExportDocTask(catalogueURL, connectionFactory.getRabbitConnectionFactory(), queueName, mappingURL, generatorURL, limit, exportID);
            convertTask = exec.submit(task);
            taskMap.put(taskID, convertTask);

            Timer timer = meterRegistry.timer("export.doc.task.start");
            timer.recordCallable(task);
        }
        ProcessingStatus process = new ProcessingStatus();
        process.setCatalogueURL(new URL(catalogueURL));
        if (convertTask.isDone()) {
            taskMap.remove(taskID);
            process.setStatus("FINISHED");
            return process;
        } else {
            process.setStatus("WORKING");
            return process;
        }
    }

    public Collection<String> listRecords(String catalogueURL, Integer limit) throws MalformedURLException, IOException, InterruptedException {
        ExportDocTask task = new ExportDocTask(catalogueURL, connectionFactory.getRabbitConnectionFactory(), null, null, null, limit, null);
        CatalogueExporter exp;
        exp = task.getExporter(catalogueURL);
        if (limit != null && limit > -1) {
            exp.setLimit(limit);
        }
        return exp.fetchAllDatasetUUIDs();
    }

    public List<DavResource> listResults(String webdavURL) throws IOException {
        Sardine sardine = SardineFactory.begin();
        return sardine.list(webdavURL);
    }

    public String zipRecords(String webDAVURL) throws IOException {
        try {
            Sardine sardine = SardineFactory.begin();

            List<DavResource> resources = sardine.list(webDAVURL);
            URL url = new URL(webDAVURL);
            String base = url.getProtocol() + "://" + url.getHost();
            if (url.getPort() > -1) {
                base += ":" + url.getPort();
            }

            File output = new File(System.getProperty("java.io.tmpdir") + File.separator + "records");
            if (output.exists()) {
                output.delete();
            }
            output.mkdirs();

            for (DavResource res : resources) {
                if (!res.isDirectory()) {
                    File file = downloadFile(res, sardine, base, output);
                }
            }

            zipFolder(Paths.get(output.getAbsolutePath()), Paths.get(output.getAbsolutePath() + ".zip"));
            return output.getAbsolutePath() + ".zip";
        } catch (Exception ex) {
            Logger.getLogger(ConvertService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void zipFolder(Path sourceFolderPath, Path zipPath) throws Exception {
        if (zipPath.toFile().exists()) {
            zipPath.toFile().delete();
        }
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                    zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));

                    Files.copy(file, zos);

                    zos.closeEntry();

                    return FileVisitResult.CONTINUE;

                }

            });
        }

    }

    private File downloadFile(DavResource resource, Sardine sardine, String webDAVURL, File output) {

        InputStream in = null;
        File file = null;
        String webdavFile = webDAVURL + "/" + resource.getPath();
        try {
            in = sardine.get(webdavFile);

            file = new File(output, resource.getName());
            try (OutputStream out = new FileOutputStream(file.getAbsoluteFile())) {
                IOUtils.copy(in, out);
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(ConvertService.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(ConvertService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ConvertService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return file;
    }

    public String getCatalogueType(String catalogueURL) throws MalformedURLException, InterruptedException {
        if (Util.isCKAN(catalogueURL)) {
            return "CKAN";
        }
        if (Util.isCSW(catalogueURL)) {
            return "CSW";
        }

        return null;
    }

    public void ingest(String webDAVURL, String ingestCatURL,String datasetName) throws IOException {
        Sardine sardine = SardineFactory.begin();

        List<DavResource> resources = sardine.list(webDAVURL);
        URL url = new URL(webDAVURL);
        String base = url.getProtocol() + "://" + url.getHost();
        if (url.getPort() > -1) {
            base += ":" + url.getPort();
        }
        
        for (DavResource res : resources) {
            if (!res.isDirectory() && res.getName().endsWith("ttl")) {
                try (InputStream ins = getWebDavInputStream(res, sardine, base)) {
                    uploadRDF(ins, ingestCatURL, datasetName);
                }
            }
        }
    }

    public void uploadRDF(InputStream rdfIns, String serviceURI, String datasetName)
            throws IOException {

        Model m = ModelFactory.createDefaultModel();

        m.read(rdfIns, null, "text/turtle");
        if (!serviceURI.endsWith("/")) {
            serviceURI += "/";
        }
        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI + datasetName + "/data");
        accessor.add(m);
    }

    private InputStream getWebDavInputStream(DavResource resource, Sardine sardine, String webDAVURL) throws IOException {
        String webdavFile = webDAVURL + "/" + resource.getPath();
        return sardine.get(webdavFile);
    }

    public Integer countRDFRecords(String catalogueURL, String datasetName) {
        String query = "SELECT (COUNT(*) AS ?count) WHERE { ?subject ?predicate ?object}";
        if (!catalogueURL.endsWith("/")) {
            catalogueURL += "/";
        }
        ResultSet rs = QueryExecutionFactory.sparqlService(catalogueURL + datasetName + "/query", query).execSelect();

        QuerySolution r = rs.next();
        Literal countLiteral = ((Literal) r.get("count"));
        return countLiteral.getInt();

    }

}
