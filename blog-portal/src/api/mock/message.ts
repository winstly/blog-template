import type { Message } from '../types'

export const mockMessages: Message[] = [
  {
    id: '1',
    author: '燕十三',
    avatar: 'https://thirdqq.qlogo.cn/qqapp/101465933/7627F745B95BFAC18C6C481FE72B4EB1/100',
    content: '剑气纵横三万里，一剑光寒十九洲。',
    location: '四川成都',
    date: '2018-01-01 12:30',
    replies: [
      {
        id: '2',
        author: '燕十四',
        avatar: 'https://thirdqq.qlogo.cn/qqapp/101465933/7627F745B95BFAC18C6C481FE72B4EB1/100',
        content: '好诗好诗！',
        location: '四川',
        date: '2018-01-02 09:15',
        replyTo: '燕十三',
      },
    ],
  },
  {
    id: '3',
    author: '路人甲',
    avatar: 'https://picsum.photos/100/100?random=1',
    content: '博客做得不错，学习了！',
    location: '北京',
    date: '2018-02-15 16:42',
  },
  {
    id: '4',
    author: '程序员小王',
    avatar: 'https://picsum.photos/100/100?random=2',
    content: '请问博主用的什么主题？很漂亮！',
    location: '上海',
    date: '2018-03-20 08:30',
    replies: [
      {
        id: '5',
        author: '燕十三',
        avatar: 'https://thirdqq.qlogo.cn/qqapp/101465933/7627F745B95BFAC18C6C481FE72B4EB1/100',
        content: '自己写的主题，喜欢就好~',
        location: '四川成都',
        date: '2018-03-21 10:20',
        replyTo: '程序员小王',
      },
    ],
  },
  {
    id: '6',
    author: '游客',
    avatar: 'https://picsum.photos/100/100?random=3',
    content: '路过踩踩，加油！',
    location: '广东',
    date: '2018-05-10 22:18',
  },
]
