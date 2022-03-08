# 3/4 實做課 JWT

### 專案中，實作 ip 白名單 + JWT驗證 

> 鎖定只能由本機呼叫 127.0.0.1\
> 鎖定/api/**需要 token 才能呼叫

## JWT授權
> token 由 /auth/{name} 來授權\
> 代表身分認證

1. POST http://127.0.0.1:8080/api/hello/kenny
> 收到回覆 => UNAUTHORIZED
2. POST http://127.0.0.1:8080/bye/kenny
> 收到回覆 => Bye, kenny
3. POST http://127.0.0.1:8080/auth/kenny
> 收到回覆 => eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZW5ueSIsImF1dGgiOiJVU0VSIiwiaWF0IjoxNjQ2NTc4Mjk1LCJleHAiOjE2NDY2NjQ2OTV9.yC14M0cR__OXQlCeNhNxcrV85pSls--3KrmE69jOhBo
> JWT token每次回覆皆會不同
4. POST http://127.0.0.1:8080/api/hello/kenny
> request Header 加上 Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZW5ueSIsImF1dGgiOiJVU0VSIiwiaWF0IjoxNjQ2NTc4Mjk1LCJleHAiOjE2NDY2NjQ2OTV9.yC14M0cR__OXQlCeNhNxcrV85pSls--3KrmE69jOhBo
> 收到回覆 => Hello World, kenny
5. POST http://127.0.0.1:8080/bye/kenny
> 收到回覆 => Bye, kenny

### 有疑問請寄信
> kenny_lu@provision.com.tw