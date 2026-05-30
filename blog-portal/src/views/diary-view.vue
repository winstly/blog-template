<script setup lang="ts">
import PageBanner from '@/components/shared/PageBanner.vue'
import { getDiariesByYear, getYears } from '@/mock'

const groupedByYear = getDiariesByYear()
const years = getYears()

function formatDate(dateStr: string): string {
  const parts = dateStr.split('-')
  if (parts.length >= 3) {
    return `${parts[0]}年${parseInt(parts[1])}月${parseInt(parts[2])}日`
  }
  return dateStr
}

const expandedYears = ref<Set<number>>(new Set(years.map(y => y)))

function toggleYear(year: number) {
  if (expandedYears.value.has(year)) {
    expandedYears.value.delete(year)
  } else {
    expandedYears.value.add(year)
  }
}

import { ref } from 'vue'
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
        <div class="diary-content">
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
                  :key="entry.id"
                  class="diary-card"
                >
                  <div class="card-date">
                    <i class="fa fa-calendar-o"></i>
                    {{ formatDate(entry.date) }}
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
      </div>
    </div>
  </div>
</template>

<style scoped>
.diary-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.doc-container {
  width: 90%;
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 15px;
}

.container-fixed {
  padding: 20px 15px;
}

.diary-content {
  max-width: 800px;
  margin: 0 auto;
}

.year-group {
  margin-bottom: 30px;
}

.year-header {
  display: flex;
  align-items: center;
  padding: 14px 20px;
  background: #fff;
  border-radius: 10px;
  cursor: pointer;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
  margin-bottom: 16px;
}

.year-header:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.year-header h2 {
  margin: 0;
  font-size: 18px;
  color: #2d3748;
  font-weight: 600;
}

.year-header .count {
  margin-left: 12px;
  font-size: 13px;
  color: #a0aec0;
}

.year-header i {
  margin-left: auto;
  color: #a0aec0;
  transition: transform 0.3s;
  font-size: 12px;
}

.year-header i.rotated {
  transform: rotate(-90deg);
}

.diary-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
}

.diary-card {
  background: #fff;
  border-radius: 10px;
  padding: 18px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
}

.diary-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.card-date {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #667eea;
  margin-bottom: 10px;
}

.card-date i {
  margin-right: 6px;
}

.card-content {
  color: #4a5568;
  line-height: 1.8;
  font-size: 14px;
}

.card-content :deep(p) {
  margin: 0;
}

.expand-enter-active,
.expand-leave-active {
  transition: all 0.3s ease;
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
  padding: 60px 20px;
  color: #a0aec0;
}

.empty-state i {
  font-size: 48px;
  color: #e2e8f0;
  margin-bottom: 16px;
}

.empty-state p {
  font-size: 14px;
}

@media screen and (min-width: 768px) {
  .diary-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
