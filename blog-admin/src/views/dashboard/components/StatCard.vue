<script setup lang="ts">
import { computed } from 'vue'
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue'

interface Props {
  icon: any
  value: number
  title: string
  trend?: number
  trendType?: 'up' | 'down'
  color?: string
  bgColor?: string
}

const props = withDefaults(defineProps<Props>(), {
  trend: 0,
  trendType: 'up',
  color: '#3b82f6',
  bgColor: 'rgba(59, 130, 246, 0.1)',
})

function formatNumber(num: number): string {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万'
  }
  return num.toLocaleString()
}

const trendClass = computed(() =>
  props.trendType === 'up' ? 'trend-up' : 'trend-down'
)

const trendColor = computed(() =>
  props.trendType === 'up' ? '#22c55e' : '#ef4444'
)
</script>

<template>
  <div class="stat-card">
    <div class="stat-icon-wrapper" :style="{ background: bgColor }">
      <el-icon :size="22" :style="{ color: color }">
        <component :is="icon" />
      </el-icon>
    </div>
    <div class="stat-content">
      <p class="stat-title">{{ title }}</p>
      <p class="stat-value" :style="{ color: color }">
        {{ formatNumber(value) }}
      </p>
    </div>
    <div
      v-if="trend !== 0"
      class="stat-trend"
      :style="{ color: trendColor, background: trendType === 'up' ? 'rgba(34, 197, 94, 0.1)' : 'rgba(239, 68, 68, 0.1)' }"
    >
      <el-icon :size="12" :class="trendClass">
        <ArrowUp v-if="trendType === 'up'" />
        <ArrowDown v-else />
      </el-icon>
      <span>{{ Math.abs(trend) }}%</span>
    </div>
  </div>
</template>

<style scoped>
.stat-card {
  background: white;
  border-radius: 14px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 14px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  border: 1px solid var(--color-border-light);
  position: relative;
  overflow: hidden;
  animation: fadeInUp 0.5s ease-out backwards;
  transition: all 0.25s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.06);
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 100px;
  height: 100px;
  background: radial-gradient(circle at top right, rgba(59, 130, 246, 0.05) 0%, transparent 70%);
  pointer-events: none;
}

.stat-icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
}

.stat-title {
  font-size: 13px;
  color: var(--color-gray-400);
  margin: 0 0 4px;
  font-weight: 500;
}

.stat-value {
  font-family: var(--font-display);
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  line-height: 1.2;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 2px;
  font-size: 12px;
  font-weight: 600;
  padding: 4px 8px;
  border-radius: 20px;
}

.trend-up {
  transform: rotate(-45deg);
}

.trend-down {
  transform: rotate(45deg);
}

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
</style>
