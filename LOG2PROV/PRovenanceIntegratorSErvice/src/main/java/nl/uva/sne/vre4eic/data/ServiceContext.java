/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.data;

/**
 *
 * @author S. Koulouzis
 */
public class ServiceContext {

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

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
    private String id;
    private Logs logs;
}
