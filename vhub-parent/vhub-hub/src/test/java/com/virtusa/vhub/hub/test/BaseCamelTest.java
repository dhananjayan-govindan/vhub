package com.virtusa.vhub.hub.test;

import org.apache.camel.test.junit4.CamelTestSupport;

/**
 * Base test class for testing the camel routes, components and processors.
 */
public abstract class BaseCamelTest extends CamelTestSupport {

    @Override
    public boolean isCreateCamelContextPerClass() {
        return true;
    }

    @Override
    protected boolean useJmx() {
        return false;
    }

}
