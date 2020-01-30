/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.statistics_cat;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import org.gephi.graph.api.GraphModel;
import org.openide.util.Exceptions;

/**
 *
 * @author S. Koulouzis
 */
public class RDFAnalyser {

    public static void main(String args[]) {
        InputStream in = null;
        try {
            Model m = ModelFactory.createDefaultModel();
            RDFReader arp = m.getReader();
            // initialize arp
// Do not warn on use of unqualified RDF attributes.
            arp.setProperty("WARN_UNQUALIFIED_RDF_ATTRIBUTE", "EM_IGNORE");
            in = new FileInputStream("/home/alogo/Downloads/target/abramis_brama_absence_generation_from_obis_id71eb69a754ab48cebefb52a448433fa0.rdf");
            arp.read(m, in, "STRING");
            in.close();
            
            Graph g = m.getGraph();
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

    }

}
