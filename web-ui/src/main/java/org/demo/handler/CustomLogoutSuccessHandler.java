package org.demo.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("User Logout Success, Logout User : [{}]", authentication.getName());
//        authService.logout((UserDetails) authentication.getPrincipal());
        setDefaultTargetUrl("/login");
        super.onLogoutSuccess(request, response, authentication);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        authService.logout((UserDetails) authentication.getPrincipal());
    }
}
