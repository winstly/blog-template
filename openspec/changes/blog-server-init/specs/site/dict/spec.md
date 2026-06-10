# SiteContext / Dict

## Requirement: 友链管理

管理员可以管理博客友情链接。

### Scenario: 创建友链

- Given 管理员填写了友链名称、URL、描述
- When 创建友链
- Then 在 `Dict` 中创建记录（`bizCode` = 'friend_link', `subCode` = '', `itemCode` = 友链标识）
- And `itemLabel` 存友链名称
- And `itemValue` 存 URL
- And `sortOrder` 控制排列顺序

### Scenario: 查询友链列表

- Given 存在 5 条 `bizCode` = 'friend_link' 的 Dict 记录
- When 查询友链列表
- Then 返回 5 条 `FriendLinkVO`（name, url, logo, description）
- And 按 `sortOrder` 升序排列

### Scenario: 删除友链

- Given 存在一条友链记录
- When 删除友链
- Then 该 Dict 记录 `isDeleted` = 1

---

## Requirement: 站点配置管理

管理员可以管理博客个人信息和站点配置。

### Scenario: 查询个人信息

- Given `Dict` 中存在 `bizCode` = 'site', `subCode` = 'profile' 的记录
- When 查询个人信息
- Then 返回 `ProfileVO`（nickname, signature, avatar, bio, location, qq, email）

### Scenario: 更新个人信息

- Given 管理员修改了昵称为 '新昵称'
- When 更新个人信息
- Then 对应 Dict 记录的 `itemValue` 更新

---

## Requirement: 导航项管理

管理员可以管理博客导航菜单。

### Scenario: 查询导航项

- Given `Dict` 中存在 `bizCode` = 'nav' 的记录
- When 查询导航项
- Then 返回 `NavItemVO` 列表（label, path, icon）
- And 按 `sortOrder` 升序排列

---

## Requirement: 社交链接管理

管理员可以管理社交链接。

### Scenario: 查询社交链接

- Given `Dict` 中存在 `bizCode` = 'social' 的记录
- When 查询社交链接
- Then 返回 `SocialLinkVO` 列表（icon, label, url）

---

## Requirement: 字典通用 CRUD

Dict 作为通用 KV 存储，支持标准增删改查。

### Scenario: 三元组唯一性

- Given 已存在 Dict(bizCode='friend_link', subCode='', itemCode='juejin')
- When 创建相同三元组的 Dict
- Then 拒绝创建（不变量 I11）

### Scenario: 按一级码查询

- Given 存在多条 `bizCode` = 'friend_link' 的记录
- When 按 `bizCode` 查询
- Then 返回该业务码下所有可见记录（`displayStatus` = 1）
