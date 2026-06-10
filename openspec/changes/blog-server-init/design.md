# Design: blog-server-init

## 技术栈

| Component | Selection | Version |
|-----------|----------|---------|
| JDK | Oracle OpenJDK | 21 |
| Framework | Spring Boot | 3.x |
| ORM | MyBatis-Plus | 3.5.x |
| Build | Maven | 3.9+ |
| DB (dev) | H2 | in-memory |
| DB (test/prod) | MySQL | 8.0+ |

## 分层架构

```
blog-server/
├── pom.xml
├── src/main/java/cn/yanshisan/blog/
│   ├── BlogServerApplication.java
│   │
│   ├── content/                          # ContentContext
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── Article.java
│   │   │   │   ├── Content.java
│   │   │   │   └── ContentRevision.java
│   │   │   ├── vo/
│   │   │   │   ├── ArticleCode.java
│   │   │   │   └── PublishStatus.java
│   │   │   ├── spec/
│   │   │   │   └── PublishableSpec.java
│   │   │   ├── event/
│   │   │   │   ├── ArticleCreatedEvent.java
│   │   │   │   ├── ArticlePublishedEvent.java
│   │   │   │   ├── ArticleDeletedEvent.java
│   │   │   │   ├── ContentUpdatedEvent.java
│   │   │   │   └── RevisionCreatedEvent.java
│   │   │   ├── repository/
│   │   │   │   ├── ArticleRepository.java
│   │   │   │   └── ContentRepository.java
│   │   │   ├── factory/
│   │   │   │   └── ArticleFactory.java
│   │   │   └── service/
│   │   │       ├── ArticlePublicationService.java
│   │   │       └── ArticleManagementService.java
│   │   ├── application/                  # Application Service (编排层)
│   │   │   └── ArticleApplicationService.java
│   │   ├── infrastructure/
│   │   │   ├── persistence/
│   │   │   │   ├── mapper/
│   │   │   │   │   ├── ArticleMapper.java
│   │   │   │   │   ├── ContentMapper.java
│   │   │   │   │   └── ContentRevisionMapper.java
│   │   │   │   └── impl/
│   │   │   │       └── ArticleRepositoryImpl.java
│   │   │   └── ohs/                     # Open Host Service
│   │   │       └── ArticleOHSImpl.java
│   │   └── interfaces/
│   │       ├── api/
│   │       │   └── ArticleController.java
│   │       └── dto/
│   │           ├── ArticleCreateDTO.java
│   │           ├── ArticleUpdateDTO.java
│   │           └── ArticleVO.java
│   │
│   ├── classification/                   # ClassificationContext
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── Tag.java
│   │   │   │   └── ArticleTag.java
│   │   │   ├── vo/
│   │   │   │   ├── TreeNode.java
│   │   │   │   └── RelationType.java
│   │   │   ├── spec/
│   │   │   │   └── TagMovableSpec.java
│   │   │   ├── event/
│   │   │   │   ├── TagCreatedEvent.java
│   │   │   │   ├── TagMovedEvent.java
│   │   │   │   ├── TagDeletedEvent.java
│   │   │   │   ├── RelationCreatedEvent.java
│   │   │   │   └── RelationRemovedEvent.java
│   │   │   ├── repository/
│   │   │   │   ├── TagRepository.java
│   │   │   │   └── RelationRepository.java
│   │   │   ├── factory/
│   │   │   │   └── TagFactory.java
│   │   │   └── service/
│   │   │       └── TagManagementService.java
│   │   ├── application/
│   │   ├── infrastructure/
│   │   │   ├── persistence/
│   │   │   └── ohs/
│   │   │       └── TagOHSImpl.java
│   │   └── interfaces/
│   │
│   ├── interaction/                      # InteractionContext
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   └── Interaction.java
│   │   │   ├── vo/
│   │   │   │   ├── Target.java
│   │   │   │   └── Action.java
│   │   │   ├── acl/                     # Anti-Corruption Layer
│   │   │   │   └── ArticleTranslator.java
│   │   │   ├── event/
│   │   │   ├── repository/
│   │   │   │   └── InteractionRepository.java
│   │   │   ├── factory/
│   │   │   │   └── InteractionFactory.java
│   │   │   └── service/
│   │   │       └── InteractionService.java
│   │   ├── application/
│   │   ├── infrastructure/
│   │   └── interfaces/
│   │
│   ├── site/                             # SiteContext
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   └── SysDict.java
│   │   │   ├── vo/
│   │   │   │   └── DictTriple.java
│   │   │   ├── repository/
│   │   │   │   └── DictRepository.java
│   │   │   └── service/
│   │   │       └── DictService.java
│   │   ├── application/
│   │   ├── infrastructure/
│   │   │   └── ohs/
│   │   │       └── DictOHSImpl.java
│   │   └── interfaces/
│   │
│   └── shared/                           # 跨上下文共享
│       ├── entity/
│       │   └── BaseEntity.java           # 公共审计字段
│       ├── exception/
│       │   ├── BusinessException.java
│       │   └── ErrorCode.java
│       ├── api/
│       │   ├── R.java                    # 统一响应
│       │   └── PageResult.java
│       ├── ohs/                          # OHS 接口定义
│       │   ├── ArticleOHS.java
│       │   ├── TagOHS.java
│       │   └── DictOHS.java
│       └── config/
│           └── MyBatisPlusConfig.java
│
├── src/main/resources/
│   ├── application.yml
│   ├── application-dev.yml               # H2
│   ├── application-test.yml              # MySQL
│   ├── application-prod.yml              # MySQL
│   ├── db/
│   │   ├── schema-h2.sql                # H2 兼容 DDL
│   │   └── schema.sql                   # MySQL DDL (blog.sql)
│   └── mapper/                           # MyBatis XML (如需要)
│
└── src/test/java/
```

