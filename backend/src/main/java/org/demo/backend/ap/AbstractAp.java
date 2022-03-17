package org.demo.backend.ap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.demo.backend.entity.impl.FepMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

public abstract class AbstractAp implements BaseAp{

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected JmsTemplate jmsTemplate;

    protected void sendTo(String destination, FepMsg fepMsg){
        jmsTemplate.convertAndSend(destination, fepMsg);
    }
}
