package com.virtusa.vhub.hub;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.virtusa.vhub.common.entity.CommandRequestEntity;
import com.virtusa.vhub.common.entity.CommandResponseEntity;

@Path("/api")
public interface CommandRouterResource {
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	@Path("/commandRouter")
	public CommandResponseEntity sendData(CommandRequestEntity command);
}
