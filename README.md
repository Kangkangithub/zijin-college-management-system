# 紫金学院教务管理系统

南京理工大学紫金学院 · 跨平台开发实践课程设计

## 技术栈

- **后端**: Spring Boot 3.3 + MyBatis-Plus + MySQL
- **前端**: Vue 3 + Vite + Element Plus + Vue Router + Pinia
- **数据库**: MySQL 8.0+

## 项目结构

`
zijin-system/
├── zijin-server/          # SpringBoot 后端
│   ├── src/main/java/com/zijin/
│   │   ├── common/        # 通用返回类
│   │   ├── config/        # CORS 配置
│   │   ├── controller/    # 控制器层
│   │   ├── dto/           # 数据传输对象
│   │   ├── entity/        # 实体类
│   │   ├── mapper/        # MyBatis Mapper
│   │   └── service/       # 业务逻辑层
│   └── src/main/resources/
│       ├── application.yml
│       └── sql/init.sql   # 数据库初始化脚本
├── zijin-web/             # Vue3 前端
│   ├── src/
│   │   ├── api/           # Axios 封装
│   │   ├── router/        # 路由配置 + 权限拦截
│   │   └── views/         # 页面组件
│   └── package.json
└── README.md
`

## 快速启动

### 1. 初始化数据库

`ash
mysql -uroot -p205318 < zijin-server/src/main/resources/sql/init.sql
`

### 2. 启动后端 (端口 8088)

`ash
cd zijin-server
mvn spring-boot:run
`

### 3. 启动前端 (端口 5173)

`ash
cd zijin-web
npm install
npm run dev
`

### 4. 访问系统

打开浏览器访问 http://localhost:5173

## 测试账号

| 角色   | 用户名    | 密码   |
|--------|----------|--------|
| 管理员 | admin    | 123456 |
| 教师   | teacher1 | 123456 |
| 教师   | teacher2 | 123456 |
| 学生   | student1 | 123456 |
| 学生   | student2 | 123456 |
| 学生   | student3 | 123456 |

## 功能模块

- **登录/注册**: 用户认证，新用户注册
- **首页**: 欢迎页 + 数据统计概览
- **人事管理** (管理员): 部门管理、专业管理
- **教务管理**: 班级管理（管理员可CRUD，教师只读）
- **系统管理** (管理员): 账户维护、角色分配、状态控制
- **角色权限**: 管理员/教师/学生三种角色，菜单动态渲染

## 跨平台适配

- PC 端: 侧边栏 + 内容区布局，表格展示
- 移动端 (<768px): 抽屉式侧边栏，响应式表格，触摸友好
