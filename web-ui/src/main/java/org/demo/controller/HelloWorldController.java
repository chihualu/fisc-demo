package org.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class HelloWorldController {

    @PostMapping("/api/hello/{name}")
    private String hello(@PathVariable("name") String name) {

        return "Hello World, "+name;
    }
    @PostMapping("/bye/{name}")
    private String goodbye(@PathVariable("name") String name) {

        return "Bye, "+name;
    }
}
