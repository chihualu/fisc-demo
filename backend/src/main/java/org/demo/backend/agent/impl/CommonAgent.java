package org.demo.backend.agent.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.demo.backend._enum.LocalChannel;
import org.demo.backend.agent.AbstractAgent;
import org.demo.backend.entity.impl.FepMsg;
import org.demo.entity.web.WebMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Message;
import java.io.IOException;

@Log4j2
public class CommonAgent extends AbstractAgent {

    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void onMessage(Message message) throws Exception {
        log.debug(message);
        FepMsg fepMsg = message.getBody(FepMsg.class);
        if(fepMsg.getLocalChannel().equals(LocalChannel.ap)) {
            executeI2E(fepMsg);
        } else if(fepMsg.getLocalChannel().equals(LocalChannel.connector)) {
            executeE2I(fepMsg);
        } else {
            log.error("error channel");
        }
    }

    @Override
    public void executeI2E(FepMsg fepMsg) throws IOException {
        jmsTemplate.convertAndSend("WEB.AGENT.TO.CONNECTOR", fepMsg);
    }

    @Override
    public void executeE2I(FepMsg fepMsg) throws IOException {

        WebMessage webMessage = objectMapper.readValue(fepMsg.getNativeMsg(), WebMessage.class);
        jmsTemplate.convertAndSend("WEB.AP."+webMessage.getTxnType()+"."+webMessage.getTxnCode(), fepMsg);
    }

}
