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
import eu.delving.x3ml.X3MLEngine;
import eu.delving.x3ml.X3MLGeneratorPolicy;
import eu.delving.x3ml.engine.Generator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author S. Koulouzis
 */
public class CerifConverterWorker {

    private static final String TASK_QUEUE_NAME = "ckan_task_queue";
    private static String rabbitMQHost;
    private static String mappingsPath;
    private static String generatorPathPolicy;
    private static File output;

    public static void main(String[] argv) {
        try {
            Options options = new Options();
            Option rabbitHost = new Option("r", "rabbit_host", true, "rabbitMQ host");
            rabbitHost.setRequired(true);
            options.addOption(rabbitHost);

            Option mappings = new Option("m", "mappings", true, "mappings path");
            mappings.setRequired(true);
            options.addOption(mappings);

            Option generator = new Option("g", "generator", true, "generator path policy");
            generator.setRequired(true);
            options.addOption(generator);

            CommandLineParser parser = new BasicParser();
            HelpFormatter formatter = new HelpFormatter();
            CommandLine cmd;

            try {
                cmd = parser.parse(options, argv);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
                formatter.printHelp("utility-name", options);
                System.exit(1);
                return;
            }

            rabbitMQHost = cmd.getOptionValue("rabbit_host");
            Logger.getLogger(CerifConverterWorker.class.getName()).log(Level.INFO, "rabbitMQ host: {0}", rabbitMQHost);
            mappingsPath = cmd.getOptionValue("mappings");
            Logger.getLogger(CerifConverterWorker.class.getName()).log(Level.INFO, "mappings path: {0}", mappingsPath);
            generatorPathPolicy = cmd.getOptionValue("generator");
            Logger.getLogger(CerifConverterWorker.class.getName()).log(Level.INFO, "generator policy path: {0}", generatorPathPolicy);
            output = new File(System.getProperty("java.io.tmpdir") + File.separator + "cerif");
            output.mkdirs();
            consume();

        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(CerifConverterWorker.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static X3MLEngine.Output convert(String task) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
//        final String MAPPINGS_PATH = "/home/alogo/Downloads/ckan_d4sincen_cat/Mapping62.x3ml";
//        final String GENERATOR_POLICY_PATH = "/home/alogo/Downloads/ckan_d4sincen_cat/generator.xml";  //if empty, the generator will not be used

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

    private static void consume() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQHost);
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        channel.basicQos(1);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException, FileNotFoundException {

                byte[] decodedBytes = Base64.decodeBase64(body);
                String message = new String(decodedBytes, "UTF-8");
                try {
                    X3MLEngine.Output rdf = convert(message);
                    String fileName = UUID.randomUUID().toString();
                    rdf.write(new PrintStream(new File(output + File.separator + fileName + ".rdf")), "application/rdf+xml");
                } catch (ParserConfigurationException | SAXException ex) {
                    Logger.getLogger(CerifConverterWorker.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
    }

    private enum outputFormat {
        RDF_XML,
        NTRIPLES,
        TURTLE
    }

    private enum outputStream {
        SYSTEM_OUT,
        FILE,
        DISABLED
    }

    private static X3MLEngine createEngine(String path) throws FileNotFoundException {
        return X3MLEngine.load(new FileInputStream(new File(path)));
    }

    private static Element createDocument(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        return documentBuilderFactory().newDocumentBuilder().parse(in).getDocumentElement();
    }

    private static DocumentBuilderFactory documentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory;
    }
}
