package com.virtusa.vhub.consumer2.entity;

import javax.jms.Destination;

import com.virtusa.vhub.common.entity.CommandRequestEntity;

public class CommandMessageWrapper {
	private CommandRequestEntity command;
	private String messageId;
	private String correlationId;
	private Destination replyTo;
	private String commandCallbackUrl;

	public CommandRequestEntity getCommand() {
		return command;
	}

	public void setCommand(CommandRequestEntity command) {
		this.command = command;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public Destination getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(Destination replyTo) {
		this.replyTo = replyTo;
	}

	public String getCommandCallbackUrl() {
		return commandCallbackUrl;
	}

	public void setCommandCallbackUrl(String commandCallbackUrl) {
		this.commandCallbackUrl = commandCallbackUrl;
	}

}
