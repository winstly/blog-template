# ClassificationContext / Relation

## Requirement: 创建关联

可以为文章和标签建立分类或标签关联。

### Scenario: 关联文章与分类

- Given 存在文章（`articleCode` = 'vue3-setup'）
- And 存在标签 '前端开发'（`tagCode` = 'frontend'）
- When 创建关联（`relationType` = 'category'）
- Then 创建 `Relation` 记录（articleCode, tagCode, relationType='category'）
- And 发布 `RelationCreated` 事件

### Scenario: 关联文章与标签

- Given 存在文章和标签
- When 创建关联（`relationType` = 'tag'）
- Then 创建 `Relation` 记录（articleCode, tagCode, relationType='tag'）

### Scenario: 同一文章同一标签同一类型不可重复关联

- Given 已存在 Relation(articleCode='vue3-setup', tagCode='frontend', relationType='category')
- When 再次创建相同三元组的关联
- Then 拒绝创建（不变量 I7）

### Scenario: 同一文章同一标签不同类型可共存

- Given 已存在 Relation(articleCode='vue3-setup', tagCode='frontend', relationType='category')
- When 创建 Relation(articleCode='vue3-setup', tagCode='frontend', relationType='tag')
- Then 创建成功

---

## Requirement: 移除关联

可以移除文章与标签的关联。

### Scenario: 移除关联

- Given 存在一条有效的 Relation
- When 移除关联
- Then `Relation` 的 `isDeleted` 设为 1
- And 发布 `RelationRemoved` 事件

---

## Requirement: 按文章查关联

查询某篇文章关联的所有分类和标签。

### Scenario: 查询文章的分类和标签

- Given 文章关联了分类 '前端开发' 和标签 'Vue3'、'TypeScript'
- When 查询文章的关联
- Then 返回分类列表 ['前端开发'] 和标签列表 ['Vue3', 'TypeScript']

---

## Requirement: 按标签统计文章数

统计某个分类或标签下关联的文章数量。

### Scenario: 统计分类下文章数

- Given 标签 '前端开发' 通过 Relation(relationType='category') 被 3 篇文章关联
- When 统计 '前端开发' 分类下的文章数
- Then 返回 3
