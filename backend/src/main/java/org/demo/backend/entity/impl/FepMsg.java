package org.demo.backend.entity.impl;

import lombok.Getter;
import lombok.Setter;
import org.demo.backend._enum.LocalChannel;

import javax.jms.Destination;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FepMsg implements Serializable {

    @Getter@Setter
    private Destination replyTo;
    @Getter@Setter
    private String messageId;
    @Getter@Setter
    private byte[] nativeMsg;
    @Getter@Setter
    private LocalChannel localChannel;

    private Map<String, String> data = new ConcurrentHashMap();
    public String getItem(String key) {
        return data.getOrDefault(key, "");
    }
    public void setItem(String key, String value) {
        data.put(key, value);
    }

}
