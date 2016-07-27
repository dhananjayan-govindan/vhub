package com.virtusa.vhub.consumer1.executor;

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
import com.virtusa.vhub.consumer1.entity.CommandMessageWrapper;

public class CommandResponderTask implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(CommandResponderTask.class);

    private final CommandMessageWrapper commandMessageWrapper;
    private final CommandResponseEntity.AckType ackType;
    private final ConnectionFactory connectionFactory;

    public CommandResponderTask(final CommandMessageWrapper commandMessageWrapper,
                    final CommandResponseEntity.AckType ackType, final ConnectionFactory connectionFactory) {
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
                final QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                final Queue queue = (Queue) commandMessageWrapper.getReplyTo();
                // Queue queue = queueSession
                // .createQueue("VHUB.COMMAND.CONS1.RES");
                final QueueSender queueSender = queueSession.createSender(queue);

                final CommandResponseEntity response = new CommandResponseEntity();
                response.setAckType(ackType);
                response.setCommand(commandMessageWrapper.getCommand());
                final CommandExecutionStatus status = new CommandExecutionStatus();
                status.setExecutorId(commandMessageWrapper.getExecutorId());
                if (ackType == AckType.INITIAL) {
                    status.setStatusCode("202");
                    status.setStatusDescription("Accepted");
                } else if (ackType == AckType.FINAL && commandMessageWrapper.isThrowExceptionAtConsumer()) {
                    status.setStatusCode("500");
                    status.setStatusDescription("Failure");
                } else {
                    status.setStatusCode("200");
                    status.setStatusDescription("Success");
                }
                response.setCommandResults(Collections.singletonList(status));

                final StringWriter writer = new StringWriter();
                final JAXBContext context = JAXBContext.newInstance(CommandResponseEntity.class);
                context.createMarshaller().marshal(response, writer);
                final TextMessage textMessage = queueSession.createTextMessage();
                textMessage.setJMSCorrelationID(commandMessageWrapper.getCorrelationId());
                textMessage.setStringProperty("CommandCallbackUrl", commandMessageWrapper.getCommandCallbackUrl());
                textMessage.setStringProperty("AckType", ackType.toString());
                // textMessage.setJMSCorrelationID(commandMessageWrapper.getMessageId());
                textMessage.setText(writer.toString());
                log.info("Sending initial response: {}", writer.toString());
                queueSender.send(textMessage);
            } finally {
                queueConnection.close();
            }
        } catch (final Exception e) {
            log.error("Problem occurred in sending initial response.", e);
        }
    }
}
