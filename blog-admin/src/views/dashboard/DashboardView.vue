<script setup lang="ts">
import { ref, onMounted } from 'vue'
import {
  Document,
  View,
  ChatDotRound,
  Link,
  Edit,
  Message,
  Folder,
  CollectionTag,
  TrendCharts,
  Clock,
  ArrowDown,
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { dashboardService } from '@/api/services/dashboard'
import { formatRelativeDate } from '@/utils/date'
import type { Article, Message as MessageType, Statistics, ViewTrend } from '@/api/types'

// Import dashboard components
import StatCard from './components/StatCard.vue'
import TrafficChart from './components/TrafficChart.vue'
import CategoryPieChart from './components/CategoryPieChart.vue'
import RecentArticles from './components/RecentArticles.vue'

const statistics = ref<Statistics>({
  totalArticles: 0,
  totalViews: 0,
  totalComments: 0,
  totalLinks: 0,
})

const trendDays = ref<7 | 30>(7)
const viewTrends = ref<ViewTrend[]>([])
const recentArticles = ref<Article[]>([])
const recentComments = ref<MessageType[]>([])
const loading = ref(true)

const quickActions = [
  { label: '写文章', path: '/articles/create', type: 'primary' as const, icon: Edit },
  { label: '查看留言', path: '/messages', type: 'success' as const, icon: Message },
  { label: '管理分类', path: '/categories', type: 'warning' as const, icon: Folder },
  { label: '管理标签', path: '/tags', type: 'info' as const, icon: CollectionTag },
]

type StatKey = keyof Statistics

interface StatCardConfig {
  key: StatKey
  title: string
  icon: typeof Document
  color: string
  bgColor: string
}

const statCards: StatCardConfig[] = [
  { key: 'totalArticles', title: '总文章数', icon: Document, color: '#3b82f6', bgColor: 'rgba(59, 130, 246, 0.1)' },
  { key: 'totalViews', title: '总浏览量', icon: View, color: '#0ea5e9', bgColor: 'rgba(14, 165, 233, 0.1)' },
  { key: 'totalComments', title: '总评论数', icon: ChatDotRound, color: '#8b5cf6', bgColor: 'rgba(139, 92, 246, 0.1)' },
  { key: 'totalLinks', title: '友情链接', icon: Link, color: '#f59e0b', bgColor: 'rgba(245, 158, 11, 0.1)' },
]

// Category distribution data for pie chart
const categoryDistribution = ref<{ name: string; value: number }[]>([
  { name: '技术分享', value: 35 },
  { name: '生活随笔', value: 20 },
  { name: '项目经验', value: 15 },
  { name: '学习笔记', value: 12 },
  { name: '其他', value: 8 },
])

async function loadStatistics() {
  try {
    statistics.value = await dashboardService.getStatistics()
  } catch {
    ElMessage.error('加载统计数据失败')
  }
}

async function loadViewTrends() {
  try {
    viewTrends.value = await dashboardService.getViewTrends(trendDays.value)
  } catch {
    ElMessage.error('加载访问趋势失败')
  }
}

async function loadRecentArticles() {
  try {
    recentArticles.value = await dashboardService.getRecentArticles(5)
  } catch {
    ElMessage.error('加载最近文章失败')
  }
}

async function loadRecentComments() {
  try {
    recentComments.value = await dashboardService.getRecentComments(5)
  } catch {
    ElMessage.error('加载最近留言失败')
  }
}

async function loadAllData() {
  loading.value = true
  await Promise.all([
    loadStatistics(),
    loadViewTrends(),
    loadRecentArticles(),
    loadRecentComments(),
  ])
  loading.value = false
}

onMounted(() => {
  loadAllData()
})
</script>

<template>
  <div class="dashboard">
    <!-- Statistics Cards -->
    <section class="stats-section">
      <div class="stats-grid">
        <StatCard
          v-for="(card, index) in statCards"
          :key="card.key"
          :icon="card.icon"
          :value="statistics[card.key] || 0"
          :title="card.title"
          :color="card.color"
          :bg-color="card.bgColor"
          :trend="12"
          trend-type="up"
          :style="{ animationDelay: `${index * 0.05}s` }"
        />
      </div>
    </section>

    <!-- Charts and Quick Actions -->
    <section class="main-section">
      <div class="chart-card">
        <div class="card-header">
          <div class="header-left">
            <div class="header-icon">
              <TrendCharts />
            </div>
            <div>
              <h3 class="header-title">访问趋势</h3>
              <p class="header-subtitle">过去 {{ trendDays }} 天的访问量统计</p>
            </div>
          </div>
          <el-radio-group v-model="trendDays" size="small" aria-label="选择时间范围" @change="loadViewTrends">
            <el-radio-button :value="7">近7天</el-radio-button>
            <el-radio-button :value="30">近30天</el-radio-button>
          </el-radio-group>
        </div>
        <TrafficChart :data="viewTrends" />
      </div>

      <div class="quick-actions-card">
        <div class="card-header compact">
          <h3 class="header-title">快捷操作</h3>
        </div>
        <div class="actions-grid">
          <router-link
            v-for="action in quickActions"
            :key="action.path"
            :to="action.path"
            class="action-item"
          >
            <div class="action-icon">
              <el-icon :size="20">
                <component :is="action.icon" />
              </el-icon>
            </div>
            <span class="action-label">{{ action.label }}</span>
            <el-icon class="action-arrow" :size="14"><ArrowDown /></el-icon>
          </router-link>
        </div>

        <div class="recent-activity">
          <h4 class="activity-title">
            <el-icon :size="14"><Clock /></el-icon>
            <span>最近动态</span>
          </h4>
          <div class="activity-list">
            <div class="activity-item">
              <span class="activity-dot" style="background: #3b82f6;" />
              <span class="activity-text">发布了新文章《Vue3 最佳实践》</span>
              <span class="activity-time">2小时前</span>
            </div>
            <div class="activity-item">
              <span class="activity-dot" style="background: #0ea5e9;" />
              <span class="activity-text">收到了新的留言</span>
              <span class="activity-time">5小时前</span>
            </div>
            <div class="activity-item">
              <span class="activity-dot" style="background: #f59e0b;" />
              <span class="activity-text">更新了分类设置</span>
              <span class="activity-time">1天前</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Charts Row: Category Distribution -->
    <section class="charts-row">
      <div class="list-card">
        <div class="card-header">
          <div class="header-left">
            <div class="header-icon" style="background: rgba(139, 92, 246, 0.1); color: #8b5cf6;">
              <CollectionTag />
            </div>
            <h3 class="header-title">分类分布</h3>
          </div>
        </div>
        <CategoryPieChart :data="categoryDistribution" />
      </div>
    </section>

    <!-- Recent Lists -->
    <section class="lists-section">
      <!-- Recent Articles -->
      <div class="list-card">
        <div class="card-header">
          <div class="header-left">
            <div class="header-icon" style="background: rgba(59, 130, 246, 0.1); color: #3b82f6;">
              <Document />
            </div>
            <h3 class="header-title">最新文章</h3>
          </div>
          <router-link to="/articles" class="view-all-link">
            查看全部
            <el-icon><ArrowDown /></el-icon>
          </router-link>
        </div>
        <RecentArticles :articles="recentArticles" />
      </div>

      <!-- Recent Comments -->
      <div class="list-card">
        <div class="card-header">
          <div class="header-left">
            <div class="header-icon" style="background: rgba(139, 92, 246, 0.1); color: #8b5cf6;">
              <ChatDotRound />
            </div>
            <h3 class="header-title">最新留言</h3>
          </div>
          <router-link to="/messages" class="view-all-link">
            查看全部
            <el-icon><ArrowDown /></el-icon>
          </router-link>
        </div>
        <div class="list-content">
          <div
            v-for="(message, index) in recentComments"
            :key="message.id"
            class="comment-item"
            :style="{ animationDelay: `${index * 0.05}s` }"
          >
            <el-avatar :size="36" :src="message.avatar" class="comment-avatar">
              {{ message.username.charAt(0).toUpperCase() }}
            </el-avatar>
            <div class="comment-content">
              <div class="comment-header">
                <span class="comment-author">{{ message.username }}</span>
                <span class="comment-time">{{ formatRelativeDate(message.date) }}</span>
              </div>
              <p class="comment-text">{{ message.content }}</p>
            </div>
          </div>
          <el-empty v-if="recentComments.length === 0" description="暂无留言" />
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* Statistics Section */
.stats-section {
  animation: fadeInUp 0.5s ease-out;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

/* Main Section */
.main-section {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
  animation: fadeInUp 0.5s ease-out 0.1s backwards;
}

.chart-card,
.quick-actions-card,
.list-card {
  background: white;
  border-radius: 14px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  border: 1px solid var(--color-border-light);
  overflow: hidden;
}

.chart-card {
  padding: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.card-header.compact {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--color-gray-100);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-icon {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-gray-900);
  margin: 0;
}

.header-subtitle {
  font-size: 12px;
  color: var(--color-gray-400);
  margin: 2px 0 0;
}

/* Quick Actions */
.quick-actions-card {
  padding: 20px;
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  margin-bottom: 24px;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px;
  background: var(--color-gray-50);
  border-radius: 12px;
  text-decoration: none;
  color: var(--color-gray-700);
  font-size: 13px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.action-item:hover {
  background: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transform: translateY(-1px);
}

.action-icon {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  background: rgba(59, 130, 246, 0.08);
  color: var(--color-primary);
}

.action-label {
  flex: 1;
  position: relative;
  z-index: 1;
}

.action-arrow {
  color: var(--color-gray-300);
  transform: rotate(-90deg);
  transition: all 0.2s ease;
}

.action-item:hover .action-arrow {
  color: #409eff;
  transform: rotate(-90deg) translateX(2px);
}

/* Recent Activity */
.activity-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  font-weight: 600;
  color: var(--color-gray-700);
  margin: 0 0 12px;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 12px;
  padding: 8px;
  border-radius: 8px;
  background: var(--color-gray-50);
}

.activity-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  flex-shrink: 0;
}

.activity-text {
  flex: 1;
  color: var(--color-gray-600);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.activity-time {
  color: var(--color-gray-400);
  font-size: 11px;
  flex-shrink: 0;
}

/* Charts Row */
.charts-row {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
  animation: fadeInUp 0.5s ease-out 0.15s backwards;
}

/* Lists Section */
.lists-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  animation: fadeInUp 0.5s ease-out 0.2s backwards;
}

.list-card {
  padding: 20px;
}

.view-all-link {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--color-primary);
  text-decoration: none;
  font-weight: 500;
  transition: all 0.2s ease;
}

.view-all-link:hover {
  color: var(--color-primary-dark);
  gap: 6px;
}

.view-all-link .el-icon {
  transform: rotate(-90deg);
  font-size: 12px;
}

.list-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

/* Comment Item */
.comment-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 14px;
  background: var(--color-gray-50);
  border-radius: 10px;
  animation: fadeInUp 0.4s ease-out backwards;
}

.comment-avatar {
  flex-shrink: 0;
  border: 2px solid white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.comment-content {
  flex: 1;
  min-width: 0;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.comment-author {
  font-size: 13px;
  font-weight: 600;
  color: var(--color-gray-900);
}

.comment-time {
  font-size: 11px;
  color: var(--color-gray-400);
}

.comment-text {
  font-size: 13px;
  color: var(--color-gray-600);
  margin: 0;
  line-height: 1.5;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Animations */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(12px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Responsive */
@media (max-width: 1280px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .main-section {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .lists-section {
    grid-template-columns: 1fr;
  }

  .actions-grid {
    grid-template-columns: 1fr;
  }
}
</style>
