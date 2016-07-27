package com.virtusa.vhub.common.entity;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "commandResponse")
public class CommandResponseEntity implements Serializable {
    public enum AckType {
        INITIAL, FINAL;
    }

    private static final long serialVersionUID = 1L;
    private CommandRequestEntity command;
    private AckType ackType;
    private List<CommandExecutionStatus> commandResults;

    public CommandRequestEntity getCommand() {
        return command;
    }

    public void setCommand(final CommandRequestEntity command) {
        this.command = command;
    }

    public List<CommandExecutionStatus> getCommandResults() {
        return commandResults;
    }

    public void setCommandResults(final List<CommandExecutionStatus> commandResults) {
        this.commandResults = commandResults;
    }

    public AckType getAckType() {
        return ackType;
    }

    public void setAckType(final AckType ackType) {
        this.ackType = ackType;
    }

}
