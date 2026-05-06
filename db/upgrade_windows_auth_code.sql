-- 后台菜单：Windows 今日授权码。
INSERT IGNORE INTO sys_menu VALUES('126', '今日授权码', '1', '14', 'windowsAuthCode', 'im/windowsAuthCode/index', '', 1, 0, 'C', '0', '0', 'im:windowsAuthCode:view', 'validCode', 103, 1, SYSDATE(), NULL, NULL, 'Windows客户端今日授权码');
INSERT IGNORE INTO sys_menu VALUES('1068', '今日授权码查看', '126', '1', '#', '', '', 1, 0, 'F', '0', '0', 'im:windowsAuthCode:view', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('1069', '今日授权码刷新', '126', '2', '#', '', '', 1, 0, 'F', '0', '0', 'im:windowsAuthCode:edit', '#', 103, 1, SYSDATE(), NULL, NULL, '');
