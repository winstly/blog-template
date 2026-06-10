# Tasks: blog-server-init

## Phase 1: 项目骨架与基础设施

- [x] **T1: 初始化 Spring Boot 3 项目** — 创建 Maven 项目，配置 pom.xml（JDK 21, Spring Boot 3, MyBatis-Plus, H2/MySQL 驱动），生成 Application 主类。验收：`mvn clean compile` 通过，应用可启动
- [x] **T2: 公共基础设施** — 实现 BaseEntity（公共审计字段 + @TableLogic）、统一响应 R<T>、PageResult、BusinessException、ErrorCode、MyBatisPlusConfig（自动填充 gmtCreate/gmtModified）。验收：BaseEntity 子类自动填充审计字段；R.ok()/R.fail() 正常序列化
- [x] **T3: 多环境数据库配置** — 配置 application.yml / application-dev.yml (H2) / application-test.yml (MySQL) / application-prod.yml (MySQL)；编写 schema-h2.sql（H2 兼容 DDL）；MySQL DDL 复用 blog.sql。验收：`spring.profiles.active=dev` 启动后 H2 自动建表，7 张表均可访问

## Phase 2: Domain Model — ContentContext (Core)

- [x] **T4: Article 聚合 — Entity + VO** — 实现 Article.java, Content.java, ContentRevision.java 实体；ArticleCode.java, PublishStatus.java 值对象。验收：Article 包含 publish()/pin()/unpin() 方法；Content 包含 updateBody()/publish() 方法
- [x] **T5: PublishableSpec** — 实现发布前置校验：title 非空 + body 非空 + 当前非已发布状态。验收：三种场景均正确返回
- [x] **T6: ArticleRepository + ContentRepository** — 实现 Mapper + Repository；save 含 Article+Content 同事务写入。验收：findByCode 返回含 Content 的 Article
- [x] **T7: ArticleFactory** — 实现 ArticleFactory.create() — 生成 ArticleCode，初始化 Article + Content。验收：Article 与 Content 成对存在
- [x] **T8: ArticlePublicationService + ArticleManagementService** — 实现发布编排和创建/更新编排。验收：发布事务覆盖三表写入；浏览量原子递增
- [x] **T9: ArticleController (API)** — 实现文章 RESTful API。验收：统一响应格式；分页查询正常

## Phase 3: Domain Model — ClassificationContext (Supporting)

- [x] **T10: Tag 聚合 — Entity + VO** — 实现 Tag.java, ArticleTag.java；TreeNode.java, RelationType.java。验收：Tag 包含 moveTo() 方法
- [x] **T11: TagMovableSpec** — 实现标签移动校验：不能移到自身子树下。验收：正确检测循环移动
- [x] **T12: TagRepository + RelationRepository** — 实现 Mapper + Repository；支持树形查询。验收：listChildren 正确返回子节点
- [x] **T13: TagFactory + TagManagementService** — 实现工厂和服务。验收：移动标签后子节点 treePath 同步更新
- [x] **T14: TagController + CategoryController (API)** — 实现标签/分类 API。验收：分类视图只返回 relationType='category' 的标签

## Phase 4: Domain Model — InteractionContext (Supporting)

- [x] **T15: Interaction 聚合 — Entity + VO + ACL** — 实现 Interaction.java；Target.java, Action.java；ArticleTranslator (ACL)。验收：通过 ArticleOHS 接口查询
- [x] **T16: InteractionFactory + InteractionService** — 实现工厂和服务。验收：根评论 treePath = '/' + id；回复 treePath = parentPath + '/' + id
- [x] **T17: InteractionRepository** — 实现 Repository；支持按 target 分页查询。验收：分页查询返回根评论 + 嵌套 replies
- [x] **T18: InteractionController (API)** — 实现交互 API。验收：留言板 API 只返回 targetType='site' 的交互

## Phase 5: Domain Model — SiteContext (Generic)

- [x] **T19: Dict 聚合 — Entity + VO + Service** — 实现 SysDict.java；DictTriple.java；DictService。验收：DictService.getFriendLinks() 返回 FriendLinkVO 列表
- [x] **T20: DictRepository + DictOHS** — 实现 Repository 和 OHS。验收：按 bizCode 查询正确过滤 displayStatus=1
- [x] **T21: SiteController (API)** — 实现站点 API。验收：友链按 sortOrder 排序

## Phase 6: 跨上下文集成与事件

- [x] **T22: OHS 接口实现** — 实现 ArticleOHSImpl, TagOHSImpl。验收：跨上下文查询成功
- [x] **T23: 事件发布与消费** — 实现 ApplicationEvent 进程内事件。验收：删除文章后 Relation 自动级联逻辑删除
- [x] **T24: ArchUnit 依赖约束** — 添加 ArchUnit 测试。验收：测试通过

## Phase 7: 测试与收尾

- [x] **T25: 集成测试** — 编写核心流程集成测试。验收：核心流程全部通过
- [x] **T26: H2 兼容性验证** — 在 dev 环境下验证所有功能正常。验收：所有 API 可用
