package org.demo.backend.agent;

import lombok.Data;
import org.demo.backend.entity.impl.FepMsg;

import javax.jms.JMSException;
import javax.jms.Message;


@Data
public abstract class AbstractAgent extends Thread implements BaseAgent{

}
