package org.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.demo.provider.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class AuthorizeController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/auth/{name}")
    private String hello(@PathVariable("name") String name) {
        return jwtTokenProvider.createToken(name, "USER");
    }
}
