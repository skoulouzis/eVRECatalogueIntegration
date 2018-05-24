/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.cat_exporter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author S. Koulouzis
 */
public class Task {

    private static final String CKAN_TASK_QUEUE_NAME = "ckan_task_queue";
    private static String catalogueURL;
    private static String rabbitMQHost;

    public static void main(String[] argv) {

        try {

            Options options = new Options();

            Option cat = new Option("c", "catalogue", true, "catalogue url");
            cat.setRequired(true);
            options.addOption(cat);

            Option rabbitHost = new Option("r", "rabbit_host", true, "rabbitMQ host");
            rabbitHost.setRequired(true);
            options.addOption(rabbitHost);

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

            setCatalogueURL(cmd.getOptionValue("catalogue"));
            setRabbitMQHost(cmd.getOptionValue("rabbit_host"));

            Logger.getLogger(Task.class.getName()).log(Level.INFO, "catalogue url: {0}", catalogueURL);
            Logger.getLogger(Task.class.getName()).log(Level.INFO, "rabbitMQ host: {0}", rabbitMQHost);

            doit();

        } catch (MalformedURLException ex) {
            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | InterruptedException | TimeoutException ex) {
            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getTaskQueName() {
        return CKAN_TASK_QUEUE_NAME;
    }

    private static void sendFile(File file, String rabbitmqHost) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqHost);
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            String qName = getTaskQueName();
            channel.queueDeclare(qName, true, false, false, null);

            byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            Logger.getLogger(Task.class.getName()).log(Level.INFO, "Sending {0} to {1}", new Object[]{file.getAbsolutePath(), qName});
            String message = new String(encoded, "UTF-8");

            channel.basicPublish("", qName,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));

        }
    }

    private static void monitorDirectory(String path) throws IOException, InterruptedException, TimeoutException {
        Path faxFolder = Paths.get(path);
        WatchService watchService = FileSystems.getDefault().newWatchService();
        faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        boolean valid = true;
        do {
            WatchKey watchKey = watchService.take();

            for (WatchEvent event : watchKey.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
                if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
                    String fileName = event.context().toString();
                    sendFile(new File(path + File.separator + fileName), rabbitMQHost);
                }
            }
            valid = watchKey.reset();

        } while (valid);
    }

    /**
     * @param aCatalogueURL the catalogueURL to set
     */
    public static void setCatalogueURL(String aCatalogueURL) {
        catalogueURL = aCatalogueURL;
    }

    /**
     * @param aRabbitMQHost the rabbitMQHost to set
     */
    public static void setRabbitMQHost(String aRabbitMQHost) {
        rabbitMQHost = aRabbitMQHost;
    }

    public static void doit() throws MalformedURLException, IOException, InterruptedException, TimeoutException {
        File out = new File(System.getProperty("java.io.tmpdir") + File.separator + "ckan" + new URL(catalogueURL).getHost());
        out.mkdirs();
        Logger.getLogger(Task.class.getName()).log(Level.INFO, "Documents will be saved in: {0}", out.getAbsolutePath());

        Thread exp = new Thread(new ExportDocTask(catalogueURL, out));
        exp.start();

        monitorDirectory(out.getAbsolutePath());

        exp.join();
    }
}
