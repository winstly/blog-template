<script setup lang="ts">
interface Props {
  visible: boolean
}

defineProps<Props>()
</script>

<template>
  <Transition name="loading-fade">
    <div v-if="visible" class="loading-overlay">
      <div class="loading-content">
        <div class="meditation">
          <div class="aura"></div>
          <div class="meditator">
            <div class="head"></div>
            <div class="body-upper"></div>
            <div class="arm arm-left"></div>
            <div class="arm arm-right"></div>
            <div class="leg leg-left"></div>
            <div class="leg leg-right"></div>
          </div>
        </div>
        <p class="loading-text">修炼中...</p>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
.loading-overlay {
  position: fixed;
  inset: 0;
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--color-dark-bg);
}

.loading-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-xl);
}

.meditation {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100px;
  height: 60px;
}

.aura {
  position: absolute;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: radial-gradient(circle, var(--color-accent) 0%, transparent 70%);
  opacity: 0.3;
  animation: aura-pulse 2s ease-in-out infinite;
}

.meditator {
  position: relative;
  width: 50px;
  height: 50px;
  animation: breathe 3s ease-in-out infinite;
}

.head {
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 14px;
  height: 14px;
  background: linear-gradient(135deg, var(--color-primary), var(--color-accent-secondary));
  border-radius: 50%;
}

.body-upper {
  position: absolute;
  top: 13px;
  left: 50%;
  transform: translateX(-50%);
  width: 18px;
  height: 18px;
  background: linear-gradient(180deg, var(--color-primary), var(--color-accent-gradient-start));
  border-radius: 5px 5px 4px 4px;
}

.arm {
  position: absolute;
  top: 17px;
  width: 16px;
  height: 4px;
  background: linear-gradient(90deg, var(--color-accent-secondary), var(--color-primary));
  border-radius: 2px;
}

.arm-left {
  left: 0;
  transform: rotate(20deg);
}

.arm-right {
  right: 0;
  transform: rotate(-20deg);
}

.leg {
  position: absolute;
  top: 30px;
  width: 20px;
  height: 6px;
  background: linear-gradient(90deg, var(--color-accent-gradient-start), var(--color-primary), var(--color-accent-gradient-start));
  border-radius: 3px;
}

.leg-left {
  left: 3px;
  transform: rotate(-5deg);
}

.leg-right {
  right: 3px;
  transform: rotate(5deg);
}

@keyframes breathe {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

@keyframes aura-pulse {
  0%, 100% {
    opacity: 0.2;
    transform: scale(1);
  }
  50% {
    opacity: 0.4;
    transform: scale(1.1);
  }
}

.loading-text {
  margin: 0;
  color: var(--color-text-on-dark);
  font-size: var(--font-size-base);
  font-family: var(--font-family-sans);
  letter-spacing: var(--letter-spacing-wide);
}

/* 过渡动画 */
.loading-fade-enter-active {
  transition: opacity 200ms ease-out;
}

.loading-fade-leave-active {
  transition: opacity 300ms ease-in;
}

.loading-fade-enter-from,
.loading-fade-leave-to {
  opacity: 0;
}
</style>
