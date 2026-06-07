import type { Message, PaginationParams, PaginationResult } from '@/api/types'
import { mockMessages } from '@/api/mock/data'

let messages = [...mockMessages]

export const messageService = {
  async getList(params: PaginationParams & { status?: string }): Promise<PaginationResult<Message>> {
    await new Promise((resolve) => setTimeout(resolve, 300))

    let result = [...messages]

    if (params.status && params.status !== 'all') {
      result = result.filter((m) => m.status === params.status)
    }

    result.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())

    const total = result.length
    const start = (params.page - 1) * params.pageSize
    const end = start + params.pageSize
    result = result.slice(start, end)

    return {
      list: result,
      total,
      page: params.page,
      pageSize: params.pageSize,
    }
  },

  async approve(id: string): Promise<Message> {
    return this.updateStatus(id, 'approved')
  },

  async updateStatus(id: string, status: 'pending' | 'approved'): Promise<Message> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    const index = messages.findIndex((m) => m.id === id)
    if (index === -1) throw new Error('Message not found')
    messages[index] = { ...messages[index], status }
    return messages[index]
  },

  async batchApprove(ids: string[]): Promise<void> {
    await new Promise((resolve) => setTimeout(resolve, 400))
    messages = messages.map((m) =>
      ids.includes(m.id) ? { ...m, status: 'approved' } : m
    )
  },

  async reply(messageId: string, content: string): Promise<Message> {
    await new Promise((resolve) => setTimeout(resolve, 400))
    const index = messages.findIndex((m) => m.id === messageId)
    if (index === -1) throw new Error('Message not found')

    const reply: Message = {
      id: `${messageId}-${Date.now()}`,
      author: '管理员',
      username: '管理员',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin',
      content,
      location: '本地',
      date: new Date().toISOString(),
      status: 'approved',
      replyTo: messageId,
    }

    const replies = messages[index].replies || []
    messages[index] = { ...messages[index], replies: [...replies, reply] }
    return reply
  },

  async delete(id: string): Promise<void> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    messages = messages.filter((m) => m.id !== id && m.replyTo !== id)
  },

  async batchDelete(ids: string[]): Promise<void> {
    await new Promise((resolve) => setTimeout(resolve, 400))
    messages = messages.filter((m) => !ids.includes(m.id) && !ids.includes(m.replyTo || ''))
  },
}
