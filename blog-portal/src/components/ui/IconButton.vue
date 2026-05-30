<script setup lang="ts">
interface Props {
  ariaLabel?: string
}

withDefaults(defineProps<Props>(), {
  ariaLabel: '',
})

const emit = defineEmits<{
  click: []
}>()
</script>

<template>
  <button
    class="icon-button w-10 h-10 rounded-full flex items-center justify-center transition-all duration-300"
    :aria-label="ariaLabel"
    @click="emit('click')"
  >
    <slot />
  </button>
</template>

<style scoped>
.icon-button {
  background-color: var(--color-surface-secondary);
  color: var(--color-text-on-dark);
}

.icon-button:hover {
  background-color: var(--color-accent);
}
</style>
