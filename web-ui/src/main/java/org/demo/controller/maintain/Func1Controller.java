package org.demo.controller.maintain;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class Func1Controller {

    @GetMapping("/web/maintain/func1")
    public String page(Model model){
        model.addAttribute("time", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        return "maintain/Func1";
    }
}
