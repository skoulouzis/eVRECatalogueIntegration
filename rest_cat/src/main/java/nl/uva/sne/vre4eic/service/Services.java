/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.service;

import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import nl.uva.sne.vre4eic.cat_exporter.ExportDocTask;
import nl.uva.sne.vre4eic.model.ProcessingStatus;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class Services {

    @Autowired
    ThreadPoolTaskExecutor exec;

    @Autowired
    CachingConnectionFactory connectionFactory;

    @Autowired
    MetricsEndpoint endpoint;

    @Autowired
    MeterRegistry meterRegistry;

    Map<String, Future<String>> taskMap = new HashMap<>();

    public ProcessingStatus doProcess(String catalogueURL) throws MalformedURLException {
        Future<String> retdouble = taskMap.get(catalogueURL);

        if (retdouble == null) {
            ExportDocTask task = new ExportDocTask(catalogueURL, connectionFactory.getRabbitConnectionFactory());
            retdouble = exec.submit(task);
            taskMap.put(catalogueURL, retdouble);
        }
        ProcessingStatus process = new ProcessingStatus();
        process.setCatalogueURL(new URL(catalogueURL));
//        Set<String> names = endpoint.listNames().getNames();
//        for (String name : names) {
//            System.err.println("Name: " + name);
//        }
//
//        MetricsEndpoint.MetricResponse metric = endpoint.metric("rabbitmq.acknowledged", null);
//        System.err.println("------------- rabbitmq.acknowledged -------------------------");
//        Iterator<MetricsEndpoint.AvailableTag> tags = metric.getAvailableTags().iterator();
//        while (tags.hasNext()) {
//            MetricsEndpoint.AvailableTag tag = tags.next();
//            System.err.println("tag: " + tag.getTag());
//            Set<String> vals = tag.getValues();
//            for (String v : vals) {
//                System.err.println("value: " + v);
//            }
//        }
//
//        List<MetricsEndpoint.Sample> measurements = metric.getMeasurements();
//        for (MetricsEndpoint.Sample s : measurements) {
//            System.err.println(s.getStatistic().name() + ": " + s.getValue());
//        }
//
//        System.err.println("------------- rabbitmq.published -------------------------");
//        metric = endpoint.metric("rabbitmq.published", null);
//        tags = metric.getAvailableTags().iterator();
//        while (tags.hasNext()) {
//            MetricsEndpoint.AvailableTag tag = tags.next();
//            System.err.println("tag: " + tag.getTag());
//            Set<String> vals = tag.getValues();
//            for (String v : vals) {
//                System.err.println("value: " + v);
//            }
//        }
//
//        measurements = metric.getMeasurements();
//        for (MetricsEndpoint.Sample s : measurements) {
//            System.err.println(s.getStatistic().name() + ": " + s.getValue());
//        }
//
//        System.err.println("------------- rabbitmq.consumed -------------------------");
//        metric = endpoint.metric("rabbitmq.consumed", null);
//        tags = metric.getAvailableTags().iterator();
//        while (tags.hasNext()) {
//            MetricsEndpoint.AvailableTag tag = tags.next();
//            System.err.println("tag: " + tag.getTag());
//            Set<String> vals = tag.getValues();
//            for (String v : vals) {
//                System.err.println("value: " + v);
//            }
//        }
//
//        measurements = metric.getMeasurements();
//        for (MetricsEndpoint.Sample s : measurements) {
//            System.err.println(s.getStatistic().name() + ": " + s.getValue());
//        }
//
//        System.err.println("------------- rabbitmq.consumed -------------------------");
//        metric = endpoint.metric("rabbitmq.consumed", null);
//        tags = metric.getAvailableTags().iterator();
//        while (tags.hasNext()) {
//            MetricsEndpoint.AvailableTag tag = tags.next();
//            System.err.println("tag: " + tag.getTag());
//            Set<String> vals = tag.getValues();
//            for (String v : vals) {
//                System.err.println("value: " + v);
//            }
//        }
//
//        measurements = metric.getMeasurements();
//        for (MetricsEndpoint.Sample s : measurements) {
//            System.err.println(s.getStatistic().name() + ": " + s.getValue());
//        }
//
//        System.err.println("------------- meters -------------------------");
//        List<Meter> meters = meterRegistry.getMeters();
//        for (Meter m : meters) {
//            System.err.println(m.getId());
//            Iterable<Measurement> measure = m.measure();
//            for (Measurement me : measure) {
//                System.err.println(me.getStatistic());
//                System.err.println(me.getStatistic().name());
//                System.err.println(me.getValue());
//            }
//        }

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
