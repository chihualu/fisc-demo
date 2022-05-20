package org.demo.controller.fisc;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.demo.annotatioon.Logging;
import org.demo.config.StanUtil;
import org.demo.controller.fisc.form.FiscNoticeReq;
import org.demo.controller.fisc.form.FiscNoticeRsp;
import org.demo.controller.fisc.form.FiscStatusRsp;
import org.demo.entity.web.impl.P3100Req;
import org.demo.entity.web.impl.P3100Rsp;
import org.demo.entity.web.impl.P3109Req;
import org.demo.entity.web.impl.P3109Rsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.jms.TextMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller("fisc.status")
@Log4j2
public class FiscStatusController {

    private static ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private JmsTemplate jmsTemplate;

    @GetMapping("/web/fisc/status")
    public String page(Model model) {
        model.addAttribute("time", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        return "fisc/FiscStatus";
    }

    @PostMapping(value = "/api/v1/fisc/status")
    @Logging
    public ResponseEntity getInfo(@RequestBody FiscNoticeReq fiscNoticeReq) {

        log.debug("into getInfo");

        try {
            P3109Req p3109Req = P3109Req.builder()
                    .txnType("0200")
                    .txnCode("3109")
                    .txnDateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))
                    .txnStan(StanUtil.getStan())
                    .returnCode("0000")
                    .bankCode(fiscNoticeReq.getBankCode())
                    .build();

            String reqStr = objectMapper.writeValueAsString(p3109Req);

            TextMessage rspMessage = (TextMessage) jmsTemplate.sendAndReceive("WEB.TO.FEP", session -> {
                TextMessage reqMessage = session.createTextMessage(reqStr);
                reqMessage.setJMSReplyTo(new ActiveMQQueue("FEP.TO.WEB"));
                return reqMessage;
            });
            String rspStr = rspMessage.getText();
            log.debug("web rsp msg : " + rspStr);
            P3109Rsp p3109Rsp = objectMapper.readValue(rspStr, P3109Rsp.class);

            FiscStatusRsp fiscStatusRsp = new FiscStatusRsp();
            fiscStatusRsp.setReturnCode(p3109Rsp.getReturnCode());
            fiscStatusRsp.setReturnDesc(p3109Rsp.getReturnCode().equals("0001") ? "Success" : "Failed");
            fiscStatusRsp.setFiscStatus(p3109Rsp.getFiscStatus());
            fiscStatusRsp.setBankStatus(p3109Rsp.getBankStatus());
            fiscStatusRsp.setAppStatus(p3109Rsp.getAppStatus());

            return ResponseEntity.ok(fiscStatusRsp);
        } catch (Exception e) {
            log.error("", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
