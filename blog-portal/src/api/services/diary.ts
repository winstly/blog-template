import { http } from '@/api/client';
import type { DiaryEntry, PaginationResult } from '@/api/types';

export const diaryService = {
  async getList(params: { page?: number; pageSize?: number; year?: number } = {}): Promise<PaginationResult<DiaryEntry>> {
    return http.get('/v1/diaries', { params });
  },
  async getYears(): Promise<number[]> {
    return http.get('/v1/diaries/years');
  },
};
