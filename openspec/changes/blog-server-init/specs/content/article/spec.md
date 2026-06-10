# ContentContext / Article

## Requirement: 创建文章

博主可以创建一篇新文章，包含标题、摘要、封面图和正文内容。

### Scenario: 创建草稿文章

- Given 博主填写了标题和正文
- When 创建文章
- Then 生成唯一的 `ArticleCode`
- And `Article` 的 `publishedAt` 为空
- And `Content` 的 `publishStatus` 为 DRAFT
- And `Content` 的 `version` 为 1
- And `Article` 与 `Content` 成对存在（不变量 I1）

### Scenario: 标题不可为空

- Given 博主未填写标题
- When 创建文章
- Then 拒绝创建，返回参数校验错误

---

## Requirement: 发布文章

博主可以将草稿文章发布为公开可见状态，发布时自动创建版本快照。

### Scenario: 发布草稿文章

- Given 存在一篇草稿文章（`publishStatus` = DRAFT）
- And 正文内容不为空
- When 发布文章
- Then `Article` 的 `publishedAt` 设为当前时间
- And `Content` 的 `publishStatus` 变为 PUBLISHED
- And 创建一个 `Revision`（版本号 = 当前 version）
- And 发布 `ArticlePublished` 事件

### Scenario: 已发布文章不可重复发布

- Given 存在一篇已发布文章（`publishStatus` = PUBLISHED）
- When 发布文章
- Then 拒绝操作，返回业务错误

### Scenario: 正文为空时不可发布

- Given 存在一篇草稿文章，但正文为空
- When 发布文章
- Then 拒绝操作，返回业务错误

---

## Requirement: 更新正文

博主可以修改已创建文章的正文内容，每次修改自动递增版本号并创建快照。

### Scenario: 修改已发布文章的正文

- Given 存在一篇已发布文章，当前 `version` = 3
- When 更新正文
- Then `Content` 的 `body` 更新为新内容
- And `Content` 的 `version` 递增为 4
- And `Content` 的 `wordCount` 重新计算
- And 创建 `Revision`（version = 3，保存修改前快照）
- And 发布 `ContentUpdated` 事件

### Scenario: 版本号严格递增

- Given `Content` 当前 `version` = 3
- When 更新正文
- Then 新 `version` 必须为 4，不可跳号或回退（不变量 I5）

---

## Requirement: 置顶文章

博主可以将文章设为置顶或取消置顶。

### Scenario: 设为置顶

- Given 存在一篇已发布文章，`isPinned` = false
- When 置顶文章
- Then `Article` 的 `isPinned` 设为 true
- And 发布 `ArticlePinned` 事件

### Scenario: 取消置顶

- Given 存在一篇置顶文章，`isPinned` = true
- When 取消置顶
- Then `Article` 的 `isPinned` 设为 false
- And 发布 `ArticleUnpinned` 事件

---

## Requirement: 删除文章

博主可以逻辑删除文章，删除后关联数据通过事件级联清理。

### Scenario: 逻辑删除文章

- Given 存在一篇文章
- When 删除文章
- Then `Article` 的 `isDeleted` 设为 1
- And `Content` 同步逻辑删除
- And 发布 `ArticleDeleted` 事件（携带 `articleCode`）

---

## Requirement: 浏览文章

访客浏览文章时，浏览量原子递增。

### Scenario: 浏览量递增

- Given 存在一篇已发布文章，`viewCount` = 100
- When 访客浏览文章
- Then `viewCount` 原子递增为 101
- And 不触发版本快照
