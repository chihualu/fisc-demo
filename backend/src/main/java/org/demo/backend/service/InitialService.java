package org.demo.backend.service;

import lombok.extern.log4j.Log4j2;
import org.demo.backend.connector.impl.JmsConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Log4j2
public class InitialService {
    @Autowired
    private JmsConnector jmsConnector;
}
