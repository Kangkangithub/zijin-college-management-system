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
![ECharts](https://img.shields.io/badge/ECharts-5.5-AA344D?logo=apacheecharts&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)

</div>

---

## 📖 项目简介

本项目是一个**前后端分离**的学院综合管理系统，支持 **PC 端与移动端双端适配**，划分**管理员、教师、学生**三种角色，实现菜单动态渲染、功能级权限控制、**跨角色协作交互**与 **ECharts 数据可视化**。

### ✨ 核心功能

| 模块 | 功能 | 权限 |
|------|------|------|
| 🔐 登录/注册 | 用户认证、新用户自助注册 | 所有用户 |
| 🏠 首页仪表盘 | 数据统计概览（按角色差异化展示） | 所有用户 |
| 👥 人事管理 | 部门管理 + 专业管理 CRUD | 管理员 |
| 📚 教务管理 | 班级管理 + 班级内学生管理 | 管理员/教师 |
| ⚙️ 系统管理 | 账户维护、角色分配、状态控制 | 管理员 |
| 👤 个人中心 | 学生信息编辑、修改密码、查看同班同学 | 学生 |
| 📊 **数据可视化** | ECharts 柱状图、饼图、统计卡片动效 | 按角色展示 |
| 📢 **公告互动** | 发布公告、点赞、评论互动 | 🔥 跨角色 |
| 📝 **请假审批** | 学生提交 → 教师审批 → 管理员查看 | 🔥 跨角色 |

### 🎭 三种角色 & 测试账号

| 角色 | 用户名 | 密码 | 权限范围 |
|------|--------|------|----------|
| 🔴 管理员 | `admin` | `123456` | 全部功能（含发布/删除公告、全局查看请假） |
| 🟠 教师 | `teacher1` | `123456` | 班级管理、发布公告、审批请假、评论互动 |
| 🟢 学生 | `student1` | `123456` | 个人信息、查看公告、评论点赞、提交请假 |

---

## 🔥 跨角色协作交互

系统实现了**两个核心跨角色交互场景**，满足"不同角色之间至少有一个核心功能的交互页面展示"的课程要求：

### 📢 公告互动系统

```
管理员/教师发布公告 ──→ 全体用户查看 ──→ 点赞 + 评论互动
     │                      │
     └── 删除管理 ←─────────┘
```

| 操作 | 管理员 | 教师 | 学生 |
|------|:---:|:---:|:---:|
| 发布公告 | ✅ | ✅ | — |
| 删除公告 | ✅ | — | — |
| 点赞 | ✅ | ✅ | ✅ |
| 评论 | ✅ | ✅ | ✅ |

### 📝 请假审批系统

```
学生提交申请 ──→ 教师审批(通过/驳回) ──→ 学生查看结果
                                         │
                   管理员全局监控 ←───────┘
```

| 操作 | 管理员 | 教师 | 学生 |
|------|:---:|:---:|:---:|
| 提交请假 | — | — | ✅ |
| 审批(通过/驳回) | — | ✅ | — |
| 全局查看 | ✅ | ✅ | — |
| 查看自己的 | — | — | ✅ |

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
│  8 张表：t_user · t_role · t_department  │
│  t_major · t_class · t_announcement      │
│  t_comment · t_leave                     │
└──────────────────────────────────────────┘
```

---

## 📁 项目结构

```
zijin-system/
├── zijin-server/                     # Spring Boot 后端
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/zijin/
│       │   ├── ZijinApplication.java     # 启动类
│       │   ├── common/Result.java        # 统一响应
│       │   ├── config/CorsConfig.java    # 跨域配置
│       │   ├── controller/               # 7 个控制器
│       │   │   ├── LoginController.java
│       │   │   ├── DepartmentController.java
│       │   │   ├── MajorController.java
│       │   │   ├── ClassController.java
│       │   │   ├── UserController.java
│       │   │   ├── AnnouncementController.java  # 公告+评论
│       │   │   └── LeaveController.java         # 请假审批
│       │   ├── service/                 # 业务逻辑层 (7个)
│       │   ├── mapper/                  # MyBatis Mapper (8个)
│       │   ├── entity/                  # 8 个实体类
│       │   └── dto/                     # LoginDto, RegisterDto
│       └── resources/
│           ├── application.yml
│           └── sql/init.sql
│
├── zijin-web/                         # Vue 3 前端
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── main.js
│       ├── api/index.js               # Axios 封装 (baseURL=/api)
│       ├── router/index.js            # 路由 + 登录拦截
│       └── views/
│           ├── Login.vue              # 登录页
│           ├── Register.vue           # 注册页
│           ├── Layout.vue             # 主布局 (角色菜单动态渲染)
│           ├── Home.vue               # 首页仪表盘 (按角色展示)
│           ├── Profile.vue            # 学生个人中心
│           ├── Department.vue         # 部门管理 CRUD
│           ├── Major.vue              # 专业管理 CRUD
│           ├── Class.vue              # 班级管理 + 班级学生管理
│           ├── Students.vue           # 学生管理 (按班级筛选)
│           ├── Account.vue            # 账户维护 CRUD
│           ├── Announcement.vue       # 🆕 公告互动 (发布/点赞/评论)
│           └── Leave.vue              # 🆕 请假审批 (提交/审批/查看)
│
├── README.md
├── 答辩准备-系统理解.md
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

### 方式一：一键启动（推荐）

```bash
# 双击运行，自动完成数据库检查、后端编译启动、前端启动
startup.bat
```

### 方式二：分步启动

**1. 初始化数据库**

```bash
# 首次运行需导入建表脚本和测试数据
mysql -uroot -p205318 -e "CREATE DATABASE IF NOT EXISTS zijin DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

**2. 启动后端（端口 8088）**

```bash
cd zijin-server
mvn clean package -DskipTests
java --enable-native-access=ALL-UNNAMED -jar target/zijin-server-1.0.0.jar
```

**3. 启动前端（端口 5173）**

```bash
cd zijin-web
npm install
npm run dev
```

**4. 访问系统**

打开浏览器访问 **http://localhost:5173**

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
| 公告/请假卡片 | 宽屏卡片 | 满宽堆叠，信息纵向排列 |

使用 Chrome DevTools 切换 Device Toolbar 即可预览移动端效果。

---

## 🗄 数据库设计

| 表名 | 说明 | 核心字段 |
|------|------|---------|
| `t_user` | 用户表 | id, username, pwd, real_name, student_no, gender, role_id, major_id, class_id, status |
| `t_role` | 角色表 | id, role_name, role_code |
| `t_department` | 部门表 | id, dept_name, dept_code |
| `t_major` | 专业表 | id, major_name, major_code, dept_id |
| `t_class` | 班级表 | id, class_name, class_code, major_id, grade |
| `t_announcement` | 🆕 公告表 | id, title, content, publisher_id, publisher_name, like_count |
| `t_comment` | 🆕 评论表 | id, announcement_id, user_id, user_name, content |
| `t_leave` | 🆕 请假表 | id, student_id, student_name, class_id, reason, start_date, end_date, status, approver_name |

**表间关系**：  
- `t_user.role_id` → `t_role.id`，`t_user.major_id` → `t_major.id`，`t_user.class_id` → `t_class.id`  
- `t_major.dept_id` → `t_department.id`，`t_class.major_id` → `t_major.id`  
- `t_announcement.publisher_id` → `t_user.id`  
- `t_comment.announcement_id` → `t_announcement.id`（一对多），`t_comment.user_id` → `t_user.id`  
- `t_leave.student_id` → `t_user.id`，`t_leave.class_id` → `t_class.id`

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
| ECharts | 5.5 | 数据可视化图表库 |

### 后端
| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.3 | Java Web 框架 |
| MyBatis-Plus | 3.5 | ORM 增强工具 |
| MySQL | 8.0 | 关系型数据库 |
| Maven | 3.9 | 项目构建 |

---

## 📋 API 接口一览

### 认证
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/login` | 用户登录 |
| POST | `/api/register` | 用户注册 |

### 部门管理
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/department` | 部门列表 |
| POST | `/api/department` | 新增部门 |
| PUT | `/api/department` | 修改部门 |
| DELETE | `/api/department/{id}` | 删除部门 |

### 专业管理
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/major` | 专业列表 |
| POST | `/api/major` | 新增专业 |
| PUT | `/api/major` | 修改专业 |
| DELETE | `/api/major/{id}` | 删除专业 |

### 班级管理
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/class` | 班级列表 |
| POST | `/api/class` | 新增班级 |
| PUT | `/api/class` | 修改班级 |
| DELETE | `/api/class/{id}` | 删除班级 |

### 用户管理
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/user` | 用户列表 |
| POST | `/api/user` | 新增用户 |
| PUT | `/api/user` | 修改用户（含状态切换） |
| DELETE | `/api/user/{id}` | 删除用户 |

### 🆕 公告互动
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/announcement` | 公告列表（含嵌套评论） |
| POST | `/api/announcement` | 发布公告 |
| DELETE | `/api/announcement/{id}` | 删除公告及评论 |
| PUT | `/api/announcement/like/{id}` | 点赞 +1 |
| POST | `/api/announcement/comment` | 发表评论 |

### 🆕 数据统计
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/stats` | 系统统计数据（各表数量、角色分布） |

### 🆕 请假审批
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/leave` | 请假列表（支持 studentId/classId 筛选） |
| POST | `/api/leave` | 提交请假申请 |
| PUT | `/api/leave/approve/{id}` | 审批通过/驳回 |

---

## 📝 开发说明

- 按课程设计指导书要求，采用**最小技术栈**，不引入 Spring Security、JWT、密码加密等组件
- 密码以明文存储于 `t_user.pwd` 字段
- 登录状态通过 `localStorage`（key: `zijin_user`）维护，路由守卫拦截未登录请求
- 角色权限通过前端菜单条件渲染 + 路由 meta 实现
- 后端采用 MyBatis-Plus `BaseMapper` 实现零 SQL 的 CRUD 操作
- 中文编码使用 `utf8mb4`，数据导入须通过 Python pymysql 直连，避免 PowerShell GBK 编码损坏

---

## 📄 License

MIT License · 仅供学习交流使用

---

<p align="center">Made with ❤️ for 紫金学院</p>
