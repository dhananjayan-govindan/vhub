package com.virtusa.vhub.common.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "status")
public class CommandExecutionStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private String executorId;
    private String statusCode;
    private String statusDescription;

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(final String executorId) {
        this.executorId = executorId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(final String statusDescription) {
        this.statusDescription = statusDescription;
    }

}
