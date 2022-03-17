package org.demo.backend.codec.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.demo.backend.codec.AbstractCodec;
import org.demo.backend.entity.impl.FepMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JsonCodec extends AbstractCodec {

    private ObjectMapper objectMapper = new ObjectMapper();

//    @Autowired
//    public void setObjectMapper(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }

    @Override
    public byte[] render(FepMsg fepMsg) throws Exception{
        return new byte[0];
    }

    @Override
    public void parse(FepMsg fepMsg) throws Exception{
        Map data = objectMapper.readValue(fepMsg.getNativeMsg(), Map.class);
        data.keySet().forEach(key -> {
            parse("", key, data, fepMsg);
        });
    }

    private void parse(String base, Object key, Map data, FepMsg fepMsg) {
//        data.get(key)

    }
}
