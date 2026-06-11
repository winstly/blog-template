import { http } from '@/api/client';
import type { Category } from '@/api/types';

export const categoryService = {
  async getList(): Promise<Category[]> {
    return http.get('/v1/categories');
  },
};
