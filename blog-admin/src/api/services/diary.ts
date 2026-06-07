import type { DiaryEntry } from '@/api/types'
import { mockDiaries } from '@/api/mock/data'

const STORAGE_KEY = 'blog_admin_diaries'

function loadDiaries(): DiaryEntry[] {
  try {
    const stored = localStorage.getItem(STORAGE_KEY)
    if (stored) return JSON.parse(stored)
  } catch {
    // ignore
  }
  return [...mockDiaries]
}

function saveDiaries(data: DiaryEntry[]): void {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(data))
  } catch {
    // ignore
  }
}

let diaries = loadDiaries()

interface DiaryListParams {
  page?: number
  pageSize?: number
  year?: number
}

export const diaryService = {
  async getList(params?: DiaryListParams): Promise<DiaryEntry[]> {
    await new Promise((resolve) => setTimeout(resolve, 300))

    let result = [...diaries]

    if (params?.year) {
      result = result.filter((d) => d.year === params.year)
    }

    result.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())

    if (params?.page && params?.pageSize) {
      const start = (params.page - 1) * params.pageSize
      const end = start + params.pageSize
      result = result.slice(start, end)
    }

    return result
  },

  async getYears(): Promise<number[]> {
    await new Promise((resolve) => setTimeout(resolve, 200))
    const years = [...new Set(diaries.map((d) => d.year))]
    return years.sort((a, b) => b - a)
  },

  async create(data: Omit<DiaryEntry, 'id' | 'year'>): Promise<DiaryEntry> {
    await new Promise((resolve) => setTimeout(resolve, 400))
    const year = new Date(data.date).getFullYear()
    const newDiary: DiaryEntry = {
      ...data,
      id: Date.now().toString(),
      year,
    }
    diaries.unshift(newDiary)
    saveDiaries(diaries)
    return newDiary
  },

  async update(id: string, data: Partial<DiaryEntry>): Promise<DiaryEntry> {
    await new Promise((resolve) => setTimeout(resolve, 400))
    const index = diaries.findIndex((d) => d.id === id)
    if (index === -1) throw new Error('日记不存在')

    const year = data.date ? new Date(data.date).getFullYear() : diaries[index].year
    diaries[index] = { ...diaries[index], ...data, year }
    saveDiaries(diaries)
    return diaries[index]
  },

  async delete(id: string): Promise<void> {
    await new Promise((resolve) => setTimeout(resolve, 300))
    diaries = diaries.filter((d) => d.id !== id)
    saveDiaries(diaries)
  },

  reset(): void {
    diaries = [...mockDiaries]
    localStorage.removeItem(STORAGE_KEY)
  },
}
