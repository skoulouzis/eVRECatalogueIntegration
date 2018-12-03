/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.uva.sne.vre4eic.data.Provenance;
import nl.uva.sne.vre4eic.prise.util.Util;
import nl.uva.sne.vre4eic.prise.util.WebDAVClient;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author S. Koulouzis
 */
@Service
public class ProvenanceService {

    @Value("${prov.reposetory.uri:=http://localhost:3030}")
    private String logRepoURI;

    String getProvenanceEndpoint(File provFile) throws IOException {
        insertProvFile(provFile);
        return logRepoURI;
    }

    private String insertProvFile(File provFile) {
        if (Util.urlExists(logRepoURI)) {
            try {
                WebDAVClient c = new WebDAVClient(logRepoURI);
                String webdavFolder = "prov";
                c.putFile(provFile, webdavFolder, "application/vnd.taverna.t2flow+xml");
                return logRepoURI + "/" + webdavFolder + "/" + provFile.getName();
            } catch (Exception ex) {
                Logger.getLogger(ProvenanceService.class.getName()).log(Level.WARNING, null, ex);
                return null;
            }

        }
        return null;
    }

    Provenance getProvenance(File provFile) throws IOException {
        insertProvFile(provFile);
        Provenance prov = new Provenance();
        prov.setId(UUID.randomUUID().toString());
        prov.setLocation(logRepoURI);
        return prov;
    }

}
