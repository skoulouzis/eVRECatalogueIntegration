/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.conf;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MetricsCollector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

//@Configuration
//@PropertySources({
//    @PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
//    ,
//    @PropertySource(value = "file:etc/application.properties", ignoreResourceNotFound = true)
//})
public class RabbitConfig {

//    @Value("${rabbit.host}")
//    private String rabbitHost;
//    @Value("${rabbit.username}")
//    private String rabbitUserName;
//
//    @Bean
//    public ConnectionFactory getConnectionFactory() {
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost(rabbitHost);
////     MetricsCollector metricsCollector = new Inf
//        factory.setUsername(rabbitUserName);
////factory.setPassword(password);
////factory.setMetricsCollector(metricsCollector);
//        return factory;
//    }
}
