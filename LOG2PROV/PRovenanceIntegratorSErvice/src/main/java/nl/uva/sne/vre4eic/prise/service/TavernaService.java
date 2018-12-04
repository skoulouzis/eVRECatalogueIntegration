/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import nl.uva.sne.vre4eic.prise.util.Util;
import nl.uva.sne.vre4eic.prise.util.WebDAVClient;
import org.apache.taverna.scufl2.api.io.ReaderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

/**
 *
 * @author S. Koulouzis
 */
@Service
public class TavernaService {

    @Value("${wf.reposetory.uri:=http://localhost}")
    private String wfRepoURI;

    private static final String URL_REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private final String[] NON_HOSTS = new String[]{"http://taverna.sf.net/2008/xml/t2flow", "http://schemas.opengis.net/gml/2.1.2/feature.xsd", "http://taverna.sf.net/2008/xml/t2flow"};
    private Set<String> serviceIds;

    Collection<String> getSysIDs(File t2File) throws ParserConfigurationException, SAXException, IOException {

        if (serviceIds == null) {
            getServiceIDs(t2File);
        }
        Set<String> sysIds = new HashSet<>();
        for (String id : serviceIds) {
            URL url = new URL(id);
            sysIds.add(url.getHost());
        }

        return sysIds;

//            Iterator<Processor> it = procs.iterator();
//            while (it.hasNext()) {
//                Processor p = it.next();
//                Configuration conf = p.getConfiguration(profile);
//                System.err.println(conf.getJsonAsString());
//                System.err.println(conf.getJsonSchema());
//            }
    }

    Date getStartDate() {
        return new Date();
    }

    Date getEndDate() {
        return new Date();
    }

    private Collection<String> getServiceIDs(File workflowFile) throws IOException {
        if (serviceIds == null) {
            serviceIds = new HashSet<>();
        }

        byte[] encoded = Files.readAllBytes(Paths.get(workflowFile.getAbsolutePath()));
        String text = new String(encoded, "UTF-8");
        Set<String> nonHostSet = new HashSet<>(Arrays.asList(NON_HOSTS));

        Pattern patt = Pattern.compile(URL_REGEX);
        Matcher matcher = patt.matcher(text);
        while (matcher.find()) {
            String matched = matcher.group();
            if (!nonHostSet.contains(matched)) {
                serviceIds.add(matched);
            }
        }
        return serviceIds;
    }

    Collection<String> getServiceIDs(File workflowFile, String sysID) throws IOException {
        if (serviceIds == null) {
            getServiceIDs(workflowFile);
        }
        Set<String> loaclServiceId = new HashSet<>();
        for (String id : serviceIds) {
            URL url = new URL(id);
            if (url.getHost().equals(sysID)) {
                loaclServiceId.add(url.toString());
            }
        }
        return loaclServiceId;

    }

    String getWorkflowID(File t2File) throws ReaderException, IOException {
//        WorkflowBundleIO io = new WorkflowBundleIO();
//
//        WorkflowBundle wfBundle = io.readBundle(t2File, APPLICATION_VND_TAVERNA_T2FLOW_XML);
//        Set<String> names = wfBundle.getAnnotations().getNames();
//        Workflow wf = wfBundle.getMainWorkflow();
        byte[] encoded = Files.readAllBytes(Paths.get(t2File.getAbsolutePath()));
        String text = new String(encoded, "UTF-8");

        final Pattern pattern = Pattern.compile("<dataflow id=\"(.+?)\" role=\"top\">", Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(text);
        matcher.find();
        String id = matcher.group(1);

        return id;
    }

    String getWorkflowEndpoint(File workflowFile) throws IOException {
        return insertWorkflowFile(workflowFile);
    }

    private String insertWorkflowFile(File workflowFile) {
        if (Util.urlExists(wfRepoURI)) {
            try {
                WebDAVClient c = new WebDAVClient(wfRepoURI);

                String webdavFolder = "workflows";
                c.putFile(workflowFile, webdavFolder, "application/vnd.taverna.t2flow+xml");
                return wfRepoURI + "/" + webdavFolder + "/" + workflowFile.getName();
            } catch (Throwable ex) {
                Logger.getLogger(TavernaService.class.getName()).log(Level.WARNING, null, ex);
                return null;
            }

        }
        return null;

    }

    nl.uva.sne.vre4eic.data.Workflow getWorkflow(File workflowFile) throws ReaderException, IOException {
        nl.uva.sne.vre4eic.data.Workflow wf = new nl.uva.sne.vre4eic.data.Workflow();
        String id = getWorkflowID(workflowFile);
        wf.setId(id);
        String endpoint = getWorkflowEndpoint(workflowFile);
        wf.setLocation(endpoint);
        return wf;
    }

}
