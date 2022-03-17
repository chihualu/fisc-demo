package org.demo.backend.connector.impl;

import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.demo.backend.connector.BaseConnector;

import javax.jms.Message;

@Log4j2
public class TcpConnector implements BaseConnector {
    @Override
    public void onI2EMessage(Message message) {

    }

    @Override
    public void onE2IMessage(Message message) {

    }
}
