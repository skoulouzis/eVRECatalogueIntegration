package gr.forth.ics.isl.exporter;

import gr.forth.ics.isl.cerifdataharvester.gui.HarvesterGui;
import gr.forth.ics.isl.common.RcukResources;
import gr.forth.ics.isl.exception.GenericException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.Logger;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class RcukExporter {
    private static final Logger logger=Logger.getLogger(RcukExporter.class);
    private String endpointUrl;
    
    public RcukExporter(String endpointUrl){
        this.endpointUrl=endpointUrl;
    }
    
    public void exportResources(String exportUrl, String folderName, String filename) throws GenericException{
        int fileCounter=1;
        try{
            while(true){
                URL extendedURL=new URL(exportUrl+"?"+
                                        RcukResources.PAGE+"="+fileCounter+"&"+
                                        RcukResources.FETCH_SIZE+"="+RcukResources.STEP);
                HttpURLConnection conn=(HttpURLConnection)extendedURL.openConnection();
                conn.setRequestProperty("Content-Type", "application/xml");
                conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
                logger.debug("Fetching data from "+extendedURL);
                HarvesterGui.htmlOutput.append("Exporting contents to file ").append(folderName).append("/").append(filename).append(fileCounter).append(".xml ...");
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                File outputFile=new File(folderName+"/"+filename+fileCounter+".xml");
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
                String line;
                while((line=br.readLine())!=null){
                    bw.append(line)
                      .append("\n");
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
        this.exportResources(this.endpointUrl+RcukResources.PERSONS_SUFFIX,
                                 RcukResources.PERSONS,
                                 RcukResources.PERSONS);
        this.exportResources(this.endpointUrl+RcukResources.PROJECTS_SUFFIX,
                                 RcukResources.PROJECTS,
                                 RcukResources.PROJECTS);
        this.exportResources(this.endpointUrl+RcukResources.PUBLICATIONS_SUFFIX,
                                 RcukResources.PUBLICATIONS,
                                 RcukResources.PUBLICATIONS);
        this.exportResources(this.endpointUrl+RcukResources.ORGANIZATION_UNITS,
                                 RcukResources.ORGANIZATION_UNITS,
                                 RcukResources.ORGANIZATION_UNITS);
    }
    
    public static void main(String[] args) throws MalformedURLException, GenericException{
        new RcukExporter(RcukResources.RCUK_ENDPOINT_URL).exportAll();
    }
}