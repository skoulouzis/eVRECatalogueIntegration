/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.service;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.uva.sne.vre4eic.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author S. Koulouzis
 */
@Service
public class GitHubLookupService {

    @Async
    public CompletableFuture<User> findUser(String user) throws InterruptedException {

        Logger.getLogger(GitHubLookupService.class.getName()).log(Level.INFO, "Looking up {0}", user);
        User results = new User();
        results.setBlog("blog");
        results.setName(user);
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(results);
    }

}
