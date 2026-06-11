import { http } from '@/api/client';
import type { Message, PaginationResult } from '@/api/types';

export const interactionService = {
  async getList(params: { targetType: string; targetCode: string; actionType?: string; page?: number; pageSize?: number }): Promise<PaginationResult<Message>> {
    return http.get('/v1/interactions', { params });
  },
  async create(data: { targetType: string; targetCode: string; actionType: string; userName: string; remark: string; parentId?: number }): Promise<Message> {
    return http.post('/v1/interactions', data);
  },
};
