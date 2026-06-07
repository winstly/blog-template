import { createPinia } from 'pinia'

// Export pinia instance
export const pinia = createPinia()

// Export all stores for centralized access
export { useAuthStore } from './auth'
export { useTabNavStore } from './tabNav'
export { usePermissionStore } from './permission'
