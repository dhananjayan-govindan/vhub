package com.virtusa.vhub.partner2;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtusa.vhub.common.entity.CommandRequestEntity;
import com.virtusa.vhub.common.entity.CommandResponseEntity;

public class CommandInvokerResourceImpl implements CommandInvokerResource {
	private static final Logger log = LoggerFactory
			.getLogger(CommandInvokerResourceImpl.class);

	@Override
	public CommandResponseEntity buildAndInvoke(String commandName) {
		CommandCatalog commandCatalog = CommandCatalog.valueOf(commandName);

		CommandRequestEntity cmdRequest = new CommandRequestEntity();
		cmdRequest.setId(commandCatalog.getId());
		cmdRequest.setCommand(commandName);
		cmdRequest.setType(commandCatalog.getType());

		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client
				.target("http://localhost:8181/cxf/vhub/hub/api/commandRouter");
		Entity<CommandRequestEntity> commandRequestEntity = Entity
				.xml(cmdRequest);
		Builder builder = target.request();
		builder.accept(MediaType.APPLICATION_XML);
		builder.header("CommandType", cmdRequest.getType());
		builder.header("CommandCallbackUrl",
				"localhost:8181/cxf/vhub/partner2/api/commandResponseHandler");
		Response response = builder.post(commandRequestEntity);
		CommandResponseEntity commandResponseEntity = response
				.readEntity(CommandResponseEntity.class);
		log.info("Initial response received is: {}", commandResponseEntity);
		response.close();
		return commandResponseEntity;
	}

	private enum CommandCatalog {
		TRIGGER_JOB1(1, "COMMAND1"), TRIGGER_JOB2(2, "COMMAND1"), TRIGGER_JOB3(
				1, "COMMAND2"), TRIGGER_JOB4(1, "COMMAND3");
		private CommandCatalog(int id, String type) {
			this.id = id;
			this.type = type;

		}

		public int getId() {
			return id;
		}

		public String getType() {
			return type;
		}

		private int id;
		private String type;
	}
}
