<template>
  <div class="space-y-4">
    <div class="flex items-center justify-between">
      <h2 class="text-xl font-bold text-gray-800">留言管理</h2>
      <el-button type="primary" @click="handleCreate">发表留言</el-button>
    </div>

    <!-- SearchCard -->
    <SearchCard actions-align="right">
      <template #fields>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="状态" class="w-full !mb-0">
              <el-select v-model="filters.status" placeholder="全部" clearable class="w-full" @change="handleSearch">
                <el-option label="待审核" value="pending" />
                <el-option label="已通过" value="approved" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="关键词" class="w-full !mb-0">
              <el-input
                v-model="filters.keyword"
                placeholder="搜索留言内容"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="排序" class="w-full !mb-0">
              <el-select v-model="filters.sort" class="w-full" @change="handleSearch">
                <el-option label="最新优先" value="newest" />
                <el-option label="最早优先" value="oldest" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </template>
      <template #actions>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </template>
    </SearchCard>

    <!-- Message Tree -->
    <div v-loading="loading" class="message-list space-y-4">
      <div
        v-for="thread in displayedMessages"
        :key="thread.id"
        class="message-thread"
      >
        <!-- Root Message (第一层平铺) -->
        <div class="message-card root-message" :class="{ 'pending': thread.status === 'pending' }">
          <div class="message-header">
            <div class="user-info">
              <el-avatar :size="40" :src="thread.userAvatarUrl">
                {{ thread.userName?.charAt(0).toUpperCase() || '?' }}
              </el-avatar>
              <div class="user-meta">
                <span class="username">{{ thread.userName }}</span>
                <span class="time">{{ formatDate(thread.date) }}</span>
              </div>
            </div>
            <div class="message-actions">
              <el-tag :type="thread.status === 'approved' ? 'success' : 'warning'" size="small">
                {{ thread.status === 'approved' ? '已通过' : '待审核' }}
              </el-tag>
              <el-dropdown trigger="click">
                <el-button link type="primary">
                  <el-icon><More /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item v-if="thread.status === 'pending'" @click="handleApprove(thread)">
                      <el-icon><Check /></el-icon> 通过
                    </el-dropdown-item>
                    <el-dropdown-item @click="handleReply(thread)">
                      <el-icon><ChatDotRound /></el-icon> 回复
                    </el-dropdown-item>
                    <el-dropdown-item divided type="danger" @click="handleDelete(thread)">
                      <el-icon><Delete /></el-icon> 删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
          <div class="message-content">{{ thread.content }}</div>

          <!-- Replies (回复列表) -->
          <div v-if="thread.replies?.length" class="replies-list">
            <div
              v-for="reply in thread.replies"
              :key="reply.id"
              class="reply-item"
              :class="{ 'pending': reply.status === 'pending' }"
            >
              <!-- 回复关系：X 回复 Y -->
              <div class="reply-header">
                <div class="user-info">
                  <el-avatar :size="32" :src="reply.userAvatarUrl">
                    {{ reply.userName?.charAt(0).toUpperCase() || '?' }}
                  </el-avatar>
                  <div class="user-meta">
                    <span class="username">{{ reply.userName }}</span>
                    <span class="reply-indicator">
                      <el-icon><Right /></el-icon>
                    </span>
                    <span class="reply-target">{{ getParentUsername(reply, thread) }}</span>
                    <span class="time">{{ formatDate(reply.date) }}</span>
                  </div>
                </div>
                <div class="reply-actions">
                  <el-tag :type="reply.status === 'pending' ? 'warning' : 'success'" size="small">
                    {{ reply.status === 'pending' ? '待审核' : '已通过' }}
                  </el-tag>
                  <el-button
                    v-if="reply.status === 'pending'"
                    link
                    type="success"
                    size="small"
                    @click="handleApprove(reply)"
                  >
                    <el-icon><Check /></el-icon> 通过
                  </el-button>
                  <el-button link type="primary" size="small" @click="handleReply(reply)">
                    <el-icon><ChatDotRound /></el-icon> 回复
                  </el-button>
                  <el-button link type="danger" size="small" @click="handleDelete(reply)">
                    <el-icon><Delete /></el-icon> 删除
                  </el-button>
                </div>
              </div>

              <!-- 回复内容 -->
              <div class="reply-content">{{ reply.content }}</div>

              <!-- 引用的原内容 -->
              <div class="reply-quote">
                <div class="quote-label">引用 @{{ getParentUsername(reply, thread) }}</div>
                <div class="quote-content">{{ getParentContent(reply, thread) }}</div>
              </div>
            </div>
          </div>

          <!-- 快速回复按钮 -->
          <div v-if="!thread.replies?.length" class="quick-reply">
            <el-button link type="primary" @click="handleReply(thread)">
              <el-icon><ChatDotRound /></el-icon>
              回复这条留言
            </el-button>
          </div>
        </div>
      </div>

      <el-empty v-if="!loading && displayedMessages.length === 0" description="暂无留言" />

      <!-- 加载更多 -->
      <div v-if="loadingMore" class="text-center py-4 text-gray-500">
        <el-icon class="animate-spin"><Loading /></el-icon> 加载中...
      </div>
      <div v-else-if="noMore && displayedMessages.length > 0" class="text-center py-4 text-gray-400 text-sm">
        没有更多留言了
      </div>
      <div v-else-if="!noMore && displayedMessages.length > 0" class="text-center py-4">
        <el-button link type="primary" @click="loadMore">加载更多</el-button>
      </div>
    </div>

    <!-- Reply/Create Drawer -->
    <el-drawer
      v-model="dialogVisible"
      :title="dialogTitle"
      direction="rtl"
      size="500px"
    >
      <!-- 显示回复目标 -->
      <div v-if="currentMessage" class="reply-context mb-4">
        <div class="context-label">回复：</div>
        <div class="context-content">
          <span class="context-user">@{{ currentMessage.userName }}</span>
          <span class="context-text">{{ currentMessage.content }}</span>
        </div>
      </div>

      <!-- 表单 -->
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item v-if="!isReply" label="昵称" prop="username">
          <el-input v-model="form.username" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item v-if="!isReply" label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱（可选）" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="4"
            placeholder="输入留言内容..."
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">发送</el-button>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  More,
  Check,
  Delete,
  ChatDotRound,
  Right,
  Loading,
} from '@element-plus/icons-vue'
import SearchCard from '@/components/common/SearchCard.vue'
import { messageService } from '@/api/services/message'
import type { Message } from '@/api/types'
import type { FormInstance, FormRules } from 'element-plus'