## 数据模型映射

| DDD Entity | DB Table | ORM Entity | 说明 |
|-----------|----------|------------|------|
| Article | blog_article | Article.java | @TableLogic 逻辑删除 |
| Content | blog_content | Content.java | 寄生模式：targetType + targetCode |
| Revision | blog_content_revision | ContentRevision.java | 不可变快照 |
| Tag | blog_tag | Tag.java | 物化路径树 |
| Relation | blog_article_tag | ArticleTag.java | relationType 区分分类/标签 |
| Interaction | blog_interaction | Interaction.java | 多态目标 + 树形回复 |
| Dict | sys_dict | SysDict.java | 三级编码 KV |

### 公共审计字段基类

```java
@Data
public abstract class BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String creator;
    private LocalDateTime gmtCreate;
    private String modifier;
    private LocalDateTime gmtModified;
    @TableLogic
    private Integer isDeleted;
}
```

## 跨上下文集成

### OHS 接口（shared/ohs/）

```java
public interface ArticleOHS {
    String getArticleTitle(String articleCode);
    ArticleSummaryVO getArticleSummary(String articleCode);
    boolean existsArticle(String articleCode);
}

public interface TagOHS {
    List<TagVO> listCategoriesByArticle(String articleCode);
    List<TagVO> listTagsByArticle(String articleCode);
    List<TagVO> listAllCategories();
    List<TagVO> listAllTags();
    boolean existsTag(String tagCode);
}

public interface DictOHS {
    List<FriendLinkVO> getFriendLinks();
    SiteConfigVO getSiteConfig();
    List<NavItemVO> getNavItems();
    List<SocialLinkVO> getSocialLinks();
    ProfileVO getProfile();
}
```

### ACL（interaction/domain/acl/）

```java
@Component
public class ArticleTranslator {
    private final ArticleOHS articleOHS;

    public String translateTitle(String articleCode) {
        return articleOHS.getArticleTitle(articleCode);
    }

    public boolean exists(String articleCode) {
        return articleOHS.existsArticle(articleCode);
    }
}
```

### 事件发布（进程内）

```java
// 发布端
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void onArticlePublished(ArticlePublishedEvent event) {
    // ClassificationContext: 无直接动作（关联在创建时已建立）
}

// 删除事件消费
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void onArticleDeleted(ArticleDeletedEvent event) {
    // ClassificationContext: 级联删除 Relation
    relationRepository.logicalDeleteByArticleCode(event.getArticleCode());
}
```

## 多环境数据库

### dev (H2)

- `spring.datasource.url=jdbc:h2:mem:blog;DB_CLOSE_DELAY=-1`
- `spring.sql.init.mode=always`
- `spring.sql.init.schema-locations=classpath:db/schema-h2.sql`
- H2 兼容处理：
  - `JSON` → `CLOB`
  - `TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` → `TIMESTAMP`（由应用层维护 gmtModified）
  - `FULLTEXT INDEX ... WITH PARSER ngram` → 省略
  - `MEDIUMTEXT` → `CLOB`

### test/prod (MySQL)

- `spring.datasource.url=jdbc:mysql://localhost:3306/blog?useUnicode=true&characterEncoding=utf8mb4`
- `spring.sql.init.mode=never`（由 DBA 管理 DDL）

## API 规范

### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 分页响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [],
    "pagination": {
      "page": 1,
      "pageSize": 20,
      "total": 100
    }
  }
}
```

### 错误码

| Range | Usage |
|-------|-------|
| 1000-1999 | 参数错误 |
| 2000-2999 | 业务错误 |
| 3000-3999 | 系统错误 |

### API 路由

| Context | Path | 说明 |
|---------|------|------|
| Content | `/api/v1/articles` | 文章 CRUD |
| Content | `/api/v1/articles/{code}` | 文章详情 |
| Content | `/api/v1/articles/{code}/content` | 正文与版本 |
| Classification | `/api/v1/tags` | 标签 CRUD |
| Classification | `/api/v1/categories` | 分类视图 |
| Interaction | `/api/v1/interactions` | 交互 CRUD |
| Interaction | `/api/v1/messages` | 留言板 |
| Site | `/api/v1/friend-links` | 友链 |
| Site | `/api/v1/site-config` | 站点配置 |

## 事务策略

- **默认**：1 个事务修改 1 个聚合实例
- **Article 聚合**：Article + Content + Revision 同事务（不变量 I1）
- **跨聚合编排**：`ArticleManagementService.create()` 在 `@Transactional` 中编排 Article 创建 + Relation 关联
- **浏览量**：`UPDATE SET view_count = view_count + 1` 原子操作，无需加锁

## 依赖方向约束（ArchUnit）

```java
// ContentContext 不直接依赖 ClassificationContext 的 Repository
noClasses()
    .that().resideInAPackage("cn.yanshisan.blog.content..")
    .should().dependOnClassesThat()
    .resideInAPackage("cn.yanshisan.blog.classification.infrastructure..")
    .check(importedClasses);

// InteractionContext 只通过 OHS 接口访问 ContentContext
noClasses()
    .that().resideInAPackage("cn.yanshisan.blog.interaction..")
    .should().dependOnClassesThat()
    .resideInAPackage("cn.yanshisan.blog.content.infrastructure.persistence..")
    .check(importedClasses);
```
