/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.controller;

import java.io.File;
import nl.uva.sne.vre4eic.data.Context;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.uva.sne.vre4eic.prise.service.Log2ProvService;
import nl.uva.sne.vre4eic.prise.service.ContextService;
import org.apache.taverna.scufl2.api.container.WorkflowBundle;
import org.apache.taverna.scufl2.api.core.Workflow;
import org.apache.taverna.scufl2.api.io.ReaderException;
import org.apache.taverna.scufl2.api.io.WorkflowBundleIO;
import static org.apache.taverna.scufl2.translator.t2flow.T2FlowReader.APPLICATION_VND_TAVERNA_T2FLOW_XML;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProvIntegratorController {

    @Autowired
    Log2ProvService service;

    @Autowired
    ContextService cntxService;

    private final String[] logMimeTypes = new String[]{"text/plain", "text/x-log"};
    private final String[] provMimeTypes = new String[]{"text/turtle"};
    private final String[] workflowMimeTypes = new String[]{"application/octet-stream", "application/vnd.taverna.t2flow+xml"};
    private final String[] workflowExtentions = new String[]{"t2flow"};

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = {"application/json"})
    public @ResponseBody
    Context submit(@RequestParam("files") MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {

                if (file.getContentType() != null) {
                    for (String ext : workflowExtentions) {
                        if (file.getOriginalFilename().endsWith(ext)) {
                            cntxService.setWorkflowFile(file);
                        }
                        break;
                    }
                    for (String prov : provMimeTypes) {
                        if (file.getContentType().toLowerCase().equals(prov)) {
                            cntxService.setWorkflowProvFile(file);
                            break;
                        }
                    }
                    for (String log : logMimeTypes) {
                        if (file.getContentType().toLowerCase().equals(log)) {
                            cntxService.setWorkflowLogFile(file);
                        }
                    }
                }

            }
            return cntxService.generate();
        } catch (IOException ex) {
            Logger.getLogger(ProvIntegratorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    

}
