/**
 * 日期格式化工具函数
 */

/**
 * 格式化日期为本地字符串
 * @param dateStr - 日期字符串
 * @returns 格式化后的日期字符串
 */
export function formatDate(dateStr: string): string {
  return new Date(dateStr).toLocaleString('zh-CN')
}

/**
 * 格式化日期为相对时间（如：刚刚、5分钟前、昨天等）
 * @param dateStr - 日期字符串
 * @returns 相对时间字符串
 */
export function formatRelativeDate(dateStr: string): string {
  const date = new Date(dateStr)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))

  if (days === 0) {
    const hours = Math.floor(diff / (1000 * 60 * 60))
    if (hours === 0) {
      const minutes = Math.floor(diff / (1000 * 60))
      return minutes === 0 ? '刚刚' : `${minutes}分钟前`
    }
    return `${hours}小时前`
  }
  if (days === 1) return '昨天'
  if (days < 7) return `${days}天前`

  return date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' })
}
