-- 语音识别接口菜单，挂到「系统管理」下。
INSERT INTO sys_menu
SELECT '124', '语音识别接口', '1', '12', 'asrConfig', 'im/asrConfig/index', '', 1, 0, 'C', '0', '0',
       'im:asrConfig:view', 'record', 103, 1, SYSDATE(), NULL, NULL, '语音识别接口配置'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 124);

INSERT INTO sys_menu
SELECT '1066', '语音识别查询', '124', '1', '#', '', '', 1, 0, 'F', '0', '0',
       'im:asrConfig:view', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 1066);

INSERT INTO sys_menu
SELECT '1067', '语音识别修改', '124', '2', '#', '', '', 1, 0, 'F', '0', '0',
       'im:asrConfig:edit', '#', 103, 1, SYSDATE(), NULL, NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 1067);
