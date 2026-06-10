# Proposal: blog-server-init

## Why

博客系统前端（portal/admin）依赖硬编码的 mock 数据，缺少后端服务支撑，无法实现内容的持久化存储、动态管理与在线交互。需要构建 blog-server 后端服务，使博客内容可持久化、可管理、可交互，实现从静态展示到动态运营的闭环。

## What Changes

### Core: 内容管理 (ContentContext)

| Capability | Priority | Description |
|-----------|----------|-------------|
| 文章发布 | P0 | 创建/编辑/发布/置顶 Article，管理草稿与发布状态流转 |
| 正文版本 | P0 | Content 寄生存储 + Revision 版本快照与回溯 |

### Supporting: 分类标签 (ClassificationContext)

| Capability | Priority | Description |
|-----------|----------|-------------|
| 标签管理 | P1 | Tag 树形结构管理（物化路径），display_status 控制 |
| 分类关联 | P1 | Category 作为 Tag + Relation(relation_type='category') 的视图 |
| 文章-标签关联 | P1 | Relation 管理 Article 与 Tag 的多对多关联 |

### Supporting: 访客交互 (InteractionContext)

| Capability | Priority | Description |
|-----------|----------|-------------|
| 访客交互 | P1 | Comment/Message/Like，树形嵌套回复 |

### Generic: 站点运营 (SiteContext)

| Capability | Priority | Description |
|-----------|----------|-------------|
| 字典管理 | P2 | Dict 通用 KV 存储 |
| 友链管理 | P2 | FriendLink 寄生于 Dict |
| 站点配置 | P2 | SiteConfig/NavItem/SocialLink 寄生于 Dict |

### 技术基础设施

| Capability | Priority | Description |
|-----------|----------|-------------|
| 多环境数据库 | P0 | dev → H2, test/prod → MySQL |
| RESTful API | P0 | 统一响应格式、错误码、分页 |
| 公共审计字段 | P0 | creator/gmt_create/modifier/gmt_modified 基类 |

## Impact

### 聚合变更清单

| Aggregate | Context | Change Type |
|-----------|---------|-------------|
| Article (含 Content, Revision) | ContentContext | 新增 |
| Tag | ClassificationContext | 新增 |
| Relation | ClassificationContext | 新增 |
| Interaction | InteractionContext | 新增 |
| Dict | SiteContext | 新增 |

### 跨上下文集成点

| Integration Point | Pattern | Risk |
|-------------------|---------|------|
| Content ↔ Classification (双向查询) | OHS/PL | 双向依赖，未来拆分需重构 |
| Content → Interaction (评论挂载) | OHS + ACL | 低风险 |
| Site → Portal (配置查询) | OHS/PL | 无风险 |

### 受影响的外部系统

| System | Impact |
|--------|--------|
| blog-portal | API 从 mock 切换为真实后端 |
| blog-admin | 新建，依赖 blog-server 全部 API |

## Goals

| Goal | Metric | Target |
|------|--------|--------|
| 文章发布可靠 | 草稿→发布事务成功率 | > 99.9% |
| 版本回溯可用 | 任意版本加载时间 | < 500ms |
| 内容完整性 | Article 与 Content 无孤儿记录 | 0 孤儿 |
| 开发效率 | dev 环境零配置启动 | H2 自动建表 |
| API 一致性 | 统一响应格式覆盖率 | 100% |

## Non-goals

- 用户认证/鉴权（当前阶段不做登录体系）
- 文件上传/存储（图片由外部 OSS 或 Nginx 托管）
- 全文搜索 API（ngram 全文检索后续迭代）
- 消息队列/异步处理（单体应用，无 MQ 需求）
- 微服务拆分（单体 Spring Boot 应用）
