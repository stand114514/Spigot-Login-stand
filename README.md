[![image](https://img.shields.io/badge/bilibili-stand-orange.svg)](https://space.bilibili.com/382365750)
[![image](https://img.shields.io/badge/java-Minecraft--1.19.3-green.svg)](https://www.minecraft.net/)
[![image](https://img.shields.io/badge/java-Spigot--1.19.3-blue.svg)](https://www.spigotmc.org/)
# Login-stand
**IDEA项目**  
一个我的世界(1.19.3)spigot核心的登录插件

## 命令
    /login 密码
(注册和登录都是这个)

## 功能
**玩家进入服务器检测是否注册**
- 如果没有注册，需要输入指令来设置密码，若不设置将会限制你的移动
- 如果已经注册，先会检测你的ip是否和注册时ip相同
    1. 如果相同，自动登录
    2. 如果不同，限制移动

## 配置
**每次插件启动时，会检测插件目录是否存在玩家密码文件(plugins/login.yml)  
如果不存在，则会创建**

## 贡献者
[**Stand**](https://github.com/stand114514)