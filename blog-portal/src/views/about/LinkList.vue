<script setup lang="ts">
import type { FriendLink } from '@/api/types'
import LinkCard from './LinkCard.vue'

interface Props {
  links: FriendLink[]
}

defineProps<Props>()
</script>

<template>
  <ul class="link-list">
    <LinkCard v-for="link in links" :key="link.id" :link="link" />
  </ul>
</template>

<style scoped>
.link-list {
  margin-top: 10px;
  padding: 0;
  list-style: none;
  overflow: hidden;
}

.link-list::after {
  content: '';
  display: table;
  clear: both;
}
</style>
