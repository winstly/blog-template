import type { DiaryEntry } from '@/types'

export const mockDiaryEntries: DiaryEntry[] = [
  {
    id: '1',
    date: '2018-10-16',
    content: '江湖路远，有缘再见。',
    year: 2018,
  },
  {
    id: '2',
    date: '2018-08-12',
    content: '花光所有运气遇见你。',
    year: 2018,
  },
  {
    id: '3',
    date: '2018-08-10',
    content: '没人在乎你怎样在深夜痛哭，也没人在乎你辗转反侧的要熬几个秋。外人只看结果，自己要独撑过程。等我们都明白了这个道理，便不会再在人前矫情，四处诉说以求宽慰。',
    year: 2018,
  },
  {
    id: '4',
    date: '2018-06-28',
    content: '当你的才华还撑不起你的野心时，那你就应该静下心来学习。',
    year: 2018,
  },
  {
    id: '5',
    date: '2018-06-26',
    content: '6.29 江湖再见',
    year: 2018,
  },
  {
    id: '6',
    date: '2018-05-24',
    content: '林花谢了春红，太匆匆，无奈朝来寒雨晚来风。胭脂泪，相留醉，几时重？自是人生长恨水长东。——李煜《相见欢》',
    year: 2018,
  },
  {
    id: '7',
    date: '2018-04-15',
    content: '人生若只如初见，何事秋风悲画扇。',
    year: 2018,
  },
  {
    id: '8',
    date: '2017-12-25',
    content: '圣诞节快乐，愿所有人都能被温柔以待。',
    year: 2017,
  },
  {
    id: '9',
    date: '2017-10-01',
    content: '国庆快乐，祝祖国繁荣昌盛。',
    year: 2017,
  },
  {
    id: '10',
    date: '2017-06-18',
    content: '父亲节，感恩父爱如山。',
    year: 2017,
  },
]

export function getDiariesByYear(): Record<number, DiaryEntry[]> {
  const grouped: Record<number, DiaryEntry[]> = {}
  for (const entry of mockDiaryEntries) {
    if (!grouped[entry.year]) {
      grouped[entry.year] = []
    }
    grouped[entry.year].push(entry)
  }
  return grouped
}

export function getYears(): number[] {
  const years = new Set(mockDiaryEntries.map((e) => e.year))
  return Array.from(years).sort((a, b) => b - a)
}
