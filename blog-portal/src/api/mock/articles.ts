import type { Article, Profile, NavItem, SocialLink, Category, Tag } from '../types'

export const mockArticles: Article[] = [
  {
    id: '1',
    title: '.NET Spire.Doc组件使用指南',
    summary: 'Spire.Doc for .NET是一款由E-iceblue公司开发的专业的Word .NET类库，使用该工具开发人员可以在任意.NET平台上快速创建，读取，写入，转换，打印Word文档。',
    cover: '/images/cover/doc.png',
    date: '2019-01-21',
    link: '/article/1',
    author: '燕十三',
    views: 57,
    comments: 1,
    isTop: true,
    category: 'ASP.NET MVC',
    tags: ['ASP.NET', 'C#', '组件'],
  },
  {
    id: '2',
    title: '使用码云和VS托管本地代码',
    summary: '本文介绍如何使用码云(Gitee)和Visual Studio进行代码托管和版本控制，包括创建仓库、克隆代码、提交更改等基本操作。',
    cover: '/images/cover/git.png',
    date: '2018-05-22',
    link: '/article/2',
    author: '燕十三',
    views: 37,
    comments: 0,
    isTop: false,
    category: '开发工具',
    tags: ['Git', 'VS', '码云'],
  },
  {
    id: '3',
    title: 'Vue3 + TypeScript 项目搭建最佳实践',
    summary: '从零开始搭建一个现代化的Vue3项目，包括Vite配置、TypeScript集成、状态管理、路由配置等核心内容。',
    cover: '/images/cover/vue.png',
    date: '2024-01-15',
    link: '/article/3',
    author: '燕十三',
    views: 128,
    comments: 5,
    isTop: true,
    category: '前端开发',
    tags: ['Vue3', 'TypeScript', 'Vite'],
  },
  {
    id: '4',
    title: 'Tailwind CSS 实战技巧总结',
    summary: '分享在使用Tailwind CSS过程中积累的一些实用技巧，包括响应式设计、暗色模式、自定义配置等内容。',
    cover: '/images/cover/tailwind.png',
    date: '2024-02-20',
    link: '/article/4',
    author: '燕十三',
    views: 89,
    comments: 3,
    isTop: false,
    category: '前端开发',
    tags: ['CSS', 'Tailwind', '响应式'],
  },
  {
    id: '5',
    title: 'Pinia 状态管理深入解析',
    summary: '深入理解Pinia的状态管理机制，包括Store定义、Actions使用、持久化存储等进阶用法。',
    cover: '/images/cover/pinia.png',
    date: '2024-03-10',
    link: '/article/5',
    author: '燕十三',
    views: 65,
    comments: 2,
    isTop: false,
    category: '前端开发',
    tags: ['Vue3', 'Pinia', '状态管理'],
  },
]

export const mockProfile: Profile = {
  nickname: '燕十三',
  signature: '剑气纵横三万里，一剑光寒十九洲。',
  bio: '爱好游戏，动漫。闲来无事喜欢在一些不健康的场所虚度光阴，撸起代码就会废寝忘食。',
  location: '四川成都金牛区金科北路39号',
  qq: '930054439',
  email: '930054439@qq.com',
}

export const mockNavItems: NavItem[] = [
  { label: '首页', path: '/', icon: 'home' },
  { label: '博客', path: '/blog', icon: 'file-text' },
  { label: '日记', path: '/diary', icon: 'book' },
  { label: '关于我', path: '/about', icon: 'user' },
  { label: '留言', path: '/message', icon: 'message-circle' },
]

export const mockSocialLinks: SocialLink[] = [
  { icon: 'user', label: '关于我', url: '/about' },
  { icon: 'book-open', label: '博文', url: '/blog' },
  { icon: 'message-circle', label: '留言', url: '/message' },
  { icon: 'snowflake', label: '日记', url: '/diary' },
]

export const mockCategories: Category[] = [
  { id: '1', name: '前端开发', count: 3 },
  { id: '2', name: 'ASP.NET MVC', count: 1 },
  { id: '3', name: '开发工具', count: 1 },
]

export const mockTags: Tag[] = [
  { id: '1', name: 'Vue3', count: 2 },
  { id: '2', name: 'TypeScript', count: 1 },
  { id: '3', name: 'Tailwind', count: 1 },
  { id: '4', name: 'Git', count: 1 },
  { id: '5', name: 'C#', count: 1 },
  { id: '6', name: 'Pinia', count: 1 },
]

export const mockHotArticles: Article[] = mockArticles
  .sort((a, b) => (b.views || 0) - (a.views || 0))
  .slice(0, 5)
