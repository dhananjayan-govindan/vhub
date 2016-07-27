package com.virtusa.vhub.hub.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtusa.vhub.common.entity.CommandResponseEntity;
import com.virtusa.vhub.hub.util.CommandExceptionResponseUtil;

public class CommandResponseAggregationStrategy implements AggregationStrategy {
    Logger log = LoggerFactory.getLogger(CommandResponseAggregationStrategy.class);

    @Override
    public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange) {
        log.info("Aggerating response messages; isFailed: {}", newExchange.isFailed());

        if (newExchange.isFailed()) {
            Exception exception = newExchange.getException();
            log.error("Exception occurred:", exception);
            newExchange.setException(null);
            newExchange.getIn().setBody(CommandExceptionResponseUtil.getExcpetionResponse(exception));
        }

        if (oldExchange == null) {
            return newExchange;
        }

        final CommandResponseEntity commandResponseEntityOld = oldExchange.getIn().getBody(CommandResponseEntity.class);
        final CommandResponseEntity commandResponseEntityNew = newExchange.getIn().getBody(CommandResponseEntity.class);
        commandResponseEntityOld.getCommandResults().add(commandResponseEntityNew.getCommandResults().get(0));
        log.info("Number of reponses received:" + commandResponseEntityOld.getCommandResults().size());
        return oldExchange;
    }

}
