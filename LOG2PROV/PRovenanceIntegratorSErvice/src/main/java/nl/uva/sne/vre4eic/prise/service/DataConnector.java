package nl.uva.sne.vre4eic.prise.service;

import nl.uva.sne.vre4eic.data.RESTService;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;

public class DataConnector {
    //Test InfluxDB connection. This is only for an example

    public static void connect(RESTService restservice) {
        InfluxDB influxDB = InfluxDBFactory.connect(restservice.getEndpoint());
        Pong response = influxDB.ping();
        if (response.getVersion().equalsIgnoreCase("unknown")) {
            System.out.println("Error pinging server: " + restservice.getEndpoint());
        }
    }
}
