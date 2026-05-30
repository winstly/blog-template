import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/home-view.vue'),
    },
    {
      path: '/blog',
      name: 'blog',
      component: () => import('@/views/blog-view.vue'),
    },
    {
      path: '/article/:id',
      name: 'article',
      component: () => import('@/views/article-view.vue'),
    },
    {
      path: '/message',
      name: 'message',
      component: () => import('@/views/message-view.vue'),
    },
    {
      path: '/diary',
      name: 'diary',
      component: () => import('@/views/diary-view.vue'),
    },
    {
      path: '/link',
      name: 'link',
      component: () => import('@/views/link-view.vue'),
    },
  ],
  scrollBehavior() {
    return { top: 0 }
  },
})

export default router
