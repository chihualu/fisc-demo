package org.demo.backend.agent;

import org.demo.backend.entity.impl.FepMsg;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.IOException;


public interface BaseAgent {
    void onMessage(Message message) throws JMSException, IOException, Exception;
    void executeI2E(FepMsg fepMsg) throws IOException;
    void executeE2I(FepMsg fepMsg) throws IOException;
}
