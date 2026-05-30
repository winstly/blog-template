<script setup lang="ts">
import { mockNavItems } from '@/mock'

interface Props {
  isOpen: boolean
}

defineProps<Props>()

const emit = defineEmits<{
  close: []
}>()
</script>

<template>
  <Transition name="menu-fade">
    <div v-if="isOpen" class="menu-overlay" @click="emit('close')">
      <Transition name="menu-slide">
        <div v-if="isOpen" class="menu-panel" @click.stop>
          <nav class="menu-nav">
            <RouterLink
              v-for="item in mockNavItems"
              :key="item.path"
              :to="item.path"
              class="menu-item"
              @click="emit('close')"
            >
              {{ item.label }}
            </RouterLink>
          </nav>
        </div>
      </Transition>
    </div>
  </Transition>
</template>

<style scoped>
.menu-overlay {
  position: fixed;
  inset: 0;
  z-index: 100;
  background: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(4px);
}

.menu-panel {
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 280px;
  background: var(--color-surface);
  box-shadow: -4px 0 30px rgba(0, 0, 0, 0.1);
}

.menu-nav {
  padding: 100px 30px 40px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.menu-item {
  display: block;
  padding: 16px 20px;
  text-decoration: none;
  color: var(--color-text-secondary);
  font-size: var(--font-size-lg);
  font-weight: 500;
  border-radius: 10px;
  transition: all var(--transition-fast) ease;
  border-left: 3px solid transparent;
}

.menu-item:hover {
  background: var(--color-surface-secondary);
  color: var(--color-accent-secondary);
  border-left-color: var(--color-accent-secondary);
}

.menu-item.router-link-exact-active {
  background: linear-gradient(135deg, #f0f4ff 0%, #e8f0fe 100%);
  color: var(--color-accent-secondary);
  border-left-color: var(--color-accent-secondary);
}

/* 过渡动画 */
.menu-fade-enter-active,
.menu-fade-leave-active {
  transition: opacity var(--transition-base) ease;
}

.menu-fade-enter-from,
.menu-fade-leave-to {
  opacity: 0;
}

.menu-slide-enter-active,
.menu-slide-leave-active {
  transition: transform var(--transition-base) cubic-bezier(0.4, 0, 0.2, 1);
}

.menu-slide-enter-from,
.menu-slide-leave-to {
  transform: translateX(100%);
}

/* 响应式 */
@media screen and (max-width: 768px) {
  .menu-panel {
    width: 100%;
    max-width: 300px;
  }

  .menu-nav {
    padding: 80px 20px 30px;
  }

  .menu-item {
    padding: 14px 16px;
    font-size: var(--font-size-base);
  }
}
</style>
