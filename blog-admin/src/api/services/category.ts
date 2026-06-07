import type { Category } from '@/api/types'
import { mockCategories, mockArticles } from '@/api/mock/data'

const STORAGE_KEY = 'blog_admin_categories'

// 从 localStorage 加载或使用初始 mock 数据
function loadCategories(): Category[] {
  try {
    const stored = localStorage.getItem(STORAGE_KEY)
    if (stored) {
      return JSON.parse(stored)
    }
  } catch {
    // ignore parse errors
  }
  return [...mockCategories]
}

// 保存到 localStorage
function saveCategories(data: Category[]): void {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(data))
  } catch {
    // ignore storage errors
  }
}

let categories = loadCategories()

export const categoryService = {
  async getList(): Promise<Category[]> {
    await new Promise((resolve) => setTimeout(resolve, 200))
    // Update counts from articles
    const counts = mockArticles.reduce((acc, article) => {
      acc[article.category] = (acc[article.category] || 0) + 1
      return acc
    }, {} as Record<string, number>)

    return categories.map((c) => ({
      ...c,
      count: counts[c.name] || 0,
    }))
  },

  async getAll(): Promise<Category[]> {
    return this.getList()
  },

  async create(data: { name: string; parentId?: string; sort?: number }): Promise<Category> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    const existing = categories.find((c) => c.name === data.name)
    if (existing) throw new Error('分类名称已存在')

    const newCategory: Category = {
      id: Date.now().toString(),
      name: data.name,
      count: 0,
      parentId: data.parentId,
      sort: data.sort || 0,
    }
    categories.push(newCategory)
    saveCategories(categories)
    return newCategory
  },

  async update(id: string, data: { name?: string; parentId?: string; sort?: number }): Promise<Category> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    const index = categories.findIndex((c) => c.id === id)
    if (index === -1) throw new Error('分类不存在')

    if (data.name) {
      const existing = categories.find((c) => c.name === data.name && c.id !== id)
      if (existing) throw new Error('分类名称已存在')
    }

    categories[index] = { ...categories[index], ...data }
    saveCategories(categories)
    return categories[index]
  },

  async delete(id: string): Promise<void> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    const category = categories.find((c) => c.id === id)
    if (!category) throw new Error('分类不存在')

    // Check if category has articles
    const hasArticles = mockArticles.some((a) => a.category === category.name)
    if (hasArticles) throw new Error('该分类下有文章，无法删除')

    categories = categories.filter((c) => c.id !== id)
    saveCategories(categories)
  },

  // 重置为初始 mock 数据（用于开发测试）
  reset(): void {
    categories = [...mockCategories]
    localStorage.removeItem(STORAGE_KEY)
  },
}
