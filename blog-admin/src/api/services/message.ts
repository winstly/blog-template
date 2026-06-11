import { http } from '@/api/client'
import type { Message, PaginationParams, PaginationResult } from '@/api/types'

export const messageService = {
  async getList(params: PaginationParams & { status?: string }): Promise<PaginationResult<Message>> {
    return http.get('/v1/messages', { params })
  },
  async approve(id: number): Promise<void> {
    return http.put(`/v1/messages/${id}/approve`)
  },
  async reply(messageId: number, content: string): Promise<Message> {
    return http.post('/v1/messages', {
      targetType: 'site',
      targetCode: 'site',
      actionType: 'message',
      userName: '管理员',
      remark: content,
      parentId: messageId,
    })
  },
  async delete(id: number): Promise<void> {
    return http.delete(`/v1/messages/${id}`)
  },
  async batchApprove(ids: number[]): Promise<void> {
    return http.put('/v1/messages/batch-approve', ids)
  },
  async batchDelete(ids: number[]): Promise<void> {
    return http.delete('/v1/messages/batch', { data: ids })
  },
}
