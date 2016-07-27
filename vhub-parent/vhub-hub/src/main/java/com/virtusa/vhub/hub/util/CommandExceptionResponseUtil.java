package com.virtusa.vhub.hub.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationException;

import org.apache.camel.processor.validation.SchemaValidationException;

import com.virtusa.vhub.common.entity.CommandExecutionStatus;
import com.virtusa.vhub.common.entity.CommandResponseEntity;
import com.virtusa.vhub.common.entity.CommandResponseEntity.AckType;

public class CommandExceptionResponseUtil {

    public static CommandResponseEntity getExcpetionResponse(Exception exception) {
        return getExcpetionResponse(exception, null, null);
    }

    public static CommandResponseEntity getExcpetionResponse(Exception exception, String statusCode,
                    String statusMessage) {
        CommandResponseEntity commandResponseEntity = new CommandResponseEntity();
        commandResponseEntity.setAckType(AckType.INITIAL);
        List<CommandExecutionStatus> commandResults = new ArrayList<>();
        CommandExecutionStatus status = new CommandExecutionStatus();
        commandResults.add(status);
        commandResponseEntity.setCommandResults(commandResults);
        status.setExecutorId("vHub");
        if (statusCode == null) {
            if (ValidationException.class.isAssignableFrom(exception.getClass())
                            || SchemaValidationException.class.isAssignableFrom(exception.getClass())) {
                status.setStatusCode("400");
                status.setStatusDescription("Data validation failed");
            } else {
                status.setStatusCode("500");
                status.setStatusDescription("Something went wrong while routing your request, please try agin later");
            }
        } else {
            status.setStatusCode(statusCode);
        }

        if (statusMessage == null) {
            status.setStatusDescription(status.getStatusDescription() + "; Details:" + exception.getMessage());
        } else {
            status.setStatusDescription(statusMessage + "; Details:" + exception.getMessage());
        }

        return commandResponseEntity;
    }
}
