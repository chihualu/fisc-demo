package org.demo.backend.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

@Configuration
@EnableJms
@Log4j2
public class JmsConfig {

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public ArtemisConfigurationCustomizer customizer() {
        return new ArtemisConfigurationCustomizer() {
            @Override
            public void customize(org.apache.activemq.artemis.core.config.Configuration configuration) {
                try {
                    configuration.addAcceptorConfiguration("netty", "tcp://0.0.0.0:61616");
                } catch (Exception e) {
                    throw new RuntimeException("Failed to add netty transport acceptor to artemis instance", e);
                }
            }
        };
    }

    @Bean
    public JmsListenerContainerFactory myFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setErrorHandler(t -> {
            log.info("An error has occurred in the transaction");
            log.error("", t);
        });

        factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);

        configurer.configure(factory, connectionFactory);
        factory.setConcurrency("5");

        return factory;
    }
}