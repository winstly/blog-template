import { http } from '@/api/client'
import type { Article, PaginationParams, PaginationResult } from '@/api/types'

export const articleService = {
  async getList(params: PaginationParams & { keyword?: string; status?: string; category?: string }): Promise<PaginationResult<Article>> {
    return http.get('/v1/articles/admin', { params })
  },
  async getByCode(code: string): Promise<Article> {
    return http.get(`/v1/articles/${code}`)
  },
  async create(data: { title: string; summary?: string; coverUrl?: string; body: string; contentFormat?: string }): Promise<Article> {
    return http.post('/v1/articles', data)
  },
  async update(code: string, data: { title?: string; summary?: string; coverUrl?: string }): Promise<Article> {
    return http.put(`/v1/articles/${code}`, data)
  },
  async updateContent(code: string, body: string): Promise<Article> {
    return http.put(`/v1/articles/${code}/content`, { body })
  },
  async publish(code: string): Promise<Article> {
    return http.put(`/v1/articles/${code}/publish`)
  },
  async unpublish(code: string): Promise<Article> {
    return http.put(`/v1/articles/${code}/unpublish`)
  },
  async togglePin(code: string): Promise<Article> {
    return http.put(`/v1/articles/${code}/pin`)
  },
  async delete(code: string): Promise<void> {
    return http.delete(`/v1/articles/${code}`)
  },
  async batchDelete(codes: string[]): Promise<void> {
    return http.delete('/v1/articles/batch', { data: codes })
  },
  async assignTags(code: string, tagCodes: string[], categoryCodes: string[]): Promise<void> {
    return http.put(`/v1/articles/${code}/tags`, { tagCodes, categoryCodes })
  },
}
