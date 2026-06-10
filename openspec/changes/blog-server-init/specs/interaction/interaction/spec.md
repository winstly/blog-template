# InteractionContext / Interaction

## Requirement: 创建评论

访客可以对文章发表评论。

### Scenario: 发表文章评论

- Given 存在已发布文章（`articleCode` = 'vue3-setup'）
- And 访客填写了昵称、内容
- When 创建评论
- Then 创建 `Interaction`（`targetType` = 'article', `targetCode` = 'vue3-setup', `actionType` = 'comment'）
- And `treeDepth` = 0（根评论）
- And `treePath` = '/' + interaction.id（后填）
- And 发布 `InteractionCreated` 事件

### Scenario: 目标文章不存在

- Given `articleCode` = 'not-exist' 的文章不存在
- When 创建评论
- Then 拒绝创建（不变量 I9）

---

## Requirement: 回复评论

访客可以回复已有评论，形成树形嵌套。

### Scenario: 回复根评论

- Given 存在根评论（`treePath` = '/10', `treeDepth` = 0）
- When 回复该评论
- Then 创建 `Interaction`（`treePath` = '/10/' + newId, `treeDepth` = 1）
- And `remark` 存回复内容
- And 发布 `ReplyPosted` 事件

### Scenario: 多层嵌套回复

- Given 存在评论链 `/10/20/30`（`treeDepth` = 2）
- When 回复 depth=2 的评论
- Then 新 `Interaction` 的 `treePath` = '/10/20/30/' + newId
- And `treeDepth` = 3
- And `treePath` 层级数与 `treeDepth` 一致（不变量 I10）

---

## Requirement: 创建留言

访客可以在留言板发表留言。

### Scenario: 发表留言

- Given 访客填写了昵称、内容
- When 创建留言
- Then 创建 `Interaction`（`targetType` = 'site', `actionType` = 'message'）
- And `treeDepth` = 0（根留言）

---

## Requirement: 点赞

访客可以对文章或评论点赞。

### Scenario: 点赞文章

- Given 存在已发布文章
- When 点赞
- Then 创建 `Interaction`（`targetType` = 'article', `actionType` = 'like'）

### Scenario: 点赞评论

- Given 存在一条评论
- When 点赞
- Then 创建 `Interaction`（`targetType` = 'interaction', `actionType` = 'like'）

---

## Requirement: 查询交互列表

按目标分页查询评论/留言，支持树形展示。

### Scenario: 查询文章评论

- Given 文章有 15 条根评论，每条有若干回复
- When 分页查询（page=1, size=10）
- Then 返回 10 条根评论
- And 每条根评论的 `replies` 包含其下所有回复

### Scenario: 按目标类型筛选

- Given 留言板有 20 条留言
- When 查询 `targetType` = 'site' 的交互
- Then 只返回留言板留言，不返回文章评论
