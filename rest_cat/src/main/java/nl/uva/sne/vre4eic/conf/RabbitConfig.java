/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.conf;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MetricsCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
//     MetricsCollector metricsCollector = new Inf
//     factory.setUsername("");
//factory.setPassword(password);
//factory.setMetricsCollector(metricsCollector);
        return factory;
    }
}
