/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.cat_exporter;

import java.io.File;
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
public class CerifConverterMain {

    private static String rabbitMQHost;
    private static String zookeeperHost;
    private static String ftpHost;
    private static String ftpUser;
    private static String ftpPass;

    public static void main(String[] argv) {
        try {
            Options options = new Options();
            Option rabbitHostOption = new Option("r", "rabbit_host", true, "rabbitMQ host");
            rabbitHostOption.setRequired(true);
            options.addOption(rabbitHostOption);

            Option zookeeperHostOption = new Option("z", "zookeeper_host", true, "zookeeper host");
            zookeeperHostOption.setRequired(true);
            options.addOption(zookeeperHostOption);

            Option ftpHostOption = new Option("f", "ftp_host", true, "ftp host");
            ftpHostOption.setRequired(true);
            options.addOption(ftpHostOption);

            Option ftpUserOtion = new Option("u", "ftp_user", true, "ftp user");
            ftpUserOtion.setRequired(false);
            options.addOption(ftpUserOtion);

            Option ftpUPassOtion = new Option("p", "ftp_pass", true, "ftp pass");
            ftpUPassOtion.setRequired(false);
            options.addOption(ftpUPassOtion);
//
//            Option mappings = new Option("m", "mappings", true, "mappings path");
//            mappings.setRequired(true);
//            options.addOption(mappings);
//
//            Option generator = new Option("g", "generator", true, "generator path policy");
//            generator.setRequired(true);
//            options.addOption(generator);
            CommandLineParser parser = new BasicParser();
            HelpFormatter formatter = new HelpFormatter();
            CommandLine cmd;

            try {
                cmd = parser.parse(options, argv);
            } catch (ParseException e) {
                System.err.println(e.getMessage());
                formatter.printHelp("utility-name", options);
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, "------------EXIT----------------");
                System.exit(1);
                return;
            }

            rabbitMQHost = cmd.getOptionValue("rabbit_host");
            Logger.getLogger(CerifConverterMain.class.getName()).log(Level.INFO, "rabbitMQ host: {0}", rabbitMQHost);

            zookeeperHost = cmd.getOptionValue("zookeeper_host");
            Logger.getLogger(CerifConverterMain.class.getName()).log(Level.INFO, "zookeeper host: {0}", zookeeperHost);

            ftpHost = cmd.getOptionValue("ftp_host");
            Logger.getLogger(CerifConverterMain.class.getName()).log(Level.INFO, "ftp host: {0}", ftpHost);

            ftpUser = cmd.getOptionValue("ftp_user");
            Logger.getLogger(CerifConverterMain.class.getName()).log(Level.INFO, "ftp host: {0}", ftpUser);

            ftpPass = cmd.getOptionValue("ftp_pass");
            Logger.getLogger(CerifConverterMain.class.getName()).log(Level.INFO, "ftp host: {0}", ftpPass);

            Logger.getLogger("org.apache.zookeeper").setLevel(Level.WARNING);
            Logger.getLogger("org.apache.hadoop.hbase.zookeeper").setLevel(Level.WARNING);
            Logger.getLogger("org.apache.hadoop.hbase.client").setLevel(Level.WARNING);
            String taskQName = "ckan2cerif";
            File output = new File(System.getProperty("java.io.tmpdir") + File.separator + "cerif");
            output.mkdirs();
            new Worker(rabbitMQHost, ftpHost, ftpUser, ftpPass, taskQName, output.getAbsolutePath()).consume();
//            new Executor(rabbitMQHost, zookeeperHost, ftpHost, ftpUser, ftpPass, "/catmap_conf").run();

//            mappingsPath = cmd.getOptionValue("mappings");
//            Logger.getLogger(CerifConverterWorker.class.getName()).log(Level.INFO, "mappings path: {0}", mappingsPath);
//            generatorPathPolicy = cmd.getOptionValue("generator");
//            Logger.getLogger(CerifConverterWorker.class.getName()).log(Level.INFO, "generator policy path: {0}", generatorPathPolicy);
//            consume();
        } catch (Throwable ex) {
            Logger.getLogger(CerifConverterMain.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, "------------EXIT----------------");
            System.exit(-1);
        }

    }
}
