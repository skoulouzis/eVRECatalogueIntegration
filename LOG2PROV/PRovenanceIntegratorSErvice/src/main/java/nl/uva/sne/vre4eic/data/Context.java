/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.data;

import java.util.List;



/**
 *
 * @author S. Koulouzis
 */
public class Context {

    /**
     * @return the workflowContext
     */
    public WorkflowContext getWorkflowContext() {
        return workflowContext;
    }

    /**
     * @param workflowContext the workflowContext to set
     */
    public void setWorkflowContext(WorkflowContext workflowContext) {
        this.workflowContext = workflowContext;
    }

    /**
     * @return the systemContext
     */
    public List<SystemContext> getSystemContext() {
        return systemContext;
    }

    /**
     * @param systemContext the systemContext to set
     */
    public void setSystemContext(List<SystemContext> systemContext) {
        this.systemContext = systemContext;
    }
    
    private WorkflowContext workflowContext;
    
    private List<SystemContext> systemContext;
}
