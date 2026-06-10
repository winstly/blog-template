# ClassificationContext / Tag

## Requirement: 创建标签

管理员可以创建新标签，支持指定父节点形成树形结构。

### Scenario: 创建根标签

- Given 管理员填写了 `tagName` = '前端开发'，未指定父标签
- When 创建标签
- Then 生成唯一的 `TagCode`
- And `treeDepth` = 0
- And `treePath` = '/' + tag.id
- And `displayStatus` = 1

### Scenario: 创建子标签

- Given 存在父标签 '前端开发'（`treePath` = '/1', `treeDepth` = 0）
- And 管理员填写了 `tagName` = 'Vue3'，指定父标签为 '前端开发'
- When 创建标签
- Then `treeDepth` = 1
- And `treePath` = '/1/' + tag.id
- And `displayStatus` = 1

### Scenario: tagCode 不可重复

- Given 已存在 `tagCode` = 'frontend' 的标签
- When 创建 `tagCode` = 'frontend' 的新标签
- Then 拒绝创建（不变量 I6）

---

## Requirement: 移动标签

管理员可以将标签移动到新的父节点下。

### Scenario: 移动标签到新父节点

- Given 标签 'Vue3'（`treePath` = '/1/3'）的 `treeDepth` = 1
- And 目标父标签 '后端开发'（`treePath` = '/2'）
- When 移动标签
- Then 'Vue3' 的 `treePath` 更新为 '/2/' + tag.id
- And `treeDepth` 更新为 1
- And 所有后代节点的 `treePath` 和 `treeDepth` 同步更新

### Scenario: 不可移动到自身子树下

- Given 标签 '前端开发'（`treePath` = '/1'）
- And 其子标签 'Vue3'（`treePath` = '/1/3'）
- When 将 '前端开发' 移动到 'Vue3' 下
- Then 拒绝移动，会形成循环（TagMovableSpec 校验失败）

### Scenario: treePath 与 treeDepth 必须一致

- Given 标签移动后 `treePath` = '/2/5'
- Then `treeDepth` 必须为 1（不变量 I8）

---

## Requirement: 隐藏/显示标签

管理员可以控制标签的展示状态。

### Scenario: 隐藏标签

- Given 标签 `displayStatus` = 1
- When 隐藏标签
- Then `displayStatus` 设为 0
- And 前端查询时不返回该标签

### Scenario: 显示标签

- Given 标签 `displayStatus` = 0
- When 显示标签
- Then `displayStatus` 设为 1

---

## Requirement: 删除标签

管理员可以逻辑删除标签，删除后级联清理关联。

### Scenario: 逻辑删除标签

- Given 存在标签 'Vue3'
- And 该标签下有 3 条 Relation
- When 删除标签
- Then `Tag` 的 `isDeleted` 设为 1
- And 发布 `TagDeleted` 事件（携带 `tagCode`）
- And 消费者级联删除相关 Relation

---

## Requirement: 分类视图

Category 是 `Relation(relationType='category')` 的视图，不是独立实体。

### Scenario: 查询所有分类

- Given 存在标签 '前端开发'、'Vue3'、'后端开发'
- And '前端开发' 通过 Relation(relationType='category') 被文章引用 3 次
- And 'Vue3' 通过 Relation(relationType='tag') 被文章引用 5 次
- When 查询所有分类
- Then 返回 '前端开发'（articleCount = 3）和 '后端开发'（articleCount = N）
- And 不返回 'Vue3'（它只是标签，未被当作分类关联）
