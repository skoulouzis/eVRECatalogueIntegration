/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.cat_exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Executor implements Watcher, Runnable, IConfigListener {

    private String znode;
    private final ConfigMonitor dm;
    private final ZooKeeper zk;
    private final String filename;
    private static final String TASK_QUEUE_NAME = "ckan_task_queue";
    private final File output;
    private final String rabbimqHost;

    public Executor(String rabbimqHost, String hostPort, String znode, String filename) throws KeeperException, IOException {
        this.filename = filename;
        zk = new ZooKeeper(hostPort, 3000, this);
        dm = new ConfigMonitor(zk, znode, null, this);
        this.output = new File(System.getProperty("java.io.tmpdir") + File.separator + "cerif");
        output.mkdirs();
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

    @Override
    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void exists(byte[] data) {
        if (data == null) {
            Logger.getLogger(Executor.class.getName()).log(Level.INFO, "Killing worker");
        } else {
            try {
                Logger.getLogger(Executor.class.getName()).log(Level.INFO, "Stopping worker");
                try {
                    try (FileOutputStream fos = new FileOutputStream(filename)) {
                        fos.write(data);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Executor.class.getName()).log(Level.SEVERE, null, ex);
                }
                Logger.getLogger(Executor.class.getName()).log(Level.INFO, "Starting worker");
                new Worker(rabbimqHost, TASK_QUEUE_NAME, output.getAbsolutePath(), filename, "").consume();
            } catch (IOException | TimeoutException ex) {
                Logger.getLogger(Executor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
