package com.virtusa.vhub.consumer1.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.jms.ConnectionFactory;

import com.virtusa.vhub.common.entity.CommandResponseEntity.AckType;
import com.virtusa.vhub.consumer1.entity.CommandMessageWrapper;

public class CommandExecutor implements Runnable {

	private CommandMessageWrapper commandMessageWrapper;
	private ConnectionFactory connectionFactory;

	public CommandExecutor(CommandMessageWrapper commandMessageWrapper,
			ConnectionFactory connectionFactory) {
		this.commandMessageWrapper = commandMessageWrapper;
		this.connectionFactory = connectionFactory;
	}

	@Override
	public void run() {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
		service.schedule(new CommandResponderTask(commandMessageWrapper,
				AckType.INITIAL, connectionFactory), 10, TimeUnit.MILLISECONDS);
		service.schedule(new CommandResponderTask(commandMessageWrapper,
				AckType.FINAL, connectionFactory), 5000, TimeUnit.MILLISECONDS);
	}
}
