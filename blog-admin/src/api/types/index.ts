/** 发布状态 */
export type PublishStatus = 'DRAFT' | 'PUBLISHED'

/** 标签摘要（用于 ArticleVO 中） */
export interface TagSummary {
  tagCode: string
  tagName: string
}

/** 文章 */
export interface Article {
  id: number
  articleCode: string
  title: string
  summary: string | null
  coverUrl: string | null
  isPinned: boolean
  viewCount: number
  publishedAt: string | null
  gmtCreate: string
  gmtModified: string
  body: string | null
  contentFormat: string | null
  wordCount: number | null
  version: number | null
  publishStatus: PublishStatus
  tags: TagSummary[]
  category: TagSummary | null
}

/** 友链 */
export interface FriendLink {
  itemCode: string
  name: string
  url: string
  logo: string | null
  description: string | null
  sortOrder: number
}

/** 个人信息 */
export interface Profile {
  nickname: string
  signature: string
  avatar?: string
  bio: string
  location: string
  qq: string
  email: string
}

/** 社交链接 */
export interface SocialLink {
  code: string
  label: string
  url: string
}

/** 导航项 */
export interface NavItem {
  code: string
  label: string
  path: string
}

/** 日记条目 */
export interface DiaryEntry {
  id: number
  diaryCode: string
  content: string
  images: string | null
  diaryDate: string
  year: number
  gmtCreate: string
  gmtModified: string
}

/** 留言/评论 */
export interface Message {
  id: number
  userName: string
  userAvatarUrl: string | null
  content: string
  location: string | null
  date: string
  status: 'pending' | 'approved'
  replyTo: string | null
  replies: Message[]
}

/** 分类（后端用 Tag + relation_type='category'） */
export interface Category {
  id: number
  tagCode: string
  tagName: string
  treePath: string | null
  treeDepth: number
  sortOrder: number
  displayStatus: number
  description: string | null
  articleCount: number
  children?: Category[]
  gmtCreate: string
  gmtModified: string
}

/** 标签 */
export interface Tag {
  id: number
  tagCode: string
  tagName: string
  treePath: string | null
  treeDepth: number
  sortOrder: number
  displayStatus: number
  description: string | null
  articleCount: number
  gmtCreate: string
  gmtModified: string
}

/** 用户状态 */
export type UserStatus = 'enabled' | 'disabled'

/** 角色状态 */
export type RoleStatus = 'enabled' | 'disabled'

/** 字典状态 */
export type DictStatus = 'enabled' | 'disabled'

/** 权限类型 */
export type PermissionType = 'menu' | 'button'

/** 用户 */
export interface User {
  id: string
  username: string
  nickname: string
  email?: string
  avatar?: string
  role: 'admin' | 'editor'
  permissions?: string[]
}

/** 系统用户 */
export interface SystemUser {
  id: string
  username: string
  nickname: string
  email: string
  avatar?: string
  roles: string[]
  status: UserStatus
  createTime: string
}

/** 角色选项 */
export interface RoleOption {
  id: string
  name: string
}

/** 角色 */
export interface Role {
  id: string
  name: string
  code: string
  description: string
  status: RoleStatus
  isSystem: boolean
  userCount: number
  createTime: string
  permissions: string[]
}

/** 权限 */
export interface Permission {
  id: string
  name: string
  code: string
  type: PermissionType
  description?: string
  parentId?: string
  isSystem?: boolean
  children?: Permission[]
  isLeaf?: boolean
  hasChildren?: boolean
}

/** 字典项 */
export interface DictItem {
  id: string
  level: 1 | 2 | 3
  sceneCode: string
  groupCode?: string
  bizCode?: string
  fieldName: string
  fieldValue?: string
  order: number
  status: DictStatus
  children?: DictItem[]
  isLeaf?: boolean
  hasChildren?: boolean
}

/** 统计数据 */
export interface Statistics {
  totalArticles: number
  totalViews: number
  totalComments: number
  totalLinks: number
}

/** 视图趋势数据 */
export interface ViewTrend {
  date: string
  views: number
}

/** 登录请求 */
export interface LoginRequest {
  username: string
  password: string
}

/** 登录响应 */
export interface LoginResponse {
  token: string
  user: User
}

/** 分页请求 */
export interface PaginationParams {
  page: number
  pageSize: number
}

/** 分页响应（匹配后端 PageResult） */
export interface PaginationResult<T> {
  list: T[]
  pagination: {
    page: number
    pageSize: number
    total: number
  }
}
