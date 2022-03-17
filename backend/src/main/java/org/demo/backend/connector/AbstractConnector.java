package org.demo.backend.connector;


import lombok.Data;

@Data
public abstract class AbstractConnector extends Thread implements BaseConnector{
    protected String serviceName;
    protected String status;
    protected String e2iConnector;  //WEB.TO.FEP
    protected String i2eConnector;  //FEP.TO.WEB
    protected String e2iAgent;      //WEB.CONNECTOR.TO.AGENT
    protected String i2eAgent;      //WEB.AGENT.TO.CONNECTOR

    public String getServiceName() {
        return serviceName;
    }
    public String getStatus() {
        return status;
    }
}
