CREATE TABLE WEB_USER_INFO (
    USER_ID VARCHAR(30) PRIMARY KEY,
    PASSWORD VARCHAR(300),
    USER_NAME VARCHAR(300),
    ENABLED Boolean,
    ROLE_ID VARCHAR(30),
    API_TOKEN VARCHAR(300),
    LAST_LOGIN_IP VARCHAR(50)
);

CREATE TABLE WEB_ROLE_INFO (
    ROLE_ID VARCHAR(30) PRIMARY KEY ,
    ROLE_NAME VARCHAR(100),
    FUNC_LIST TEXT
);

CREATE TABLE WEB_FUNC_INFO (
    FUNC_ID VARCHAR(50) PRIMARY KEY,
    FUNC_NAME VARCHAR(100),
    FUNC_URL VARCHAR(50),
    HIDDEN Boolean,
    PARENT_ID VARCHAR(50),
    ORDER_NUM BIGINT,
    ACTIONS VARCHAR(20)
);