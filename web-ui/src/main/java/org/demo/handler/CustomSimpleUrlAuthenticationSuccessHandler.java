package org.demo.handler;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Log4j2
@Component
public class CustomSimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired(required = false)
    private LocaleResolver localeResolver;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("User Login Success, Login User : [{}]", authentication.getName());
        String locale = request.getParameter("locale");
        if (StringUtils.isNotBlank(locale)) {
            try {
                localeResolver.setLocale(request, response, new Locale(locale));
            } catch (Exception e) {
                log.error("", e);
                localeResolver.setLocale(request, response, Locale.TAIWAN);
            }
        }
//        authService.login((UserDetails) authentication.getPrincipal());
        setDefaultTargetUrl("/web/index");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
