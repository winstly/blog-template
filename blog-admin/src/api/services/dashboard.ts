import { http } from '@/api/client'
import type { Statistics, ViewTrend, Article, Message } from '@/api/types'

export const dashboardService = {
  async getStatistics(): Promise<Statistics> {
    return http.get('/v1/dashboard/statistics')
  },
  async getViewTrends(days: 7 | 30 = 7): Promise<ViewTrend[]> {
    return http.get('/v1/dashboard/view-trends', { params: { days } })
  },
  async getRecentArticles(limit: number = 5): Promise<Article[]> {
    return http.get('/v1/dashboard/recent-articles', { params: { limit } })
  },
  async getRecentComments(limit: number = 5): Promise<Message[]> {
    return http.get('/v1/dashboard/recent-comments', { params: { limit } })
  },
}
