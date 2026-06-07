/** 文章状态 */
export type ArticleStatus = 'draft' | 'published'

/** 文章 */
export interface Article {
  id: string
  title: string
  summary: string
  content: string
  cover: string
  date: string
  author?: string
  views: number
  comments: number
  isTop: boolean
  status: ArticleStatus
  category: string
  tags: string[]
}

/** 友链 */
export interface FriendLink {
  id: string
  name: string
  url: string
  logo?: string
  description?: string
  order: number
  active: boolean
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
  icon: string
  label: string
  url: string
}

/** 日记条目 */
export interface DiaryEntry {
  id: string
  date: string
  content: string
  year: number
  images?: string[]
}

/** 留言状态 */
export type MessageStatus = 'pending' | 'approved'

/** 留言/评论 */
export interface Message {
  id: string
  author: string
  username: string
  avatar: string
  content: string
  location: string
  date: string
  status: MessageStatus
  replyTo?: string
  reply?: string
  replies?: Message[]
}

/** 分类 */
export interface Category {
  id: string
  name: string
  count: number
  parentId?: string
  children?: Category[]
  sort?: number
}

/** 标签 */
export interface Tag {
  id: string
  name: string
  count: number
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
  /** 用户权限码列表 */
  permissions?: string[]
}

/** 系统用户（用户管理模块使用） */
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

/** 角色选项（用于下拉选择） */
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

/** 分页响应 */
export interface PaginationResult<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
}
