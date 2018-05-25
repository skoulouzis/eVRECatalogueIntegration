/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(value = "/convert", method = RequestMethod.GET, params = {"catalogue_url","cerif_store_url"})
    public @ResponseBody
    String convert(@RequestParam(value = "catalogue_url") String catalogueURL) {
        try {
            URL catURL = new URL(catalogueURL);
            return catURL.toString();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConvertController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
