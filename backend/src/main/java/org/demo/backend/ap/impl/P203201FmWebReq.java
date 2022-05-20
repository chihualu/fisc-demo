package org.demo.backend.ap.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.demo.backend._enum.LocalChannel;
import org.demo.backend.ap.AbstractAp;
import org.demo.backend.entity.impl.FepMsg;
import org.demo.entity.web.impl.P3100Rsp;
import org.demo.entity.web.impl.P3109Req;
import org.demo.entity.web.impl.P3109Rsp;
import org.demo.fisc.FiscServer;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Log4j2
public class P203201FmWebReq extends AbstractAp {


    @Override
    @JmsListener(destination = "WEB.AP.0200.3201", concurrency = "1-3")
    public void process(FepMsg fepMsg) {
        log.debug(fepMsg);
        fepMsg.setLocalChannel(LocalChannel.ap);
        try {
            P3109Req p3109Req = objectMapper.readValue(fepMsg.getNativeMsg(), P3109Req.class);

            String rspMsg = new FiscServer().sendToFiscAndGetRsp(objectMapper.writeValueAsString(p3109Req));

            P3109Rsp p3109Rsp = objectMapper.readValue(rspMsg, P3109Rsp.class);
            log.debug("return msg : " + rspMsg);
            fepMsg.setNativeMsg(objectMapper.writeValueAsBytes(p3109Rsp));

            sendTo("WEB.AP.TO.AGENT", fepMsg);
        } catch (Exception e) {
            log.error("", e);
            P3100Rsp p3100Rsp = P3100Rsp.builder()
                    .txnType("0210")
                    .txnStan("")
                    .txnDateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))
                    .txnCode("3100")
                    .returnCode("2999")
                    .build();
            try {
                fepMsg.setNativeMsg(objectMapper.writeValueAsBytes(p3100Rsp));
            } catch (JsonProcessingException ex) {
                log.error("", ex);
            }
            sendTo("WEB.AP.TO.AGENT", fepMsg);
        }


    }
}
