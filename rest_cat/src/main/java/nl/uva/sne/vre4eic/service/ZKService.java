/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import nl.uva.sne.vre4eic.rest.ConvertController;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZKService {

    @Autowired
    private ZooKeeper zk;

    public void createParent(String confParentPath) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(confParentPath, true);
        if (stat == null) {
            zk.create(confParentPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        }

    }

    public void create(String path, byte[] data) throws KeeperException,
            InterruptedException {
        Stat stat = zk.exists(path, true);
        if (stat == null) {
            Logger.getLogger(ZKService.class.getName()).log(Level.INFO, "Creating node: {0}. Data len: {1}", new Object[]{path, data.length});

            zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        } else {
            Logger.getLogger(ZKService.class.getName()).log(Level.INFO, "Deleteing node: {0}", path);
            zk.delete(path, 0);
            Logger.getLogger(ZKService.class.getName()).log(Level.INFO, "Creating node: {0}. Data len: {1}", new Object[]{path, data.length});
            zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
//            zk.setData(path, data, 0);
        }
    }

}
