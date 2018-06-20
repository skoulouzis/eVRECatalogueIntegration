/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.rest;

import io.micrometer.core.annotation.Timed;
import java.net.MalformedURLException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.uva.sne.vre4eic.model.ProcessingStatus;
import nl.uva.sne.vre4eic.model.User;
import nl.uva.sne.vre4eic.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author S. Koulouzis
 */
@RestController
@Timed
public class ConvertController {

    @Autowired
    private Services service;

//    @RequestMapping(value = "/convert", method = RequestMethod.GET, params = {"catalogue_url", "cerif_store_url"})
    @RequestMapping(value = "/convert", method = RequestMethod.GET)
    public @ResponseBody
    ProcessingStatus convert(@RequestParam(value = "catalogue_url") String catalogueURL) {
        try {
            ProcessingStatus status = service.doProcess(catalogueURL);
            return status;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConvertController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
