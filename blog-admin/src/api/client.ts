/**
 * HTTP Client - Axios Instance with Unified Interceptors
 *
 * Provides centralized HTTP request handling with:
 * - Base URL configuration
 * - Request interceptors (authentication token injection)
 * - Response interceptors (unified error handling)
 * - 401 response handling (automatic logout)
 */

import axios, { AxiosError } from 'axios'
import type { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import { useAuthStore } from '@/plugins/stores'
import { pinia } from '@/plugins/stores'

// API base URL - can be configured via environment variables
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

// HTTP status codes
const HTTP_STATUS = {
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  INTERNAL_ERROR: 500,
  BAD_GATEWAY: 502,
  SERVICE_UNAVAILABLE: 503,
} as const

// Error messages mapping
const ERROR_MESSAGES: Record<number, string> = {
  [HTTP_STATUS.UNAUTHORIZED]: '登录已过期，请重新登录',
  [HTTP_STATUS.FORBIDDEN]: '没有权限访问该资源',
  [HTTP_STATUS.NOT_FOUND]: '请求的资源不存在',
  [HTTP_STATUS.INTERNAL_ERROR]: '服务器内部错误',
  [HTTP_STATUS.BAD_GATEWAY]: '网关错误',
  [HTTP_STATUS.SERVICE_UNAVAILABLE]: '服务不可用',
}

/**
 * Create axios instance with default configuration
 */
function createAxiosInstance(): AxiosInstance {
  const instance = axios.create({
    baseURL: API_BASE_URL,
    timeout: 30000, // 30 seconds
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
  })

  // Request interceptor - add authentication token
  instance.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
      // Get token from auth store
      const authStore = useAuthStore(pinia)
      const token = authStore.token

      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }

      // Add request timestamp for debugging
      config.headers['X-Request-Time'] = new Date().toISOString()

      return config
    },
    (error: AxiosError) => {
      return Promise.reject(error)
    }
  )

  // Response interceptor - handle errors uniformly
  instance.interceptors.response.use(
    (response: AxiosResponse) => {
      // Return response data directly if wrapped in standard format
      if (response.data && typeof response.data === 'object') {
        // Check for API error code in response body
        if (response.data.code && response.data.code !== 200) {
          const errorMessage = response.data.message || '请求失败'
          return Promise.reject(new Error(errorMessage))
        }
        return response.data.data ?? response.data
      }
      return response.data
    },
    (error: AxiosError) => {
      return handleResponseError(error)
    }
  )

  return instance
}

/**
 * Handle HTTP response errors
 */
function handleResponseError(error: AxiosError): Promise<never> {
  if (error.response) {
    const { status, data } = error.response
    const message = (data as { message?: string })?.message || ERROR_MESSAGES[status] || '请求失败'

    // Handle 401 - Unauthorized (token expired or invalid)
    if (status === HTTP_STATUS.UNAUTHORIZED) {
      const authStore = useAuthStore(pinia)
      authStore.logout()
      // Redirect to login page
      window.location.href = '/login'
    }

    // Handle 403 - Forbidden
    if (status === HTTP_STATUS.FORBIDDEN) {
      console.warn('Permission denied:', message)
    }

    return Promise.reject(new Error(message))
  }

  // Network errors
  if (error.request) {
    return Promise.reject(new Error('网络连接失败，请检查网络设置'))
  }

  // Other errors
  return Promise.reject(error)
}

// Create and export the axios instance
export const httpClient = createAxiosInstance()

// Export types and utilities
export type { AxiosError, AxiosResponse }
export { HTTP_STATUS }

/**
 * HTTP request methods with type safety
 */
export const http = {
  get: <T>(url: string, config?: Record<string, unknown>): Promise<T> =>
    httpClient.get(url, config),

  post: <T>(url: string, data?: unknown, config?: Record<string, unknown>): Promise<T> =>
    httpClient.post(url, data, config),

  put: <T>(url: string, data?: unknown, config?: Record<string, unknown>): Promise<T> =>
    httpClient.put(url, data, config),

  patch: <T>(url: string, data?: unknown, config?: Record<string, unknown>): Promise<T> =>
    httpClient.patch(url, data, config),

  delete: <T>(url: string, config?: Record<string, unknown>): Promise<T> =>
    httpClient.delete(url, config),
}
