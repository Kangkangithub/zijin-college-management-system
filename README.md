# 🏫 紫金学院管理系统

> **Zijin College Management System**  
> 南京理工大学紫金学院 · 跨平台开发实践课程设计  
> Spring Boot 3.3 + Vue 3 + MySQL 全栈项目

---

<div align="center">

![Vue](https://img.shields.io/badge/Vue-3.4-4FC08D?logo=vue.js&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-5.4-646CFF?logo=vite&logoColor=white)
![Element Plus](https://img.shields.io/badge/Element_Plus-2.5-409EFF?logo=element&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3-6DB33F?logo=springboot&logoColor=white)
![MyBatis Plus](https://img.shields.io/badge/MyBatis_Plus-3.5-1E90FF?logo=mybatis&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)

</div>

---

## 📖 项目简介

本项目是一个**前后端分离**的学院综合管理系统，支持 **PC 端与移动端双端适配**，划分**管理员、教师、学生**三种角色，实现菜单动态渲染与功能级权限控制。

### ✨ 核心功能

| 模块 | 功能 | 权限 |
|------|------|------|
| 🔐 登录/注册 | 用户认证、新用户自助注册 | 所有用户 |
| 🏠 首页仪表盘 | 数据统计概览（部门/班级/用户数） | 按角色展示 |
| 👥 人事管理 | 部门管理 + 专业管理 CRUD | 管理员 |
| 📚 教务管理 | 班级管理 CRUD | 管理员/教师 |
| ⚙️ 系统管理 | 账户维护、角色分配、状态控制 | 管理员 |

### 🎭 三种角色

| 角色 | 用户名 | 密码 | 权限范围 |
|------|--------|------|----------|
| 🔴 管理员 | `admin` | `123456` | 全部功能 |
| 🟠 教师 | `teacher1` | `123456` | 班级查看 |
| 🟢 学生 | `student1` | `123456` | 个人信息 |

---

## 🏗 项目架构

```
┌──────────────────────────────────────────┐
│         浏览器 (PC / 移动端)               │
│         http://localhost:5173             │
│         Vue 3 + Element Plus              │
└──────────────┬───────────────────────────┘
               │  Axios (RESTful API)
               ▼
┌──────────────────────────────────────────┐
│      Spring Boot (端口 8088)              │
│  Controller → Service → Mapper           │
│  MyBatis-Plus 自动 CRUD                   │
└──────────────┬───────────────────────────┘
               │  JDBC
               ▼
┌──────────────────────────────────────────┐
│          MySQL (zijin)                   │
│  t_user · t_role · t_department          │
│  t_major · t_class                       │
└──────────────────────────────────────────┘
```

---

## 📁 项目结构

```
zijin-system/
├── zijin-server/                    # Spring Boot 后端
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/zijin/
│       │   ├── ZijinApplication.java    # 启动类
│       │   ├── common/Result.java       # 统一响应
│       │   ├── config/CorsConfig.java   # 跨域配置
│       │   ├── controller/              # 5 个控制器
│       │   ├── service/                 # 业务逻辑层
│       │   ├── mapper/                  # MyBatis Mapper
│       │   ├── entity/                  # 5 个实体类
│       │   └── dto/                     # 数据传输对象
│       └── resources/
│           ├── application.yml          # 配置
│           └── sql/init.sql             # 建表+测试数据
│
├── zijin-web/                        # Vue 3 前端
│   ├── package.json
│   ├── vite.config.js                # 代理配置
│   └── src/
│       ├── main.js                   # 入口
│       ├── api/index.js              # Axios 封装
│       ├── router/index.js           # 路由 + 权限拦截
│       └── views/
│           ├── Login.vue             # 登录页
│           ├── Register.vue          # 注册页
│           ├── Layout.vue            # 主布局(侧边栏+顶栏)
│           ├── Home.vue              # 首页仪表盘
│           ├── Department.vue        # 部门管理
│           ├── Major.vue             # 专业管理
│           ├── Class.vue             # 班级管理
│           └── Account.vue           # 账户维护
│
├── README.md                         # 项目说明
├── 答辩准备-系统理解.md                # 答辩问答
├── startup.bat                       # 一键启动脚本
└── .gitignore
```

---

## 🚀 快速启动

### 环境要求

- **JDK** 17+
- **Maven** 3.9+
- **Node.js** 18+
- **MySQL** 8.0+

### 1. 初始化数据库

```bash
mysql -uroot -p205318 < zijin-server/src/main/resources/sql/init.sql
```

### 2. 启动后端（端口 8088）

```bash
cd zijin-server
mvn spring-boot:run
```

### 3. 启动前端（端口 5173）

```bash
cd zijin-web
npm install
npm run dev
```

### 4. 访问系统

打开浏览器访问 **http://localhost:5173**，使用测试账号登录。

> 💡 或直接双击 `startup.bat` 一键启动。

---

## 📱 跨平台适配

| 特性 | PC 端 (≥768px) | 移动端 (<768px) |
|------|---------------|----------------|
| 布局 | 侧边栏 + 内容区双栏 | 抽屉式侧边栏 + 遮罩层 |
| 导航 | 侧边栏常驻 240px | 汉堡菜单触发滑出 |
| 表格 | 完整列宽展示 | 横向滚动 + 压缩列宽 |
| 弹窗 | 固定 500px 宽 | 94% 屏宽，全屏适配 |
| 表单 | 标签 80px | 标签 70px，小字号 |
| 统计卡片 | 三列栅格 | 单列堆叠 |

使用 Chrome DevTools 切换 Device Toolbar 即可预览移动端效果。

---

## 🗄 数据库设计

| 表名 | 说明 | 核心字段 |
|------|------|---------|
| `t_user` | 用户表 | id, username, pwd, real_name, role_id, major_id, class_id, status |
| `t_role` | 角色表 | id, role_name, role_code |
| `t_department` | 部门表 | id, dept_name, dept_code |
| `t_major` | 专业表 | id, major_name, major_code, dept_id |
| `t_class` | 班级表 | id, class_name, class_code, major_id, grade |

**表间关系**：`t_user` → `t_role` / `t_major` / `t_class` ← `t_major` → `t_department`

---

## 🛠 技术栈

### 前端
| 技术 | 版本 | 用途 |
|------|------|------|
| Vue 3 | 3.4 | 渐进式前端框架 |
| Vite | 5.4 | 开发构建工具 |
| Element Plus | 2.5 | UI 组件库 |
| Vue Router | 4.3 | 前端路由 |
| Pinia | 2.1 | 状态管理 |
| Axios | 1.6 | HTTP 客户端 |

### 后端
| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.3 | Java Web 框架 |
| MyBatis-Plus | 3.5 | ORM 增强工具 |
| MySQL | 8.0 | 关系型数据库 |
| Maven | 3.9 | 项目构建 |

---

## 📋 API 接口一览

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | `/api/login` | 用户登录 | 公开 |
| POST | `/api/register` | 用户注册 | 公开 |
| GET | `/api/department` | 部门列表 | admin |
| POST | `/api/department` | 新增部门 | admin |
| PUT | `/api/department` | 修改部门 | admin |
| DELETE | `/api/department/{id}` | 删除部门 | admin |
| GET | `/api/major` | 专业列表 | 公开 |
| POST | `/api/major` | 新增专业 | admin |
| PUT | `/api/major` | 修改专业 | admin |
| DELETE | `/api/major/{id}` | 删除专业 | admin |
| GET | `/api/class` | 班级列表 | 登录用户 |
| POST | `/api/class` | 新增班级 | admin |
| PUT | `/api/class` | 修改班级 | admin |
| DELETE | `/api/class/{id}` | 删除班级 | admin |
| GET | `/api/user` | 用户列表 | admin |
| POST | `/api/user` | 新增用户 | admin |
| PUT | `/api/user` | 修改用户 | admin |
| DELETE | `/api/user/{id}` | 删除用户 | admin |

---

## 📝 开发说明

- 本系统为课程设计项目，按指导书要求采用**最小技术栈**，不引入 Spring Security、JWT、密码加密等复杂组件
- 密码以明文存储于 `t_user.pwd` 字段
- 登录状态通过 `localStorage` 维护，路由守卫拦截未登录请求
- 角色权限通过前端菜单条件渲染 + 路由 meta 实现

---

## 📄 License

MIT License · 仅供学习交流使用

---

<p align="center">Made with ❤️ for 紫金学院</p>