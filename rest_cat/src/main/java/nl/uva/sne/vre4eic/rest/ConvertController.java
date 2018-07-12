/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.rest;

import io.micrometer.core.annotation.Timed;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.uva.sne.vre4eic.model.GraphStats;
import nl.uva.sne.vre4eic.model.ProcessingStatus;
import nl.uva.sne.vre4eic.service.ConvertService;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    private ConvertService service;

//    http://localhost:8080/rest/convert?catalogue_url=%20https://ckan-d4s.d4science.org&mapping_url=https://raw.githubusercontent.com/skoulouzis/CatMap/master/etc/Mapping62.x3ml&generator_url=https://raw.githubusercontent.com/skoulouzis/CatMap/master/etc/generator.xml
//    http://localhost:8080/catalogue_mapper/convert?catalogue_url=%20https://ckan-d4s.d4science.org&mapping_url=https://raw.githubusercontent.com/skoulouzis/CatMap/master/etc/Mapping62.x3ml&generator_url=https://raw.githubusercontent.com/skoulouzis/CatMap/master/etc/generator.xml
    @RequestMapping(value = "/convert", method = RequestMethod.GET, params = {"catalogue_url", "mapping_url", "generator_url"})
    @GetMapping("/")
    public @ResponseBody
    ProcessingStatus convert(@RequestParam(value = "catalogue_url") String catalogueURL,
            @RequestParam(value = "mapping_url") String mappingURL,
            @RequestParam(value = "generator_url") String generatorURL) {
        try {
            ProcessingStatus status = service.doProcess(catalogueURL, mappingURL, generatorURL);
            return status;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConvertController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | KeeperException | InterruptedException ex) {
            Logger.getLogger(ConvertController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "/get_stats", method = RequestMethod.GET, params = {"rdf_url"})
    @GetMapping("/")
    public @ResponseBody
    GraphStats getRDFStats(@RequestParam(value = "rdf_url") String rdfURL) {
        return null;
    }
}
