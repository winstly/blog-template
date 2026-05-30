<script setup lang="ts">
interface Props {
  hover?: boolean
}

withDefaults(defineProps<Props>(), {
  hover: true,
})
</script>

<template>
  <div
    class="base-card bg-white rounded overflow-hidden transition-all duration-300"
    :class="hover ? 'hover:shadow-lg' : ''"
  >
    <slot />
  </div>
</template>

<style scoped>
.base-card {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}
</style>
