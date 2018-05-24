/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.cat_exporter;

import gr.forth.ics.isl.exception.GenericException;
import gr.forth.ics.isl.exporter.CatalogueExporter;
import gr.forth.ics.isl.exporter.D4ScienceExporter;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author S. Koulouzis
 */
public class ExportDocTask implements Runnable {

    private final String catalogueURL;
    private final File outputFolder;

    public ExportDocTask(String catalogueURL, File outputFolder) {
        this.catalogueURL = catalogueURL;
        this.outputFolder = outputFolder;
    }

    @Override
    public void run() {
        try {
            exportDocuments(this.catalogueURL, this.outputFolder);
        } catch (MalformedURLException | GenericException ex) {
            Logger.getLogger(ExportDocTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static File exportDocuments(String catalogueURL, File outputFolder) throws MalformedURLException, GenericException {
        CatalogueExporter exporter = getExporter(catalogueURL);
        exporter.exportAll(outputFolder.getAbsolutePath());
        Logger.getLogger(Task.class.getName()).log(Level.INFO, "Exported documents in: {0}", outputFolder.getAbsolutePath());
        return outputFolder;
    }

    private static CatalogueExporter getExporter(String catalogueURL) {
        return new D4ScienceExporter(catalogueURL);
    }
}
