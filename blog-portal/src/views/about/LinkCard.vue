<script setup lang="ts">
import type { FriendLink } from '@/api/types'

interface Props {
  link: FriendLink
}

defineProps<Props>()
</script>

<template>
  <li>
    <a :href="link.url" target="_blank" rel="noopener noreferrer" class="link-card">
      <img :src="link.logo" :alt="link.name" class="avatar" />
      <div class="info">
        <h3>{{ link.name }}</h3>
        <p>{{ link.description }}</p>
      </div>
    </a>
  </li>
</template>

<style scoped>
li {
  float: left;
  width: 50%;
  list-style: none;
  padding: 0 10px;
  margin-bottom: 20px;
}

.link-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  text-decoration: none;
  color: inherit;
  transition: all var(--transition-base) ease;
  box-shadow: var(--shadow-md);
  border: 1px solid var(--color-border-light);
}

.link-card:hover {
  box-shadow: 0 8px 30px rgba(102, 126, 234, 0.15);
  transform: translateY(-3px);
  border-color: rgba(102, 126, 234, 0.2);
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 16px;
  flex-shrink: 0;
}

.info {
  flex: 1;
  min-width: 0;
}

.info h3 {
  margin: 0 0 6px;
  font-size: var(--font-size-base);
  font-weight: 600;
  color: var(--color-text-dark);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.info p {
  margin: 0;
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

@media screen and (max-width: 768px) {
  li {
    width: 100%;
    padding: 0;
  }
}
</style>
