import { http } from '@/api/client'
import type { DiaryEntry, PaginationParams, PaginationResult } from '@/api/types'

export const diaryService = {
  async getList(params: PaginationParams & { year?: number }): Promise<PaginationResult<DiaryEntry>> {
    return http.get('/v1/diaries', { params })
  },
  async getYears(): Promise<number[]> {
    return http.get('/v1/diaries/years')
  },
  async create(data: { content: string; images?: string; diaryDate: string }): Promise<DiaryEntry> {
    return http.post('/v1/diaries', data)
  },
  async update(code: string, data: { content?: string; images?: string; diaryDate?: string }): Promise<DiaryEntry> {
    return http.put(`/v1/diaries/${code}`, data)
  },
  async delete(code: string): Promise<void> {
    return http.delete(`/v1/diaries/${code}`)
  },
}
