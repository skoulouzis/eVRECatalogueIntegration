/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.uva.sne.vre4eic.data.Logs;
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
                endpoint = new URL("http://" + sysID + ":8093/query").toString();
            } catch (MalformedURLException ex1) {
                Logger.getLogger(LogsService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return endpoint;
    }

    String getWorkflowLogsEndpoint(File workflowLogFile) {
        insertLog(workflowLogFile);
        return logRepoURI + "/log/_search";

    }

    private void insertLog(File workflowLogFile) {

    }

    Logs getServcieLogs(String id, Date start, Date end) {
        Logs logs = new Logs();
        String location = getQueryLogEndpotin(id, start, end);
        logs.setLocation(location);
        return logs;
    }

    Logs getWorkflowLogs(File workflowLogFile) {
        String endpoint = getWorkflowLogsEndpoint(workflowLogFile);
        Logs logs = new Logs();
        logs.setLocation(endpoint);
        return logs;

    }
}
