package org.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Controller
@Log4j2
public class LoginContorller {
    @Autowired
    LocaleResolver localeResolver;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String locale, @RequestParam(required = false) String timeout, @RequestParam(required = false) String logout) throws IOException {
        setLocale(request, response, locale);
        return "login";
    }
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @RequestMapping(value = {"/", "/index"})
    public String redirectToIndex(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String locale, Model model) throws IOException {
        setLocale(request, response, locale);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            setLocale(request, response, locale);
            return "redirect:/web/index";
        } else {
            return "login";
        }
    }

    @RequestMapping(value = {"/web", "/web/index"})
    public String index(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String locale) {
        setLocale(request, response, locale);
        return "index";
    }

    private void setLocale(HttpServletRequest request, HttpServletResponse response, String locale) {
        if (locale != null) {
            try {
                localeResolver.setLocale(request, response, new Locale(locale));
            } catch (Exception e) {
                log.error("", e);
                localeResolver.setLocale(request, response, Locale.TAIWAN);
            }
        }
    }
}
