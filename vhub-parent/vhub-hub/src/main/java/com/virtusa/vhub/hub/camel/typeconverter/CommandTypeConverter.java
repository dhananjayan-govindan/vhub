package com.virtusa.vhub.hub.camel.typeconverter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Converter;
import org.apache.camel.TypeConverters;

import com.virtusa.vhub.common.entity.CommandExecutionStatus;
import com.virtusa.vhub.common.entity.CommandRequestEntity;
import com.virtusa.vhub.common.entity.CommandResponseEntity;
import com.virtusa.vhub.common.entity.CommandResponseEntity.AckType;

public class CommandTypeConverter implements TypeConverters {

	@Converter
	public Map<String, String> convertTo(CommandRequestEntity commandEntity) {
		Map<String, String> result = new LinkedHashMap<String, String>();
		result.put("command", commandEntity.getCommand());
		result.put("id", Integer.toString(commandEntity.getId()));
		result.put("status", commandEntity.getType());
		return result;
	}

	@Converter
	public CommandResponseEntity convertTo(List<Map<String, String>> records) {
		CommandResponseEntity commandRespone = new CommandResponseEntity();
		Map<String, String> record = records.get(0);

		commandRespone.setAckType(AckType.valueOf(record.get("ackType")));

		CommandExecutionStatus status = new CommandExecutionStatus();
		status.setExecutorId(record.get("executorId"));
		status.setStatusCode(record.get("statusCode"));
		status.setStatusDescription(record.get("statusDescription"));
		ArrayList<CommandExecutionStatus> results = new ArrayList<CommandExecutionStatus>();
		results.add(status);
		commandRespone.setCommandResults(results);
		return commandRespone;
	}

}
