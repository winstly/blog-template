import type { Tag } from '@/api/types'
import { mockTags, mockArticles } from '@/api/mock/data'

const STORAGE_KEY = 'blog_admin_tags'

function loadTags(): Tag[] {
  try {
    const stored = localStorage.getItem(STORAGE_KEY)
    if (stored) return JSON.parse(stored)
  } catch {
    // ignore
  }
  return [...mockTags]
}

function saveTags(data: Tag[]): void {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(data))
  } catch {
    // ignore
  }
}

let tags = loadTags()

export const tagService = {
  async getList(): Promise<Tag[]> {
    await new Promise((resolve) => setTimeout(resolve, 200))
    const counts = mockArticles.reduce((acc, article) => {
      article.tags.forEach((tag) => {
        acc[tag] = (acc[tag] || 0) + 1
      })
      return acc
    }, {} as Record<string, number>)

    return tags.map((t) => ({
      ...t,
      count: counts[t.name] || 0,
    }))
  },

  async getAll(): Promise<Tag[]> {
    return this.getList()
  },

  async create(data: { name: string }): Promise<Tag> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    const existing = tags.find((t) => t.name === data.name)
    if (existing) throw new Error('标签名称已存在')

    const newTag: Tag = {
      id: Date.now().toString(),
      name: data.name,
      count: 0,
    }
    tags.push(newTag)
    saveTags(tags)
    return newTag
  },

  async update(id: string, data: { name: string }): Promise<Tag> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    const index = tags.findIndex((t) => t.id === id)
    if (index === -1) throw new Error('标签不存在')

    const oldName = tags[index].name
    tags[index] = { ...tags[index], name: data.name }
    saveTags(tags)

    // Update articles with the new tag name
    mockArticles.forEach((article) => {
      const tagIndex = article.tags.indexOf(oldName)
      if (tagIndex !== -1) {
        article.tags[tagIndex] = data.name
      }
    })

    return tags[index]
  },

  async delete(id: string): Promise<void> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    const tag = tags.find((t) => t.id === id)
    if (!tag) throw new Error('标签不存在')

    mockArticles.forEach((article) => {
      article.tags = article.tags.filter((t) => t !== tag.name)
    })

    tags = tags.filter((t) => t.id !== id)
    saveTags(tags)
  },

  async merge(sourceId: string, targetId: string): Promise<void> {
    return this.mergeMultiple([sourceId], targetId)
  },

  async mergeMultiple(sourceIds: string[], targetId: string): Promise<void> {
    await new Promise((resolve) => setTimeout(resolve, 400))
    const targetTag = tags.find((t) => t.id === targetId)
    if (!targetTag) throw new Error('目标标签不存在')

    const sourceTags = tags.filter((t) => sourceIds.includes(t.id))

    mockArticles.forEach((article) => {
      sourceTags.forEach((sourceTag) => {
        const index = article.tags.indexOf(sourceTag.name)
        if (index !== -1) {
          article.tags[index] = targetTag.name
        }
      })
    })

    tags = tags.filter((t) => !sourceIds.includes(t.id))
    saveTags(tags)
  },

  reset(): void {
    tags = [...mockTags]
    localStorage.removeItem(STORAGE_KEY)
  },
}
