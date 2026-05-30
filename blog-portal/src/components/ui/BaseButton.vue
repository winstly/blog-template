<script setup lang="ts">
interface Props {
  variant?: 'primary' | 'outline' | 'ghost'
  size?: 'sm' | 'md' | 'lg'
  href?: string
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  variant: 'primary',
  size: 'md',
})

const sizeClasses = {
  sm: 'px-3 py-1 text-sm',
  md: 'px-6 py-2 text-base',
  lg: 'px-8 py-3 text-lg',
}

const variantClasses = {
  primary: 'bg-primary text-white hover:bg-primary/80',
  outline: 'border-2 border-current bg-transparent hover:bg-white/10',
  ghost: 'bg-transparent hover:bg-white/10',
}
</script>

<template>
  <component
    :is="href ? 'a' : 'button'"
    :href="href"
    :disabled="disabled"
    class="base-button inline-block rounded font-medium transition-all duration-300 cursor-pointer"
    :class="[
      sizeClasses[size],
      variantClasses[variant],
      disabled ? 'opacity-50 cursor-not-allowed' : '',
    ]"
  >
    <slot />
  </component>
</template>

<style scoped>
.base-button {
  font-family: 'Microsoft YaHei', sans-serif;
}
</style>
