<script setup lang="ts">
import { ref, onMounted } from 'vue';
import PageBanner from '@/components/shared/PageBanner.vue';
import MessageForm from './MessageForm.vue';
import MessageList from './MessageList.vue';
import { messageService } from '@/api/services';
import type { Message } from '@/api/types';

const messages = ref<Message[]>([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const result = await messageService.getList({ page: 1, pageSize: 50 });
    messages.value = result.list;
  } catch (e) {
    console.error('Failed to fetch messages:', e);
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="message-page">
    <PageBanner
      title="留言板"
      subtitle="沟通交流，拉近你我"
      color="#f093fb"
    />
    <div class="doc-container">
      <div class="container-fixed">
        <div class="container-inner">
          <MessageForm />
          <div v-if="!loading">
            <MessageList :messages="messages" />
          </div>
          <div v-else class="text-center py-8">
            <i class="fa fa-spinner fa-spin"></i> 加载中...
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.message-page {
  min-height: 100vh;
  background: var(--color-background);
}

.doc-container {
  width: 90%;
  max-width: 800px;
  margin: 0 auto;
  padding: 0 15px;
}

.container-fixed {
  padding: var(--spacing-xl) 15px;
}

.container-inner {
  max-width: 100%;
}
</style>
