/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.File;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author S. Koulouzis
 */
@Service
public class Log2ProvService {

    @Autowired
    CachingConnectionFactory connectionFactory;
    private final String requestQeueName = "log2prov";

    public String convert(String fileName, byte[] logFileBytes) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory rabbitConnectionFactory = connectionFactory.getRabbitConnectionFactory();
        try (Connection connection = rabbitConnectionFactory.newConnection(); Channel channel = connection.createChannel()) {

            JSONObject json = new JSONObject();
            json.put("filename", fileName);
            json.put("payload", new String(Base64.encodeBase64(logFileBytes), "UTF-8"));

            byte[] encoded = (Base64.encodeBase64(json.toString().getBytes()));
            String message = new String(encoded, "UTF-8");

            String replyQueueName = channel.queueDeclare().getQueue();
            channel.queuePurge(requestQeueName);

            final String corrId = UUID.randomUUID().toString();

            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .build();
            channel.basicPublish("", requestQeueName, props, message.getBytes("UTF-8"));

            final BlockingQueue<String> response = new ArrayBlockingQueue(1);

            channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    if (properties.getCorrelationId().equals(corrId)) {
                        response.offer(new String(body, "UTF-8"));
                    }
                }
            });
            String messageDataDecoded = new String(Base64.decodeBase64(response.take()));
            return messageDataDecoded;
        }
    }
}
