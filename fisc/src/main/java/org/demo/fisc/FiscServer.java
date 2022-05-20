package org.demo.fisc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Log4j2
public class FiscServer {
    private static Set<String> stans = new HashSet<>();
    static ObjectMapper objectMapper = new ObjectMapper();

    public String sendToFiscAndGetRsp(String reqJson) {
        String rspStr = "";
        try {
            Map<String, String> map = objectMapper.readValue(reqJson, Map.class);

            Map<String, String> rspMap = objectMapper.readValue(reqJson, Map.class);

//            for (Map.Entry<String, String> entry : map.entrySet()) {
//                log.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//            }

            rspMap.put("txnType", "0210");
            rspMap.put("txnCode", map.get("txnCode"));
            rspMap.put("txnDateTime", map.get("txnDateTime"));
            rspMap.put("txnStan", map.get("txnStan"));
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date()) ;
            if(map.get("returnCode") == null ||  !"0000".equals(map.get("returnCode") )) {
                rspMap.put("returnCode", "2999");
            }
            if(map.get("txnDateTime") == null || !map.get("txnDateTime").substring(0, 8).equals(date) ) {
                rspMap.put("returnCode", "2999");
            }
            try{
                if(map.get("txnStan") == null ||  Integer.parseInt(map.get("txnStan")) < 0 ) {
                    rspMap.put("returnCode", "2999");
                }
                if(map.get("txnStan") != null) {
                    if(stans.contains(map.get("txnStan"))) {
                        rspMap.put("returnCode", "2999");
                    }else{
                        stans.add(map.get("txnStan"));
                    }
                }
            }catch (Exception e){
                rspMap.put("returnCode", "2999");
            }


            if("0000".equals(rspMap.get("returnCode"))) {
                if (map.get("txnCode").equals("3100")) {
                    rspMap.put("returnCode", "0001");
                }else if (map.get("txnCode").equals("3201") || map.get("txnCode").equals("3109")) {
                    p3109process(rspMap);
                }else {
                    rspMap.put("returnCode", "2999");
                }

            }

            rspStr = objectMapper.writeValueAsString(rspMap);
        } catch (JsonProcessingException e) {
            log.error("", e);
        }

        return rspStr;
    }

    ;

    private void p3109process(Map<String, String> rspMap) {
        if (!rspMap.containsKey("bankCode")){
            rspMap.put("returnCode", "2999");
        }

        String bankId = rspMap.get("bankCode");
        rspMap.put("returnCode", "0001");
        switch (bankId){
            case "983":
                rspMap.put("fiscStatus","1");
                rspMap.put("bankStatus","3");
                rspMap.put("appStatus","1");
                break;

            case "982":
                rspMap.put("fiscStatus","3");
                rspMap.put("bankStatus","1");
                rspMap.put("appStatus","1");
                break;

            case "981":
                rspMap.put("fiscStatus","1");
                rspMap.put("bankStatus","1");
                rspMap.put("appStatus","3");
                break;

            case "700":
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
