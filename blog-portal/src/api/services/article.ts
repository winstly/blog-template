import { http } from '@/api/client';
import type { Article, PaginationResult } from '@/api/types';

export const articleService = {
  async getList(params: { page?: number; pageSize?: number } = {}): Promise<PaginationResult<Article>> {
    return http.get('/v1/articles', { params });
  },
  async getByCode(code: string): Promise<Article> {
    return http.get(`/v1/articles/${code}`);
  },
};
