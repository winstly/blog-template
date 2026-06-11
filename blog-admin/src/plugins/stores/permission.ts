import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import type { Permission } from '@/api/types'

// Re-export Permission type for backward compatibility
export type { Permission } from '@/api/types'

/**
 * Flatten permission tree to array of permission codes
 */
function flattenPermissions(permissions: Permission[]): string[] {
  const result: string[] = []

  function traverse(items: Permission[]) {
    for (const item of items) {
      result.push(item.code)
      if (item.children?.length) {
        traverse(item.children)
      }
    }
  }

  traverse(permissions)
  return result
}

// Mock data - permission tree
const mockPermissions: Permission[] = [
  {
    id: 'content',
    name: '内容管理',
    code: 'content',
    type: 'menu',
    description: '内容管理模块',
    isSystem: true,
    children: [
      {
        id: 'article',
        name: '文章管理',
        code: 'article',
        type: 'menu',
        description: '文章管理功能',
        parentId: 'content',
        isSystem: true,
        children: [
          { id: 'article:view', name: '文章查看', code: 'article:view', type: 'button', description: '查看文章列表和详情', parentId: 'article', isSystem: true },
          { id: 'article:create', name: '文章创建', code: 'article:create', type: 'button', description: '创建新文章', parentId: 'article', isSystem: true },
          { id: 'article:edit', name: '文章编辑', code: 'article:edit', type: 'button', description: '编辑文章', parentId: 'article', isSystem: true },
          { id: 'article:delete', name: '文章删除', code: 'article:delete', type: 'button', description: '删除文章', parentId: 'article', isSystem: true },
          { id: 'article:publish', name: '文章发布', code: 'article:publish', type: 'button', description: '发布/下架文章', parentId: 'article', isSystem: true },
        ],
      },
      {
        id: 'category',
        name: '分类管理',
        code: 'category',
        type: 'menu',
        description: '文章分类管理',
        parentId: 'content',
        isSystem: true,
        children: [
          { id: 'category:view', name: '分类查看', code: 'category:view', type: 'button', description: '查看分类列表', parentId: 'category', isSystem: true },
          { id: 'category:manage', name: '分类管理', code: 'category:manage', type: 'button', description: '创建、编辑、删除分类', parentId: 'category', isSystem: true },
        ],
      },
      {
        id: 'tag',
        name: '标签管理',
        code: 'tag',
        type: 'menu',
        description: '文章标签管理',
        parentId: 'content',
        isSystem: true,
        children: [
          { id: 'tag:view', name: '标签查看', code: 'tag:view', type: 'button', description: '查看标签列表', parentId: 'tag', isSystem: true },
          { id: 'tag:manage', name: '标签管理', code: 'tag:manage', type: 'button', description: '创建、编辑、删除标签', parentId: 'tag', isSystem: true },
        ],
      },
      {
        id: 'diary',
        name: '日记管理',
        code: 'diary',
        type: 'menu',
        description: '日记管理功能',
        parentId: 'content',
        isSystem: true,
        children: [
          { id: 'diary:view', name: '日记查看', code: 'diary:view', type: 'button', description: '查看日记列表', parentId: 'diary', isSystem: true },
          { id: 'diary:manage', name: '日记管理', code: 'diary:manage', type: 'button', description: '创建、编辑、删除日记', parentId: 'diary', isSystem: true },
        ],
      },
      {
        id: 'message',
        name: '留言管理',
        code: 'message',
        type: 'menu',
        description: '留言管理功能',
        parentId: 'content',
        isSystem: true,
        children: [
          { id: 'message:view', name: '留言查看', code: 'message:view', type: 'button', description: '查看留言列表', parentId: 'message', isSystem: true },
          { id: 'message:manage', name: '留言管理', code: 'message:manage', type: 'button', description: '审核、回复、删除留言', parentId: 'message', isSystem: true },
        ],
      },
      {
        id: 'link',
        name: '友链管理',
        code: 'link',
        type: 'menu',
        description: '友情链接管理',
        parentId: 'content',
        isSystem: true,
        children: [
          { id: 'link:view', name: '友链查看', code: 'link:view', type: 'button', description: '查看友链列表', parentId: 'link', isSystem: true },
          { id: 'link:manage', name: '友链管理', code: 'link:manage', type: 'button', description: '添加、编辑、删除友链', parentId: 'link', isSystem: true },
        ],
      },
    ],
  },
  {
    id: 'system',
    name: '系统管理',
    code: 'system',
    type: 'menu',
    description: '系统管理模块',
    isSystem: true,
    children: [
      {
        id: 'dict',
        name: '字典管理',
        code: 'dict',
        type: 'menu',
        description: '数据字典管理',
        parentId: 'system',
        isSystem: true,
        children: [
          { id: 'dict:view', name: '字典查看', code: 'dict:view', type: 'button', description: '查看字典列表', parentId: 'dict', isSystem: true },
          { id: 'dict:manage', name: '字典管理', code: 'dict:manage', type: 'button', description: '创建、编辑、删除字典', parentId: 'dict', isSystem: true },
        ],
      },
      {
        id: 'user',
        name: '用户管理',
        code: 'user',
        type: 'menu',
        description: '系统用户管理',
        parentId: 'system',
        isSystem: true,
        children: [
          { id: 'user:view', name: '用户查看', code: 'user:view', type: 'button', description: '查看用户列表', parentId: 'user', isSystem: true },
          { id: 'user:manage', name: '用户管理', code: 'user:manage', type: 'button', description: '创建、编辑、删除用户', parentId: 'user', isSystem: true },
          { id: 'user:resetPwd', name: '重置密码', code: 'user:resetpwd', type: 'button', description: '重置用户密码', parentId: 'user', isSystem: true },
        ],
      },
      {
        id: 'role',
        name: '角色管理',
        code: 'role',
        type: 'menu',
        description: '角色权限管理',
        parentId: 'system',
        isSystem: true,
        children: [
          { id: 'role:view', name: '角色查看', code: 'role:view', type: 'button', description: '查看角色列表', parentId: 'role', isSystem: true },
          { id: 'role:manage', name: '角色管理', code: 'role:manage', type: 'button', description: '创建、编辑、删除角色', parentId: 'role', isSystem: true },
          { id: 'role:assign', name: '权限分配', code: 'role:assign', type: 'button', description: '为角色分配权限', parentId: 'role', isSystem: true },
        ],
      },
      {
        id: 'permission',
        name: '权限管理',
        code: 'permission',
        type: 'menu',
        description: '权限点管理',
        parentId: 'system',
        isSystem: true,
        children: [
          { id: 'permission:view', name: '权限查看', code: 'permission:view', type: 'button', description: '查看权限列表', parentId: 'permission', isSystem: true },
          { id: 'permission:manage', name: '权限管理', code: 'permission:manage', type: 'button', description: '创建、编辑、删除权限', parentId: 'permission', isSystem: true },
        ],
      },
    ],
  },
]

