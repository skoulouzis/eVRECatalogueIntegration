/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.service;

import java.io.File;
import java.util.UUID;
import nl.uva.sne.vre4eic.data.Provenance;
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

    String getProvenanceEndpoint(File provFile) {
        insertProvFile(provFile);
        return logRepoURI;
    }

    private void insertProvFile(File provFile) {

    }

    Provenance getProvenance(File provFile) {
        insertProvFile(provFile);
        Provenance prov = new Provenance();
        prov.setId(UUID.randomUUID().toString());
        prov.setLocation(logRepoURI);
        return prov;
    }

}
