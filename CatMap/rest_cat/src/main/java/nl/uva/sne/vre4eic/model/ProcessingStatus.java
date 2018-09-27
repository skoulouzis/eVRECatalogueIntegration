/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.MicrometerMetricsCollector;
import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessingStatus {

    public ProcessingStatus() {
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        io.micrometer.core.instrument.MeterRegistry
//        JmxMeterRegistry registry = new JmxMeterRegistry();
//        MicrometerMetricsCollector metrics;
//ConnectionFactory connectionFactory = new ConnectionFactory();
//connectionFactory.setMetricsCollector(metrics);
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
    private URL catalogueURL;

    /**
     * @return the catalogueURL
     */
    public URL getCatalogueURL() {
        return catalogueURL;
    }

    /**
     * @param catalogueURL the catalogueURL to set
     */
    public void setCatalogueURL(URL catalogueURL) {
        this.catalogueURL = catalogueURL;
    }

}
