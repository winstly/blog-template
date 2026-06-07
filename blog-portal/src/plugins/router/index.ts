import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/home/HomeView.vue'),
    },
    {
      path: '/blog',
      name: 'blog',
      component: () => import('@/views/blog/BlogView.vue'),
    },
    {
      path: '/article/:id',
      name: 'article',
      component: () => import('@/views/article/ArticleView.vue'),
    },
    {
      path: '/message',
      name: 'message',
      component: () => import('@/views/message/MessageView.vue'),
    },
    {
      path: '/diary',
      name: 'diary',
      component: () => import('@/views/diary/DiaryView.vue'),
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('@/views/about/AboutView.vue'),
    },
  ],
  scrollBehavior() {
    return { top: 0 }
  },
})

export default router
