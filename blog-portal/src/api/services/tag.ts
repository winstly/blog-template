import { http } from '@/api/client';
import type { Tag } from '@/api/types';

export const tagService = {
  async getList(): Promise<Tag[]> {
    return http.get('/v1/tags');
  },
};
