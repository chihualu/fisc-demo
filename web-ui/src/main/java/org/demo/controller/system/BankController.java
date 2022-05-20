package org.demo.controller.system;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.demo.annotatioon.Logging;
import org.demo.controller.system.form.BankReq;
import org.demo.controller.system.form.BankRsp;
import org.demo.db.modal.WebBankInfo;
import org.demo.db.repository.WebBankInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller("system.bank")
@Log4j2
public class BankController {

    @Autowired
    private WebBankInfoRepository WebBankInfoRepository;

    @GetMapping("/web/fisc/bank")
    public String queryPage(Model model) {
        return "fisc/FiscBank";
    }

    @GetMapping("/web/fisc/bank/dtl")
    public String detailPage(Model model, BankReq bankReq) {
        try {
            model.addAttribute("time", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
            String bankCode = bankReq.getEditBankCode();
            WebBankInfo webBankInfo = new WebBankInfo();
            if (StringUtils.isNotBlank(bankCode)) {
                // 修改
                webBankInfo = WebBankInfoRepository.findById(bankCode).orElse(new WebBankInfo());
                model.addAttribute("action", "update");
            } else {
                // 新增
                model.addAttribute("action", "create");
            }
            model.addAttribute("webBankInfo", webBankInfo);
        } catch (Exception e) {
            log.error("", e);
        }
        return "fisc/FiscBankDtl";
    }

    @GetMapping("/api/v1/fisc/bank")
    @Logging
    public ResponseEntity list(BankReq bankReq) {
        try {
            BankRsp bankRsp = new BankRsp();
            bankRsp.setData(WebBankInfoRepository.findAllByBankCodeAndTelZone(bankReq.getBankCode(), bankReq.getTelZone()));

            return ResponseEntity.ok(bankRsp);
        } catch (Exception e) {
            log.error("", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/v1/fisc/bank/{bankCode}")
    @Logging
    public ResponseEntity<Object> read(@PathVariable(value = "bankCode") String bankCode) {
        Map map = new HashMap();
        try {
            Optional<WebBankInfo> optionalWebBankInfo = WebBankInfoRepository.findById(bankCode);
            if (optionalWebBankInfo.isPresent()) {
                WebBankInfo webBankInfo = optionalWebBankInfo.get();
                return ResponseEntity.ok(webBankInfo);
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

    @PostMapping("/api/v1/fisc/bank")
    @Logging
    public ResponseEntity<Object> create(@RequestBody WebBankInfo webBankInfo) {
        Map map = new HashMap();
        try {
            // 後端檢核
            if (StringUtils.isBlank(webBankInfo.getBankCode())) {
                map.put("message", "行庫代碼不得為空");
                return ResponseEntity.badRequest().body(map);
            }
            if (StringUtils.isBlank(webBankInfo.getBankName())) {
                map.put("message", "行庫名稱不得為空");
                return ResponseEntity.badRequest().body(map);
            }
            if (StringUtils.isBlank(webBankInfo.getTelZone())) {
                map.put("message", "區碼不得為空");
                return ResponseEntity.badRequest().body(map);
            }
            if (StringUtils.isBlank(webBankInfo.getTelNo())) {
                map.put("message", "電話不得為空");
                return ResponseEntity.badRequest().body(map);
            }
            if (WebBankInfoRepository.existsById(webBankInfo.getBankCode())) {
                map.put("message", "資料已存在");
                return ResponseEntity.badRequest().body(map);
            }
            // 新增資料
            webBankInfo.setUpDateDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            webBankInfo = WebBankInfoRepository.saveAndFlush(webBankInfo);
            return ResponseEntity.ok().body(webBankInfo);
        } catch (Exception e) {
            log.error("", e);
            map.put("message", "系統錯誤");
            return ResponseEntity.badRequest().body(map);
        }
    }

    @PutMapping("/api/v1/fisc/bank/{bankCode}")
    @Logging
    public ResponseEntity<Object> update(@PathVariable(value = "bankCode") String pathBankCode, @RequestBody WebBankInfo webBankInfo) {
        Map map = new HashMap();
        try {
            if (!webBankInfo.getBankCode().equalsIgnoreCase(pathBankCode)) {
                map.put("message", "行庫代碼錯誤");
                return ResponseEntity.badRequest().body(map);
            }
            if (StringUtils.isBlank(webBankInfo.getBankName())) {
                map.put("message", "行庫名稱不得為空");
                return ResponseEntity.badRequest().body(map);
            }
            if (StringUtils.isBlank(webBankInfo.getTelZone())) {
                map.put("message", "區碼不得為空");
                return ResponseEntity.badRequest().body(map);
            }
            if (StringUtils.isBlank(webBankInfo.getTelNo())) {
                map.put("message", "電話不得為空");
                return ResponseEntity.badRequest().body(map);
            }

            Optional<WebBankInfo> optionalWebBankInfo = WebBankInfoRepository.findById(webBankInfo.getBankCode());
            if (optionalWebBankInfo.isPresent()) {
                WebBankInfo webBankInfoUpdate = optionalWebBankInfo.get();
                webBankInfoUpdate.setBankName(webBankInfo.getBankName());
                webBankInfoUpdate.setTelZone(webBankInfo.getTelZone());
                webBankInfoUpdate.setTelNo(webBankInfo.getTelNo());
                webBankInfoUpdate.setUpDateDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                webBankInfoUpdate = WebBankInfoRepository.saveAndFlush(webBankInfoUpdate);
                return ResponseEntity.ok(webBankInfoUpdate);
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

    @DeleteMapping("/api/v1/fisc/bank/{bankCode}")
    @Logging
    public ResponseEntity<Object> delete(@PathVariable(value = "bankCode") String pathBankCode) {
        Map map = new HashMap();
        try {
            if (StringUtils.isBlank(pathBankCode)) {
                map.put("message", "BankCode不可為空");
                return ResponseEntity.badRequest().body(map);
            }
            if (!WebBankInfoRepository.existsById(pathBankCode)) {
                map.put("message", "資料不存在");
                return ResponseEntity.badRequest().body(map);
            } else {
                WebBankInfoRepository.deleteById(pathBankCode);
                map.put("message", pathBankCode + " 刪除成功");
                return ResponseEntity.ok(map);
            }
        } catch (Exception e) {
            log.error("", e);
            map.put("message", "系統錯誤");
            return ResponseEntity.badRequest().body(map);
        }
    }

}
