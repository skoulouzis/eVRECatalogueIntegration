package nl.uva.sne.vre4eic.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import nl.uva.sne.vre4eic.amqp.rpc.Caller;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author S. Koulouzis
 */
@Service
public class StatsService {

    @Value("${spring.rabbitmq.host}")
    private String messageBrokerHost;
    @Autowired
    CachingConnectionFactory connectionFactory;

    private final String requestQeueName = "rdf_location";

    public Map<String, String> getStats(String rdf_url) throws IOException, TimeoutException, InterruptedException {
        try (Caller stats = new Caller(messageBrokerHost, requestQeueName)) {
            return stats.call(rdf_url);
        }
    }

}
