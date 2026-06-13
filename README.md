<p align="center">
  <h1 align="center">🏫 校园二手交易平台 · CampusTrade</h1>
  <p align="center">
    <em>A full-stack campus second-hand goods trading platform</em>
  </p>
  <p align="center">
    <img src="https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen?logo=springboot" alt="Spring Boot">
    <img src="https://img.shields.io/badge/Vue-3.4-4fc08d?logo=vuedotjs" alt="Vue 3">
    <img src="https://img.shields.io/badge/Java-17-orange?logo=openjdk" alt="Java 17">
    <img src="https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql" alt="MySQL 8">
    <img src="https://img.shields.io/badge/license-MIT-green" alt="License">
  </p>
</p>

---

## 📖 项目简介 | About

**CampusTrade（校园二手交易平台）** 是一个面向高校学生的二手商品交易平台。学生可以在平台上发布闲置物品、浏览购买商品、收藏心仪好物，并完成交易后的双向评价。

平台采用 **前后端分离** 架构，后端提供 RESTful API，前端为 SPA（单页应用），包含完整的用户认证、商品管理、订单流程、评价体系和管理员后台。

**CampusTrade** is a second-hand goods trading platform designed for university students. It supports product listing, browsing, purchasing, favorites, and mutual reviews after transactions — all backed by a full admin audit system.

---

## ✨ 功能特性 | Features

### 用户端 | For Users
- 🔐 **JWT 认证** — 注册/登录，角色区分（普通用户 / 管理员）
- 🛒 **商品市场** — 搜索、分类浏览、按价格/时间排序
- 📦 **商品发布** — 多图上传、商品状态、成色分级
- ❤️ **收藏系统** — 收藏/取消收藏感兴趣的商品
- 📋 **订单流程** — 下单 → 卖家确认 → 买家收货 → 交易完成
- ⭐ **双向评价** — 交易完成后买卖双方互评（1-5星），影响信誉分
- 🔔 **通知系统** — 订单状态变更、审核结果通知
- 👤 **个人中心** — 资料管理、密码修改、我的商品/订单/收藏

### 管理端 | For Admins
- 📊 **数据仪表盘** — 平台总数据概览（用户/商品/订单数）
- 👥 **用户管理** — 查看、搜索、禁用/启用用户
- ✅ **商品审核** — 审核通过/拒绝，填写拒绝原因并通知卖家
- 📦 **商品管理** — 查看所有商品，按状态筛选，强制下架
- 🏷️ **分类管理** — 增删改查商品分类

---

## 🛠️ 技术栈 | Tech Stack

| Layer | Technology |
|-------|-----------|
| **Backend** | Spring Boot 3.4.3, Spring Security, MyBatis-Plus 3.5.5 |
| **Frontend** | Vue 3 (Composition API), Vite 5, Element Plus 2.7 |
| **Database** | MySQL 8.0 |
| **Auth** | JWT (jjwt 0.12.6) + BCrypt |
| **State** | Pinia 2 |
| **HTTP** | Axios 1.6 |
| **API Docs** | Knife4j (Swagger / OpenAPI 3) |
| **Build** | Gradle (backend), Vite (frontend) |

---

## 🚀 快速开始 | Quick Start

### 环境要求 | Prerequisites

| Software | Version | Notes |
|----------|---------|-------|
| JDK | 17+ | Backend runtime |
| MySQL | 8.0+ | Database |
| Node.js | 18+ | Frontend runtime |
| Gradle | Wrapper included | No manual install needed |

### 1. 克隆项目 | Clone

```bash
git clone <repo-url>
cd CampusTrade
```

### 2. 初始化数据库 | Database Setup

执行 `src/main/resources/db/init.sql` 脚本：

```bash
mysql -u root -p < src/main/resources/db/init.sql
```

脚本会自动创建 `school_trade` 数据库、8 张业务表、8 个默认商品分类。

### 3. 配置数据库连接 | Configure Database

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    username: root        # 你的 MySQL 用户名
    password: yourpass    # 你的 MySQL 密码
```

同时修改文件上传路径 `file.upload-path` 为你本机的实际路径。

### 4. 启动后端 | Start Backend

```bash
# macOS / Linux
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

后端运行在 **http://localhost:8080**  
API 文档：**http://localhost:8080/doc.html**

### 5. 启动前端 | Start Frontend

```bash
cd frontend
npm install
npm run dev
```

前端运行在 **http://localhost:5173**

