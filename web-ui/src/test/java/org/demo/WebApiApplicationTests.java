package org.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Assert;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WebApiApplicationTests {

    private static int score = 0;
    private static int total = 0;
    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZW5ueSIsImF1dGgiOiJST0xFMSIsImlhdCI6MTY1MjkzODMyMCwiZXhwIjoxNjUzMDI0NzIwfQ.WIyjqGD4IclD4r0-eo8dcfie1eigBqlG_OwxAvGDQU4";
    private String host = "http://127.0.0.1:";
    @LocalServerPort
    private int port;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestRestTemplate restTemplate;
    @Test
    @Order(0)
    @DisplayName("初始化")
    void initial() {
        jmsTemplate.setReceiveTimeout(500);
    }
    @Test
    @Order(1)
    @DisplayName("第一次先看有沒有初始化資料資料")
    void bankListForNull() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            if( ((List)requestResult.getBody().get("data")).size() == 0 ){
                score ++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(2)
    @DisplayName("新增一筆銀行檔987")
    void bankCreateFirst() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "987");
        data.put("bankName", "XX銀行");
        data.put("telZone", "02");
        data.put("telNo", "23456789");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(3)
    @DisplayName("查詢列表是否有一筆資料")
    void bankListForOneRecord() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            if( ((List)requestResult.getBody().get("data")).size() == 1 ){
                score++;
                return;
            }
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(4)
    @DisplayName("查詢列表是否有一筆資料")
    void bankQueryFor987() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/987", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            Map data = requestResult.getBody();
            if( data.get("bankCode").equals("987") ){
                score++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(4)
    @DisplayName("查詢列表是否有一筆資料且updatedate有值")
    void bankQueryFor987_1() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/987", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            Map data = requestResult.getBody();
            System.out.println();
            if( DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd").equals(data.get("updateDate")) ){
                score++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(5)
    @DisplayName("新增同一筆銀行檔987")
    void bankCreateForDuplicate() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "987");
        data.put("bankName", "XX銀行2");
        data.put("telZone", "02");
        data.put("telNo", "23456780");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(6)
    @DisplayName("查詢列表是否987有覆蓋")
    void bankQueryDuplicate() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/987", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            Map data = requestResult.getBody();
            if( !(data.get("bankCode").equals("987") && data.get("bankName").equals("XX銀行2")) ){
                score++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(7)
    @DisplayName("新增一筆銀行檔欄位太長")
    void bankCreateForBank987X_badRequest_1() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "985X");
        data.put("bankName", "XX銀行");
        data.put("telZone", "02");
        data.put("telNo", "23456789");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(7)
    @DisplayName("新增一筆銀行檔欄位太長")
    void bankCreateForBank987X_badRequest_2() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "985");
        data.put("bankName", "XX銀行XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        data.put("telZone", "02");
        data.put("telNo", "23456789");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(7)
    @DisplayName("新增一筆銀行檔欄位太長")
    void bankCreateForBank987X_badRequest_ˇ() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "985");
        data.put("bankName", "XX銀行");
        data.put("telZone", "02ccccc");
        data.put("telNo", "23456789");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(7)
    @DisplayName("新增一筆銀行檔欄位太長")
    void bankCreateForBank987X_badRequest_4() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "985");
        data.put("bankName", "XX銀行");
        data.put("telZone", "02");
        data.put("telNo", "23456789xxxx");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(8)
    @DisplayName("新增一筆銀行檔986")
    void bankCreateForBank986() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "986");
        data.put("bankName", "OO銀行");
        data.put("telZone", "02");
        data.put("telNo", "23456780");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(9)
    @DisplayName("查詢銀行代碼986")
    void bankQueryFor986_1() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/986", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            Map data = requestResult.getBody();
            if( data.get("bankCode").equals("986") && data.get("bankName").equals("OO銀行") &&
                    data.get("telZone").equals("02") && data.get("telNo").equals("23456780")){
                score++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(10)
    @DisplayName("更新一筆銀行檔986資料")
    void bankUpdateForBank986() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "986");
        data.put("bankName", "QQ銀行");
        data.put("telZone", "03");
        data.put("telNo", "23456730");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/986", HttpMethod.PUT,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(11)
    @DisplayName("查詢列表是否有一筆資料")
    void bankQueryFor986_2() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/986", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            Map data = requestResult.getBody();
            if( data.get("bankCode").equals("986") && data.get("bankName").equals("QQ銀行") &&
                    data.get("telZone").equals("03") && data.get("telNo").equals("23456730")){
                score++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(12)
    @DisplayName("更新一筆銀行檔986資料")
    void bankUpdateForBank986_2() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "986");
        data.put("bankName", "QQ銀行");
        data.put("telZone", "03");
        data.put("telNo", "23456730");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/987", HttpMethod.PUT,  getHttpEntity(data),  Map.class);
        if(!requestResult.getStatusCode().is2xxSuccessful()) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(12)
    @DisplayName("更新一筆銀行檔986資料")
    void bankUpdateForBank986_3() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/986", HttpMethod.PUT,  getHttpEntity(null),  Map.class);
        if(!requestResult.getStatusCode().is2xxSuccessful()) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(13)
    @DisplayName("查詢列表_bankCode=987")
    void bankListForCondition_1() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank?bankCode=987", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            if( ((List)requestResult.getBody().get("data")).size() == 1 ){
                score ++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(13)
    @DisplayName("查詢列表_bankCode=986")
    void bankListForCondition_2() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank?bankCode=986", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            if( ((List)requestResult.getBody().get("data")).size() == 1 ){
                score ++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(13)
    @DisplayName("查詢列表_telZone=02")
    void bankListForCondition_3() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank?telZone=02", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            if( ((List)requestResult.getBody().get("data")).size() == 1 ){
                score ++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(13)
    @DisplayName("查詢列表_telZone=03")
    void bankListForCondition_4() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank?telZone=03", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            if( ((List)requestResult.getBody().get("data")).size() == 1 ){
                score ++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(13)
    @DisplayName("查詢列表_telZone=03&bankCode=987")
    void bankListForCondition_5() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank?telZone=03&bankCode=987", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            if( ((List)requestResult.getBody().get("data")).size() == 0 ){
                score ++;
                return;
            }
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(13)
    @DisplayName("查詢列表_telZone=02&bankCode=986")
    void bankListForCondition_6() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank?telZone=02&bankCode=986", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            if( ((List)requestResult.getBody().get("data")).size() == 0 ){
                score ++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(13)
    @DisplayName("查詢列表_telZone=&bankCode=")
    void bankListForCondition_7() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank?telZone=&bankCode=", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            if( ((List)requestResult.getBody().get("data")).size() == 2 ){
                score ++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(13)
    @DisplayName("查詢列表_telZone=null&bankCode=null")
    void bankListForCondition_8() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank?telZone=null&bankCode=null", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            if( ((List)requestResult.getBody().get("data")).size() == 0 ){
                score ++;
                return;
            }
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(14)
    @DisplayName("刪除銀行987")
    void deleteBank987() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/987", HttpMethod.DELETE,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(14)
    @DisplayName("刪除銀行988")
    void deleteBank988() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/988", HttpMethod.DELETE,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(14)
    @DisplayName("刪除銀行986")
    void deleteBank986() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/986", HttpMethod.DELETE,  getHttpEntity(null),  Map.class);
        System.out.println(requestResult.getStatusCode().value());
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(15)
    @DisplayName("再次刪除銀行987")
    void deleteBank987_2() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/987", HttpMethod.DELETE,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(16)
    @DisplayName("查詢列表_telZone=&bankCode=")
    void bankListForCondition_10() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank?telZone=&bankCode=", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            if( ((List)requestResult.getBody().get("data")).size() == 0 ){
                score ++;
                return;
            }
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(17)
    @DisplayName("新增一筆銀行檔有欄位空")
    void bankCreateForBank984_blankField_1() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "984");
        data.put("bankName", "");
        data.put("telZone", "02");
        data.put("telNo", "23456789");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(17)
    @DisplayName("新增一筆銀行檔有欄位空")
    void bankCreateForBank984_blankField_2() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "984");
        data.put("bankName", "PP銀行");
        data.put("telZone", "");
        data.put("telNo", "23456789");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(17)
    @DisplayName("新增一筆銀行檔有欄位空")
    void bankCreateForBank984_blankField_3() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "984");
        data.put("bankName", "PP銀行");
        data.put("telZone", "05");
        data.put("telNo", "");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(17)
    @DisplayName("新增一筆銀行檔有欄位空")
    void bankCreateForBank984_blankField_4() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "984");
        data.put("bankName", null);
        data.put("telZone", null);
        data.put("telNo", "XXXXXXXX");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(17)
    @DisplayName("新增一筆銀行檔有欄位空")
    void bankCreateForBank984_blankField_5() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "");
        data.put("bankName", "PP銀行");
        data.put("telZone", "05");
        data.put("telNo", "23451234");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(18)
    @DisplayName("更新沒有銀行檔的銀行資料")
    void bankUpdateForBank984() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "984");
        data.put("bankName", "PP銀行");
        data.put("telZone", "05");
        data.put("telNo", "23451234");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/984", HttpMethod.PUT,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(19)
    @DisplayName("查詢984")
    void bankQueryFor984() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank/984", HttpMethod.GET,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().value() == 400 || requestResult.getStatusCode().value() == 204) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }








    @Test
    @Order(19)
    @DisplayName("新增一筆銀行檔983，之後發查用")
    void bankCreateFirst_2() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "983");
        data.put("bankName", "測試銀行");
        data.put("telZone", "02");
        data.put("telNo", "23456789");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(19)
    @DisplayName("新增一筆銀行檔982，之後發查用")
    void bankCreateFirst_3() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "982");
        data.put("bankName", "測試銀行2");
        data.put("telZone", "02");
        data.put("telNo", "23456789");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(19)
    @DisplayName("新增一筆銀行檔981，之後發查用")
    void bankCreateFirst_4() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "981");
        data.put("bankName", "測試銀行4");
        data.put("telZone", "02");
        data.put("telNo", "23456789");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/bank", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(20)
    @DisplayName("POST發查銀行狀態987")
    void fiscStatusQuery_1() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "987");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/status/", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(20)
    @DisplayName("GET發查銀行狀態987")
    void fiscStatusQuery_2() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "987");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/status/", HttpMethod.GET,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().value() == 405) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(20)
    @DisplayName("POST發查銀行狀態null")
    void fiscStatusQuery_5() {
        total++;
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/status/", HttpMethod.POST,  getHttpEntity(null),  Map.class);
        if(requestResult.getStatusCode().value() == 400) {
            score ++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(20)
    @DisplayName("POST發查銀行狀態983")
    void fiscStatusQuery_3() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "983");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/status/", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            Map rspData = requestResult.getBody();
            System.out.println(rspData);
            if("1".equals(rspData.get("fiscStatus")) &&
                    "3".equals(rspData.get("bankStatus")) &&
                    "1".equals(rspData.get("appStatus")) ) {
                score++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(20)
    @DisplayName("POST發查銀行狀態982")
    void fiscStatusQuery_6() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "982");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/status/", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            Map rspData = requestResult.getBody();
            if("3".equals(rspData.get("fiscStatus")) &&
                    "1".equals(rspData.get("bankStatus")) &&
                    "1".equals(rspData.get("appStatus")) ) {
                score++;
                return;
            }
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(20)
    @DisplayName("POST發查銀行狀態981")
    void fiscStatusQuery_7() {
        total++;
        Map data = new HashMap();
        data.put("bankCode", "981");
        ResponseEntity<Map> requestResult = this.restTemplate.exchange(host + port + "/api/v1/fisc/status/", HttpMethod.POST,  getHttpEntity(data),  Map.class);
        if(requestResult.getStatusCode().is2xxSuccessful()) {
            Map rspData = requestResult.getBody();
            if("1".equals(rspData.get("fiscStatus")) &&
                    "1".equals(rspData.get("bankStatus")) &&
                    "3".equals(rspData.get("appStatus")) ) {
                score++;
                return;
            }
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(21)
    @DisplayName("直接測試Backend_700")
    void backend_1() throws JMSException, JsonProcessingException {
        total++;
        Map data = new HashMap();
        data.put("txnType", "0200");
        data.put("txnCode", "3201");
        data.put("txnDateTime", DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss"));
        data.put("txnStan", "9000001");
        data.put("returnCode", "0000");
        data.put("bankCode", "700");
        String req = objectMapper.writeValueAsString(data);
        TextMessage rspMessage = (TextMessage) jmsTemplate.sendAndReceive("WEB.TO.FEP", session -> {
            TextMessage reqMessage = session.createTextMessage(req);
            reqMessage.setJMSReplyTo(new ActiveMQQueue("FEP.TO.WEB"));
            return reqMessage;
        });
        Map rspData = objectMapper.readValue(rspMessage.getText(), Map.class);
        if( "0001".equals(rspData.get("returnCode")) &&
                "0210".equals(rspData.get("txnType")) &&
                data.get("txnStan").equals(rspData.get("txnStan")) &&
                data.get("bankCode").equals(rspData.get("bankCode")) &&
                "1".equals(rspData.get("fiscStatus")) &&
                "1".equals(rspData.get("bankStatus")) &&
                "1".equals(rspData.get("appStatus")) ) {
            score++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(22)
    @DisplayName("直接測試Backend_700_stan非數字")
    void backend_2() throws JMSException, JsonProcessingException {
        total++;
        Map data = new HashMap();
        data.put("txnType", "0200");
        data.put("txnCode", "3201");
        data.put("txnDateTime", DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss"));
        data.put("txnStan", UUID.randomUUID().toString());
        data.put("returnCode", "0000");
        data.put("bankCode", "700");
        String req = objectMapper.writeValueAsString(data);
        TextMessage rspMessage = (TextMessage) jmsTemplate.sendAndReceive("WEB.TO.FEP", session -> {
            TextMessage reqMessage = session.createTextMessage(req);
            reqMessage.setJMSReplyTo(new ActiveMQQueue("FEP.TO.WEB"));
            return reqMessage;
        });
        Map rspData = objectMapper.readValue(rspMessage.getText(), Map.class);
        if( "2999".equals(rspData.get("returnCode"))) {
            score++;
            return;
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(22)
    @DisplayName("直接測試Backend_700_日期錯誤")
    void backend_3() throws JMSException, JsonProcessingException {
        total++;
        Map data = new HashMap();
        data.put("txnType", "0200");
        data.put("txnCode", "3201");
        data.put("txnDateTime", "19111201000000");
        data.put("txnStan", "9000002");
        data.put("returnCode", "0000");
        data.put("bankCode", "700");
        String req = objectMapper.writeValueAsString(data);
        TextMessage rspMessage = (TextMessage) jmsTemplate.sendAndReceive("WEB.TO.FEP", session -> {
            TextMessage reqMessage = session.createTextMessage(req);
            reqMessage.setJMSReplyTo(new ActiveMQQueue("FEP.TO.WEB"));
            return reqMessage;
        });
        Map rspData = objectMapper.readValue(rspMessage.getText(), Map.class);
        System.out.println(rspData);
        if( "2999".equals(rspData.get("returnCode"))) {
            score++;
            return;
        }
        Assert.isTrue(false);
    }
    @Test
    @Order(22)
    @DisplayName("直接測試Backend_XXX_bankCode錯誤")
    void backend_4() throws JMSException, JsonProcessingException {
        total++;
        Map data = new HashMap();
        data.put("txnType", "0200");
        data.put("txnCode", "3201");
        data.put("txnDateTime", DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss"));
        data.put("txnStan", "9000003");
        data.put("returnCode", "0000");
        data.put("bankCode", "XXX");
        String req = objectMapper.writeValueAsString(data);
        TextMessage rspMessage = (TextMessage) jmsTemplate.sendAndReceive("WEB.TO.FEP", session -> {
            TextMessage reqMessage = session.createTextMessage(req);
            reqMessage.setJMSReplyTo(new ActiveMQQueue("FEP.TO.WEB"));
            return reqMessage;
        });
        Map rspData = objectMapper.readValue(rspMessage.getText(), Map.class);
        if( "2999".equals(rspData.get("returnCode"))) {
            score++;
            return;
        }
        Assert.isTrue(false);
    }

    @Test
    @Order(22)
    @DisplayName("直接測試Backend_3100")
    void backend_5() throws JMSException, JsonProcessingException {
        total++;
        Map data = new HashMap();
        data.put("txnType", "0200");
        data.put("txnCode", "3100");
        data.put("txnDateTime", DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss"));
        data.put("txnStan", "9000004");
        data.put("returnCode", "0000");
        data.put("message", "XXX");
        String req = objectMapper.writeValueAsString(data);
        TextMessage rspMessage = (TextMessage) jmsTemplate.sendAndReceive("WEB.TO.FEP", session -> {
            TextMessage reqMessage = session.createTextMessage(req);
            reqMessage.setJMSReplyTo(new ActiveMQQueue("FEP.TO.WEB"));
            return reqMessage;
        });
        Map rspData = objectMapper.readValue(rspMessage.getText(), Map.class);
        if( "0001".equals(rspData.get("returnCode")) &&
                !rspData.containsKey("appStatus")) {
            score++;
            return;
        }
        Assert.isTrue(false);
    }




    @Test
    @Order(23)
    @DisplayName("直接測試Backend_700序號重複")
    void backend_6() throws JMSException, JsonProcessingException {
        total++;
        Map data = new HashMap();
        data.put("txnType", "0200");
        data.put("txnCode", "3201");
        data.put("txnDateTime", DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss"));
        data.put("txnStan", "9000003");
        data.put("returnCode", "0000");
        data.put("bankCode", "700");
        String req = objectMapper.writeValueAsString(data);
        TextMessage rspMessage = (TextMessage) jmsTemplate.sendAndReceive("WEB.TO.FEP", session -> {
            TextMessage reqMessage = session.createTextMessage(req);
            reqMessage.setJMSReplyTo(new ActiveMQQueue("FEP.TO.WEB"));
            return reqMessage;
        });
        Map rspData = objectMapper.readValue(rspMessage.getText(), Map.class);
        if( "2999".equals(rspData.get("returnCode"))) {
            score++;
            return;
        }
        Assert.isTrue(false);
    }







    @Test
    @Order(999)
    @DisplayName("總分")
    void login2() {
        System.out.println("Score => " + score + "/" + total);
    }





    private HttpEntity getHttpEntity(Object body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer "+token);
        return new HttpEntity(body, httpHeaders);
    }

}
