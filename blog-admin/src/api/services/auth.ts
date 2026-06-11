import type { LoginRequest, LoginResponse, User } from '@/api/types'

// Mock admin user with full permissions
const MOCK_USER: User = {
  id: '1',
  username: 'admin',
  nickname: '管理员',
  avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin',
  role: 'admin',
  permissions: [
    // Content management
    'article:view', 'article:create', 'article:edit', 'article:delete', 'article:publish',
    'category:view', 'category:manage',
    'tag:view', 'tag:manage',
    'diary:view', 'diary:manage',
    'message:view', 'message:manage',
    'link:view', 'link:manage',
    // System management
    'dict:view', 'dict:manage',
    'user:view', 'user:manage', 'user:resetpwd',
    'role:view', 'role:manage', 'role:assign',
    'permission:view', 'permission:manage',
  ],
}

// SECURITY WARNING: Hardcoded credentials for demo/testing only
// TODO: Replace with proper API authentication for production
// Production should use:
// 1. Backend API authentication with bcrypt/scrypt password hashing
// 2. HTTPS/TLS for all authentication requests
// 3. Rate limiting to prevent brute force attacks
// 4. Multi-factor authentication (MFA) for admin accounts
const VALID_CREDENTIALS = {
  username: 'admin',
  password: 'admin123',
}

export const mockAuthService = {
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    // SECURITY NOTE: This is a mock implementation for demo purposes
    // Production implementation must:
    // 1. Send credentials to backend API over HTTPS
    // 2. Use secure session tokens (JWT with proper expiration)
    // 3. Implement CSRF protection
    // 4. Log authentication attempts for security audit
    // Simulate network delay
    await new Promise((resolve) => setTimeout(resolve, 500))

    if (credentials.username === VALID_CREDENTIALS.username &&
        credentials.password === VALID_CREDENTIALS.password) {
      return {
        token: 'mock_jwt_token_' + Date.now(),
        user: MOCK_USER,
      }
    }

    throw new Error('用户名或密码错误')
  },

  async refreshToken(_token: string): Promise<string> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    return 'mock_jwt_token_' + Date.now()
  },
}
