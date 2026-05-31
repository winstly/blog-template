<script setup lang="ts">
import type { DiaryEntry } from '@/api/types'
import TimelineItem from './TimelineItem.vue'

interface Props {
  year: number
  entries: DiaryEntry[]
  expanded: boolean
}

defineProps<Props>()

const emit = defineEmits<{
  toggle: []
}>()

function formatDate(dateStr: string): string {
  const parts = dateStr.split('-')
  if (parts.length >= 3) {
    return `${parseInt(parts[1])}月${parseInt(parts[2])}日`
  }
  return dateStr
}
</script>

<template>
  <div class="timeline-year">
    <h2>
      <a class="year-toggle" @click="emit('toggle')">
        {{ year }} 年
      </a>
      <i :class="['fa', 'fa-caret-down', 'fa-fw', { rotated: expanded }]"></i>
    </h2>
    <div class="timeline-month" v-show="expanded">
      <ul>
        <TimelineItem
          v-for="entry in entries"
          :key="entry.id"
          :date="formatDate(entry.date)"
          :content="entry.content"
        />
      </ul>
    </div>
  </div>
</template>

<style scoped>
.timeline-year {
  margin: var(--spacing-sm) 0;
}

.timeline-year h2 {
  width: 31%;
  text-align: right;
  font-size: var(--font-size-base);
  margin: 5px 0;
  color: var(--color-primary);
}

.timeline-year h2 a {
  cursor: pointer;
  transition: color var(--transition-base);
}

.timeline-year h2 a:hover {
  color: var(--color-text-dark);
}

.timeline-year h2 i {
  margin-left: var(--spacing-xs);
  transition: transform var(--transition-base);
  cursor: pointer;
  color: var(--color-text-dark);
}

.timeline-year h2 i.rotated {
  transform: rotate(180deg);
}

.timeline-month {
  padding: var(--spacing-sm) 0;
}

.timeline-month ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

@media screen and (min-width: 768px) {
  .timeline-year h2 {
    width: 16%;
    font-size: var(--font-size-2xl);
  }
}
</style>
