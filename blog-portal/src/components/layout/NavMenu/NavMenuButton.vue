<script setup lang="ts">
interface Props {
  isOpen: boolean
}

defineProps<Props>()

const emit = defineEmits<{
  toggle: []
}>()
</script>

<template>
  <button
    class="menu-button"
    :class="{ 'is-open': isOpen }"
    @click="emit('toggle')"
    aria-label="菜单"
  >
    <span class="menu-icon">
      <span class="line"></span>
      <span class="line"></span>
      <span class="line"></span>
    </span>
  </button>
</template>

<style scoped>
.menu-button {
  position: fixed;
  z-index: 102;
  width: 44px;
  height: 44px;
  border-radius: 50%;
  right: 30px;
  top: 20px;
  cursor: pointer;
  background: var(--color-surface);
  border: none;
  box-shadow: var(--shadow-md);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--transition-base) ease;
}

.menu-button:hover {
  box-shadow: var(--shadow-lg);
  transform: scale(1.05);
}

.menu-button:active {
  transform: scale(0.95);
}

.menu-icon {
  width: 20px;
  height: 14px;
  position: relative;
}

.line {
  display: block;
  width: 100%;
  height: 2px;
  background: var(--color-text-dark);
  border-radius: 2px;
  position: absolute;
  left: 0;
  transition: all var(--transition-base) cubic-bezier(0.4, 0, 0.2, 1);
}

.line:nth-child(1) {
  top: 0;
}

.line:nth-child(2) {
  top: 6px;
}

.line:nth-child(3) {
  top: 12px;
}

/* 打开状态 */
.is-open .line:nth-child(1) {
  top: 6px;
  transform: rotate(45deg);
}

.is-open .line:nth-child(2) {
  opacity: 0;
  transform: translateX(10px);
}

.is-open .line:nth-child(3) {
  top: 6px;
  transform: rotate(-45deg);
}

@media screen and (max-width: 768px) {
  .menu-button {
    right: 15px;
    top: 15px;
    width: 40px;
    height: 40px;
  }

  .menu-icon {
    width: 18px;
    height: 12px;
  }

  .line:nth-child(2) {
    top: 5px;
  }

  .line:nth-child(3) {
    top: 10px;
  }

  .is-open .line:nth-child(1),
  .is-open .line:nth-child(3) {
    top: 5px;
  }
}
</style>
