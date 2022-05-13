package org.demo.fisc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@Log4j2
public class FiscServer {

    static ObjectMapper objectMapper = new ObjectMapper();

    public String sendToFiscAndGetRsp(String reqJson) {
        String rspStr = "";
        try {
            Map<String, String> map = objectMapper.readValue(reqJson, Map.class);

            Map<String, String> rspMap = objectMapper.readValue(reqJson, Map.class);

            for (Map.Entry<String, String> entry : map.entrySet()) {
                log.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }

            if (map.get("txnCode").equals("3100")) {
                rspMap.put("txnType", "0210");
                rspMap.put("returnCode", "0000");
            }

            if (map.get("txnCode").equals("3109")) {
                rspMap.put("txnType", "0210");
                p3109process(rspMap);
            }

            rspStr = objectMapper.writeValueAsString(rspMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return rspStr;
    }

    ;

    private void p3109process(Map<String, String> rspMap) {
        if (!rspMap.containsKey("bankCode")){
            rspMap.put("returnCode", "2999");
        }

        String bankId = rspMap.get("bankCode");

        switch (bankId){
            case "987":
                rspMap.put("returnCode", "0000");
                rspMap.put("fiscStatus","1");
                rspMap.put("bankStatus","1");
                rspMap.put("appStatus","1");
                break;

            case "809":
                rspMap.put("returnCode", "0000");
                rspMap.put("fiscStatus","1");
                rspMap.put("bankStatus","1");
                rspMap.put("appStatus","1");
                break;

            case "012":
                rspMap.put("returnCode", "0000");
                rspMap.put("fiscStatus","1");
                rspMap.put("bankStatus","1");
                rspMap.put("appStatus","1");
                break;

            case "013":
                rspMap.put("returnCode", "0000");
                rspMap.put("fiscStatus","1");
                rspMap.put("bankStatus","1");
                rspMap.put("appStatus","1");
                break;

            default :
                rspMap.put("returnCode", "2999");
        };

    }

    ;
}
