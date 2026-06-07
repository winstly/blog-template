<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import * as echarts from 'echarts/core'
import { LineChart } from 'echarts/charts'
import {
  GridComponent,
  TooltipComponent,
  TitleComponent,
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { graphic } from 'echarts/core'
import { LabelLayout } from 'echarts/features'
import type { EChartsOption } from 'echarts/types/dist/shared'
import type { TopLevelFormatterParams } from 'echarts/types/dist/shared'

// Register ECharts components
echarts.use([LineChart, GridComponent, TooltipComponent, TitleComponent, LabelLayout, CanvasRenderer])

interface TrendItem {
  date: string
  views: number
}

interface Props {
  data: TrendItem[]
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

  const dates = props.data.map(t => t.date.slice(5))
  const views = props.data.map(t => t.views)

  const option: EChartsOption = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#e7e5e4',
      borderWidth: 1,
      padding: [12, 16],
      textStyle: {
        color: '#44403c',
        fontSize: 13,
      },
      extraCssText: 'box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08); border-radius: 10px;',
      formatter: (params: TopLevelFormatterParams) => {
        if (!Array.isArray(params) || params.length === 0) return ''
        const data = params[0]
        return `<div style="font-weight: 600; margin-bottom: 4px;">${data.name}</div>
                <div style="display: flex; align-items: center; gap: 8px;">
                  <span style="display: inline-block; width: 8px; height: 8px; background: #3b82f6; border-radius: 50%;"></span>
                  <span>访问量: <strong>${data.value}</strong></span>
                </div>`
      },
    },
    grid: {
      left: 50,
      right: 20,
      bottom: 30,
      top: 20,
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates,
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: {
        color: '#a8a29e',
        fontSize: 12,
        margin: 12,
      },
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisTick: { show: false },
      splitLine: {
        lineStyle: {
          color: '#f5f5f4',
          type: 'dashed',
        },
      },
      axisLabel: {
        color: '#a8a29e',
        fontSize: 12,
        formatter: (value: number): string => {
          if (value >= 1000) return (value / 1000) + 'k'
          return String(value)
        },
      },
    },
    series: [
      {
        name: '访问量',
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        showSymbol: false,
        lineStyle: {
          color: '#3b82f6',
          width: 3,
          shadowColor: 'rgba(59, 130, 246, 0.3)',
          shadowBlur: 10,
          shadowOffsetY: 5,
        },
        itemStyle: {
          color: '#3b82f6',
          borderColor: '#fff',
          borderWidth: 2,
        },
        areaStyle: {
          color: new graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(59, 130, 246, 0.25)' },
            { offset: 0.5, color: 'rgba(59, 130, 246, 0.1)' },
            { offset: 1, color: 'rgba(59, 130, 246, 0.02)' },
          ]),
        },
        data: views,
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
