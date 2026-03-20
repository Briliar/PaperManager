# 🎓 毕业设计管理系统 (Graduation Design Management System)

基于 **Spring Boot 3 + Vue 3** 开发的企业级全栈毕业设计管理平台，旨在为高校提供一站式、智能化的毕设全流程管理解决方案。

## 🚀 技术栈 (Tech Stack)

### 后端 (Backend)
- **核心框架**：Java 17 + Spring Boot 3
- **持久层**：MyBatis-Plus + H2 (当前为快速启动使用内存数据库，可无缝切换 MySQL 8.0)
- **安全认证**：JWT (JSON Web Token)
- **接口文档**：Knife4j (Swagger 3 / OpenAPI 3)

### 前端 (Frontend)
- **核心框架**：Vue 3 (Composition API) + Vite
- **UI 组件库**：Element Plus
- **状态管理**：Pinia
- **路由管理**：Vue Router 4
- **网络请求**：Axios

## ✨ 已实现功能模块

1. **企业级登录与鉴权**：
   - 响应式双栏插画登录页
   - JWT 颁发与验证，Pinia 状态持久化
2. **仪表盘工作台 (Dashboard)**：
   - 动态数据看板（课题数、待办任务、答辩倒计时）
   - 系统公告时间轴与快捷操作
3. **课题管理 (Topic Management)**：
   - 课题申报与双向绑定
   - 支持多条件复杂查询与分页
   - 课题审核审批流（管理员通过/驳回）
   - 动态颜色状态标签呈现

## 🛠️ 快速启动 (Quick Start)

### 1. 启动后端 (Backend)
环境要求：Java 17, Maven
```bash
cd backend
mvn clean install -DskipTests
mvn spring-boot:run
```
*后端将默认运行在 `8080` 端口。你可以访问 `http://localhost:8080/doc.html` 查看 Knife4j 接口文档。*

### 2. 启动前端 (Frontend)
环境要求：Node.js 18+
```bash
cd frontend
npm install
npm run dev
```
*前端将默认运行在 `3000` 端口。访问 `http://localhost:3000` 进入系统。*
*测试账号: `admin` / 密码: `123456`*

## 📁 目录结构

```text
├── backend/                # Spring Boot 3 后端服务
│   ├── src/main/java       # Java 业务代码
│   ├── src/main/resources  # 配置文件及 SQL 脚本 (包含 application.yml 与 db/init.sql)
│   └── pom.xml             # Maven 依赖配置
├── frontend/               # Vue 3 前端工程
│   ├── src/api             # Axios 请求接口封装
│   ├── src/views           # 视图页面 (Login, Layout, Dashboard, Topic)
│   ├── src/store           # Pinia 状态管理
│   ├── vite.config.js      # Vite 构建与代理配置
│   └── package.json        # Node 依赖配置
└── README.md               # 项目说明文档
```
