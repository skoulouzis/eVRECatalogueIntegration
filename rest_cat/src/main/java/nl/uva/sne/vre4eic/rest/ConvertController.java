/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.uva.sne.vre4eic.model.User;
import nl.uva.sne.vre4eic.service.GitHubLookupService;
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
public class ConvertController {

    @Autowired
    private GitHubLookupService service;

//    @RequestMapping(value = "/convert", method = RequestMethod.GET, params = {"catalogue_url", "cerif_store_url"})
    @RequestMapping(value = "/convert", method = RequestMethod.GET)
    public @ResponseBody
    String convert(@RequestParam(value = "catalogue_url") String catalogueURL) {
        try {
            CompletableFuture<User> res = service.findUser("skoulouzis");
            return res.toString();
        } catch (InterruptedException ex) {
            Logger.getLogger(ConvertController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }
}
