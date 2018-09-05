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
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONObject;
import org.w3c.dom.Element;

public class Worker {

    private final String rabbitMQHost;
    private final String taskQeueName;
    private final String outputRfdFolder;
    private final String ftpHost;
    private String ftpUser;
    private String ftpPass;

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
    public Worker(String rabbitMQHost, String ftpHost, String ftpUser, String ftpPass, String taskQeueName, String output) throws IOException {
        this.taskQeueName = taskQeueName;
        this.rabbitMQHost = rabbitMQHost;
        this.outputRfdFolder = output;
        if (ftpHost == null) {
            this.ftpHost = System.getenv("FTP_HOST");
        } else {
            this.ftpHost = ftpHost;
            this.ftpUser = ftpUser;
            this.ftpPass = ftpPass;
        }

        Logger.getLogger(Worker.class.getName()).log(Level.INFO, "Consuming from qeue: {0}", taskQeueName);
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
                JSONObject jObject = new JSONObject(message);
                File mapping = null;
                File generator = null;
//                Logger.getLogger(Worker.class.getName()).log(Level.INFO, "message: {0}", message);
                try {
                    byte[] mappingData = getBytes(new URL(jObject.getString("mappingURL")));
                    byte[] generatorData = getBytes(new URL(jObject.getString("generatorURL")));

                    mapping = File.createTempFile("mapping", ".tmp");
                    FileUtils.writeByteArrayToFile(mapping, mappingData);

                    generator = File.createTempFile("generator", ".tmp");
                    FileUtils.writeByteArrayToFile(generator, generatorData);

                    X3MLEngine.Output rdf = convert(jObject.getString("payload"), mapping, generator);
//                    Logger.getLogger(Worker.class.getName()).log(Level.INFO, "rdf: {0}", rdf);

                    String path = new URL(jObject.getString("mappingURL")).getPath();
                    String mappingName = path.substring(path.lastIndexOf('/') + 1);

                    String fileName = mappingName + "_" + UUID.randomUUID().toString();
                    File rdfFile = new File(outputRfdFolder + File.separator + fileName + ".rdf");
                    Logger.getLogger(Worker.class.getName()).log(Level.INFO, "fileName: {0}", fileName);
                    try {
                        rdf.write(new PrintStream(rdfFile), "application/rdf+xml");
                    } catch (Exception ex) {
                        Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                        System.exit(-1);

                    }
                    Logger.getLogger(Worker.class.getName()).log(Level.INFO, "Saved file :{0}", rdfFile.getAbsolutePath());
                    if (ftpHost != null) {
                        FTPClient client = new FTPClient();

                        client.connect(ftpHost);
//                        client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
                        Logger.getLogger(Worker.class.getName()).log(Level.INFO, "Connected to :{0}", ftpHost);
                        String ftpUserEnv = System.getenv("FTP_USER_NAME");
                        if (ftpUserEnv != null) {
                            ftpUser = ftpUserEnv;
                        }
                        String ftpPasswordEnv = System.getenv("FTP_USER_PASS");
                        if (ftpPasswordEnv != null) {
                            ftpPasswordEnv = ftpPass;
                        }
                        client.login(ftpUser, ftpPass);
                        client.enterLocalPassiveMode();

                        client.changeWorkingDirectory("/" + taskQeueName);
//                        channel.queueDeclare("conversion_result", true, false, false, null);
                        if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
                            client.makeDirectory("/" + taskQeueName);
                            client.changeWorkingDirectory("/" + taskQeueName);

//                            channel.basicPublish("", "ftp://" + ftpUser + ":" + ftpPass + "@" + ftpHost,
//                                    MessageProperties.PERSISTENT_TEXT_PLAIN,
//                                    message.getBytes("UTF-8"));
                        }

                        FileInputStream fis = new FileInputStream(rdfFile);

                        client.storeFile(fileName + ".rdf", fis);

                        client.logout();
                    }

                } catch (IOException | ParserConfigurationException | SAXException ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, "------------EXIT----------------");
                    System.exit(-1);
                } finally {
                    if (channel.isOpen()) {
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                    if (mapping != null) {
                        mapping.delete();
                    }
                    if (generator != null) {
                        generator.delete();
                    }
                }
            }
        };
        channel.basicConsume(taskQeueName, false, consumer);
    }

    private X3MLEngine.Output convert(String task, File mappingsFile, File generatorPathFile) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
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
