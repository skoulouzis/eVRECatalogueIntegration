/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.ics.isl.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author S. Koulouzis
 */
public class XML {

    public static Node getNode(Node node, String nodeName) {
        if (node == null) {
            return null;
        }
        if (node.getNodeType() == Node.ELEMENT_NODE && node.getLocalName().equals(nodeName)) {
            return node;
        } else {
            return getNode(node.getNextSibling(), nodeName);
        }
    }

    public static Node getNode(String xml, String nodeName) throws ParserConfigurationException, SAXException, IOException {

        InputSource is = new InputSource(new StringReader(xml));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);

        return getNode(doc.getFirstChild(), nodeName);
    }

    public static String getResourceID(String xml) throws ParserConfigurationException, SAXException, IOException {
        Node resourceNode = XML.getNode(xml, "resource");
        String id = null;
        if (resourceNode != null) {
            Node result = XML.getNode(resourceNode.getFirstChild(), "result");
            Node idNode = XML.getNode(result.getFirstChild(), "id");
            id = idNode.getTextContent();
        }
        Node mdMetadata = XML.getNode(xml, "MD_Metadata");
        if (mdMetadata != null) {
            Node fileIdentifierNode = XML.getNode(mdMetadata.getFirstChild(), "fileIdentifier");
            Node characterString = XML.getNode(fileIdentifierNode.getFirstChild(), "CharacterString");
            id = characterString.getTextContent();
        }
        return id;
    }

    public static String getResourceID(Node xml) throws ParserConfigurationException, SAXException, IOException {
        Node resourceNode = XML.getNode(xml, "resource");
        String id = null;
        if (resourceNode != null) {
            Node result = XML.getNode(resourceNode.getFirstChild(), "result");
            Node idNode = XML.getNode(result.getFirstChild(), "id");
            id = idNode.getTextContent();
        }
        Node mdMetadata = XML.getNode(xml, "MD_Metadata");
        if (mdMetadata != null) {
            Node fileIdentifierNode = XML.getNode(mdMetadata.getFirstChild(), "fileIdentifier");
            Node characterString = XML.getNode(fileIdentifierNode.getFirstChild(), "CharacterString");
            id = characterString.getTextContent();
        }
        return id;
    }

}
