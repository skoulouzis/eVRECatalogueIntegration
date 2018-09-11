/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.model;

import gr.forth.ics.isl.exception.GenericException;
import gr.forth.ics.isl.exporter.CatalogueExporter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import org.json.JSONObject;

/**
 *
 * @author S. Koulouzis
 */
public class WPSExporter implements CatalogueExporter {

    public WPSExporter(String catalogueURL) {
    }

    @Override
    public void exportAll(String outputPath) throws GenericException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<String> fetchAllDatasetUUIDs() throws MalformedURLException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject exportResource(String resourceId) throws MalformedURLException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String transformToXml(JSONObject jsonObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
