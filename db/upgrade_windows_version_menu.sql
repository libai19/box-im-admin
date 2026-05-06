-- Windows 客户端版本菜单，挂到「系统管理」下。
INSERT IGNORE INTO sys_menu VALUES('125', 'Windows客户端版本', '1', '13', 'windowsVersion', 'im/windowsVersion/index', '', 1, 0, 'C', '0', '0',
       'im:windowsVersion:view', 'international', 103, 1, SYSDATE(), NULL, NULL, 'Windows客户端版本配置');

INSERT IGNORE INTO sys_menu VALUES('12501', 'Windows客户端版本查看', '125', '1', '#', '', '', 1, 0, 'F', '0', '0',
       'im:windowsVersion:view', '#', 103, 1, SYSDATE(), NULL, NULL, '');

INSERT IGNORE INTO sys_menu VALUES('12502', 'Windows客户端版本修改', '125', '2', '#', '', '', 1, 0, 'F', '0', '0',
       'im:windowsVersion:edit', '#', 103, 1, SYSDATE(), NULL, NULL, '');
