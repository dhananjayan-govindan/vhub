package com.virtusa.vhub.hub.test;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

public class CommnandInRouteTest extends BaseCamelTest {

    @EndpointInject(uri = "mock:consumer1Endpoint")
    private MockEndpoint consumer1Endpoint;

    @Produce(uri = "direct:commandRequestFromPartner")
    private ProducerTemplate producerTemplate;

    @Test
    public void testIfCommandTypeIsCommand1() throws Exception {
        consumer1Endpoint.expectedMessageCount(1);
        producerTemplate.sendBodyAndHeader("Dummy Body", "CommandType", "COMMAND1");
        consumer1Endpoint.assertIsSatisfied();
    }

    @Test
    public void testIfCommandTypeIsNotCommand1() throws Exception {
        getMockEndpoint("mock:consumer1Endpoint").expectedMessageCount(0);
        producerTemplate.sendBodyAndHeader("Dummy Body", "CommandType", "COMMAND123");
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testIfCommandTypeIsCommand2() throws Exception {
        getMockEndpoint("mock:consumer2Endpoint").expectedMessageCount(1);
        producerTemplate.sendBodyAndHeader("Dummy Body", "CommandType", "COMMAND2");
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testIfCommandTypeIsNotCommand2() throws Exception {
        getMockEndpoint("mock:consumer2Endpoint").expectedMessageCount(0);
        producerTemplate.sendBodyAndHeader("Dummy Body", "CommandType", "COMMAND123");
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testIfCommandTypeIsCommand3() throws Exception {
        getMockEndpoint("mock:consumer2Endpoint").expectedMessageCount(1);
        getMockEndpoint("mock:consumer3Endpoint").expectedMessageCount(1);
        producerTemplate.sendBodyAndHeader("Dummy Body", "CommandType", "COMMAND3");
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testIfCommandTypeIsNotCommand3() throws Exception {
        getMockEndpoint("mock:consumer2Endpoint").expectedMessageCount(0);
        getMockEndpoint("mock:consumer3Endpoint").expectedMessageCount(0);
        producerTemplate.sendBodyAndHeader("Dummy Body", "CommandType", "COMMAND123");
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:commandRequestFromPartner").choice().when().simple("${header.CommandType} == 'COMMAND1'")
                                .to("mock:consumer1Endpoint").when().simple("${header.CommandType} == 'COMMAND2'")
                                .to("mock:consumer2Endpoint").when().simple("${header.CommandType} == 'COMMAND3'")
                                .multicast().to("mock:consumer2Endpoint").to("mock:consumer3Endpoint").end()
                                .endChoice();
            }
        };
    }
}
