# 知晓IM 后台管理

知晓IM 后台管理系统，提供用户、群组、消息和敏感词等管理能力。

## 组成

- `im-admin`：后台服务端
- `im-admin-ui`：后台前端

## 启动

```bash
cd im-admin
mvn clean package
java -jar ./ruoyi-admin/target/im-admin.jar
```

```bash
cd im-admin-ui
npm install
npm run dev
```
