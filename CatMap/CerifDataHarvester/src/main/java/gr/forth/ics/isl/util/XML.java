/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.ics.isl.util;

import org.w3c.dom.Node;

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
}
