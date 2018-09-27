package gr.forth.ics.isl.exporter;

import gr.forth.ics.isl.common.Common;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class EposExporter {
    private static final String RESEARCH_INFRASTRUCTURES_URL="http://130.186.13.192/eposWebApi/researchInfrastructures/";    
    
    public void exportAll() throws IOException, ParserConfigurationException, SAXException, TransformerException{
        exportSpecificData(RESEARCH_INFRASTRUCTURES_URL, "ResearchInfrastructures.xml");
    }
    
    private void exportSpecificData(String url, String filename) throws IOException, ParserConfigurationException, SAXException, TransformerException{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL(url).openStream());
        Common.exportToXML(doc, filename);
    }
    
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException{
        new EposExporter().exportAll();
    }
}