const loading = ref(false)
const messages = ref<Message[]>([])

const filters = reactive({
  status: '' as '' | 'pending' | 'approved',
  keyword: '',
  sort: 'newest' as 'newest' | 'oldest',
})

// Waterfall pagination
const loadingMore = ref(false)
const noMore = ref(false)
const displayedMessages = ref<Message[]>([])
const pageSize = 10
let currentPage = 1

// Dialog
const dialogVisible = ref(false)
const isReply = ref(false)
const currentMessage = ref<Message | null>(null)
const formRef = ref<FormInstance>()
const form = reactive({
  username: '',
  email: '',
  content: '',
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
}

const dialogTitle = computed(() => isReply.value ? '回复留言' : '发表留言')

// 筛选并排序数据
const filteredMessages = computed(() => {
  let result = [...messages.value]

  // 状态筛选
  if (filters.status) {
    result = result.filter(m => m.status === filters.status)
  }

  // 关键词搜索
  if (filters.keyword) {
    const keyword = filters.keyword.toLowerCase()
    result = result.filter(m =>
      m.content.toLowerCase().includes(keyword) ||
      m.userName.toLowerCase().includes(keyword) ||
      m.replies?.some(r =>
        r.content.toLowerCase().includes(keyword) ||
        r.userName.toLowerCase().includes(keyword)
      )
    )
  }

  // 排序
  result.sort((a, b) => {
    const timeA = new Date(a.date).getTime()
    const timeB = new Date(b.date).getTime()
    return filters.sort === 'newest' ? timeB - timeA : timeA - timeB
  })

  return result
})

// 加载消息列表
async function loadMessages() {
  loading.value = true
  try {
    const result = await messageService.getList({
      page: 1,
      pageSize: 999,
      status: filters.status || undefined,
    })
    messages.value = result.list
  } finally {
    loading.value = false
  }
  const allData = filteredMessages.value
  displayedMessages.value = allData.slice(0, pageSize)
  currentPage = 1
  noMore.value = allData.length <= pageSize
}

// 加载更多
function loadMore() {
  if (loadingMore.value || noMore.value) return

  loadingMore.value = true
  currentPage++

  const start = (currentPage - 1) * pageSize
  const end = start + pageSize
  const newItems = filteredMessages.value.slice(start, end)

  if (newItems.length > 0) {
    displayedMessages.value.push(...newItems)
  }

  if (end >= filteredMessages.value.length) {
    noMore.value = true
  }

  loadingMore.value = false
}

function formatDate(dateStr: string): string {
  const date = new Date(dateStr)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const days = Math.floor(hours / 24)

  if (hours < 1) return '刚刚'
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  return date.toLocaleDateString('zh-CN')
}

// 获取回复目标的昵称
function getParentUsername(reply: Message, rootThread: Message): string {
  if (!reply.replyTo || String(reply.replyTo) === String(rootThread.id)) {
    return rootThread.userName
  }
  const parent = rootThread.replies?.find(r => String(r.id) === String(reply.replyTo))
  return parent?.userName || '未知用户'
}

// 获取回复目标的内容
function getParentContent(reply: Message, rootThread: Message): string {
  if (!reply.replyTo || String(reply.replyTo) === String(rootThread.id)) {
    return rootThread.content
  }
  const parent = rootThread.replies?.find(r => String(r.id) === String(reply.replyTo))
  return parent?.content || ''
}

function handleSearch() {
  loadMessages()
}

function handleReset() {
  filters.status = ''
  filters.keyword = ''
  filters.sort = 'newest'
  loadMessages()
}


function handleCreate() {
  isReply.value = false
  currentMessage.value = null
  form.username = ''
  form.email = ''
  form.content = ''
  dialogVisible.value = true
}

function handleReply(message: Message) {
  isReply.value = true
  currentMessage.value = message
  form.content = ''
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) return

  try {
    await formRef.value.validate()

    if (isReply.value && currentMessage.value) {
      await messageService.reply(currentMessage.value.id, form.content)
      ElMessage.success('回复成功')
      loadMessages()
    } else {
      // New root message via backend
      ElMessage.info('请通过网站前端发表留言')
    }

    dialogVisible.value = false
  } catch (error) {
    if (error !== false) {
      const message = error instanceof Error ? error.message : '操作失败'
      ElMessage.error(message)
    }
  }
}

