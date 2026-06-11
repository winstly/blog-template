import type { Article, PaginationParams, PaginationResult } from '@/api/types'
import { mockArticles } from '@/api/mock/data'

let articles = [...mockArticles]

async function getFilteredArticles(
  params: PaginationParams & { keyword?: string; status?: string; category?: string }
): Promise<Article[]> {
  await new Promise((resolve) => setTimeout(resolve, 300))

  let result = [...articles]

  if (params.status && params.status !== 'all') {
    result = result.filter((a) => a.status === params.status)
  }

  if (params.category) {
    result = result.filter((a) => a.category === params.category)
  }

  if (params.keyword) {
    const keyword = params.keyword.toLowerCase()
    result = result.filter(
      (a) =>
        a.title.toLowerCase().includes(keyword) ||
        a.summary.toLowerCase().includes(keyword)
    )
  }

  return result
}

export const articleService = {
  async getList(
    params: PaginationParams & { keyword?: string; status?: string; category?: string }
  ): Promise<PaginationResult<Article>> {
    const allResults = await getFilteredArticles(params)
    const start = (params.page - 1) * params.pageSize
    const end = start + params.pageSize
    const list = allResults.slice(start, end)

    return {
      list,
      total: allResults.length,
      page: params.page,
      pageSize: params.pageSize,
    }
  },

  async getById(id: string): Promise<Article | null> {
    await new Promise((resolve) => setTimeout(resolve, 200))
    return articles.find((a) => a.id === id) || null
  },

  async create(data: Omit<Article, 'id'>): Promise<Article> {
    await new Promise((resolve) => setTimeout(resolve, 400))
    const newArticle: Article = {
      ...data,
      id: Date.now().toString(),
    }
    articles.unshift(newArticle)
    return newArticle
  },

  async update(id: string, data: Partial<Article>): Promise<Article> {
    await new Promise((resolve) => setTimeout(resolve, 400))
    const index = articles.findIndex((a) => a.id === id)
    if (index === -1) throw new Error('Article not found')
    articles[index] = { ...articles[index], ...data }
    return articles[index]
  },

  async delete(id: string): Promise<void> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    articles = articles.filter((a) => a.id !== id)
  },

  async batchDelete(ids: string[]): Promise<void> {
    await new Promise((resolve) => setTimeout(resolve, 400))
    articles = articles.filter((a) => !ids.includes(a.id))
  },
}
