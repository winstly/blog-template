import { http } from '@/api/client'
import type { FriendLink } from '@/api/types'

export const linkService = {
  async getList(): Promise<FriendLink[]> {
    return http.get('/v1/friend-links')
  },
  async getAll(): Promise<FriendLink[]> {
    return this.getList()
  },
  async create(data: { name: string; url: string; logo?: string; description?: string; sortOrder?: number }): Promise<FriendLink> {
    return http.post('/v1/friend-links', data)
  },
  async update(itemCode: string, data: { name?: string; url?: string; logo?: string; description?: string; sortOrder?: number }): Promise<FriendLink> {
    return http.put(`/v1/friend-links/${itemCode}`, data)
  },
  async delete(itemCode: string): Promise<void> {
    return http.delete(`/v1/friend-links/${itemCode}`)
  },
}
