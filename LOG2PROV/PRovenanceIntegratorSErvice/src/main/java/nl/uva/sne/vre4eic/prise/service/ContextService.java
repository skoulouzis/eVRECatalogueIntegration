/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import nl.uva.sne.vre4eic.data.Context;
import nl.uva.sne.vre4eic.data.Logs;
import nl.uva.sne.vre4eic.data.Provenance;
import nl.uva.sne.vre4eic.data.ServiceContext;
import nl.uva.sne.vre4eic.data.SystemContext;
import nl.uva.sne.vre4eic.data.Workflow;
import nl.uva.sne.vre4eic.data.WorkflowContext;
import nl.uva.sne.vre4eic.prise.util.Util;
import org.apache.taverna.scufl2.api.io.ReaderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.xml.sax.SAXException;

/**
 *
 * @author S. Koulouzis
 */
@Service
public class ContextService {

    private File workflowFile;
    private File provFile;
    private File workflowLogFile;

    @Autowired
    LogsService logsService;

    @Autowired
    TavernaService tavernaService;

    @Autowired
    ProvenanceService provenanceService;

    public void setWorkflowFile(MultipartFile workflowMultipartFile) throws IOException {
        this.workflowFile = Util.convert(workflowMultipartFile);
    }

    public void setWorkflowProvFile(MultipartFile provMultipartFile) throws IOException {
        this.provFile = Util.convert(provMultipartFile);
    }

    public void setWorkflowLogFile(MultipartFile workflowLogMultipartFile) throws IOException {
        this.workflowLogFile = Util.convert(workflowLogMultipartFile);
    }

    public Context generate() {

        try {
            Context ctx = new Context();

            Date start = getStartDate();
            Date end = getEndDate();
            Collection<String> sysIds = getSysIDs();
            List<SystemContext> systemContexts = new ArrayList<>();
            for (String sysID : sysIds) {
                SystemContext systemContext = generateSystemContext(sysID, start, end);
                Collection<String> ids = getServiceIDs(sysID);
                List<ServiceContext> services = new ArrayList<>();
                for (String id : ids) {
                    ServiceContext ser = new ServiceContext();
                    ser.setId(id);
                    Logs logs = logsService.getServcieLogs(id, start, end);
                    ser.setLogs(logs);
                    services.add(ser);
                }
                systemContext.setServices(services);
                systemContexts.add(systemContext);
            }

            ctx.setSystemContext(systemContexts);
            WorkflowContext workflowContext = generateWorkflowContext();
            ctx.setWorkflowContext(workflowContext);
            return ctx;
        } catch (ParserConfigurationException | SAXException | IOException | ReaderException ex) {
            Logger.getLogger(ContextService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private SystemContext generateSystemContext(String sysID, Date start, Date end) {
        SystemContext sysCtx = new SystemContext();
        Logs logs = generateSysLogs(sysID, start, end);
        sysCtx.setLogs(logs);
        return sysCtx;
    }

    private WorkflowContext generateWorkflowContext() throws ReaderException, IOException {
        WorkflowContext wfCtx = new WorkflowContext();
        Logs logs = generateWorkflowLogs();
        wfCtx.setLogs(logs);
        Provenance provenance = generateWorkflowProvenance();
        wfCtx.setProvenance(provenance);
        Workflow workflow = generateWorkflow();
        wfCtx.setWorkflow(workflow);
        return wfCtx;
    }

    private Logs generateSysLogs(String sysID, Date startDate, Date endDate) {
        return logsService.getSysLogs(sysID, startDate, endDate);
    }

    private Logs generateWorkflowLogs() throws IOException {
        if (this.workflowLogFile != null && this.workflowLogFile.exists()) {
            Logs logs = logsService.getWorkflowLogs(workflowLogFile);
            return logs;
        }
        return null;
    }

    private Provenance generateWorkflowProvenance() throws IOException {
        if (this.provFile != null && this.provFile.exists()) {
            Provenance prov = provenanceService.getProvenance(provFile);
            return prov;
        }
        return null;

    }

    private Workflow generateWorkflow() throws ReaderException, IOException {
        if (this.workflowFile != null && this.workflowFile.exists()) {
            Workflow wf = tavernaService.getWorkflow(workflowFile);

            return wf;
        }
        return null;
    }

    private Date getStartDate() {
        return tavernaService.getStartDate();
    }

    private Date getEndDate() {
        return tavernaService.getEndDate();
    }

    private Collection<String> getSysIDs() throws ParserConfigurationException, SAXException, IOException {
        return tavernaService.getSysIDs(this.workflowFile);

    }

    private Collection<String> getServiceIDs(String sysID) throws IOException {
        return tavernaService.getServiceIDs(this.workflowFile, sysID);
    }

}
