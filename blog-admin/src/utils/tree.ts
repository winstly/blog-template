/**
 * 树形结构工具函数
 * 用于将扁平化的数据转换为树形结构
 */

/**
 * 树节点接口
 */
export interface TreeNode {
  id: string
  parentId?: string
  children?: TreeNode[]
  [key: string]: any
}

/**
 * 构建树形结构
 * 将扁平化的数据转换为树形结构
 *
 * @param items - 扁平化的数据数组
 * @param options - 配置选项
 * @returns 树形结构数组
 *
 * @example
 * const tree = buildTree(categories)
 * const treeWithCustomKey = buildTree(items, { idKey: 'value', parentKey: 'parentValue' })
 */
export function buildTree<T extends TreeNode>(
  items: T[],
  options: {
    /** id字段名，默认为'id' */
    idKey?: keyof T
    /** parentId字段名，默认为'parentId' */
    parentKey?: keyof T
    /** 是否对结果进行排序 */
    sortBy?: keyof T
  } = {}
): T[] {
  const { idKey = 'id', parentKey = 'parentId', sortBy } = options

  const map = new Map<string, T>()
  const roots: T[] = []

  // 首先创建所有节点的映射
  items.forEach((item) => {
    const node = { ...item, children: [] as T[] }
    map.set(String(item[idKey]), node)
  })

  // 然后构建树形关系
  items.forEach((item) => {
    const node = map.get(String(item[idKey]))!
    const parentId = item[parentKey]

    if (parentId && map.has(String(parentId))) {
      const parent = map.get(String(parentId))!
      if (!parent.children) {
        parent.children = []
      }
      parent.children.push(node)
    } else {
      roots.push(node)
    }
  })

  // 可选：按指定字段排序
  if (sortBy) {
    const sortNodes = (nodes: T[]): T[] => {
      return nodes
        .sort((a, b) => {
          const aVal = (a[sortBy] as number) || 0
          const bVal = (b[sortBy] as number) || 0
          return aVal - bVal
        })
        .map((node) => ({
          ...node,
          children: node.children ? sortNodes(node.children as T[]) : [],
        }))
    }
    return sortNodes(roots)
  }

  return roots
}
