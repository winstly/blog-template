# ContentContext / Content

## Requirement: 寄生正文存储

`Content` 通过 `targetType` + `targetCode` 寄生关联宿主实体，同一宿主只有一份正文。

### Scenario: 文章正文唯一性

- Given 存在一篇文章（`articleCode` = 'vue3-setup'）
- And 该文章已有正文
- When 再次为同一文章创建正文
- Then 拒绝创建，违反唯一约束（不变量 I4）

### Scenario: 日记正文存储

- Given 存在一篇关联 'diary' 分类的文章
- When 为该文章写入正文
- Then `Content` 的 `targetType` = 'article'，`targetCode` = 该文章的 `articleCode`
- And `contentFormat` = 'markdown'

---

## Requirement: 版本快照

每次正文变更时，自动保存修改前的版本快照到 `Revision`。

### Scenario: 快照保存修改前内容

- Given `Content` 当前 `version` = 2，`body` = '旧内容'
- When 更新正文为 '新内容'
- Then 创建 `Revision`（`contentId` = content.id, `version` = 2, `body` = '旧内容'）
- And `Content` 的 `version` 递增为 3

### Scenario: 版本号不可回退

- Given `Content` 当前 `version` = 5
- When 创建 `Revision`
- Then `Revision` 的 `version` 必须 ≤ 当前 `Content.version`
- And 新 `Content.version` 必须 = 旧 `Content.version` + 1（不变量 I5）

---

## Requirement: 版本回溯

博主可以查看历史版本快照。

### Scenario: 查看指定版本

- Given `Content` 存在 version 1-5 的快照
- When 查询 version = 3 的快照
- Then 返回 version 3 的 `body`、`contentFormat`、`wordCount`、`changeSummary`

### Scenario: 版本不存在

- Given `Content` 存在 version 1-3 的快照
- When 查询 version = 99 的快照
- Then 返回空结果
