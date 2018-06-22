/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.cat_exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Executor implements Watcher, Runnable {

    private String znode;
    private final ConfigMonitor dm;
    private final ZooKeeper zk;
    private final String rabbimqHost;

    public Executor(String rabbimqHost, String zookeeperHost, String znode) throws KeeperException, IOException {
        zk = new ZooKeeper(zookeeperHost, 3000, this);
        dm = new ConfigMonitor(zk, znode, null, this);
        this.rabbimqHost = rabbimqHost;

    }

    @Override
    public void process(WatchedEvent event) {
        dm.process(event);
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (!dm.dead) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
        }
    }

    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

//    public void exists(byte[] data) {
//        if (data == null) {
//            Logger.getLogger(Executor.class.getName()).log(Level.INFO, "Killing worker");
//        } else {
//            Logger.getLogger(Executor.class.getName()).log(Level.INFO, "Stopping worker");
//            try {
//                try (FileOutputStream fos = new FileOutputStream(configFolder)) {
//                    fos.write(data);
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(Executor.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            Logger.getLogger(Executor.class.getName()).log(Level.INFO, "Starting worker");
////            new Worker(rabbimqHost, TASK_QUEUE_NAME, output.getAbsolutePath(), configFolder, "").consume();
//        }
//    }
    public void setConf(Map<String, byte[]> conf) throws IOException, TimeoutException {
        Logger.getLogger(Executor.class.getName()).log(Level.INFO, "Starting worker");
        File configFolder = new File(System.getProperty("java.io.tmpdir") + File.separator + "X3MLEngine_conf_" + Long.toString(System.nanoTime()));
        configFolder.mkdirs();
        FileUtils.writeByteArrayToFile(new File(configFolder, "mapping"), conf.get("mapping"));
        FileUtils.writeByteArrayToFile(new File(configFolder, "generator"), conf.get("generator"));
//        mappingFile = File.

        File output = new File(System.getProperty("java.io.tmpdir") + File.separator + "cerif");
        output.mkdirs();
        new Worker(rabbimqHost, new String(conf.get("queueName"), "UTF-8"), output.getAbsolutePath(), configFolder).consume();
    }
}
