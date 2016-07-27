package com.virtusa.vhub.partner3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinalResponseHandlerResourceImpl implements FinalResponseHandlerResource {

    private static final Logger log = LoggerFactory.getLogger(FinalResponseHandlerResourceImpl.class);

    @Override
    public void processFinalResponse(final String commandResponse) {
        log.info("Final response received is: {}", commandResponse);
    }

}
