/** 标签摘要 */
export interface TagSummary {
  tagCode: string;
  tagName: string;
}

/** 文章 */
export interface Article {
  id: number;
  articleCode: string;
  title: string;
  summary: string | null;
  coverUrl: string | null;
  isPinned: boolean;
  viewCount: number;
  publishedAt: string | null;
  gmtCreate: string;
  gmtModified: string;
  body: string | null;
  contentFormat: string | null;
  wordCount: number | null;
  version: number | null;
  publishStatus: string;
  tags: TagSummary[];
  category: TagSummary | null;
}

/** 导航项 */
export interface NavItem {
  code: string;
  label: string;
  path: string;
}

/** 友链 */
export interface FriendLink {
  itemCode: string;
  name: string;
  url: string;
  logo: string | null;
  description: string | null;
  sortOrder: number;
}

/** 个人信息 */
export interface Profile {
  nickname: string;
  signature: string;
  avatar?: string;
  bio: string;
  location: string;
  qq: string;
  email: string;
}

/** 社交链接 */
export interface SocialLink {
  code: string;
  label: string;
  url: string;
}

/** 日记条目 */
export interface DiaryEntry {
  id: number;
  diaryCode: string;
  content: string;
  images: string | null;
  diaryDate: string;
  year: number;
  gmtCreate: string;
  gmtModified: string;
}

/** 留言/评论 */
export interface Message {
  id: number;
  userName: string;
  userAvatarUrl: string | null;
  content: string;
  location: string | null;
  date: string;
  status: string;
  replyTo: string | null;
  replies: Message[];
}

/** 分类 */
export interface Category {
  id: number;
  tagCode: string;
  tagName: string;
  articleCount: number;
}

/** 标签 */
export interface Tag {
  id: number;
  tagCode: string;
  tagName: string;
  articleCount: number;
}

/** 分页响应 */
export interface PaginationResult<T> {
  list: T[];
  pagination: {
    page: number;
    pageSize: number;
    total: number;
  };
}
