# 實作課 [Github](https://github.com/chihualu/fisc-demo)
##  課程宗旨
```markdown
從零開始
前端網頁製作
後臺程式間接
```
##  上課人員: 
- 3/4   Kenny   (kenny_lu@provision.com.tw)
- 3/11  Steven  (steven_lin@provision.com.tw)

##  課程大綱
- 銀行應用系統架構及跨行介紹
- 支付交易安全介紹
- 銀行跨行前置系統(FEP)介紹
- 金融卡介紹
- FEP 通匯業務介紹
- 財金業務介紹
- 財金系統介紹
- 財金與銀行OPC、結帳及清算
- FEP ATM業務介紹(提款、轉帳)
- FEP ATM業務介紹(繳稅、繳費)

##  評分標準
1. 出席率**30分**\
``10次 -> 30分，每缺一次扣3分``
2. 上課狀況**30分**\
``上課態度(10分)及提問(每提問一次加2分)``
3. 期中考**20分**\
``報告交付``
4. 期末考**20分**\
``程式交付``








##  課程內容
### 3/4   前端ip白名單 + JWT驗證 
```markdown
  特定網址白名單驗證，藉此增加安全度
  API功能需授權  
1.  POST http://127.0.0.1:8080/api/hello/kenny
  **收到回覆 => UNAUTHORIZED**
2.  POST http://127.0.0.1:8080/bye/kenny
  **收到回覆 => Bye, kenny**
3.  POST http://127.0.0.1:8080/auth/kenny
  **收到回覆 => eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZW5ueSIsImF1dGgiOiJVU0VSIiwiaWF0IjoxNjQ2NTc4Mjk1LCJleHAiOjE2NDY2NjQ2OTV9.yC14M0cR__OXQlCeNhNxcrV85pSls--3KrmE69jOhBo**
  **JWT token每次回覆皆會不同**
4.  POST http://127.0.0.1:8080/api/hello/kenny
  **request Header 加上 Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZW5ueSIsImF1dGgiOiJVU0VSIiwiaWF0IjoxNjQ2NTc4Mjk1LCJleHAiOjE2NDY2NjQ2OTV9.yC14M0cR__OXQlCeNhNxcrV85pSls--3KrmE69jOhBo**
  **收到回覆 => Hello World, kenny**
5.  POST http://127.0.0.1:8080/bye/kenny
  **收到回覆 => Bye, kenny**
```
### 3/11  網頁製作 
```markdown
1.  登入功能-Spring Security
2.  網頁模板實作-Thymeleaf
3.  多國語系-i18n
4.  設定檔加密-Jasypt
5.  AOP
```
