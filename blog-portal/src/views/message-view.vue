<script setup lang="ts">
import PageBanner from '@/components/shared/PageBanner.vue'
import MessageForm from '@/components/message/MessageForm.vue'
import MessageList from '@/components/message/MessageList.vue'
import { mockMessages } from '@/mock'
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
          <MessageList :messages="mockMessages" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.message-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.doc-container {
  width: 90%;
  max-width: 800px;
  margin: 0 auto;
  padding: 0 15px;
}

.container-fixed {
  padding: 20px 15px;
}

.container-inner {
  max-width: 100%;
}
</style>
