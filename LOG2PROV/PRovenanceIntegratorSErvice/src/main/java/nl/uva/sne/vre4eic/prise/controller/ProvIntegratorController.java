/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.controller;

import nl.uva.sne.vre4eic.data.Context;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.uva.sne.vre4eic.data.Service;
import nl.uva.sne.vre4eic.prise.service.Log2ProvService;
import nl.uva.sne.vre4eic.prise.service.ContextService;
import nl.uva.sne.vre4eic.prise.service.ProvParser;
import nl.uva.sne.vre4eic.prise.service.WorkflowParser;
import nl.uva.sne.vre4eic.prise.util.Util;
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

    ProvParser provParser;
    WorkflowParser wfParser;

    private final String[] provExtensions = new String[]{"prov.ttl"};
    private final String[] workflowExtentions = new String[]{"t2flow"};

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = {"application/json"})
    public @ResponseBody String submit(@RequestParam("files") MultipartFile[] files) {
        String output = new String();

        try {
            for (MultipartFile file : files) {
                if (file.getContentType() != null) {
                    for (String ext : workflowExtentions) {
                        if (file.getOriginalFilename().endsWith(ext)) {
                            output += new WorkflowParser(Util.convert(file)).extractServices().toString();
                        }
                        break;
                    }

                    for (String ext : provExtensions) {
                        if (file.getOriginalFilename().endsWith(ext)) {
                            provParser = new ProvParser(Util.convert(file));
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ProvIntegratorController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return output;
    }
    

}
