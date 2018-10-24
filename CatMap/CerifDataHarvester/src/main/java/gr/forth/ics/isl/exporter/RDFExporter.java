/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.ics.isl.exporter;

import gr.forth.ics.isl.exception.GenericException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author S. Koulouzis
 */
public class RDFExporter implements CatalogueExporter {

    private final String catalogueURL;

    public RDFExporter(String catalogueURL) {
        this.catalogueURL = catalogueURL;
    }

    @Override
    public void exportAll(String outputPath) throws GenericException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<String> fetchAllDatasetUUIDs() throws MalformedURLException, IOException {
        URL catURL = new URL(catalogueURL);
        Set<String> rdfs = new HashSet<>();
        if (catalogueURL.endsWith("rdf")) {
            String out = new Scanner(catURL.openStream(), "UTF-8").useDelimiter("\\A").next();
            if (out.contains("http://www.oil-e.net/ontology/oil-base.owl")) {
                rdfs.add(catalogueURL);
            }
        }

        Document doc = Jsoup.parse(catURL, 5000);

        Elements links = doc.select("a[href]"); // a with href
//        Elements links = doc.select("img[src$=.rdf]");
        for (Element fileLink : links) {
            if (fileLink.attr("href").endsWith("rdf")) {
                URL link = new URL(catURL.getProtocol(), catURL.getHost(), catURL.getPort(), fileLink.attr("href"));
                String out = new Scanner(link.openStream(), "UTF-8").useDelimiter("\\A").next();

                if (out.contains("http://www.oil-e.net/ontology/oil-base.owl")) {
                    rdfs.add(link.toString());
                }
            }
        }
        return rdfs;
    }

    @Override
    public JSONObject exportResource(String resourceId) throws MalformedURLException, IOException {
        Map<String, String> map = new HashMap();
        map.put("id", resourceId);
        return new JSONObject(map);
    }

    @Override
    public String transformToXml(JSONObject jsonObject) {
        try {
            return new Scanner(new URL(jsonObject.getString("id")).openStream(), "UTF-8").useDelimiter("\\A").next();
        } catch (MalformedURLException ex) {
            Logger.getLogger(RDFExporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RDFExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String args[]) {
        try {
            RDFExporter exporter = new RDFExporter("http://localhost:8081/Mapping120/oil-e___12-07-2018192615___3099%20(3rd%20copy).rdf");
            Collection<String> allResourceIDs = exporter.fetchAllDatasetUUIDs();

            for (String resourceId : allResourceIDs) {
                JSONObject resource = exporter.exportResource(resourceId);
                String xml = exporter.transformToXml(resource);
                System.err.println(xml);
            }
        } catch (IOException ex) {
            Logger.getLogger(RDFExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setLimit(Integer limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
