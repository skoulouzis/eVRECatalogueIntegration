/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.model;

import gr.forth.ics.isl.exporter.RDFExporter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import gr.forth.ics.isl.exception.GenericException;
import gr.forth.ics.isl.exporter.CatalogueExporter;
import gr.forth.ics.isl.exporter.D4ScienceExporter;
import io.micrometer.core.instrument.MeterRegistry;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;

/**
 *
 * @author S. Koulouzis
 */
public class ExportDocTask implements Callable<String> {

    private final String catalogueURL;
    private final ConnectionFactory factory;

    @Autowired
    MetricsEndpoint endpoint;

    @Autowired
    MeterRegistry meterRegistry;
    private final String queue;
    private String mappingURL;
    private String generatorURL;

    public ExportDocTask(String catalogueURL, ConnectionFactory factory, String queue, String mappingURL, String generatorURL) {
        this.catalogueURL = catalogueURL;
        this.factory = factory;
        if (this.factory == null) {
            throw new NullPointerException("RabbitMQ ConnectionFactory is NULL!");
        }
        this.queue = queue;
        this.mappingURL = mappingURL;
        this.generatorURL = generatorURL;
    }

    private void exportDocuments(String catalogueURL) throws MalformedURLException, GenericException {

        try {
            CatalogueExporter exporter = getExporter(catalogueURL);
            Collection<String> allResourceIDs = exporter.fetchAllDatasetUUIDs();
            for (String resourceId : allResourceIDs) {
                JSONObject resource = exporter.exportResource(resourceId);
                String xml = exporter.transformToXml(resource);

                try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
                    String qName = queue;
                    channel.queueDeclare(qName, true, false, false, null);

                    JSONObject json = new JSONObject();
                    json.put("xml_ckan", xml);
                    json.put("json_ckan", resource.toString());
                    json.put("mappingURL", mappingURL);
                    json.put("generatorURL", generatorURL);

                    byte[] encoded = (Base64.encodeBase64(json.toString().getBytes()));
                    String message = new String(encoded, "UTF-8");

                    channel.basicPublish("", qName,
                            MessageProperties.PERSISTENT_TEXT_PLAIN,
                            message.getBytes("UTF-8"));

                } catch (TimeoutException ex) {
                    Logger.getLogger(ExportDocTask.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

//            Set<String> names = endpoint.listNames().getNames();
//        endpoint.metric(catalogueURL, list);
        } catch (IOException ex) {
            Logger.getLogger(ExportDocTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CatalogueExporter getExporter(String catalogueURL) throws MalformedURLException {
        if (urlExists(catalogueURL + "/api/action/package_list")) {
            return new D4ScienceExporter(catalogueURL);
        }
        if (new URL(catalogueURL).getPath().contains("/wps/WebProcessingService") || urlExists(catalogueURL + "/wps/WebProcessingService")) {
            return new WPSExporter(catalogueURL);
        } else {
            return new RDFExporter(catalogueURL);
        }
    }

    private boolean urlExists(String URLName) {

        try {
            HttpURLConnection.setFollowRedirects(true);
            //        HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con
                    = (HttpURLConnection) new URL(URLName).openConnection();
            con.setInstanceFollowRedirects(true);
            con.setRequestMethod("HEAD");

            int code = con.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                if (code == HttpURLConnection.HTTP_MOVED_TEMP
                        || code == HttpURLConnection.HTTP_MOVED_PERM
                        || code == HttpURLConnection.HTTP_SEE_OTHER) {
                    String newUrl = con.getHeaderField("Location");

                    // get the cookie if need, for login
                    String cookies = con.getHeaderField("Set-Cookie");
                    con = (HttpURLConnection) new URL(newUrl).openConnection();
                    con.setRequestProperty("Cookie", cookies);
                    code = con.getResponseCode();
                }
            }

            return (code == HttpURLConnection.HTTP_OK);
        } catch (MalformedURLException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }
    }

    @Override
    public String call() throws Exception {
        exportDocuments(this.catalogueURL);
        return null;
    }
}
