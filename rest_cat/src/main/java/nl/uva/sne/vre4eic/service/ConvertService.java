/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.service;

import io.micrometer.core.instrument.MeterRegistry;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import nl.uva.sne.vre4eic.model.ExportDocTask;
import nl.uva.sne.vre4eic.model.ProcessingStatus;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ConvertService {

    @Autowired
    ThreadPoolTaskExecutor exec;

    @Autowired
    CachingConnectionFactory connectionFactory;

//    @Autowired
//    MetricsEndpoint endpoint;
//
    @Autowired
    MeterRegistry meterRegistry;

    Map<String, Future<String>> taskMap = new HashMap<>();

    public ProcessingStatus doProcess(String catalogueURL, String mappingURL, String generatorURL) throws MalformedURLException, IOException, FileNotFoundException, InterruptedException {
        Future<String> convertTask = taskMap.get(catalogueURL);

        if (convertTask == null) {
//            String path = new URL(mappingURL).getPath();
//            String queueName = path.substring(path.lastIndexOf('/') + 1);
            String queueName = "ckan2cerif";
            ExportDocTask task = new ExportDocTask(catalogueURL, connectionFactory.getRabbitConnectionFactory(), queueName, mappingURL, generatorURL);
            convertTask = exec.submit(task);
            taskMap.put(catalogueURL, convertTask);
        }
        ProcessingStatus process = new ProcessingStatus();
        process.setCatalogueURL(new URL(catalogueURL));
        if (convertTask.isDone()) {
            taskMap.remove(catalogueURL);
            process.setStatus("FINISHED");
            return process;
        } else {
            process.setStatus("WORKING");
            return process;
        }
    }

    public Collection<String> listRecords(String catalogueURL) throws MalformedURLException, IOException {
        ExportDocTask task = new ExportDocTask(catalogueURL, connectionFactory.getRabbitConnectionFactory(), null, null, null);
        return task.getExporter(catalogueURL).fetchAllDatasetUUIDs();
    }

}
