package com.virtusa.vhub.consumer2;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtusa.vhub.consumer2.entity.CommandMessageWrapper;
import com.virtusa.vhub.consumer2.executor.CommandExecutor;

public class CommandMessageListener implements MessageListener {
    private static final Logger log = LoggerFactory.getLogger(CommandMessageListener.class);

    private QueueConnectionFactory connectionFactory;
    private Queue queue;
    private String applicationName;

    public void init() {
        try {
            final QueueConnection queueConnection = connectionFactory.createQueueConnection();
            final QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            final QueueReceiver queueReciever = queueSession.createReceiver(queue);
            queueReciever.setMessageListener(this);
            queueConnection.start();
        } catch (final JMSException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMessage(final Message message) {
        if (!TextMessage.class.isAssignableFrom(message.getClass())) {
            log.warn("The received message is not of type TextMessage, hence ignoring!");
            return;
        }
        try {
            final TextMessage textMessage = (TextMessage) message;
            log.info("Command Received Is: " + textMessage.getText());
            final CommandMessageWrapper commandMessageWrapper = new CommandMessageWrapper();
            // TODO: set Command object
            commandMessageWrapper.setCommand(null);
            commandMessageWrapper.setCorrelationId(message.getJMSCorrelationID());
            commandMessageWrapper.setReplyTo(message.getJMSReplyTo());
            commandMessageWrapper.setCommandCallbackUrl(message.getStringProperty("CommandCallbackUrl"));
            commandMessageWrapper.setExecutorId(applicationName);
            commandMessageWrapper.setThrowExceptionAtConsumer(message.getBooleanProperty("ThrowExceptionAtConsumer"));
            new Thread(new CommandExecutor(commandMessageWrapper, connectionFactory)).start();
        } catch (final JMSException e) {
            log.error("Problem in consuming a message.", e);
        }
    }

    public QueueConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(final QueueConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(final Queue queue) {
        this.queue = queue;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(final String applicationName) {
        this.applicationName = applicationName;
    }

}
