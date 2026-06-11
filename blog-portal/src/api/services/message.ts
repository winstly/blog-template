import { http } from '@/api/client';
import type { Message, PaginationResult } from '@/api/types';

export const messageService = {
  async getList(params: { page?: number; pageSize?: number } = {}): Promise<PaginationResult<Message>> {
    return http.get('/v1/messages', { params });
  },
  async create(data: { userName: string; remark: string; userAvatarUrl?: string }): Promise<Message> {
    return http.post('/v1/messages', {
      targetType: 'site',
      targetCode: 'site',
      actionType: 'message',
      ...data,
    });
  },
};
