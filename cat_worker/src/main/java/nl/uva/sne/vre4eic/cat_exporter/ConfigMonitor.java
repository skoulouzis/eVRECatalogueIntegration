/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.cat_exporter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.data.Stat;

public class ConfigMonitor implements Watcher, StatCallback {

    ZooKeeper zk;

    String znode;

    Watcher chainedWatcher;

    boolean dead;

    Executor listener;

    byte prevData[];

    public ConfigMonitor(ZooKeeper zk, String znode, Watcher chainedWatcher,
            Executor listener) {
        this.zk = zk;
        this.znode = znode;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;
        zk.exists(znode, true, this, null);
    }

    @Override
    public void process(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Watcher.Event.EventType.None) {
            // We are are being told that the state of the
            // connection has changed
            switch (event.getState()) {
                case SyncConnected:
                    // In this particular example we don't need to do anything
                    // here - watches are automatically re-registered with 
                    // server and any watches triggered while the client was 
                    // disconnected will be delivered (in order of course)
                    break;
                case Expired:
                    // It's all over
                    dead = true;
                    listener.closing(KeeperException.Code.SessionExpired);
                    break;
            }
        } else {
            if (path != null && path.equals(znode)) {
                // Something has changed on the node, let's find out
                zk.exists(znode, true, this, null);
            }
        }
        if (chainedWatcher != null) {
            chainedWatcher.process(event);
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        boolean exists;
        switch (rc) {
            case Code.Ok:
                exists = true;
                break;
            case Code.NoNode:
                exists = false;
                break;
            case Code.SessionExpired:
            case Code.NoAuth:
                dead = true;
                listener.closing(rc);
                return;
            default:
                // Retry errors
                zk.exists(znode, true, this, null);
                return;
        }

        if (exists) {
            try {
                Map<String, byte[]> conf = new HashMap();

                Stat parent = zk.exists(znode, false);
                while (parent == null) {
                    parent = zk.exists(znode, false);
                    Thread.sleep(100);
                }
                List<String> children = zk.getChildren(znode, false, null);
                while (children == null || children.size() <= 1) {
                    children = zk.getChildren(znode, false, null);
                    Logger.getLogger(ConfigMonitor.class.getName()).log(Level.WARNING, "Node: {0} has no children. List size: {1}", new Object[]{znode, children.size()});
                    Thread.sleep(100);
                }
                for (String ch : children) {
                    byte[] b = zk.getData(znode + "/" + ch, false, null);
                    Logger.getLogger(ConfigMonitor.class.getName()).log(Level.INFO, "Children node name: {0}. Data len: {1}", new Object[]{ch, b.length});
                    switch (ch) {
                        case "mapping":
                            if (b == null) {
                                throw new NullPointerException("mapping data is null!");
                            }
                            Logger.getLogger(ConfigMonitor.class.getName()).log(Level.INFO, "Conf: {0}", conf.keySet());
                            conf.put(ch, b);
                            break;
                        case "generator":
                            if (b == null) {
                                throw new NullPointerException("generator data is null!");
                            }
                            Logger.getLogger(ConfigMonitor.class.getName()).log(Level.INFO, "Conf: {0}", conf.keySet());
                            conf.put(ch, b);
                            break;
                        case "queueName":
                            if (b == null) {
                                throw new NullPointerException("queueName data is null!");
                            }
                            Logger.getLogger(ConfigMonitor.class.getName()).log(Level.INFO, "Conf: {0}", conf.keySet());
                            conf.put(ch, b);
                            break;
                        default:
                            break;
                    }
                }
                Logger.getLogger(ConfigMonitor.class.getName()).log(Level.INFO, "Conf: {0}", conf.keySet());
                listener.setConf(conf);
            } catch (KeeperException | IOException | TimeoutException | InterruptedException ex) {
                Logger.getLogger(ConfigMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
