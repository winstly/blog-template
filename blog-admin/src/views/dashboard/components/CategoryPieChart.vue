<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import * as echarts from 'echarts/core'
import { PieChart } from 'echarts/charts'
import {
  GridComponent,
  TooltipComponent,
  LegendComponent,
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import type { EChartsOption } from 'echarts/types/dist/shared'

// Register ECharts components
echarts.use([PieChart, GridComponent, TooltipComponent, LegendComponent, CanvasRenderer])

interface CategoryItem {
  name: string
  value: number
}

interface Props {
  data: CategoryItem[]
}

const props = defineProps<Props>()

const chartRef = ref<HTMLElement>()
let chart: echarts.ECharts | null = null

function debounce<T extends (...args: unknown[]) => void>(fn: T, delay: number): (...args: Parameters<T>) => void {
  let timeoutId: number | null = null
  return (...args: Parameters<T>) => {
    if (timeoutId) clearTimeout(timeoutId)
    timeoutId = window.setTimeout(() => fn(...args), delay)
  }
}

function initChart() {
  if (!chartRef.value) return

  chart = echarts.init(chartRef.value)
  updateChart()

  window.addEventListener('resize', debouncedResize)
}

function updateChart() {
  if (!chart) return

  const option: EChartsOption = {
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#e7e5e4',
      borderWidth: 1,
      padding: [12, 16],
      textStyle: {
        color: '#44403c',
        fontSize: 13,
      },
      extraCssText: 'box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08); border-radius: 10px;',
      formatter: '{b}: {c} ({d}%)',
    },
    legend: {
      orient: 'vertical',
      right: '5%',
      top: 'center',
      textStyle: {
        color: '#57534e',
        fontSize: 12,
      },
      itemWidth: 10,
      itemHeight: 10,
      itemGap: 12,
    },
    series: [
      {
        name: '分类分布',
        type: 'pie',
        radius: ['45%', '70%'],
        center: ['35%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 6,
          borderColor: '#fff',
          borderWidth: 2,
        },
        label: {
          show: false,
          position: 'center',
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold',
            color: '#1c1917',
          },
        },
        labelLine: {
          show: false,
        },
        data: props.data,
        color: ['#3b82f6', '#0ea5e9', '#8b5cf6', '#f59e0b', '#22c55e', '#ef4444', '#ec4899', '#06b6d4'],
      },
    ],
  }

  chart.setOption(option)
}

const debouncedResize = debounce(() => {
  chart?.resize()
}, 200)

watch(() => props.data, () => {
  nextTick(() => updateChart())
}, { deep: true })

onMounted(() => {
  nextTick(() => initChart())
})

onUnmounted(() => {
  window.removeEventListener('resize', debouncedResize)
  chart?.dispose()
})
</script>

<template>
  <div ref="chartRef" class="chart-container" />
</template>

<style scoped>
.chart-container {
  height: 280px;
}
</style>
