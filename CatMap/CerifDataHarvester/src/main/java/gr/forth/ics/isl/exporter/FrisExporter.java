package gr.forth.ics.isl.exporter;

import gr.forth.ics.isl.cerifdataharvester.gui.HarvesterGui;
import gr.forth.ics.isl.common.FrisResources;
import gr.forth.ics.isl.exception.GenericException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class FrisExporter {
    private static final Logger logger=Logger.getLogger(FrisExporter.class);
    private final String endpointUrl;
    
    public FrisExporter(String endpointUrl){
        this.endpointUrl=endpointUrl;
    }
 
    public void exportResources(String exportUrl, String folderName, String filename) throws GenericException{
        logger.info("Exporting contents from category "+filename);
        int fileCounter=1;
        try{
            while(true){
                URL extendedURL=new URL(exportUrl+"?"+
                                        FrisResources.PAGE+"="+fileCounter+"&"+
                                        FrisResources.PAGE_SIZE+"="+FrisResources.PAGE_SIZE_LIMIT);
                HttpURLConnection conn=(HttpURLConnection)extendedURL.openConnection();
                conn.setRequestProperty("Content-Type", "application/xml");
                conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
                logger.debug("Fetching data from "+extendedURL);
                HarvesterGui.htmlOutput.append("Exporting contents to file ").append(folderName).append("/").append(filename).append(fileCounter).append(".xml ...");
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                File outputFile=new File(folderName+"/"+filename+fileCounter+".xml");
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
                String line;
                int linesFound=0;
                while((line=br.readLine())!=null){
                    bw.append(line)
                      .append("\n");
                    linesFound+=1;
                }
                if(linesFound < FrisResources.LINES_THRESHOLD){
                    bw.close();
                    break;
                }
                bw.flush();
                bw.close();
                br.close();
                fileCounter+=1;
                HarvesterGui.htmlOutput.append("Done\n");
            }
        }catch(IOException ex){
            logger.error("An error occured while exporting data",ex);
            throw new GenericException("An error occured while exporting data",ex);
        }
    }
    
    public void exportAll() throws GenericException{
        this.exportResources(this.endpointUrl+FrisResources.PROJECTS_SUFFIX,
                            FrisResources.PROJECTS, 
                            FrisResources.PROJECTS);
        this.exportResources(this.endpointUrl+FrisResources.PERSONS_SUFFIX,
                            FrisResources.PERSONS, 
                            FrisResources.PERSONS);
        this.exportResources(this.endpointUrl+FrisResources.ORGANIZATIONS_SUFFIX,
                            FrisResources.ORGANIZATIONS, 
                            FrisResources.ORGANIZATIONS);
        this.exportResources(this.endpointUrl+FrisResources.PUBLICATIONS_SUFFIX,
                            FrisResources.PUBLICATIONS, 
                            FrisResources.PUBLICATIONS);
    }
    
    public static void main(String[] args) throws GenericException{
        new FrisExporter(FrisResources.FRIS_ENDPOINT_URL).exportAll();
    }
}