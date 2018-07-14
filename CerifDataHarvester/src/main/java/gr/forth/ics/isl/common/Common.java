package gr.forth.ics.isl.common;



import java.io.File;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class Common {
    public static final String RELEASE_VERSION="1.0";
    
    public static void exportToXML(org.w3c.dom.Document document, String filename) throws TransformerException{
        TransformerFactory transformerFactory=TransformerFactory.newInstance();
        Transformer transformer=transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source=new DOMSource(document);
        StreamResult result=new StreamResult(new File(filename));
        transformer.transform(source, result);
    }
}