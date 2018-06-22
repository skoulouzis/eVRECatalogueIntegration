/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.cat_exporter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.zookeeper.KeeperException;

/**
 *
 * @author S. Koulouzis
 */
public class CerifConverterMain {

    private static String rabbitMQHost;
    private static String zookeeperHost;

    public static void main(String[] argv) {
        try {
            Options options = new Options();
            Option rabbitHostOption = new Option("r", "rabbit_host", true, "rabbitMQ host");
            rabbitHostOption.setRequired(true);
            options.addOption(rabbitHostOption);

            Option zookeeperHostOption = new Option("z", "zookeeper_host", true, "rzookeeper host");
            zookeeperHostOption.setRequired(true);
            options.addOption(zookeeperHostOption);
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
                System.out.println(e.getMessage());
                formatter.printHelp("utility-name", options);
                System.exit(1);
                return;
            }

            rabbitMQHost = cmd.getOptionValue("rabbit_host");
            Logger.getLogger(CerifConverterMain.class.getName()).log(Level.INFO, "rabbitMQ host: {0}", rabbitMQHost);

            zookeeperHost = cmd.getOptionValue("zookeeper_host");
            Logger.getLogger(CerifConverterMain.class.getName()).log(Level.INFO, "zookeeper host: {0}", zookeeperHost);

           
            new Executor(rabbitMQHost, zookeeperHost, "/catmap_conf").run();

//            mappingsPath = cmd.getOptionValue("mappings");
//            Logger.getLogger(CerifConverterWorker.class.getName()).log(Level.INFO, "mappings path: {0}", mappingsPath);
//            generatorPathPolicy = cmd.getOptionValue("generator");
//            Logger.getLogger(CerifConverterWorker.class.getName()).log(Level.INFO, "generator policy path: {0}", generatorPathPolicy);
//            consume();
        } catch (IOException | IllegalArgumentException | KeeperException ex) {
            Logger.getLogger(CerifConverterMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
