package org.demo.backend.connector.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.demo.backend._enum.LocalChannel;
import org.demo.backend.connector.AbstractConnector;
import org.demo.backend.entity.impl.FepMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.nio.charset.StandardCharsets;

@Log4j2
public class JmsConnector extends AbstractConnector {

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostConstruct
    public void initial(){
        start();
    }

    public void run() {
        while(true) {
            try {
                log.debug(e2iConnector);
                TextMessage textMessage = (TextMessage) jmsTemplate.receive(e2iConnector);
                onE2IMessage(textMessage);
            }catch (Exception e){
                log.error("", e);
            }
        }
    }



    @Override
    public void onI2EMessage(Message message) throws Exception {
        FepMsg fepMsg = message.getBody(FepMsg.class);
        Destination destination = new ActiveMQQueue(i2eConnector);
        if(fepMsg.getReplyTo() != null) {
            destination = fepMsg.getReplyTo();
        }
        log.debug(destination);
        jmsTemplate.send(destination,  session -> {
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(new String(fepMsg.getNativeMsg()));
            textMessage.setJMSCorrelationID(fepMsg.getMessageId());
            return textMessage;
        });

    }

    @Override
    public void onE2IMessage(Message message) throws Exception{
        TextMessage textMessage = (TextMessage) message;
        log.debug("Recv Message {}", textMessage.getText());

        FepMsg fepMsg = new FepMsg();
        fepMsg.setNativeMsg(textMessage.getText().getBytes(StandardCharsets.UTF_8));
        fepMsg.setReplyTo(textMessage.getJMSReplyTo());
        fepMsg.setMessageId(textMessage.getJMSMessageID());
        fepMsg.setLocalChannel(LocalChannel.connector);

        jmsTemplate.convertAndSend(e2iAgent, fepMsg);
    }
}
