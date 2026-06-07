import type { FriendLink } from '@/api/types'
import { mockLinks } from '@/api/mock/data'

const STORAGE_KEY = 'blog_admin_links'

function loadLinks(): FriendLink[] {
  try {
    const stored = localStorage.getItem(STORAGE_KEY)
    if (stored) return JSON.parse(stored)
  } catch {
    // ignore
  }
  return [...mockLinks]
}

function saveLinks(data: FriendLink[]): void {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(data))
  } catch {
    // ignore
  }
}

let links = loadLinks()

export const linkService = {
  async getList(): Promise<FriendLink[]> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    return [...links].sort((a, b) => a.order - b.order)
  },

  async getAll(): Promise<FriendLink[]> {
    return this.getList()
  },

  async create(data: Omit<FriendLink, 'id' | 'order'>): Promise<FriendLink> {
    await new Promise((resolve) => setTimeout(resolve, 400))
    const maxOrder = Math.max(...links.map((l) => l.order), 0)
    const newLink: FriendLink = {
      ...data,
      id: Date.now().toString(),
      order: maxOrder + 1,
    }
    links.push(newLink)
    saveLinks(links)
    return newLink
  },

  async update(id: string, data: Partial<FriendLink>): Promise<FriendLink> {
    await new Promise((resolve) => setTimeout(resolve, 400))
    const index = links.findIndex((l) => l.id === id)
    if (index === -1) throw new Error('链接不存在')
    links[index] = { ...links[index], ...data }
    saveLinks(links)
    return links[index]
  },

  async delete(id: string): Promise<void> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    links = links.filter((l) => l.id !== id)
    saveLinks(links)
  },

  async reorderSingle(id: string, direction: -1 | 1): Promise<void> {
    await new Promise((resolve) => setTimeout(resolve, 400))
    const sortedLinks = [...links].sort((a, b) => a.order - b.order)
    const index = sortedLinks.findIndex((l) => l.id === id)
    if (index === -1) return

    const newIndex = index + direction
    if (newIndex < 0 || newIndex >= sortedLinks.length) return

    const temp = sortedLinks[index].order
    sortedLinks[index].order = sortedLinks[newIndex].order
    sortedLinks[newIndex].order = temp

    links = sortedLinks
    saveLinks(links)
  },

  reset(): void {
    links = [...mockLinks]
    localStorage.removeItem(STORAGE_KEY)
  },
}
