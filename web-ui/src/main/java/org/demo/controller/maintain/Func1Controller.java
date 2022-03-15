package org.demo.controller.maintain;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
public class Func1Controller {

    @GetMapping("/web/maintain/func1")
    public String page(Model model){
        model.addAttribute("time", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        return "maintain/Func1";
    }

    @PostMapping(value = "/api/v1/maintain/func2")
    @ResponseBody
    public ResponseEntity<Map> getInfo(@RequestBody Map input){
        log.debug(">>>" + input.get("name"));
        Map map = new HashMap();
        map.put("name", input.get("name"));
        map.put("text", "Hello, "+input.get("name"));
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
