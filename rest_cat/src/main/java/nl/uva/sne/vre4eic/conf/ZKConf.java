/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.conf;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 *
 * @author S. Koulouzis
 */
@Configuration
@PropertySources({
    @PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
    ,
    @PropertySource(value = "file:etc/application.properties", ignoreResourceNotFound = true)
})
@ComponentScan("nl.uva.sne.vre4eic")
public class ZKConf {

    @Value("${spring.cloud.zookeeper.connectString}")
    private String zookeeperHost;
//

    @Bean
    public ZooKeeper getZooKeeper() throws IOException, InterruptedException {
        CountDownLatch connectedSignal = new CountDownLatch(1);
        ZooKeeper zoo = new ZooKeeper(zookeeperHost, 8000, new Watcher() {
            @Override
            public void process(WatchedEvent we) {
                if (we.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    connectedSignal.countDown();
                }
            }
        });
        connectedSignal.await();
        return zoo;
    }
}
