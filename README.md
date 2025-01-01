# 盒子IM后台管理

#### 介绍
本项目为盒子IM后台管理,主要为盒子IM提供用户管理、群组管理、消息管理、敏感词管理等后台功能。

缺少后台管理基本不影响盒子IM的业务功能，建议先把业务服务跑起来，再来搭建后台服务:
业务代码地址：https://gitee.com/bluexsx/box-im  
详细文档:  https://www.yuque.com/u1475064/mufu2a

#### 基于RuoYi-Vue-Plus框架
为了减少重复工作，达到快速开发目的，本项目选择了一款优秀的开源脚手架RuoYi-Vue-Plus进行二次开发。

RuoYi-Vue-Plus的仓库:    
后端:https://gitee.com/dromara/RuoYi-Vue-Plus   
前端: https://gitee.com/JavaLionLi/plus-ui


#### 框架改造说明
为了更好地与盒子IM的业务相结合，同时保持代码的简洁性，对ruoyi-vue-plus框架进行了以下改造:
1.  移除了定时任务、监控、工作流模块
2.  添加minio模块替代原先的oss模块
3.  加入了ruoyi-im模块，此模块即为盒子IM的核心后台模块
4.  为了兼容历史数据,逻辑删除值由'2'修改为'1'

#### 本地快速启动

1.安装运行环境
- 安装node:v18.19.0
- 安装jdk:17
- 安装maven:3.9.6
- 安装mysql:8.0,密码分别为root/root,创建名为im_admin的数据库，并执行db/im-admin.sql
- 安装redis:6.2
- 安装minio:RELEASE.2024-xx,使用默认账号、密码、端口

注: 盒子IM的后台服务同时还依赖im-platform的数据库，请在启动前先初始化该数据库

2.启动后端服务  
进入 im-admin目录，打开控制台
```
mvn clean package
java -jar ./ruoyi-admin/target/im-admin.jar
```

3.启动前端  
进入 im-admin-ui目录，打开控制台
```
npm install
npm run dev
```
访问 http://localhost:3000

#### 界面截图:




#### 点下star吧
如果项目对您有帮助，请点亮右上方的star，支持一下作者吧！