<script setup lang="ts">
import { ref } from 'vue'
import type { DiaryEntry } from '@/types'
import TimelineYear from './TimelineYear.vue'

interface Props {
  entries: DiaryEntry[]
  groupedByYear: Record<number, DiaryEntry[]>
  years: number[]
}

defineProps<Props>()

const expandedYears = ref<Set<number>>(new Set())

function toggleYear(year: number) {
  if (expandedYears.value.has(year)) {
    expandedYears.value.delete(year)
  } else {
    expandedYears.value.add(year)
  }
}

function isExpanded(year: number): boolean {
  return expandedYears.value.has(year)
}
</script>

<template>
  <div class="timeline-box shadow">
    <div class="timeline-main">
      <h1 class="timeline-title">
        <i class="fa fa-clock-o"></i>日记
      </h1>
      <div class="timeline-line"></div>
      <TimelineYear
        v-for="year in years"
        :key="year"
        :year="year"
        :entries="groupedByYear[year] || []"
        :expanded="isExpanded(year)"
        @toggle="toggleYear(year)"
      />
      <h1 class="timeline-end">
        <i class="fa fa-hourglass-end"></i>THE END
      </h1>
    </div>
  </div>
</template>

<style scoped>
.timeline-box {
  background: var(--color-surface);
  padding: 15px;
  position: relative;
  min-height: 90vh;
  margin-top: var(--spacing-xl);
}

.shadow {
  box-shadow: var(--shadow-sm);
}

.timeline-main {
  position: relative;
  min-height: 85vh;
}

.timeline-title {
  font-size: 26px;
  background: var(--color-surface);
  z-index: 1;
  position: relative;
  color: var(--color-text-secondary);
  margin: 0 0 var(--spacing-xl) 0;
  margin-left: calc(35% - 16px);
}

.timeline-title i {
  padding-right: 10px;
  font-size: 30px;
}

.timeline-end {
  font-size: var(--font-size-xl);
  padding-top: var(--spacing-xs);
  padding-bottom: 2px;
  margin-top: 40px;
  color: var(--color-text-secondary);
  margin-left: calc(35% - 11px);
}

.timeline-end i {
  padding-right: 10px;
  font-size: var(--font-size-2xl);
}

.timeline-line {
  position: absolute;
  left: 35%;
  top: 0;
  height: 100%;
  width: 2px;
  background: var(--color-text-secondary);
  z-index: 0;
}

@media screen and (max-width: 768px) {
  .timeline-box {
    padding: var(--spacing-sm);
  }

  .timeline-title {
    font-size: var(--font-size-xl);
    margin-left: 33%;
  }

  .timeline-title i {
    font-size: var(--font-size-2xl);
  }

  .timeline-end {
    font-size: var(--font-size-base);
    margin-left: 33%;
  }

  .timeline-line {
    left: 20px;
  }
}
</style>
