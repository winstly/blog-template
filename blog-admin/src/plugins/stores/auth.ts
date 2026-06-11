import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User, LoginRequest } from '@/api/types'
import { mockAuthService } from '@/api/services/auth'
import { usePermissionStore } from './permission'

const TOKEN_KEY = 'blog_admin_token'
const USER_KEY = 'blog_admin_user'

// SECURITY NOTE: Using sessionStorage instead of localStorage for token storage
// sessionStorage is cleared when browser/tab closes, reducing XSS attack window
// For production, consider:
// 1. httpOnly cookies (most secure, prevents JavaScript access)
// 2. SameSite cookie attributes to prevent CSRF
// 3. Short token expiration with refresh token rotation
// 4. Secure flag on cookies (HTTPS only)
// 5. Token binding to device/browser fingerprint
const storage = sessionStorage

export const useAuthStore = defineStore('auth', () => {
  // State
  const token = ref<string | null>(storage.getItem(TOKEN_KEY))
  const user = ref<User | null>(null)
  const loading = ref(false)

  // Initialize user from storage
  const storedUser = storage.getItem(USER_KEY)
  if (storedUser) {
    try {
      user.value = JSON.parse(storedUser)
      // Restore user permissions
      const permissionStore = usePermissionStore()
      permissionStore.setUserPermissions(user.value?.permissions || [])
    } catch {
      storage.removeItem(USER_KEY)
    }
  }

  // Getters
  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'admin')

  // Actions
  async function login(credentials: LoginRequest) {
    loading.value = true
    try {
      const response = await mockAuthService.login(credentials)
      token.value = response.token
      user.value = response.user
      storage.setItem(TOKEN_KEY, response.token)
      storage.setItem(USER_KEY, JSON.stringify(response.user))

      // Set user permissions in permission store
      const permissionStore = usePermissionStore()
      permissionStore.setUserPermissions(response.user.permissions || [])

      return true
    } catch (error) {
      return false
    } finally {
      loading.value = false
    }
  }

  function logout() {
    token.value = null
    user.value = null
    storage.removeItem(TOKEN_KEY)
    storage.removeItem(USER_KEY)

    // Clear user permissions
    const permissionStore = usePermissionStore()
    permissionStore.clearUserPermissions()
  }

  function updateToken(newToken: string) {
    token.value = newToken
    storage.setItem(TOKEN_KEY, newToken)
  }

  return {
    token,
    user,
    loading,
    isAuthenticated,
    isAdmin,
    login,
    logout,
    updateToken,
  }
})
