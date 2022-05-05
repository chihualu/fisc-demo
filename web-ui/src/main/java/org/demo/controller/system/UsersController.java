package org.demo.controller.system;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.demo.annotatioon.Logging;
import org.demo.controller.common.form.LabelValueBean;
import org.demo.controller.system.form.UsersReq;
import org.demo.controller.system.form.UsersRsp;
import org.demo.db.modal.WebRoleInfo;
import org.demo.db.modal.WebUserInfo;
import org.demo.db.repository.WebRoleInfoRepository;
import org.demo.db.repository.WebUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller("system.users")
@Log4j2
public class UsersController {

    @Autowired
    private WebUserInfoRepository webUserInfoRepository;

    @Autowired
    private WebRoleInfoRepository webRoleInfoRepository;

    @GetMapping("/web/system/users")
    public String queryPage(Model model) {
        return "system/Users";
    }

    @GetMapping("/web/system/users/dtl")
    public String detailPage(Model model, UsersReq usersReq) {
        try {
            String userId = usersReq.getEditUserId();
            WebUserInfo webUserInfo = new WebUserInfo();
            if (StringUtils.isNotBlank(userId)) {
                // 修改
                webUserInfo = webUserInfoRepository.findById(userId).orElse(new WebUserInfo());
                model.addAttribute("action", "update");
            } else {
                // 新增
                model.addAttribute("action", "create");
            }
            model.addAttribute("webUserInfo", webUserInfo);
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("webRoleInfoLVBeanList", this.getWebRoleInfoLVBeanList()); // 群組清單
        return "system/UsersDtl";
    }

    @GetMapping("/api/v1/system/users")
    @Logging
    public ResponseEntity list(UsersReq usersReq) {
        try {
            UsersRsp usersRsp = new UsersRsp();
            usersRsp.setData(webUserInfoRepository.findAllByUserId(usersReq.getUserId()));

            return ResponseEntity.ok(usersRsp);
        } catch (Exception e) {
            log.error("", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/v1/system/users/{userId}")
    @Logging
    public ResponseEntity<Object> read(@PathVariable(value = "userId") String userId) {
        Map map = new HashMap();
        try {
            Optional<WebUserInfo> optionalWebUserInfo = webUserInfoRepository.findById(userId);
            if (optionalWebUserInfo.isPresent()) {
                WebUserInfo webUserInfo = optionalWebUserInfo.get();
                return ResponseEntity.ok(webUserInfo);
            } else {
                map.put("message", "資料不存在");
                return ResponseEntity.badRequest().body(map);
            }
        } catch (Exception e) {
            log.error("", e);
            map.put("message", "系統錯誤");
            return ResponseEntity.badRequest().body(map);
        }
    }

    @PostMapping("/api/v1/system/users")
    @Logging
    public ResponseEntity<Object> create(@RequestBody WebUserInfo webUserInfo) {
        Map map = new HashMap();
        try {
            // 後端檢核
            if (StringUtils.isBlank(webUserInfo.getUserId())) {
                map.put("message", "使用者代號不得為空");
                return ResponseEntity.badRequest().body(map);
            }
            if (StringUtils.isBlank(webUserInfo.getUserName())) {
                map.put("message", "使用者名稱不得為空");
                return ResponseEntity.badRequest().body(map);
            }
            if (StringUtils.isBlank(webUserInfo.getRoleId())) {
                map.put("message", "群組代號不得為空");
                return ResponseEntity.badRequest().body(map);
            }
            if (webUserInfoRepository.existsById(webUserInfo.getUserId())) {
                map.put("message", "資料已存在");
                return ResponseEntity.badRequest().body(map);
            }
            // 新增資料
            webUserInfo = webUserInfoRepository.saveAndFlush(webUserInfo);
            return ResponseEntity.ok().body(webUserInfo);
        } catch (Exception e) {
            log.error("", e);
            map.put("message", "系統錯誤");
            return ResponseEntity.badRequest().body(map);
        }
    }

    @PutMapping("/api/v1/system/users/{userId}")
    @Logging
    public ResponseEntity<Object> update(@PathVariable(value = "userId") String pathUserId, @RequestBody WebUserInfo webUserInfo) {
        Map map = new HashMap();
        try {
            if (!webUserInfo.getUserId().equalsIgnoreCase(pathUserId)) {
                map.put("message", "userId錯誤");
                return ResponseEntity.badRequest().body(map);
            }
            if (StringUtils.isBlank(webUserInfo.getUserName())) {
                map.put("message", "使用者名稱不得為空");
                return ResponseEntity.badRequest().body(map);
            }
            if (StringUtils.isBlank(webUserInfo.getRoleId())) {
                map.put("message", "群組代號不得為空");
                return ResponseEntity.badRequest().body(map);
            }

            Optional<WebUserInfo> optionalWebUserInfo = webUserInfoRepository.findById(webUserInfo.getUserId());
            if (optionalWebUserInfo.isPresent()) {
                WebUserInfo webUserInfoUpdate = optionalWebUserInfo.get();
                webUserInfoUpdate.setUserName(webUserInfo.getUserName());
                webUserInfoUpdate.setRoleId(webUserInfo.getRoleId());
                webUserInfoUpdate = webUserInfoRepository.saveAndFlush(webUserInfoUpdate);
                return ResponseEntity.ok(webUserInfoUpdate);
            } else {
                map.put("message", "資料不存在");
                return ResponseEntity.badRequest().body(map);
            }
        } catch (Exception e) {
            log.error("", e);
            map.put("message", "系統錯誤");
            return ResponseEntity.badRequest().body(map);
        }
    }

    @DeleteMapping("/api/v1/system/users/{userId}")
    @Logging
    public ResponseEntity<Object> delete(@PathVariable(value = "userId") String pathUserId) {
        Map map = new HashMap();
        try {
            if (StringUtils.isBlank(pathUserId)) {
                map.put("message", "userId不可為空");
                return ResponseEntity.badRequest().body(map);
            }
            if (!webUserInfoRepository.existsById(pathUserId)) {
                map.put("message", "資料不存在");
                return ResponseEntity.badRequest().body(map);
            } else {
                webUserInfoRepository.deleteById(pathUserId);
                map.put("message", pathUserId + " 刪除成功");
                return ResponseEntity.ok(map);
            }
        } catch (Exception e) {
            log.error("", e);
            map.put("message", "系統錯誤");
            return ResponseEntity.badRequest().body(map);
        }
    }

    /**
     * 群組清單
     *
     * @return
     */
    private List<LabelValueBean> getWebRoleInfoLVBeanList() {
        ArrayList<LabelValueBean> webRoleInfoLVBeanList = new ArrayList<>();
        List<WebRoleInfo> webRoleInfoList = webRoleInfoRepository.findAllByOrderByRoleIdAsc();
        for (WebRoleInfo webRoleInfo : webRoleInfoList) {
            webRoleInfoLVBeanList.add(new LabelValueBean(webRoleInfo.getRoleName(), webRoleInfo.getRoleId()));
        }
        return webRoleInfoLVBeanList;
    }
}
