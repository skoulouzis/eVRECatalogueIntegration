/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.service;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import nl.uva.sne.vre4eic.data.Provenance;
import nl.uva.sne.vre4eic.prise.util.Util;
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

    private String insertProvFile(File provFile) throws IOException {
        if (Util.urlExists(logRepoURI)) {
            Sardine sardine = SardineFactory.begin();
            String webdavFolder = "prov";
            sardine.put(logRepoURI + "/" + webdavFolder + "/" + provFile.getName(), FileUtils.readFileToByteArray(provFile));
            return logRepoURI + "/" + webdavFolder + "/" + provFile.getName();
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
