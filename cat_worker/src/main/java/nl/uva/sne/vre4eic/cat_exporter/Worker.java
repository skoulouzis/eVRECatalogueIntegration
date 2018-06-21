/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.cat_exporter;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.codec.binary.Base64;
import org.xml.sax.SAXException;
import eu.delving.x3ml.X3MLEngine;
import eu.delving.x3ml.X3MLGeneratorPolicy;
import eu.delving.x3ml.engine.Generator;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import nl.uva.sne.vre4eic.cat_exporter.CerifConverterMain;
import org.w3c.dom.Element;

public class Worker {

    private final String rabbitMQHost;
    private final String taskQeueName;
    private final String outputRfdFolder;
    private final String mappingsPath;
    private final String generatorPathPolicy;

//        private enum outputFormat {
//        RDF_XML,
//        NTRIPLES,
//        TURTLE
//    }
//
//    private enum outputStream {
//        SYSTEM_OUT,
//        FILE,
//        DISABLED
//    }
    public Worker(String rabbitMQHost, String taskQeueName, String output, String mappingsPath, String generatorPathPolicy) {
        this.taskQeueName = taskQeueName;
        this.rabbitMQHost = rabbitMQHost;
        this.outputRfdFolder = output;
        this.mappingsPath = mappingsPath;
        this.generatorPathPolicy = generatorPathPolicy;
    }

    public void consume() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQHost);
//        MeterRegistry registry = new InfluxMeterRegistry(InfluxConfig.DEFAULT, Clock.SYSTEM);
//
//        MicrometerMetricsCollector metricsCollector = new MicrometerMetricsCollector(registry);
//        factory.setMetricsCollector(metricsCollector);

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(taskQeueName, true, false, false, null);

        channel.basicQos(1);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException, FileNotFoundException {

                byte[] decodedBytes = Base64.decodeBase64(body);
                String message = new String(decodedBytes, "UTF-8");
                try {
//                    X3MLEngine.Output rdf = convert(message);
//                    String fileName = UUID.randomUUID().toString();
//                    rdf.write(new PrintStream(new File(outputRfdFolder + File.separator + fileName + ".rdf")), "application/rdf+xml");
                } catch (Exception ex) {
                    Logger.getLogger(CerifConverterMain.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(taskQeueName, false, consumer);
    }

    private X3MLEngine.Output convert(String task) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        final int UUID_SIZE = 2;

        X3MLEngine.REPORT_PROGRESS = true;
        X3MLEngine engine;
        engine = createEngine(mappingsPath);

        Generator policy;
        if (generatorPathPolicy.isEmpty()) {
            policy = X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(UUID_SIZE));
        } else {
            policy = X3MLGeneratorPolicy.load(new FileInputStream(new File(generatorPathPolicy)), X3MLGeneratorPolicy.createUUIDSource(UUID_SIZE));
        }
        Element doc = createDocument(new ByteArrayInputStream(task.getBytes()));
        X3MLEngine.Output output = engine.execute(doc, policy);
        return output;

    }

    private Element createDocument(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        return documentBuilderFactory().newDocumentBuilder().parse(in).getDocumentElement();
    }

    private DocumentBuilderFactory documentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory;
    }

    private X3MLEngine createEngine(String path) throws FileNotFoundException {
        return X3MLEngine.load(new FileInputStream(new File(path)));
    }

}
