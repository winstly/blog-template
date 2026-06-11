import { http } from '@/api/client'
import type { Tag } from '@/api/types'

export const tagService = {
  async getList(): Promise<Tag[]> {
    return http.get('/v1/tags')
  },
  async getAll(): Promise<Tag[]> {
    return this.getList()
  },
  async create(data: { tagCode: string; tagName: string; description?: string; parentTagCode?: string }): Promise<Tag> {
    return http.post('/v1/tags', data)
  },
  async update(code: string, data: { tagName?: string; description?: string; displayStatus?: number }): Promise<Tag> {
    return http.put(`/v1/tags/${code}`, data)
  },
  async move(code: string, newParentTagCode: string | null): Promise<Tag> {
    return http.put(`/v1/tags/${code}/move`, { newParentTagCode })
  },
  async delete(code: string): Promise<void> {
    return http.delete(`/v1/tags/${code}`)
  },
}
