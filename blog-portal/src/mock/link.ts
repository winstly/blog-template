import type { FriendLink } from '@/types'

export const mockFriendLinks: FriendLink[] = [
  {
    id: '1',
    name: '燕十三',
    url: 'https://www.yanshisan.cn',
    logo: 'https://www.yanshisan.cn/logo.png',
    description: '燕十三·一个人的江湖',
  },
  {
    id: '2',
    name: '阮一峰的网络日志',
    url: 'https://www.ruanyifeng.com/blog/',
    logo: 'https://www.ruanyifeng.com/favicon.ico',
    description: '科技爱好者周刊',
  },
  {
    id: '3',
    name: '张鑫旭博客',
    url: 'https://www.zhangxinxu.com/',
    logo: 'https://www.zhangxinxu.com/favicon.ico',
    description: '前端开发博客',
  },
  {
    id: '4',
    name: '廖雪峰的官方网站',
    url: 'https://www.liaoxuefeng.com/',
    logo: 'https://www.liaoxuefeng.com/favicon.ico',
    description: 'Git/Python 教程',
  },
  {
    id: '5',
    name: 'SegmentFault',
    url: 'https://segmentfault.com/',
    logo: 'https://segmentfault.com/favicon.ico',
    description: '技术问答社区',
  },
  {
    id: '6',
    name: '掘金',
    url: 'https://juejin.cn/',
    logo: 'https://juejin.cn/favicon.ico',
    description: '开发者社区',
  },
]

export const siteInfo = {
  name: '燕十三',
  url: 'https://www.yanshisan.cn',
  logo: 'https://www.yanshisan.cn/logo.png',
  description: '燕十三·一个人的江湖',
}

export const linkRequirements = {
  rejected: ['经常宕机', '不合法规', '插边球站', '红标报毒'],
  preferred: ['原创优先', '技术优先'],
  note: '交换友链可在留言板留言。请将本站加入友链。申请提交后若无其它原因将在24小时内审核，如超过时间还未通过，请私信我。',
}
