package org.demo.backend.config;

import org.demo.backend.agent.impl.CommonAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

@Configuration
public class AgentConfig {

    @Bean("webAgent")
    public CommonAgent webAgent(){
        return new CommonAgent();
    }

    @Bean("fiscAgent")
    public CommonAgent fiscAgent(){
        return new CommonAgent();
    }

    @Autowired
    @Qualifier("webAgent")
    private CommonAgent webAgent;
    @Autowired
    @Qualifier("fiscAgent")
    private CommonAgent fiscAgent;

    @JmsListener(destination = "WEB.CONNECTOR.TO.AGENT", concurrency = "1", containerFactory = "myFactory")
    public void webAgentOnConnectorMessage(Message message) throws Exception {
        webAgent.onMessage(message);
    }

    @JmsListener(destination = "WEB.AP.TO.AGENT", concurrency = "1", containerFactory = "myFactory")
    public void webAgentOnApMessage(Message message) throws Exception {
        webAgent.onMessage(message);
    }

    @JmsListener(destination = "FISC.CONNECTOR.TO.AGENT", concurrency = "1")
    public void fiscAgentOnMessage(Message message) throws Exception {
        fiscAgent.onMessage(message);
    }

}
