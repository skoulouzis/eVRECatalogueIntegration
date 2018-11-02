/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.ics.isl.exporter;

import gr.forth.ics.isl.exception.GenericException;
import gr.forth.ics.isl.util.XML;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

/**
 *
 * @author S. Koulouzis
 */
public class OGCCSWExporter implements CatalogueExporter {

    private static final Map<String, Collection<String>> UUID_CACHE = new HashMap<>();
    private static final Map<String, Document> RESOURCE_CACHE = new HashMap<>();
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

            Collection<String> recordIDs = this.getFromUUIDCache(endpointUrl);
            if (recordIDs != null && this.limit <= recordIDs.size()) {
                if (this.limit <= -1 || this.limit == recordIDs.size()) {
                    return recordIDs;
                } else {
                    Collection<String> tempCollection = new HashSet<>(this.limit);
                    Iterator<String> it = recordIDs.iterator();
                    int counter = 0;
                    while (it.hasNext()) {
                        String next = it.next();
                        tempCollection.add(next);
                        counter++;
                        if (counter >= this.limit) {
                            break;
                        }
                    }
                    return tempCollection;
                }
            }

            int startPosition = 1;
            int maxRecords = 10;
            int totalReturned = 0;

            Integer nextRecord = 0;
            Integer numberOfRecordsMatched = 0;
            Integer numberOfRecordsReturned = 0;
            recordIDs = new ArrayList<>();
            totalReturned += numberOfRecordsReturned;
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            boolean done = false;
            while (!done) {

                String cswPath = "/csw?"
                        + "REQUEST=GetRecords&"
                        + "SERVICE=CSW&"
                        + "VERSION=2.0.2&"
                        + "constraint_language_version=1.1.0&"
                        + "typeNames=csw:Record&"
                        + "RESULTTYPE=results&"
                        + "elementsetname=full&"
                        + "startPosition=" + startPosition + "&"
                        + "maxRecords=" + maxRecords + "&"
                        + "outputSchema=http://www.isotc211.org/2005/gmd";
                URL urlObj = new URL(this.endpointUrl + cswPath);
                InputStream is = urlObj.openStream();
//            Parser parser = new Parser(new CSWConfiguration());
//            GetRecordsResponseType gr = (GetRecordsResponseType) parser.parse(is);

                Document doc = fac.newDocumentBuilder().parse(is);

                Node recordsResponseNode = XML.getNode(doc.getFirstChild(), "GetRecordsResponse");
                if (recordsResponseNode == null) {
                    break;
                }

                Node searchResults = XML.getNode(recordsResponseNode.getFirstChild(), "SearchResults");
                NamedNodeMap attributes = searchResults.getAttributes();

                nextRecord = Integer.valueOf(attributes.getNamedItem("nextRecord").getNodeValue());
                numberOfRecordsMatched = Integer.valueOf(attributes.getNamedItem("numberOfRecordsMatched").getNodeValue());

                totalReturned += Integer.valueOf(attributes.getNamedItem("numberOfRecordsReturned").getNodeValue());

                List<Document> metadataDocs = getRecords(searchResults);
                for (Document metadataDoc : metadataDocs) {
                    String id = getResourceID(metadataDoc.getFirstChild());
                    recordIDs.add(id);
                }
                startPosition = nextRecord;
                if (totalReturned >= numberOfRecordsMatched || (this.limit > 0 && totalReturned >= this.limit)) {
                    done = true;
                }
            }
            UUID_CACHE.put(this.endpointUrl, recordIDs);
            return recordIDs;
        } catch (SAXException | ParserConfigurationException ex) {
            Logger.getLogger(OGCCSWExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<Document> getRecords(Node recordsResponseNode) throws ParserConfigurationException {
        NodeList nodes = recordsResponseNode.getChildNodes();
        List<Document> metadataNodes = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node != null && node.getLocalName() != null && node.getLocalName().equals("MD_Metadata")) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document newDocument = builder.newDocument();
                Node importedNode = newDocument.importNode(node, true);
                newDocument.appendChild(importedNode);
                String id = getResourceID(node);

                RESOURCE_CACHE.put(id, newDocument);
                metadataNodes.add(newDocument);
            }
        }
        return metadataNodes;
    }

    private String getResourceID(Node node) {
        Node fileIdentifierNode = XML.getNode(node.getFirstChild(), "fileIdentifier");
        Node characterString = XML.getNode(fileIdentifierNode.getFirstChild(), "CharacterString");
        return characterString.getTextContent();
    }

    @Override
    public Object exportResource(String resourceId) throws MalformedURLException, IOException {
        try {
            Document doc = this.getFromResourceCache(resourceId);
            if (doc != null) {
                return doc;
            }

            String cswPath = "/csw?"
                    + "REQUEST=GetRecordById&"
                    + "SERVICE=CSW&"
                    + "VERSION=2.0.2&"
                    + "constraint_language_version=1.1.0&"
                    + "typeNames=csw:Record&"
                    + "RESULTTYPE=results&"
                    + "elementsetname=full&"
                    + "id=" + resourceId + "&"
                    + "outputSchema=http://www.isotc211.org/2005/gmd";
            URL urlObj = new URL(this.endpointUrl + cswPath);
            InputStream is = urlObj.openStream();

            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            doc = fac.newDocumentBuilder().parse(is);
            RESOURCE_CACHE.put(resourceId, doc);
            return doc;
        } catch (ParserConfigurationException | SAXException ex) {
            Logger.getLogger(OGCCSWExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Document getFromResourceCache(String key) {
        final long now = System.currentTimeMillis();
        final long delta = now - cacheCleanupLastTime;
        if (delta < 0 || delta > 120 * 60 * 1000) {
            cacheCleanupLastTime = now;
            RESOURCE_CACHE.clear();
            return null;
        }
        return RESOURCE_CACHE.get(key);
    }

    @Override
    public String transformToXml(JSONObject jsonObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    private Collection<String> getFromUUIDCache(String key) {
        final long now = System.currentTimeMillis();
        final long delta = now - cacheCleanupLastTime;
        if (delta < 0 || delta > 15 * 60 * 1000) {
            cacheCleanupLastTime = now;
            UUID_CACHE.clear();
            return null;
        }
        return UUID_CACHE.get(key);
    }

    public static void main(String[] args) {
        try {
            OGCCSWExporter exporter = new OGCCSWExporter("http://catalogue2.sedoo.fr/geonetwork/srv/eng");
            Collection<String> allResourceIDs = exporter.fetchAllDatasetUUIDs();

            String xml;
            for (String resourceId : allResourceIDs) {
                Object resource = exporter.exportResource(resourceId);
                if (resource instanceof JSONObject) {
                    xml = exporter.transformToXml((JSONObject) resource);
                } else if (resource instanceof Document) {
                    DOMSource domSource = new DOMSource(((Document) resource));
                    StringWriter writer = new StringWriter();
                    StreamResult result = new StreamResult(writer);
                    TransformerFactory tf = TransformerFactory.newInstance();
                    Transformer transformer = tf.newTransformer();
                    transformer.transform(domSource, result);
                    xml = writer.toString();
                    System.err.println(xml);
                }
            }
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(OGCCSWExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
