import 'vue-router'

declare module 'vue-router' {
  interface RouteMeta {
    /** 页面标题，用于 tab 和 document.title */
    title?: string
    /** 是否为公开路由（无需登录即可访问） */
    public?: boolean
    /** 是否在菜单中隐藏 */
    hidden?: boolean
    /** 允许访问的角色列表，用户角色需在列表中才能访问 */
    roles?: string[]
    /** 需要的权限码列表，用户需拥有所有权限才能访问 */
    permissions?: string[]
    /** 是否需要登录 */
    requiresAuth?: boolean
  }
}
