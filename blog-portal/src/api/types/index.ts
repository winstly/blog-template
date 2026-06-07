/** 文章 */
export interface Article {
  id: string
  title: string
  summary: string
  cover: string
  date: string
  link: string
  author?: string
  views?: number
  comments?: number
  isTop?: boolean
  category?: string
  tags?: string[]
}

/** 导航项 */
export interface NavItem {
  label: string
  path: string
  icon: string
}

/** 友链 */
export interface FriendLink {
  id: string
  name: string
  url: string
  logo?: string
  description?: string
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
}

/** 留言/评论 */
export interface Message {
  id: string
  author: string
  avatar: string
  content: string
  location: string
  date: string
  replyTo?: string
  replies?: Message[]
}

/** 分类 */
export interface Category {
  id: string
  name: string
  count: number
}

/** 标签 */
export interface Tag {
  id: string
  name: string
  count: number
}
