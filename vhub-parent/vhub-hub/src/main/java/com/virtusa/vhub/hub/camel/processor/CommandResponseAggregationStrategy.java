package com.virtusa.vhub.hub.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtusa.vhub.common.entity.CommandResponseEntity;

public class CommandResponseAggregationStrategy implements AggregationStrategy {
    Logger log = LoggerFactory.getLogger(CommandResponseAggregationStrategy.class);

    @Override
    public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange) {
        log.info("Aggerating messages...");
        if (oldExchange == null) {
            return newExchange;
        }
        final CommandResponseEntity commandResponseEntityOld = oldExchange.getIn().getBody(CommandResponseEntity.class);
        final CommandResponseEntity commandResponseEntityNew = newExchange.getIn().getBody(CommandResponseEntity.class);
        commandResponseEntityOld.getCommandResults().add(commandResponseEntityNew.getCommandResults().get(0));
        return oldExchange;
    }

}
