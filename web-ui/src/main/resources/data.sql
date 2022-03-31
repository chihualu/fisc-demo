INSERT INTO WEB_USER_INFO (USER_ID, PASSWORD, USER_NAME, ENABLED, ROLE_ID, API_TOKEN, LAST_LOGIN_IP) VALUES
    ('kenny', '$2a$10$8BVNZ8bgcNaHBbarQCiqxexfji6n1QKJkpJygEO3gfgLA/S/Uxr92', 'KennyLu', true, 'ROLE1', '', ''),
    ('steve', '$2a$10$8BVNZ8bgcNaHBbarQCiqxexfji6n1QKJkpJygEO3gfgLA/S/Uxr92', 'SteveWang', true, 'ROLE2', '', '');
INSERT INTO WEB_ROLE_INFO (ROLE_ID, ROLE_NAME, FUNC_LIST) VALUES
    ('ROLE1', '使用者群組1', '{"folder.system":[],"system.users":["q","m"],"system.roles": ["q","m"],"folder.fisc":[],"fisc.notice":["x"]}'),
    ('ROLE2', '使用者群組2', '{"folder.system":[],"system.users":["q","m"],"system.roles": ["q","m"],"system.permissions":["q","m"]}');
INSERT INTO WEB_FUNC_INFO (FUNC_ID, FUNC_NAME, FUNC_URL, HIDDEN, PARENT_ID, ORDER_NUM, ACTIONS) VALUES
    ('folder.system', '系統設定', '#', false, 'root', 1, '[]'),
    ('system.users', '使用者設定', '/web/system/users', false, 'folder.system', 1, '["q", "m"]'),
    ('system.roles', '群組設定', '/web/system/roles', false, 'folder.system', 2, '["q", "m"]'),
    ('system.permissions', '權限設定', '/web/system/permission', false, 'folder.system', 3, '["q", "m"]'),
    ('folder.fisc', '財金類別', '#', false, 'root', 2, '[]'),
    ('fisc.notice', '對財金發送訊息', '/web/fisc/notice', false, 'folder.fisc', 1, '["q", "m"]');
