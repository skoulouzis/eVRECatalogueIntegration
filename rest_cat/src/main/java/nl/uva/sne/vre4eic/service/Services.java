/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import nl.uva.sne.vre4eic.cat_exporter.ExportDocTask;
import nl.uva.sne.vre4eic.model.ProcessingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class Services {

    @Autowired
    ThreadPoolTaskExecutor exec;

    Map<String, Future<String>> taskMap = new HashMap<>();

    public ProcessingStatus doProcess(String catalogueURL) throws MalformedURLException {
        Future<String> retdouble = taskMap.get(catalogueURL);

        if (retdouble == null) {
            ExportDocTask task = new ExportDocTask(catalogueURL);
            retdouble = exec.submit(task);
            taskMap.put(catalogueURL, retdouble);
        }
        ProcessingStatus process = new ProcessingStatus();
        process.setCatalogueURL(new URL(catalogueURL));
        if (retdouble.isDone()) {
            taskMap.remove(catalogueURL);
            process.setStatus("FINISHED");
            return process;
        } else {
            process.setStatus("WORKING");
            return process;
        }
    }

}
