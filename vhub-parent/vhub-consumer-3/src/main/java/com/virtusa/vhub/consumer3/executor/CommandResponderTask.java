package com.virtusa.vhub.consumer3.executor;

import java.io.StringWriter;
import java.util.Collections;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtusa.vhub.common.entity.CommandExecutionStatus;
import com.virtusa.vhub.common.entity.CommandResponseEntity;
import com.virtusa.vhub.common.entity.CommandResponseEntity.AckType;
import com.virtusa.vhub.consumer3.entity.CommandMessageWrapper;

public class CommandResponderTask implements Runnable {
	private static final Logger log = LoggerFactory
			.getLogger(CommandResponderTask.class);

	private CommandMessageWrapper commandMessageWrapper;
	private CommandResponseEntity.AckType ackType;
	private ConnectionFactory connectionFactory;

	public CommandResponderTask(CommandMessageWrapper commandMessageWrapper,
			CommandResponseEntity.AckType ackType,
			ConnectionFactory connectionFactory) {
		this.commandMessageWrapper = commandMessageWrapper;
		this.ackType = ackType;
		this.connectionFactory = connectionFactory;
	}

	@Override
	public void run() {
		try {
			QueueConnection queueConnection = ((QueueConnectionFactory) connectionFactory)
					.createQueueConnection();
			try {
				QueueSession queueSession = queueConnection.createQueueSession(
						false, Session.AUTO_ACKNOWLEDGE);
				Queue queue = (Queue) commandMessageWrapper.getReplyTo();
				// Queue queue = queueSession
				// .createQueue("VHUB.COMMAND.CONS1.RES");
				QueueSender queueSender = queueSession.createSender(queue);

				CommandResponseEntity response = new CommandResponseEntity();
				response.setAckType(ackType);
				response.setCommand(commandMessageWrapper.getCommand());
				CommandExecutionStatus status = new CommandExecutionStatus();
				status.setExecutorId(commandMessageWrapper.getExecutorId());
				if (ackType == AckType.INITIAL) {
					status.setStatusCode("202");
					status.setStatusDescription("Accepted");
				} else {
					status.setStatusCode("200");
					status.setStatusDescription("Success");
				}
				response.setCommandResults(Collections.singletonList(status));

				StringWriter writer = new StringWriter();
				JAXBContext context = JAXBContext
						.newInstance(CommandResponseEntity.class);
				context.createMarshaller().marshal(response, writer);
				TextMessage textMessage = queueSession.createTextMessage();
				textMessage.setJMSCorrelationID(commandMessageWrapper
						.getCorrelationId());
				textMessage.setStringProperty("CommandCallbackUrl",
						commandMessageWrapper.getCommandCallbackUrl());
				textMessage.setStringProperty("AckType", ackType.toString());
				// textMessage.setJMSCorrelationID(commandMessageWrapper.getMessageId());
				textMessage.setText(writer.toString());
				log.info("Sending initial response: {}", writer.toString());
				queueSender.send(textMessage);
			} finally {
				queueConnection.close();
			}
		} catch (Exception e) {
			log.error("Problem occurred in sending initial response.", e);
		}
	}
}
