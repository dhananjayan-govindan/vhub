package com.virtusa.vhub.partner2;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.virtusa.vhub.common.entity.CommandResponseEntity;

@Path("/api/commandInvoker")
public interface CommandInvokerResource {
    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("/{command}")
    public CommandResponseEntity buildAndInvoke(@PathParam("command") String commandName);

}