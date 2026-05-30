<script setup lang="ts">
interface Props {
  date: string
  content: string
}

defineProps<Props>()
</script>

<template>
  <li class="timeline-item">
    <div class="h4 animated fadeInLeft">
      <p class="date">{{ date }}</p>
    </div>
    <p class="dot-circle animated">
      <i class="fa fa-dot-circle-o"></i>
    </p>
    <div class="content animated fadeInRight" v-html="content"></div>
    <div class="clear"></div>
  </li>
</template>

<style scoped>
.timeline-item {
  padding: var(--spacing-sm) 0;
  list-style: none;
}

.timeline-item .h4 {
  display: inline-block;
  width: 31%;
  text-align: right;
  float: left;
}

.date {
  display: inline-block;
  padding: 2px var(--spacing-xs);
  color: var(--color-text-dark);
  font-size: var(--font-size-base);
  margin: 5px 0 0 0;
}

.dot-circle {
  color: var(--color-text-dark);
  width: 8%;
  text-align: center;
  font-size: var(--font-size-2xl);
  z-index: 1;
  position: relative;
  background: var(--color-surface);
  float: left;
  margin: 0;
}

.content {
  max-width: 50%;
  float: left;
  padding: var(--spacing-sm);
  margin-left: var(--spacing-sm);
  position: relative;
  z-index: 1;
  background: var(--color-text-dark);
  color: var(--color-text-on-dark);
  line-height: 1.8;
}

.content::before {
  position: absolute;
  left: -20px;
  top: 6px;
  height: 0;
  width: 0;
  content: '';
  border: 10px solid rgba(255, 255, 255, 0);
  border-top: 6px solid rgba(255, 255, 255, 0);
  border-bottom: 6px solid rgba(255, 255, 255, 0);
  border-right-color: var(--color-text-dark);
}

.content img {
  width: 100%;
}

.clear {
  clear: both;
}

@media screen and (min-width: 768px) {
  .timeline-item .h4 {
    width: 16%;
  }

  .dot-circle {
    width: 4%;
    font-size: var(--font-size-2xl);
  }

  .content {
    max-width: 70%;
  }
}

@media screen and (max-width: 768px) {
  .dot-circle {
    font-size: var(--font-size-lg);
  }

  .content {
    font-size: var(--font-size-sm);
  }
}
</style>
