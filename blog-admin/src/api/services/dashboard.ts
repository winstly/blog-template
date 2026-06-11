import type { Statistics, ViewTrend, Article, Message } from '@/api/types'
import { mockArticles, mockMessages, mockLinks, mockViewTrends } from '@/api/mock/data'

export const dashboardService = {
  async getStatistics(): Promise<Statistics> {
    await new Promise((resolve) => setTimeout(resolve, 200))
    const totalViews = mockArticles.reduce((sum, a) => sum + a.views, 0)
    const totalComments = mockArticles.reduce((sum, a) => sum + a.comments, 0)

    return {
      totalArticles: mockArticles.length,
      totalViews,
      totalComments,
      totalLinks: mockLinks.length,
    }
  },

  async getViewTrends(days: 7 | 30 = 7): Promise<ViewTrend[]> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    return mockViewTrends.slice(-days)
  },

  async getRecentArticles(limit: number = 5): Promise<Article[]> {
    await new Promise((resolve) => setTimeout(resolve, 200))
    return mockArticles
      .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
      .slice(0, limit)
  },

  async getRecentComments(limit: number = 5): Promise<Message[]> {
    await new Promise((resolve) => setTimeout(resolve, 200))
    return mockMessages
      .filter((m) => m.status === 'approved')
      .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
      .slice(0, limit)
  },
}
