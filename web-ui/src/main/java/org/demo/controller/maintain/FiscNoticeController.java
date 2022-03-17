package org.demo.controller.maintain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.demo.annotatioon.Logging;
import org.demo.controller.maintain.form.FiscNoticeReq;
import org.demo.controller.maintain.form.FiscNoticeRsp;
import org.demo.entity.web.impl.P3100Req;
import org.demo.entity.web.impl.P3100Rsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jms.TextMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@Log4j2
public class FiscNoticeController {

    private static ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private JmsTemplate jmsTemplate;

    @GetMapping("/web/maintain/fiscNotice")
    public String page(Model model){
        model.addAttribute("time", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        return "maintain/FiscNotice";
    }

    @PostMapping(value = "/api/v1/maintain/fiscNotice")
    @Logging
    public ResponseEntity getInfo(@RequestBody FiscNoticeReq fiscNoticeReq) {
        try {
            P3100Req p3100Req = P3100Req.builder()
                    .txnType("0200")
                    .txnCode("3100")
                    .txnDateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))
                    .txnStan(UUID.randomUUID().toString())
                    .returnCode("0000")
                    .message(fiscNoticeReq.getMessage())
                    .build();

            String reqStr = objectMapper.writeValueAsString(p3100Req);

            TextMessage rspMessage = (TextMessage) jmsTemplate.sendAndReceive("WEB.TO.FEP", session -> {
                TextMessage reqMessage = session.createTextMessage(reqStr);
                reqMessage.setJMSReplyTo(new ActiveMQQueue("FEP.TO.WEB"));
                return reqMessage;
            });
            String rspStr = rspMessage.getText();
            P3100Rsp p3100Rsp = objectMapper.readValue(rspStr, P3100Rsp.class);

            FiscNoticeRsp fiscNoticeRsp = new FiscNoticeRsp();
            fiscNoticeRsp.setReturnCode(p3100Rsp.getReturnCode());
            fiscNoticeRsp.setReturnDesc(p3100Rsp.getReturnCode().equals("0000") ? "Success" : "Failed");

            return ResponseEntity.ok(fiscNoticeRsp);
        }catch (Exception e){
            log.error("", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
