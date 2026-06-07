import type {
  Article,
  DiaryEntry,
  Message,
  FriendLink,
  Category,
  Tag,
  ViewTrend,
} from '@/api/types'

// Mock Articles
export const mockArticles: Article[] = [
  {
    id: '1',
    title: 'Vue 3 组合式 API 最佳实践',
    summary: '深入探讨 Vue 3 Composition API 的使用技巧和最佳实践...',
    content: '# Vue 3 组合式 API 最佳实践\n\nVue 3 带来了全新的 Composition API...',
    cover: 'https://picsum.photos/400/300?random=1',
    date: '2024-06-01',
    author: '管理员',
    views: 1234,
    comments: 56,
    isTop: true,
    status: 'published',
    category: '技术',
    tags: ['Vue', '前端', 'JavaScript'],
  },
  {
    id: '2',
    title: 'Tailwind CSS 实战指南',
    summary: '从入门到精通，掌握 Tailwind CSS 的实用技巧...',
    content: '# Tailwind CSS 实战指南\n\nTailwind CSS 是一个功能类优先的 CSS 框架...',
    cover: 'https://picsum.photos/400/300?random=2',
    date: '2024-05-28',
    author: '管理员',
    views: 892,
    comments: 34,
    isTop: false,
    status: 'published',
    category: '技术',
    tags: ['CSS', 'Tailwind', '前端'],
  },
  {
    id: '3',
    title: 'TypeScript 高级类型编程',
    summary: '探索 TypeScript 类型系统的强大功能...',
    content: '# TypeScript 高级类型编程\n\nTypeScript 的类型系统非常强大...',
    cover: 'https://picsum.photos/400/300?random=3',
    date: '2024-05-20',
    author: '管理员',
    views: 2156,
    comments: 89,
    isTop: false,
    status: 'published',
    category: '技术',
    tags: ['TypeScript', 'JavaScript'],
  },
  {
    id: '4',
    title: '我的 2024 年度计划',
    summary: '记录一下今年的目标和计划...',
    content: '# 我的 2024 年度计划\n\n今年有很多想要完成的事情...',
    cover: 'https://picsum.photos/400/300?random=4',
    date: '2024-06-02',
    author: '管理员',
    views: 0,
    comments: 0,
    isTop: false,
    status: 'draft',
    category: '生活',
    tags: ['计划', '生活'],
  },
  {
    id: '5',
    title: '周末旅行日记：西湖之旅',
    summary: '分享一次难忘的西湖旅行经历...',
    content: '# 周末旅行日记：西湖之旅\n\n这个周末去了杭州西湖...',
    cover: 'https://picsum.photos/400/300?random=5',
    date: '2024-05-15',
    author: '管理员',
    views: 567,
    comments: 23,
    isTop: false,
    status: 'published',
    category: '旅行',
    tags: ['旅行', '杭州', '西湖'],
  },
]

// Mock Diary Entries
export const mockDiaries: DiaryEntry[] = [
  {
    id: '1',
    date: '2024-06-03',
    content: '今天学习了很多 Vue 3 的新特性，感觉收获满满。Composition API 真的比 Options API 灵活太多了。',
    year: 2024,
  },
  {
    id: '2',
    date: '2024-06-01',
    content: '儿童节快乐！虽然已经不是孩子了，但保持一颗童心很重要。',
    year: 2024,
  },
  {
    id: '3',
    date: '2024-05-28',
    content: '项目进展很顺利，今天完成了文章管理模块的开发。明天开始做留言管理功能。',
    year: 2024,
  },
  {
    id: '4',
    date: '2023-12-31',
    content: '2023 年的最后一天，回顾这一年，有很多收获，也有遗憾。希望 2024 会更好。',
    year: 2023,
  },
  {
    id: '5',
    date: '2023-06-15',
    content: '夏天到了，天气越来越热。今天开始了自己的博客项目开发。',
    year: 2023,
  },
]

