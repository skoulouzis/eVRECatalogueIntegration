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
public class WorkflowContext {

    /**
     * @return the provenance
     */
    public Provenance getProvenance() {
        return provenance;
    }

    /**
     * @param provenance the provenance to set
     */
    public void setProvenance(Provenance provenance) {
        this.provenance = provenance;
    }

    /**
     * @return the workflow
     */
    public Workflow getWorkflow() {
        return workflow;
    }

    /**
     * @param workflow the workflow to set
     */
    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
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
    private Provenance provenance;
    private Workflow workflow;
    private Logs logs;
}
