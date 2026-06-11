import { http } from '@/api/client'
import type { Category } from '@/api/types'

export const categoryService = {
  async getList(): Promise<Category[]> {
    return http.get('/v1/categories')
  },
  async getAll(): Promise<Category[]> {
    return this.getList()
  },
  async create(data: { tagCode: string; tagName: string; description?: string; parentTagCode?: string }): Promise<Category> {
    return http.post('/v1/tags', data)
  },
  async update(code: string, data: { tagName?: string; description?: string; displayStatus?: number }): Promise<Category> {
    return http.put(`/v1/tags/${code}`, data)
  },
  async move(code: string, newParentTagCode: string | null): Promise<Category> {
    return http.put(`/v1/tags/${code}/move`, { newParentTagCode })
  },
  async delete(code: string): Promise<void> {
    return http.delete(`/v1/tags/${code}`)
  },
}
