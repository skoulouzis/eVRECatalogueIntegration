/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.uva.sne.vre4eic.prise.service.Log2ProvService;
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

    private final String[] logMimeTypes = new String[]{"text/plain", "text/x-log"};

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public @ResponseBody
    String submit(@RequestParam("files") MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                for (String logMimetype : logMimeTypes) {
                    if (file.getContentType() != null && file.getContentType().toLowerCase().equals(logMimetype)) {
                        String prov = service.convert(file.getOriginalFilename(), file.getBytes());
                        System.err.println(prov);
                        break;
                    }
                }
            }
        } catch (IOException | TimeoutException | InterruptedException ex) {
            Logger.getLogger(ProvIntegratorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "done";
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    String get() {
        return "done";
    }

}
