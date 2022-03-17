package org.demo.backend.connector;

import javax.jms.BytesMessage;
import javax.jms.Message;

public interface BaseConnector {

    void onI2EMessage(Message message) throws Exception;
    void onE2IMessage(Message message) throws Exception;
}
