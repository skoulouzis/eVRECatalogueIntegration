/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.ics.isl.exporter;

import gr.forth.ics.isl.exception.GenericException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.opengis.cat.csw20.GetRecordsResponseType;
import org.geotools.csw.CSWConfiguration;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.geotools.xsd.Parser;
import net.opengis.cat.csw20.GetRecordsType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author S. Koulouzis
 */
public class OGCCSWExporter implements CatalogueExporter {

    private static final Map<String, Collection<String>> UUID_CACHE = new HashMap<>();

    private long cacheCleanupLastTime;

    private final String endpointUrl;
    private Integer limit = -1;

    public OGCCSWExporter(String endpointUrl) {
        this.endpointUrl = endpointUrl;
        cacheCleanupLastTime = System.currentTimeMillis();
    }

    @Override
    public void exportAll(String outputPath) throws GenericException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<String> fetchAllDatasetUUIDs() throws MalformedURLException, IOException {
        try {
            String cswPath = "/csw?REQUEST=GetRecords&SERVICE=CSW&VERSION=2.0.2&constraint_language_version=1.1.0&typeNames=csw:Record&RESULTTYPE=results&elementsetname=brief";
            URL urlObj = new URL(this.endpointUrl + cswPath);
            InputStream is = urlObj.openStream();
//            Parser parser = new Parser(new CSWConfiguration());
//            GetRecordsResponseType gr = (GetRecordsResponseType) parser.parse(is);

            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            Document doc = fac.newDocumentBuilder().parse(is);

            Node recordsResponseNode = getNode(doc.getFirstChild(), "GetRecordsResponse");

            Node searchResults = getNode(recordsResponseNode.getFirstChild(), "SearchResults");
            List<Node> briefRecordNodes = getGetBriefRecordNodes(searchResults);
//            for (Node briefRecordNode : briefRecordNodes) {
//                System.err.println(briefRecordNode.getFirstChild().getLocalName());
//            }

//
//            StringBuilder sb;
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
//                String line;
//                sb = new StringBuilder();
//                while ((line = br.readLine()) != null) {
//                    sb.append(line).append("\n");
//                }
//            }
//            System.err.println(sb.toString());
            return null;
        } catch (SAXException ex) {
            Logger.getLogger(OGCCSWExporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(OGCCSWExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Node getNode(Node node, String nodeName) {
        if (node == null) {
            return null;
        }
        if (node.getNodeType() == Node.ELEMENT_NODE && node.getLocalName().equals(nodeName)) {
            return node;
        } else {
            return getNode(node.getNextSibling(), nodeName);
        }
    }

    private List<Node> getGetBriefRecordNodes(Node recordsResponseNode) {
        NodeList nodes = recordsResponseNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node != null) {
                Node briefRecordNode = getNode(node, "BriefRecord");
                if (briefRecordNode != null) {
//                    Node id = getNode(briefRecordNode.getFirstChild(), "identifier");
//                    System.err.println(id.getLocalName() + " " + id.getTextContent());
                }
            }

        }
        return null;
    }

    @Override
    public JSONObject exportResource(String resourceId) throws MalformedURLException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String transformToXml(JSONObject jsonObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLimit(Integer limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void main(String[] args) {
        try {
            OGCCSWExporter exp = new OGCCSWExporter("http://catalogue2.sedoo.fr/geonetwork/srv/eng");
            Collection<String> ids = exp.fetchAllDatasetUUIDs();
        } catch (IOException ex) {
            Logger.getLogger(OGCCSWExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
