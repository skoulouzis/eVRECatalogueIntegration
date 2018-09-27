package gr.forth.ics.isl.exporter;

import gr.forth.ics.isl.cerifdataharvester.gui.HarvesterGui;
import gr.forth.ics.isl.common.CerifResources;
import gr.forth.ics.isl.common.Common;
import gr.forth.ics.isl.common.EktResources;
import gr.forth.ics.isl.exception.GenericException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class EktExporter {
    private static final Logger logger=Logger.getLogger(EktExporter.class);
    private String endpointUrl;
    
    public EktExporter(String endpointUrl){
        this.endpointUrl=endpointUrl;
    }
    
    public void exportAll() throws GenericException {
        this.exportLinksIterative(this.endpointUrl+EktResources.ORG_UNITS_SUFFIX, 
                                  CerifResources.CF_URI_ELEMENT,
                                  EktResources.ORGANIZATION_UNITS, 
                                  CerifResources.CF_ORG_UNIT_ELEMENT);
        this.exportLinksIterative(this.endpointUrl+EktResources.PERSONS_SUFFIX, 
                                  CerifResources.CF_PERS_ELEMENT, 
                                  EktResources.PERSONS, 
                                  CerifResources.CF_PERS_ELEMENT);
        this.exportLinksIterative(this.endpointUrl+EktResources.PROJECTS_SUFFIX, 
                                  CerifResources.CF_PROJ_ELEMENT, 
                                  EktResources.PROJECTS, 
                                  CerifResources.CF_PROJ_ELEMENT);
        this.exportLinksIterative(this.endpointUrl+EktResources.PUBLICATIONS_SUFFIX, 
                                  CerifResources.CF_RES_PUBL_ELEMENT, 
                                  EktResources.PUBLICATIONS, 
                                  CerifResources.CF_RES_PUBL_ELEMENT);
        this.exportLinksIterative(this.endpointUrl+EktResources.FUNDINGS_SUFFIX, 
                                  CerifResources.CF_FUND_ELEMENT, 
                                  EktResources.FUNDINGS, 
                                  CerifResources.CF_FUND_ELEMENT);
        this.exportLinksIterative(this.endpointUrl+EktResources.SERVICES_SUFFIX, 
                                  CerifResources.CF_SRV_ELEMENT, 
                                  EktResources.SERVICES, 
                                  CerifResources.CF_SRV_ELEMENT);
        this.exportLinksIterative(this.endpointUrl+EktResources.POSTAL_ADDRESSES_SUFFIX,
                                  CerifResources.CF_PADDR_ELEMENT, 
                                  EktResources.POSTAL_ADDRESSES, 
                                  CerifResources.CF_PADDR_ELEMENT);
        this.exportLinksIterative(this.endpointUrl+EktResources.ELECTRONIC_ADDRESSES_SUFFIX,
                                  CerifResources.CF_EADDR_ELEMENT,
                                  EktResources.ELECTRONIC_ADDRESSES,
                                  CerifResources.CF_EADDR_ELEMENT);
    }
    
    public Set<String> exportLinksIterative(String initialURL, String separator, String filename, String tagToRetrieve) throws GenericException{
        logger.info("Exporting contents from category "+filename);
        Set<String> retSet=new HashSet<>();
        int cnt=0;
        boolean hasMoreResults=true;
        try{
            while(hasMoreResults){
                retSet.clear();
                String url=initialURL+"/?"+
                           CerifResources.OFFSET+"="+(cnt*EktResources.STEP)+"&"+
                           CerifResources.PAGE_SIZE+"="+(EktResources.STEP);
                logger.debug("Export from URL: "+url);
                InputStream is =new URL(url).openStream();
                Document doc=Jsoup.parse(is, "UTF-8", url);
                Elements itemElements=doc.getElementsByTag(separator);
                for(Element elem : itemElements){
                    retSet.add(elem.text());
                }
                if(itemElements.isEmpty()){
                    hasMoreResults=false;
                }
                cnt+=1;
                is.close();
                HarvesterGui.htmlOutput.append("Exporting contents to file ").append(filename).append(cnt).append(".xml ...");
                this.exportOrgUnits(filename+""+cnt+".xml", retSet, tagToRetrieve);
                HarvesterGui.htmlOutput.append("DONE\n");
            }
        }catch(IOException | ParserConfigurationException | SAXException | TransformerException ex){
            logger.error("An error occured while exporting data",ex);
            throw new GenericException("An error occured while exporting data",ex);
        }
        return retSet;
    }
    
    private void exportOrgUnits(String filename, Set<String> orgUnits, String tagToRetrieve) throws ParserConfigurationException, SAXException, IOException, TransformerException{
        org.w3c.dom.Node rootNode=this.createRootElements();
        for(String url : orgUnits){
            Node childNode=this.getSpecificElement(rootNode.getOwnerDocument(), url, tagToRetrieve);
            if(childNode!=null){
                rootNode.appendChild(childNode);
            }
        }
        Common.exportToXML(rootNode.getOwnerDocument(), filename);
    }
    
    private Node getSpecificElement(org.w3c.dom.Document masterDoc, String url, String tagToRetrieve) throws ParserConfigurationException, SAXException, IOException{
        logger.debug("Exporting url: "+url);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	org.w3c.dom.Document doc = dBuilder.parse(new URL(url).openStream());
        org.w3c.dom.NodeList nodeList=doc.getElementsByTagName(tagToRetrieve);
        if(nodeList.getLength()==0){
            return null;
        }
        return masterDoc.importNode(nodeList.item(0), true);
            
    }
    
    private org.w3c.dom.Node createRootElements() throws ParserConfigurationException{
        org.w3c.dom.Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        org.w3c.dom.Node rootNode=doc.createElement(CerifResources.CERIF_API_ELEMENT);
        org.w3c.dom.Node payloadNode=doc.createElement(CerifResources.PAYLOAD_ELEMENT);
        org.w3c.dom.Node cerifNode=doc.createElement(CerifResources.CERIF_ELEMENT);
        rootNode.appendChild(payloadNode);
        payloadNode.appendChild(cerifNode);
        doc.appendChild(rootNode);
        return cerifNode;
    }
    
    public static void main(String[] args) throws GenericException{
        new EktExporter(EktResources.EKT_ENDPOINT_URL).exportAll();
    }
}