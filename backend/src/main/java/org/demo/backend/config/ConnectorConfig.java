package org.demo.backend.config;

import lombok.extern.log4j.Log4j2;
import org.demo.backend.connector.BaseConnector;
import org.demo.backend.connector.impl.JmsConnector;
import org.demo.backend.connector.impl.TcpConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;

import javax.jms.JMSException;
import javax.jms.Message;

@Configuration
@Log4j2
public class ConnectorConfig {

    @Bean("WebConnector")
    public JmsConnector webConnector(){
        log.debug("Initial web connector");
        JmsConnector jmsConnector = new JmsConnector();
        jmsConnector.setE2iConnector("WEB.TO.FEP");
        jmsConnector.setI2eConnector("FEP.TO.WEB");
        jmsConnector.setE2iAgent("WEB.CONNECTOR.TO.AGENT");
        jmsConnector.setI2eAgent("WEB.AGENT.TO.CONNECTOR");
        return jmsConnector;
    }
    @Bean("FiscConnector")
    public TcpConnector fiscConnector(){
        return new TcpConnector();
    }

    @Autowired
    @Qualifier("WebConnector")
    private BaseConnector webConnector;

    @Autowired
    @Qualifier("FiscConnector")
    private BaseConnector fiscConnector;


    @JmsListener(destination = "WEB.AGENT.TO.CONNECTOR", concurrency = "1")
    public void webConnectorOnMessage(Message message) throws Exception {
        webConnector.onI2EMessage(message);
    }

    @JmsListener(destination = "FISC.AGENT.TO.CONNECTOR", concurrency = "1")
    public void fiscAgentOnMessage(Message message) throws Exception {
        fiscConnector.onI2EMessage(message);
    }

    @JmsListener(destination = "WEB.TO.FEP")
    public void webExternalOnMessage(Message message) throws Exception {
        webConnector.onE2IMessage(message);
    }


}
