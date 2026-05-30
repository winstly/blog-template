# Blog

个人博客系统，包含门户前端、管理端和服务端。

## 子项目

| 项目 | 目录 | 技术栈 | 状态 |
|------|------|--------|------|
| 门户前端 | `blog-portal/` | Vue 3 + Vite | 开发中 |
| 管理端 | `blog-admin/` | Vue 3 + Vite | 规划中 |
| 服务端 | `blog-server/` | Spring Boot + Maven | 规划中 |

## 项目结构

```
blog/
├── blog-portal/      # 门户前端
├── blog-admin/       # 管理端前端
├── blog-server/      # Java 服务端
└── docs/             # 文档
```

## 环境要求

- Node.js 18+
- Java 17+
- Maven 3.6+

## 快速开始

```bash
# 门户前端
cd blog-portal && npm install && npm run dev

# 管理端 (规划中)
cd blog-admin && npm install && npm run dev

# 服务端 (规划中)
cd blog-server && mvn spring-boot:run
```

## License

MIT
