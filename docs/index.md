# 實作課 
##  課程宗旨
```markdown
- 從零開始學Java
- 銀行系統概論
- 系統間的資料交換
```
##  上課人員:
```markdown
- 2/25  [Jerry](mailto:jerry_yang@provision.com.tw)
- 3/04  [Kenny](mailto:kenny_lu@provision.com.tw)
- 3/11  [Steven](mailto:steven_lin@provision.com.tw)
- 3/18  [Jasper](mailto:jasper_lin@provision.com.tw)
- 4/01  [Steve](mailto:Steve_Wang@provision.com.tw)
- 5/06  [Rio](mailto:rio_chang@provision.com.tw)
- 5/13  [Paul](mailto:paul_yang@provision.com.tw)
- 5/20  [Paul](mailto:paul_yang@provision.com.tw)
```
##  課程大綱
### **2/25**銀行應用系統架構及跨行介紹
```markdown
無實作課程
```
### **3/04**支付交易安全介紹
```markdown
前端ip白名單 + JWT驗證 
1. 特定網址白名單驗證，藉此增加安全度 
2. API功能需授權  
```
### **3/11**銀行跨行前置系統(FEP)介紹
```markdown
網頁製作
1.  登入功能-Spring Security
2.  網頁模板實作-Thymeleaf
3.  多國語系-i18n
4.  設定檔加密-Jasypt
5.  AOP
```
### **3/18**金融卡介紹
```markdown
前後端資料交換
1.  Backend 資料傳遞
2.  Backend 程式開發
3.  Frontend 資料交換
```
### **3/25**停課乙次
```markdown
無實作課程
```
### **4/01**FEP 通匯業務介紹
```markdown
資料庫使用
1. H2 簡易資料庫
2. JPA應用 CRUD
3. 登入使用Spring Security + DB驗證
4. 功能清單使用資料庫內容
```
### **4/08**停課乙次
```markdown
無實作課程
```
### **4/15**財金業務介紹
```markdown
無實作課程
```
### **4/22**財金系統介紹
```markdown
無實作課程
```
### **4/29**期中考試週
```markdown
無實作課程
```
### **5/06**財金與銀行OPC、結帳及清算
```markdown

```
### **5/13**FEP ATM業務介紹(提款、轉帳)
```markdown

```
### **5/20**FEP ATM業務介紹(繳稅、繳費)
```markdown

```
### **5/27**
```markdown
無實作課程
```

##  評分標準
1. 期中考成績（__20%__）：
   >**財金跨行系統學習心得（500-1000字）**
2. 期末考成績（__70%__）：
   > **財金跨行系統實作**
3. 課堂提問者（__10%__）：
   >**不限提問次數+2分/堂課，學期分數至多+10分。**

## 期末作業內容
  
## WebUI 
- ### 功能一: 銀行行庫檔案 的CRUD功能
   ### 功能說明
  - 頁面輸入bankCode(選填), telZone(選填)查詢
  - 詳細內容為BANKTAB資料

   ### URL格式範例
  - 新增(POST) **/api/v1/fisc/bank/**
  - 查詢多筆(GET) **/api/v1/fisc/bank?bankCode=&telZone=**
  - 查詢單筆(GET) **/api/v1/fisc/bank/987**
  - 刪除(DELETE) **/api/v1/fisc/bank/987**
  - 修改(PUT) **/api/v1/fisc/bank/987**

```markdown
format1: 新增/修改
    Req: {
            "bankCode": "987", 
            "bankName": "XX銀行", 
            "telZone": "02", 
            "telNo": "23456789"
         }
    Rsp: {
         }

format2: 查詢單筆 
    Req: null
    Rsp: {
            "bankCode": "987",
            "bankName": "XX銀行",
            "telZone": "02",
            "telNo": "23456789"
         }

format3: 查詢多筆
    Req: null
    Rsp: {
            data: [
                {
                    "bankCode": "987", 
                    "bankName": "XX銀行", 
                    "telZone": "02", 
                    "telNo": "23456789"
                },
                {
                    "bankCode": "988",
                    "bankName": "OO銀行",
                    "telZone": "02",
                    "telNo": "23456788"
                }
            ]
        }
format4: 刪除
    Req: null
    Rsp: {
         }
```

- ### 功能二: 發送OPC查詢行庫狀態
   ###  功能說明
  - 頁面輸入(bankCode)
  - 發送訊息至Backend
  - 收到訊息後顯示於畫面上

   ### URL格式範例
  - 發查(POST) **/api/v1/fisc/status/**

```markdown
UI  
    Req: {
            "bankCode": "987"
         }
    Rsp: {
            "bankCode": "987",
            "fiscStatus": "1",
            "bankStatus": "1",
            "appStatus": "1"
         }

Backend
    Req: { 
            "txnType": "0200",
            "txnCode": "3201",
            "txnDateTime": "20220428120000",
            "txnStan": "0000001",
            "returnCode": "0000",
            "bankCode": "987"
         }

    Rsp: {
            "txnType": "0210",
            "txnCode": "3201",
            "txnDateTime": "20220428120000",
            "txnStan": "0000001",
            "returnCode": "0001",
            "bankCode": "987",
            "fiscStatus": "1",
            "bankStatus": "1",
            "appStatus": "1"
         }
```
## Backend
- ### OPC查詢行庫狀態 P203201FmWebReq
  ### 功能說明
  > 1. 接收資料(bankCode)
  > 2. 發查至財金 (待討論)
  > 3. 回覆狀態回UI(fiscStatus, bankStatus, appStatus)

## Database

- ### BANKTAB(銀行行庫檔) 
  
  >BANKCODE,行庫代碼,VARCHAR(3)\
  >BANKNAME,行庫名稱,VARCHAR(36)\
  >TELZONE,區碼,VARCHAR(3)\
  >TELNO,電話,VARCHAR(10)\
  >UPDATEDATE,更新日期,VARCHAR(8)
  

### **加分名單**
```markdown
上課問問題或回答
1. 3/4  => 407411437、407410025、407410058、408417011、407410389、404411059、407411049
2. 3/11 => 407411437、407410025、407410058、408417011、407410389、404411059、407411049
3. 3/18 => 407411437、407410025、407410058、408417011、407410389、404411059、407411049、408418126
4. 4/1 => 407410025、408418126、408417011、407410058、407411437、407410389、407411049、404411059、407411395
統計: 

github pull request 
3/31 => 404411059 
```


