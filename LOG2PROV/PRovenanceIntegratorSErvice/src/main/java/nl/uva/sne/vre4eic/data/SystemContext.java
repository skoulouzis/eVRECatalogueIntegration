/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.data;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author S. Koulouzis
 */
public class SystemContext {

    /**
     * @return the logs
     */
    public Logs getLogs() {
        return logs;
    }

    /**
     * @param logs the logs to set
     */
    public void setLogs(Logs logs) {
        this.logs = logs;
    }

    /**
     * @return the services
     */
    public Collection<ServiceContext> getServices() {
        return services;
    }

    /**
     * @param services the services to set
     */
    public void setServices(Collection<ServiceContext> services) {
        this.services = services;
    }
    private Logs logs;
    private Collection<ServiceContext> services;
}
