/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import nl.uva.sne.vre4eic.model.ExportDocTask;
import nl.uva.sne.vre4eic.model.ProcessingStatus;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.zookeeper.KeeperException;
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

    @Autowired
    ZKService zkService;
//    @Autowired
//    MetricsEndpoint endpoint;
//
//    @Autowired
//    MeterRegistry meterRegistry;
    Map<String, Future<String>> taskMap = new HashMap<>();
    private String queueName;

    public ProcessingStatus doProcess(String catalogueURL, String mappingURL, String generatorURL) throws MalformedURLException, IOException, FileNotFoundException, KeeperException, InterruptedException {
        Future<String> convertTask = taskMap.get(catalogueURL);

        if (convertTask == null) {
            setConvertConf(mappingURL, generatorURL);
            ExportDocTask task = new ExportDocTask(catalogueURL, connectionFactory.getRabbitConnectionFactory(), this.queueName);
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

    private void setConvertConf(String mappingURL, String generatorURL) throws FileNotFoundException, IOException, KeeperException, InterruptedException {
        String confParentPath = "/catmap_conf";
        String mappinghPath = confParentPath + "/mapping";
        String generatorPath = confParentPath + "/generator";
        String queueNamePath = confParentPath + "/queueName";

//        File mappingFile = new File("workspace/CatMap/etc/Mapping62.x3ml");
//        byte[] mappingData = IOUtils.toByteArray(new FileInputStream(mappingFile));
//        File generatorFile = new File("workspace/CatMap/etc/generator.xml");
//        byte[] generatorData = IOUtils.toByteArray(new FileInputStream(generatorFile));
        byte[] mappingData = getBytes(new URL(mappingURL));

        byte[] generatorData = getBytes(new URL(generatorURL));

        zkService.createParent(confParentPath);
        zkService.create(mappinghPath, mappingData);
        zkService.create(generatorPath, generatorData);
        String[] segments = (new URL(mappingURL)).getPath().split("/");
        String mappingName = segments[segments.length - 1];

        this.queueName = "ckan_" + mappingName;
        zkService.create(queueNamePath, this.queueName.getBytes());
    }

    private byte[] getBytes(URL url) throws IOException {

        ByteArrayOutputStream data = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte[] buffer = new byte[1024];
            while (-1 != (n = inputStream.read(buffer))) {
                data.write(buffer, 0, n);
            }
        }
        return data.toByteArray();
    }

}
