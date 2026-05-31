<script setup lang="ts">
import { mockProfile } from '@/api/mock'
import SectionContainer from '@/components/layout/SectionContainer.vue'

const scrollToNext = () => {
  const nextSection = document.getElementById('articles-section')
  if (nextSection) {
    nextSection.scrollIntoView({ behavior: 'smooth' })
  }
}
</script>

<template>
  <SectionContainer
    bg-image="/images/hero-bg.jpg"
    full-height
  >
    <div class="fp-tablecell">
      <div class="page1">
        <div class="nav">
          <h1>{{ mockProfile.nickname }}</h1>
          <p>{{ mockProfile.signature }}</p>
          <RouterLink class="btn" to="/blog">进入博客</RouterLink>
        </div>
      </div>
    </div>
    <a class="next" @click.prevent="scrollToNext"></a>
  </SectionContainer>
</template>

<style scoped>
.page1 {
  position: relative;
}

.page1 .nav {
  text-align: center;
  color: var(--color-text-on-dark);
}

.page1 .nav h1 {
  font-size: var(--font-size-4xl);
  margin-bottom: var(--spacing-sm);
}

.page1 p {
  letter-spacing: var(--letter-spacing-wider);
  margin: var(--spacing-md) 0;
}

.page1 .nav .btn {
  padding: var(--spacing-sm) var(--spacing-2xl);
  background: transparent;
  border-radius: var(--radius-sm);
  border: 2px solid var(--color-text-on-dark);
  color: var(--color-text-on-dark);
  cursor: pointer;
  margin-top: var(--spacing-md);
  font-family: var(--font-family-sans);
  display: inline-block;
  text-decoration: none;
  transition: all var(--transition-base);
}

.page1 .nav .btn:hover {
  background: var(--color-accent);
  border-color: var(--color-accent);
}

.next {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 50px;
  border-radius: var(--radius-full);
  position: absolute;
  bottom: var(--spacing-lg);
  left: 50%;
  margin-left: -25px;
  z-index: 10;
  transition: all var(--transition-slow) ease-out;
  cursor: pointer;
  text-decoration: none;
}

.next:hover {
  background-color: var(--color-accent);
  animation: nextanimation 1s linear;
}

.next::after {
  font-family: FontAwesome;
  content: '\f107';
  color: var(--color-text-on-dark);
  font-size: 2.5rem;
  line-height: 1;
}

@keyframes nextanimation {
  0%, 20%, 53%, 80%, 100% {
    transform: translateY(0);
  }
  40%, 43% {
    transform: translateY(-30px);
  }
  70% {
    transform: translateY(-15px);
  }
  90% {
    transform: translateY(-4px);
  }
}
</style>
