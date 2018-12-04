/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.uva.sne.vre4eic.data.Logs;
import nl.uva.sne.vre4eic.prise.util.Util;
import nl.uva.sne.vre4eic.prise.util.WebDAVClient;
import org.apache.commons.io.FileUtils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.search.SearchType;
//import org.elasticsearch.index.query.QueryBuilders.*;

/**
 *
 * @author S. Koulouzis
 */
@Service
public class LogsService {

    @Value("${log.reposetory.uri:=http://localhost:9200}")
    
    private String logRepoURI;



    public Logs getSysLogs(String id, Date startDate, Date endDate) {
        Logs logs = new Logs();
        String location = getQueryLogEndpotin(id, startDate, endDate);
        logs.setLocation(location);
        return logs;
    }

    private String getQueryLogEndpotin(String sysID, Date startDate, Date endDate) {
        URL url = null;
        String endpoint = null;
        try {
            url = new URL(sysID);
            endpoint = url.getProtocol() + "://" + url.getHost() + ":9200/log/_search";
        } catch (MalformedURLException ex) {
            try {
                endpoint = new URL("http://" + sysID + ":8086/query?db=mydb").toString();
            } catch (MalformedURLException ex1) {
                Logger.getLogger(LogsService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        if (Util.urlExists(endpoint)) {
            Logger.getLogger(LogsService.class.getName()).log(Level.INFO, "endpoint: {0}", endpoint);
            return endpoint;
        }
        return null;
    }

    String getWorkflowLogsEndpoint(File workflowLogFile) {
        return insertLog(workflowLogFile);
    }

    private String insertLog(File workflowLogFile) {
        if (Util.urlExists(logRepoURI)) {
            try {
                WebDAVClient c = new WebDAVClient(logRepoURI);
                String webdavFolder = "logs";
//                sardine.put(logRepoURI + "/" + webdavFolder + "/" + workflowLogFile.getName(), FileUtils.readFileToByteArray(workflowLogFile));
                c.putFile(workflowLogFile, webdavFolder, "application/vnd.taverna.t2flow+xml");
                return logRepoURI + "/" + webdavFolder + "/" + workflowLogFile.getName();
            } catch (Throwable ex) {
                Logger.getLogger(LogsService.class.getName()).log(Level.WARNING, null, ex);
                return null;
            }

        }
        return null;
    }

    Logs getServcieLogs(String id, Date start, Date end) {
        Logs logs = new Logs();
        String location = getQueryLogEndpotin(id, start, end);
        logs.setLocation(location);
        return logs;
    }

    Logs getWorkflowLogs(File workflowLogFile) throws IOException {
        String endpoint = getWorkflowLogsEndpoint(workflowLogFile);
        Logs logs = new Logs();
        logs.setLocation(endpoint);
        return logs;

    }
}
