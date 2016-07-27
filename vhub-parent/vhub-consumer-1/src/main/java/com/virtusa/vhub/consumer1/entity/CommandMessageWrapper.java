package com.virtusa.vhub.consumer1.entity;

import javax.jms.Destination;

import com.virtusa.vhub.common.entity.CommandRequestEntity;

public class CommandMessageWrapper {
    private CommandRequestEntity command;
    private String messageId;
    private String correlationId;
    private Destination replyTo;
    private String commandCallbackUrl;
    private String executorId;
    private boolean throwExceptionAtConsumer;

    public CommandRequestEntity getCommand() {
        return command;
    }

    public void setCommand(final CommandRequestEntity command) {
        this.command = command;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(final String correlationId) {
        this.correlationId = correlationId;
    }

    public Destination getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(final Destination replyTo) {
        this.replyTo = replyTo;
    }

    public String getCommandCallbackUrl() {
        return commandCallbackUrl;
    }

    public void setCommandCallbackUrl(final String commandCallbackUrl) {
        this.commandCallbackUrl = commandCallbackUrl;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(final String executorId) {
        this.executorId = executorId;
    }

    public boolean isThrowExceptionAtConsumer() {
        return throwExceptionAtConsumer;
    }

    public void setThrowExceptionAtConsumer(final boolean throwExceptionAtConsumer) {
        this.throwExceptionAtConsumer = throwExceptionAtConsumer;
    }

}
