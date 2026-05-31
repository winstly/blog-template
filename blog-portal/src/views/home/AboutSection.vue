<script setup lang="ts">
import { mockProfile } from '@/api/mock'
import SectionContainer from '@/components/layout/SectionContainer.vue'
</script>

<template>
  <SectionContainer
    bg-color="#FAF9F9"
    class="about-section"
  >
    <div class="fp-tablecell">
      <div class="inner">
        <h1
          v-motion
          :initial="{ opacity: 0, x: -50 }"
          :visible="{ opacity: 1, x: 0, transition: { duration: 800 } }"
        >
          次元使者
        </h1>
        <p
          v-motion
          :initial="{ opacity: 0, x: 50 }"
          :visible="{ opacity: 1, x: 0, transition: { duration: 800, delay: 200 } }"
        >
          {{ mockProfile.bio }}
        </p>
      </div>
    </div>
  </SectionContainer>
</template>

<style scoped>
.about-section {
  font-family: 'Microsoft YaHei', sans-serif;
}

.inner {
  text-align: center;
}

.inner h1 {
  margin-bottom: var(--spacing-lg);
  font-size: var(--font-size-3xl);
  font-weight: 500;
  color: var(--color-text-primary);
}

.inner p {
  color: var(--color-text-muted);
  line-height: 1.8;
  max-width: 800px;
  margin: 0 auto;
  padding: 0 var(--spacing-lg);
}
</style>
