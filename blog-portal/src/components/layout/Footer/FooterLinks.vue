<script setup lang="ts">
import { mockSocialLinks } from '@/api/mock'

const iconMap: Record<string, string> = {
  'user': 'fa fa-user',
  'book-open': 'fa fa-book',
  'message-circle': 'fa fa-comments',
  'snowflake': 'fa fa-snowflake-o',
}
</script>

<template>
  <div class="footer__col">
    <div class="footer-links">
      <h2 class="footer-links__title">相关链接</h2>
      <ul class="footer-links__list">
        <li v-for="link in mockSocialLinks" :key="link.label">
          <RouterLink :to="link.url">
            <i :class="iconMap[link.icon]"></i>
            {{ link.label }}
          </RouterLink>
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.footer-links {
  margin-top: var(--spacing-lg);
}

.footer-links__title {
  color: var(--color-text-on-dark);
  font-size: var(--font-size-lg);
  margin-bottom: var(--spacing-md);
  text-transform: uppercase;
}

.footer-links__list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-links__list li {
  float: left;
  width: 50%;
  margin-bottom: var(--spacing-xs);
}

.footer-links__list li:last-child {
  margin: 0;
}

.footer-links__list li a {
  color: var(--color-text-secondary);
  text-decoration: none;
  transition: color var(--transition-base);
}

.footer-links__list li a:hover {
  color: var(--color-accent);
}

.footer-links__list li a i {
  margin-right: var(--spacing-xs);
  width: 20px;
  font-size: var(--font-size-base);
}
</style>
