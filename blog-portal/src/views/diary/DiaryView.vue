<script setup lang="ts">
import { ref, onMounted } from 'vue';
import PageBanner from '@/components/shared/PageBanner.vue';
import { diaryService } from '@/api/services';
import type { DiaryEntry } from '@/api/types';

const groupedByYear = ref<Record<number, DiaryEntry[]>>({});
const years = ref<number[]>([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const [diaryResult, yearsResult] = await Promise.all([
      diaryService.getList({ page: 1, pageSize: 100 }),
      diaryService.getYears(),
    ]);
    const entries = diaryResult.list;
    years.value = yearsResult;

    const grouped: Record<number, DiaryEntry[]> = {};
    for (const entry of entries) {
      if (!grouped[entry.year]) {
        grouped[entry.year] = [];
      }
      grouped[entry.year].push(entry);
    }
    groupedByYear.value = grouped;
  } catch (e) {
    console.error('Failed to fetch diary data:', e);
  } finally {
    loading.value = false;
  }
});

function formatDate(dateStr: string): string {
  const parts = dateStr.split('-');
  if (parts.length >= 3) {
    return `${parts[0]}年${parseInt(parts[1])}月${parseInt(parts[2])}日`;
  }
  return dateStr;
}

const expandedYears = ref<Set<number>>(new Set());

function toggleYear(year: number) {
  if (expandedYears.value.has(year)) {
    expandedYears.value.delete(year);
  } else {
    expandedYears.value.add(year);
  }
}
</script>

<template>
  <div class="diary-page">
    <PageBanner
      title="日记"
      subtitle="记录生活点滴"
      color="#667eea"
    />
    <div class="doc-container">
      <div class="container-fixed">
        <div v-if="!loading" class="diary-content">
          <div v-for="year in years" :key="year" class="year-group">
            <div class="year-header" @click="toggleYear(year)">
              <h2>{{ year }}</h2>
              <span class="count">{{ groupedByYear[year]?.length || 0 }} 篇</span>
              <i :class="['fa', 'fa-chevron-down', { rotated: !expandedYears.has(year) }]"></i>
            </div>

            <Transition name="expand">
              <div v-show="expandedYears.has(year)" class="diary-grid">
                <div
                  v-for="entry in groupedByYear[year]"
                  :key="entry.diaryCode"
                  class="diary-card"
                >
                  <div class="card-date">
                    <i class="fa fa-calendar-o"></i>
                    {{ formatDate(entry.diaryDate) }}
                  </div>
                  <div class="card-content" v-html="entry.content"></div>
                </div>
              </div>
            </Transition>
          </div>

          <div v-if="years.length === 0" class="empty-state">
            <i class="fa fa-book"></i>
            <p>暂无日记</p>
          </div>
        </div>
        <div v-else class="empty-state">
          <i class="fa fa-spinner fa-spin"></i>
          <p>加载中...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.diary-page {
  min-height: 100vh;
  background: var(--color-background);
}

.doc-container {
  width: 90%;
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 15px;
}

.container-fixed {
  padding: var(--spacing-xl) 15px;
}

.diary-content {
  max-width: 800px;
  margin: 0 auto;
}

.year-group {
  margin-bottom: var(--spacing-3xl);
}

.year-header {
  display: flex;
  align-items: center;
  padding: 14px var(--spacing-xl);
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  cursor: pointer;
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-base);
  margin-bottom: var(--spacing-lg);
}

.year-header:hover {
  box-shadow: var(--shadow-md);
}

.year-header h2 {
  margin: 0;
  font-size: var(--font-size-xl);
  color: var(--color-text-dark);
  font-weight: 600;
}

.year-header .count {
  margin-left: var(--spacing-md);
  font-size: var(--font-size-sm);
  color: var(--color-text-light);
}

.year-header i {
  margin-left: auto;
  color: var(--color-text-light);
  transition: transform var(--transition-base);
  font-size: var(--font-size-xs);
}

.year-header i.rotated {
  transform: rotate(-90deg);
}

.diary-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: var(--spacing-lg);
}

.diary-card {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 18px;
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-base);
}

.diary-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.card-date {
  display: flex;
  align-items: center;
  font-size: var(--font-size-xs);
  color: var(--color-accent-secondary);
  margin-bottom: var(--spacing-sm);
}

.card-date i {
  margin-right: 6px;
}

.card-content {
  color: var(--color-text-secondary);
  line-height: 1.8;
  font-size: var(--font-size-base);
}

.card-content :deep(p) {
  margin: 0;
}

.expand-enter-active,
.expand-leave-active {
  transition: all var(--transition-base) ease;
  overflow: hidden;
}

.expand-enter-from,
.expand-leave-to {
  opacity: 0;
  max-height: 0;
}

.expand-enter-to,
.expand-leave-from {
  opacity: 1;
  max-height: 2000px;
}

.empty-state {
  text-align: center;
  padding: 60px var(--spacing-xl);
  color: var(--color-text-light);
}

.empty-state i {
  font-size: 48px;
  color: var(--color-border-dark);
  margin-bottom: var(--spacing-lg);
}

.empty-state p {
  font-size: var(--font-size-base);
}

@media screen and (min-width: 768px) {
  .diary-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