// Mock Messages
export const mockMessages: Message[] = [
  {
    id: '1',
    author: '张三',
    username: '张三',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=1',
    content: '博主的文章写得真好，学到了很多！',
    location: '北京',
    date: '2024-06-03T10:30:00',
    status: 'approved',
  },
  {
    id: '2',
    author: '李四',
    username: '李四',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2',
    content: '请问一下 Vue 3 和 React 哪个更适合新手学习？',
    location: '上海',
    date: '2024-06-02T15:20:00',
    status: 'pending',
  },
  {
    id: '3',
    author: '王五',
    username: '王五',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3',
    content: '期待更多 TypeScript 相关的文章！',
    location: '广州',
    date: '2024-06-01T09:15:00',
    status: 'approved',
    replies: [
      {
        id: '3-1',
        author: '管理员',
        username: '管理员',
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin',
        content: '谢谢支持！我会继续更新 TypeScript 系列文章的。',
        location: '本地',
        date: '2024-06-01T10:00:00',
        status: 'approved',
        replyTo: '3',
      },
    ],
  },
  {
    id: '4',
    author: '赵六',
    username: '赵六',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=4',
    content: '博客设计得很漂亮，可以分享一下技术栈吗？',
    location: '深圳',
    date: '2024-05-30T18:45:00',
    status: 'pending',
  },
]

// Mock Friend Links
export const mockLinks: FriendLink[] = [
  {
    id: '1',
    name: 'Vue.js',
    url: 'https://vuejs.org',
    logo: 'https://vuejs.org/logo.svg',
    description: '渐进式 JavaScript 框架',
    order: 1,
    active: true,
  },
  {
    id: '2',
    name: 'Element Plus',
    url: 'https://element-plus.org',
    logo: 'https://element-plus.org/images/element-plus-logo.svg',
    description: '基于 Vue 3 的组件库',
    order: 2,
    active: true,
  },
  {
    id: '3',
    name: 'Tailwind CSS',
    url: 'https://tailwindcss.com',
    logo: 'https://tailwindcss.com/favicons/favicon-32x32.png',
    description: '功能类优先的 CSS 框架',
    order: 3,
    active: true,
  },
  {
    id: '4',
    name: 'GitHub',
    url: 'https://github.com',
    logo: 'https://github.com/favicon.ico',
    description: '全球最大的代码托管平台',
    order: 4,
    active: true,
  },
]

// Mock Categories
export const mockCategories: Category[] = [
  { id: '1', name: '技术', count: 3, sort: 1 },
  { id: '2', name: '生活', count: 1, sort: 2 },
  { id: '3', name: '旅行', count: 1, sort: 3 },
  { id: '4', name: '随笔', count: 0, sort: 4 },
  { id: '5', name: '前端开发', parentId: '1', count: 2, sort: 1 },
  { id: '6', name: '后端开发', parentId: '1', count: 1, sort: 2 },
  { id: '7', name: '数据库', parentId: '1', count: 0, sort: 3 },
  { id: '8', name: '日常记录', parentId: '2', count: 1, sort: 1 },
  { id: '9', name: '读书笔记', parentId: '2', count: 0, sort: 2 },
  { id: '10', name: '国内游记', parentId: '3', count: 1, sort: 1 },
  { id: '11', name: '海外游记', parentId: '3', count: 0, sort: 2 },
]

// Mock Tags
export const mockTags: Tag[] = [
  { id: '1', name: 'Vue', count: 1 },
  { id: '2', name: '前端', count: 2 },
  { id: '3', name: 'JavaScript', count: 1 },
  { id: '4', name: 'CSS', count: 1 },
  { id: '5', name: 'Tailwind', count: 1 },
  { id: '6', name: 'TypeScript', count: 1 },
  { id: '7', name: '计划', count: 1 },
  { id: '8', name: '生活', count: 1 },
  { id: '9', name: '旅行', count: 1 },
  { id: '10', name: '杭州', count: 1 },
]

// Mock View Trends (last 30 days)
export const mockViewTrends: ViewTrend[] = Array.from({ length: 30 }, (_, i) => {
  const date = new Date()
  date.setDate(date.getDate() - (29 - i))
  return {
    date: date.toISOString().split('T')[0],
    views: Math.floor(Math.random() * 200) + 50,
  }
})
