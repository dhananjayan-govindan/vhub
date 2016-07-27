package com.virtusa.vhub.partner3;

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
    private static final Logger log = LoggerFactory.getLogger(CommandInvokerResourceImpl.class);
    private String commandRouterUrl;
    private String commandCallbackUrl;

    @Override
    public CommandResponseEntity buildAndInvoke(final String commandName) {
        final CommandCatalog commandCatalog = CommandCatalog.valueOf(commandName);

        final CommandRequestEntity cmdRequest = new CommandRequestEntity();
        cmdRequest.setId(commandCatalog.getId());
        cmdRequest.setCommand(commandName);
        cmdRequest.setType(commandCatalog.getType());

        final Client client = ClientBuilder.newBuilder().build();
        final WebTarget target = client.target(commandRouterUrl);
        final Entity<CommandRequestEntity> commandRequestEntity = Entity.xml(cmdRequest);
        final Builder builder = target.request();
        builder.accept(MediaType.APPLICATION_XML);
        builder.header("CommandType", cmdRequest.getType());
        builder.header("CommandCallbackUrl", commandCallbackUrl);
        final Response response = builder.post(commandRequestEntity);
        final CommandResponseEntity commandResponseEntity = response.readEntity(CommandResponseEntity.class);
        log.info("Initial response received is: {}", commandResponseEntity);
        response.close();
        return commandResponseEntity;
    }

    public String getCommandRouterUrl() {
        return commandRouterUrl;
    }

    public void setCommandRouterUrl(final String commandRouterUrl) {
        this.commandRouterUrl = commandRouterUrl;
    }

    public String getCommandCallbackUrl() {
        return commandCallbackUrl;
    }

    public void setCommandCallbackUrl(final String commandCallbackUrl) {
        this.commandCallbackUrl = commandCallbackUrl;
    }

    private enum CommandCatalog {
        TRIGGER_JOB1(1, "COMMAND1"), TRIGGER_JOB2(2, "COMMAND2"), TRIGGER_JOB3(3, "COMMAND3");
        private CommandCatalog(final int id, final String type) {
            this.id = id;
            this.type = type;

        }

        public int getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        private final int id;
        private final String type;
    }
}