export const usePermissionStore = defineStore('permission', () => {
  const permissions = ref<Permission[]>([...mockPermissions])

  // User's assigned permission codes
  const userPermissions = ref<Set<string>>(new Set())

  // Computed: all available permission codes (flattened)
  const allPermissionCodes = computed(() => flattenPermissions(permissions.value))

  function getPermissionTree(): Permission[] {
    return permissions.value
  }

  function updatePermissions(newPermissions: Permission[]) {
    permissions.value = newPermissions
  }

  /**
   * Set user's permissions from array of permission codes
   */
  function setUserPermissions(codes: string[]) {
    userPermissions.value = new Set(codes)
  }

  /**
   * Clear user's permissions (on logout)
   */
  function clearUserPermissions() {
    userPermissions.value = new Set()
  }

  /**
   * Check if user has a specific permission
   */
  function hasPermission(code: string): boolean {
    return userPermissions.value.has(code)
  }

  /**
   * Check if user has all specified permissions
   */
  function hasAllPermissions(codes: string[]): boolean {
    return codes.every(code => userPermissions.value.has(code))
  }

  /**
   * Check if user has any of the specified permissions
   */
  function hasAnyPermission(codes: string[]): boolean {
    return codes.some(code => userPermissions.value.has(code))
  }

  return {
    permissions,
    userPermissions,
    allPermissionCodes,
    getPermissionTree,
    updatePermissions,
    setUserPermissions,
    clearUserPermissions,
    hasPermission,
    hasAllPermissions,
    hasAnyPermission,
  }
})
