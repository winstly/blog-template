import { createRouter, createWebHistory, type RouteLocationNormalized } from 'vue-router'
import { useAuthStore, useTabNavStore, usePermissionStore } from '@/plugins/stores'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/login/LoginView.vue'),
      meta: { public: true },
    },
    {
      path: '/',
      name: 'layout',
      component: () => import('@/components/layout/AppLayout.vue'),
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('@/views/dashboard/DashboardView.vue'),
          meta: { title: '仪表盘' },
        },
        {
          path: 'articles',
          name: 'articles',
          component: () => import('@/views/article/ArticleList.vue'),
          meta: { title: '文章管理' },
        },
        {
          path: 'articles/create',
          name: 'article-create',
          component: () => import('@/views/article/ArticleEdit.vue'),
          meta: { title: '写文章', hidden: true },
        },
        {
          path: 'articles/edit/:code',
          name: 'article-edit',
          component: () => import('@/views/article/ArticleEdit.vue'),
          meta: { title: '编辑文章', hidden: true },
        },
        {
          path: 'categories',
          name: 'categories',
          component: () => import('@/views/category/CategoryList.vue'),
          meta: { title: '分类管理' },
        },
        {
          path: 'tags',
          name: 'tags',
          component: () => import('@/views/tag/TagList.vue'),
          meta: { title: '标签管理' },
        },
        {
          path: 'diaries',
          name: 'diaries',
          component: () => import('@/views/diary/DiaryList.vue'),
          meta: { title: '日记管理' },
        },
        {
          path: 'messages',
          name: 'messages',
          component: () => import('@/views/message/MessageList.vue'),
          meta: { title: '留言管理' },
        },
        {
          path: 'links',
          name: 'links',
          component: () => import('@/views/link/LinkList.vue'),
          meta: { title: '友链管理' },
        },
        // System Management - Only accessible by admin role
        {
          path: 'dicts',
          name: 'dicts',
          component: () => import('@/views/system/DictList.vue'),
          meta: { title: '字典管理', roles: ['admin'] },
        },
        {
          path: 'users',
          name: 'users',
          component: () => import('@/views/system/UserList.vue'),
          meta: { title: '用户管理', roles: ['admin'] },
        },
        {
          path: 'roles',
          name: 'roles',
          component: () => import('@/views/system/RoleList.vue'),
          meta: { title: '角色管理', roles: ['admin'] },
        },
        {
          path: 'permissions',
          name: 'permissions',
          component: () => import('@/views/system/PermissionList.vue'),
          meta: { title: '权限管理', roles: ['admin'] },
        },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/',
    },
  ],
})

/**
 * Route guard for authentication and role-based access control
 *
 * Supports route meta:
 * - public: boolean - Allow access without authentication
 * - roles: string[] - Required roles to access this route
 * - permissions: string[] - Required permission codes
 */
router.beforeEach((to: RouteLocationNormalized) => {
  const authStore = useAuthStore()

  // Public routes - no authentication required
  if (to.meta.public) {
    // Redirect to dashboard if already logged in
    if (authStore.isAuthenticated && to.path === '/login') {
      return '/'
    }
    return true
  }

  // Check authentication
  if (!authStore.isAuthenticated) {
    return '/login'
  }

  // Check role-based access control
  if (to.meta.roles && Array.isArray(to.meta.roles)) {
    const userRole = authStore.user?.role
    const requiredRoles = to.meta.roles as string[]

    if (!userRole || !requiredRoles.includes(userRole)) {
      // User doesn't have required role - redirect to dashboard
      console.warn(`Access denied: user role '${userRole}' not in [${requiredRoles.join(', ')}]`)
      return '/'
    }
  }

  // Check permission-based access control
  if (to.meta.permissions && Array.isArray(to.meta.permissions)) {
    const requiredPermissions = to.meta.permissions as string[]
    const permissionStore = usePermissionStore()

    if (!permissionStore.hasAllPermissions(requiredPermissions)) {
      // User doesn't have required permissions - redirect to dashboard
      console.warn(`Access denied: missing permissions [${requiredPermissions.join(', ')}]`)
      return '/'
    }
  }

  return true
})

// Route guard for tab synchronization and accessibility
router.afterEach((to) => {
  // Skip hidden routes
  if (to.meta.hidden) {
    return
  }

  const title = to.meta.title as string || '未命名'

  // Accessibility: Update document title for screen readers
  document.title = title ? `${title} - Blog Admin` : 'Blog Admin'

  // Skip login page for tab management
  if (to.meta.public) {
    return
  }

  const tabNavStore = useTabNavStore()

  // Add or activate tab
  tabNavStore.addTab({
    path: to.path,
    title,
    fixed: to.path === '/dashboard',
  })

  // Accessibility: Announce page change to screen readers
  setTimeout(() => {
    const mainContent = document.getElementById('main-content')
    if (mainContent) {
      // Set aria-live region for page announcements
      mainContent.setAttribute('aria-live', 'polite')
      mainContent.setAttribute('aria-atomic', 'true')
    }
  }, 100)
})

export default router
