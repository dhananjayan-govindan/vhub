package com.virtusa.vhub.consumer3.executor;

import java.io.IOException;
import java.util.Collections;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtusa.vhub.common.entity.CommandExecutionStatus;
import com.virtusa.vhub.common.entity.CommandResponseEntity;
import com.virtusa.vhub.common.entity.CommandResponseEntity.AckType;
import com.virtusa.vhub.consumer3.entity.CommandMessageWrapper;

public class CommandResponderTask implements Runnable {
	private static final Logger log = LoggerFactory
			.getLogger(CommandResponderTask.class);

	private final CommandMessageWrapper commandMessageWrapper;
	private final CommandResponseEntity.AckType ackType;
	private final ConnectionFactory connectionFactory;

	private static final String COMMA_DELIMITER = ",";
	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	private static final String COMMAND_RESPONSE_CSV_HEADER = "ackType,executorId,statusCode,statusDescription";

	public CommandResponderTask(
			final CommandMessageWrapper commandMessageWrapper,
			final CommandResponseEntity.AckType ackType,
			final ConnectionFactory connectionFactory) {
		this.commandMessageWrapper = commandMessageWrapper;
		this.ackType = ackType;
		this.connectionFactory = connectionFactory;
	}

	@Override
	public void run() {
		try {
			final QueueConnection queueConnection = ((QueueConnectionFactory) connectionFactory)
					.createQueueConnection();
			try {
				final QueueSession queueSession = queueConnection
						.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
				final Queue queue = (Queue) commandMessageWrapper.getReplyTo();
				final QueueSender queueSender = queueSession
						.createSender(queue);

				final CommandResponseEntity response = new CommandResponseEntity();
				response.setAckType(ackType);
				response.setCommand(commandMessageWrapper.getCommand());
				final CommandExecutionStatus status = new CommandExecutionStatus();
				status.setExecutorId(commandMessageWrapper.getExecutorId());
				if (ackType == AckType.INITIAL) {
					status.setStatusCode("202");
					status.setStatusDescription("Accepted");
				} else if (ackType == AckType.FINAL
						&& commandMessageWrapper.isThrowExceptionAtConsumer()) {
					status.setStatusCode("500");
					status.setStatusDescription("Failure");
				} else {
					status.setStatusCode("200");
					status.setStatusDescription("Success");
				}
				response.setCommandResults(Collections.singletonList(status));

				StringBuilder csvData = marshalBeanToCsvText(response);

				final TextMessage textMessage = queueSession
						.createTextMessage();
				textMessage.setStringProperty("CommandCallbackUrl",
						commandMessageWrapper.getCommandCallbackUrl());
				textMessage.setStringProperty("AckType", ackType.toString());
				textMessage.setJMSCorrelationID(commandMessageWrapper
						.getCorrelationId());
				// textMessage.setJMSCorrelationID(commandMessageWrapper.getMessageId());

				String responseBodyText = csvData.toString();
				textMessage.setText(responseBodyText);

				log.info("Sending " + ackType + " response: {}",
						responseBodyText);
				queueSender.send(textMessage);
			} finally {
				queueConnection.close();
			}
		} catch (final Exception e) {
			log.error("Problem occurred in sending initial response.", e);
		}
	}

	private static StringBuilder marshalBeanToCsvText(
			CommandResponseEntity response) throws IOException {
		StringBuilder csvData = new StringBuilder();
		csvData.append(COMMAND_RESPONSE_CSV_HEADER.toString());
		csvData.append(LINE_SEPARATOR);
		csvData.append(response.getAckType());
		csvData.append(COMMA_DELIMITER);
		CommandExecutionStatus status = response.getCommandResults().get(0);
		csvData.append(status.getExecutorId());
		csvData.append(COMMA_DELIMITER);
		csvData.append(status.getStatusCode());
		csvData.append(COMMA_DELIMITER);
		csvData.append(status.getStatusDescription());
		csvData.append(LINE_SEPARATOR);
		return csvData;
	}

}
