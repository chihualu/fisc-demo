package org.demo.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutHanlder extends SecurityContextLogoutHandler {

    @Autowired
    public void setInvalidateHttpSession(){
        super.setInvalidateHttpSession(true);
    }

    @Autowired
    public void setClearAuthentication(){
        super.setClearAuthentication(true);
    }

}
