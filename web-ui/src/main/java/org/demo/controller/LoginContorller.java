package org.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.demo.db.modal.WebFuncInfo;
import org.demo.db.modal.WebRoleInfo;
import org.demo.db.modal.WebUserInfo;
import org.demo.db.repository.WebFuncInfoRepository;
import org.demo.db.repository.WebRoleInfoRepository;
import org.demo.db.repository.WebUserInfoRepository;
import org.demo.provider.JwtTokenProvider;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@Log4j2
public class LoginContorller {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private WebRoleInfoRepository webRoleInfoRepository;
    @Autowired
    private WebUserInfoRepository webUserInfoRepository;
    @Autowired
    private WebFuncInfoRepository webFuncInfoRepository;
    @Autowired
    private ObjectMapper objectMapper;
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
    public String index(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String locale) throws JsonProcessingException {
        setLocale(request, response, locale);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getUsername();
        String roleId = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        model.addAttribute("token", jwtTokenProvider.createToken(userId, roleId));
        model.addAttribute("userName", userId);
        model.addAttribute("role", roleId);


        WebUserInfo webUserInfo = webUserInfoRepository.getById(userId);
        WebRoleInfo webRoleInfo = webRoleInfoRepository.getById(webUserInfo.getRoleId());


        model.addAttribute("menuMap", getMenuMap(webUserInfo.getRoleId()));
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

    public Map<String, List<WebFuncInfo>> getMenuMap(String roleId) throws JsonProcessingException {
        Optional<WebRoleInfo> roleInfoOptional = webRoleInfoRepository.findById(roleId);
        if (roleInfoOptional.isPresent()) {
            Map<String, List<String>> roles = objectMapper.readValue(roleInfoOptional.get().getFuncList(), Map.class);
            List<WebFuncInfo> funcInfoList = webFuncInfoRepository.findAll();

            Map<String, List<WebFuncInfo>> map = new HashMap<>();
            funcInfoList.stream()
                    .forEach(funcInfo -> {
                        if (roles.containsKey(funcInfo.getFuncId())) {
                            if (roles.get(funcInfo.getFuncId()).size() != 0 || funcInfo.getFuncUrl().equals("#")) {
                                if (!map.containsKey(funcInfo.getParentId())) {
                                    map.put(funcInfo.getParentId(), new LinkedList<>());
                                }
                                map.get(funcInfo.getParentId()).add(funcInfo);
                            }
                        }
                    });

            map.forEach((k, list) -> {
                Collections.sort(list, Comparator.comparingLong(WebFuncInfo::getOrderNum));
            });
            if (clearMap(map.get("root"), map)) {
                map.remove("root");
            }
            ;

            return map;
        }
        return new ConcurrentHashMap<>();
    }

    private boolean clearMap(List<WebFuncInfo> list, Map<String, List<WebFuncInfo>> map) {
        if(list == null) {
            return true;
        }
        Object[] funcInfos = list.toArray();
        for (int i = 0; i < funcInfos.length; i++) {
            WebFuncInfo funcInfo = (WebFuncInfo) funcInfos[i];
            if (funcInfo.getFuncUrl().equals("#")) {
                if (map.containsKey(funcInfo.getFuncId())) {
                    if (clearMap(map.get(funcInfo.getFuncId()), map)) {
                        list.remove(funcInfo);
                        map.remove(funcInfo.getFuncId());
                    }
                } else {
                    list.remove(funcInfo);
                }
            }
        }
        return list.size() == 0;
    }

}
