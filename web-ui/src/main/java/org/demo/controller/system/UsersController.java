package org.demo.controller.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.demo.annotatioon.Logging;
import org.demo.controller.fisc.form.FiscNoticeReq;
import org.demo.controller.fisc.form.FiscNoticeRsp;
import org.demo.controller.system.form.UsersReq;
import org.demo.controller.system.form.UsersRsp;
import org.demo.db.repository.WebUserInfoRepository;
import org.demo.entity.web.impl.P3100Req;
import org.demo.entity.web.impl.P3100Rsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jms.TextMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller("system.users")
@Log4j2
public class UsersController {

    @Autowired
    private WebUserInfoRepository webUserInfoRepository;

    @GetMapping("/web/system/users")
    public String page(Model model){
        model.addAttribute("time", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        return "system/Users";
    }
    @GetMapping("/web/system/users/dtl")
    public String dtl(Model model){
        model.addAttribute("time", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        return "system/UsersDetail";
    }

    @GetMapping(value = "/api/v1/system/users")
    @Logging
    public ResponseEntity list(UsersReq usersReq) {
        try {
            UsersRsp usersRsp = new UsersRsp();
            usersRsp.setData(webUserInfoRepository.findAllByUserId(usersReq.getUserId()));

            return ResponseEntity.ok(usersRsp);
        }catch (Exception e){
            log.error("", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