async function handleApprove(message: Message) {
  try {
    await messageService.approve(message.id)
    ElMessage.success('已通过')
    loadMessages()
  } catch {
    ElMessage.error('操作失败')
  }
}

async function handleDelete(message: Message) {
  try {
    await ElMessageBox.confirm('确定删除此留言吗？', '确认删除', { type: 'warning' })
    await messageService.delete(message.id)
    loadMessages()
    ElMessage.success('删除成功')
  } catch {
    // cancelled or error
  }
}

onMounted(() => {
  loadMessages()
})
</script>

<style scoped>
.message-list {
  min-height: 200px;
}

.message-thread {
  border-radius: 8px;
  overflow: hidden;
}

.message-card {
  background: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 16px;
  transition: all 0.3s ease;
}

.message-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.message-card.pending {
  border-left: 3px solid #e6a23c;
}

.root-message {
  background: var(--color-gray-50);
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.username {
  font-weight: 600;
  color: var(--color-gray-900);
  font-size: 15px;
}

.time {
  font-size: 12px;
  color: var(--color-gray-400);
}

.message-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.message-content {
  font-size: 15px;
  color: #374151;
  line-height: 1.7;
  padding-left: 52px;
  margin-bottom: 16px;
}

.replies-list {
  margin-left: 52px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px dashed #e4e7ed;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.reply-item {
  padding: 12px;
  background: #fff;
  border-radius: 8px;
  border: 1px solid #f0f0f0;
}

.reply-item.pending {
  border-left: 2px solid #e6a23c;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.reply-header .user-info {
  gap: 8px;
}

.reply-header .user-meta {
  flex-direction: row;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
}

.reply-header .username {
  font-size: 14px;
  font-weight: 600;
  color: #409eff;
}

.reply-indicator {
  color: var(--color-gray-400);
  display: flex;
  align-items: center;
}

.reply-target {
  font-size: 13px;
  color: #6b7280;
  background: #f3f4f6;
  padding: 2px 8px;
  border-radius: 4px;
}

.reply-content {
  font-size: 14px;
  color: #374151;
  line-height: 1.6;
  padding-left: 40px;
  margin-bottom: 10px;
}

.reply-quote {
  margin-left: 40px;
  padding: 10px 12px;
  background: #f9fafb;
  border-left: 3px solid #d1d5db;
  border-radius: 0 6px 6px 0;
}

.quote-label {
  font-size: 12px;
  color: #9ca3af;
  margin-bottom: 4px;
}

.quote-content {
  font-size: 13px;
  color: #6b7280;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.reply-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.quick-reply {
  margin-left: 52px;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px dashed #e4e7ed;
}

/* Dialog Styles */
.reply-context {
  padding: 12px;
  background: #f5f5f4;
  border-radius: 8px;
}

.context-label {
  font-size: 12px;
  color: #9ca3af;
  margin-bottom: 6px;
}

.context-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.context-user {
  font-weight: 600;
  color: #409eff;
  font-size: 14px;
}

.context-text {
  font-size: 14px;
  color: #4b5563;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