> 前端已配置代理，`/api` 和 `/uploads` 请求会自动转发到后端 8080 端口。

### 6. 创建管理员 | Create Admin

注册一个普通账号后，在 MySQL 中将其角色设为管理员：

```sql
UPDATE user SET role = 1 WHERE username = 'your_username';
```

---

## 📁 项目结构 | Project Structure

```
CampusTrade/
├── src/main/java/com/example/schooltrade/
│   ├── config/              # 配置类 (Security, JWT, CORS, MyBatis-Plus)
│   ├── controller/          # REST API 控制器
│   ├── dto/                 # 数据传输对象 (Data Transfer Objects)
│   ├── entity/              # 实体类 (JPA Entities)
│   ├── exception/           # 全局异常处理
│   ├── interceptor/         # 拦截器
│   ├── mapper/              # MyBatis-Plus Mapper 接口
│   ├── service/             # 业务逻辑接口
│   │   └── impl/            # 业务逻辑实现
│   ├── util/                # 工具类 (JWT)
│   └── vo/                  # 视图对象 (View Objects)
├── src/main/resources/
│   ├── application.yml      # 应用配置
│   └── db/init.sql          # 数据库初始化脚本
├── frontend/                # Vue 3 前端项目
│   └── src/
│       ├── api/             # Axios API 封装
│       ├── router/          # Vue Router 路由
│       ├── stores/          # Pinia 状态管理
│       ├── utils/           # 工具函数
│       └── views/           # 页面组件
│           └── admin/       # 管理员后台页面
├── scripts/                 # 辅助脚本
├── docs/                    # 项目文档
├── uploads/                 # 上传文件存储目录
├── build.gradle             # Gradle 构建配置
└── settings.gradle          # Gradle 项目设置
```

---

## 🔌 API 概览 | API Overview

所有接口前缀：`/api` | 完整文档：http://localhost:8080/doc.html

| Module | Method | Endpoint | Description | Auth |
|--------|--------|----------|-------------|------|
| Auth | POST | `/auth/register` | Register | No |
| Auth | POST | `/auth/login` | Login | No |
| Auth | GET | `/auth/me` | Current user info | Yes |
| User | GET/PUT | `/user/profile` | Get / Update profile | Yes |
| User | PUT | `/user/password` | Change password | Yes |
| Category | GET | `/category/list` | Category list | No |
| Category | POST/PUT/DELETE | `/category` | Manage categories | Admin |
| Goods | GET | `/goods` | Search goods | No |
| Goods | GET | `/goods/{id}` | Goods detail | No |
| Goods | POST | `/goods` | Publish goods | Yes |
| Goods | PUT | `/goods/{id}` | Edit goods | Yes |
| Order | POST/PUT | `/order` | Create / Update orders | Yes |
| Order | GET | `/order/bought` | My purchased orders | Yes |
| Order | GET | `/order/sold` | My sold orders | Yes |
| Favorite | POST/DELETE/GET | `/favorite` | Add / Remove / List favorites | Yes |
| Review | POST/GET | `/review` | Create / Query reviews | Partial |
| Notice | GET/PUT | `/notice` | List / Mark read notices | Yes |
| File | POST | `/upload/image` | Upload image | Yes |
| Admin | GET | `/admin/dashboard` | Dashboard stats | Admin |
| Admin | GET/PUT | `/admin/users` | User management | Admin |
| Admin | GET/PUT | `/admin/goods/pending` | Audit goods | Admin |

---

## 🏗️ 业务流程 | Business Flow

### 购买流程 | Purchase Flow
```
Browse → View Detail → Buy Now → Order Created (Pending)
→ Seller Confirms → Buyer Receives → Order Complete → Mutual Review
```

### 发布流程 | Listing Flow
```
Publish → Pending Review → Admin Audit → Approved → Listed
→ Buyer Orders → Seller Confirms → Buyer Receives → Complete
```

### 评价体系 | Review System
- 交易完成后双方互评（1-5 星）
- 5星 +10 信誉分 | 4星 +5 | 3星 不变 | 2星 -5 | 1星 -10
- 支持匿名评价
- 商品平均评分由所有买家评价计算

---

## 📄 许可证 | License

本项目基于 MIT 许可证开源。详见 [LICENSE](LICENSE) 文件。

This project is open-sourced under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## 🙏 致谢 | Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [Element Plus](https://element-plus.org/)
- [MyBatis-Plus](https://baomidou.com/)
- [Knife4j](https://doc.xiaominfo.com/)
