/**
 * HTTP Client for blog-portal
 *
 * Simplified version without auth token injection (portal is public).
 * Response interceptor unwraps backend R<T> format: { code, message, data } → data
 */

import axios, { AxiosError } from 'axios'
import type { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

function createAxiosInstance(): AxiosInstance {
  const instance = axios.create({
    baseURL: API_BASE_URL,
    timeout: 30000,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
  })

  instance.interceptors.response.use(
    (response: AxiosResponse) => {
      if (response.data && typeof response.data === 'object') {
        if (response.data.code && response.data.code !== 200) {
          const errorMessage = response.data.message || '请求失败'
          return Promise.reject(new Error(errorMessage))
        }
        return response.data.data ?? response.data
      }
      return response.data
    },
    (error: AxiosError) => {
      if (error.response) {
        const { status, data } = error.response
        const message = (data as { message?: string })?.message || `请求失败 (${status})`
        return Promise.reject(new Error(message))
      }
      if (error.request) {
        return Promise.reject(new Error('网络连接失败，请检查网络设置'))
      }
      return Promise.reject(error)
    }
  )

  return instance
}

export const httpClient = createAxiosInstance()

export const http = {
  get: <T>(url: string, config?: Record<string, unknown>): Promise<T> =>
    httpClient.get(url, config),

  post: <T>(url: string, data?: unknown, config?: Record<string, unknown>): Promise<T> =>
    httpClient.post(url, data, config),

  put: <T>(url: string, data?: unknown, config?: Record<string, unknown>): Promise<T> =>
    httpClient.put(url, data, config),

  delete: <T>(url: string, config?: Record<string, unknown>): Promise<T> =>
    httpClient.delete(url, config),
}
