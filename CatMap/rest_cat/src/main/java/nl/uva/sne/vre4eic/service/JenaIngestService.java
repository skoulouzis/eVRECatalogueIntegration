/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.service;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import nl.uva.sne.vre4eic.util.Util;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 *
 * @author S. Koulouzis
 */
public class JenaIngestService {

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

    public void ingest(String webDAVURL, String ingestCatURL, String datasetName) throws IOException {
        Sardine sardine = SardineFactory.begin();

        List<DavResource> resources = sardine.list(webDAVURL);
        URL url = new URL(webDAVURL);
        String base = url.getProtocol() + "://" + url.getHost();
        if (url.getPort() > -1) {
            base += ":" + url.getPort();
        }

        for (DavResource res : resources) {
            if (!res.isDirectory() && res.getName().endsWith("ttl")) {
                try (InputStream ins = Util.getWebDavInputStream(res, sardine, base)) {
                    uploadRDF(ins, ingestCatURL, datasetName);
                }
            }
        }
    }
}
