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
import org.w3c.dom.Element;

public class Worker {

    private final String rabbitMQHost;
    private final String taskQeueName;
    private final String outputRfdFolder;
    private File mappingsFile;
    private File generatorPathFile;

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
    public Worker(String rabbitMQHost, String taskQeueName, String output, File confFolder) {
        this.taskQeueName = taskQeueName;
        this.rabbitMQHost = rabbitMQHost;
        this.outputRfdFolder = output;
        Logger.getLogger(Worker.class.getName()).log(Level.INFO, "Consuming from qeue: {0}", taskQeueName);
        File[] files = confFolder.listFiles();
        for (File f : files) {
            if (f.getName().equals("mapping")) {
                mappingsFile = f;
                Logger.getLogger(Worker.class.getName()).log(Level.INFO, "Using mapping file: {0}. File len: {1}", new Object[]{f.getAbsolutePath(), f.length()});
            } else if (f.getName().equals("generator")) {
                generatorPathFile = f;
                Logger.getLogger(Worker.class.getName()).log(Level.INFO, "Using generator file: " + f.getAbsolutePath() + ". File len: " + f.length());
            }
        }

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
                    X3MLEngine.Output rdf = convert(message);
                    String fileName = UUID.randomUUID().toString();
                    rdf.write(new PrintStream(new File(outputRfdFolder + File.separator + fileName + ".rdf")), "application/rdf+xml");
                } catch (IOException | ParserConfigurationException | SAXException ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (channel.isOpen()) {
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            }
        };
        channel.basicConsume(taskQeueName, false, consumer);
    }

    private X3MLEngine.Output convert(String task) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        final int UUID_SIZE = 2;

        X3MLEngine.REPORT_PROGRESS = true;
        X3MLEngine engine;
        engine = createEngine(mappingsFile);

        Generator policy;
        if (generatorPathFile == null || !generatorPathFile.exists() || generatorPathFile.length() < 1) {
            policy = X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(UUID_SIZE));
        } else {
            policy = X3MLGeneratorPolicy.load(new FileInputStream(generatorPathFile), X3MLGeneratorPolicy.createUUIDSource(UUID_SIZE));
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

    private X3MLEngine createEngine(File mappingsFile) throws FileNotFoundException {
        return X3MLEngine.load(new FileInputStream(mappingsFile));
    }

}
