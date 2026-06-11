<template>
  <el-card shadow="never">
    <!-- Fields Slot -->
    <slot name="fields" />

    <!-- Actions Slot -->
    <el-row
      v-if="$slots.actions"
      class="mt-4"
      :justify="justifyValue"
    >
      <slot name="actions" />
    </el-row>
  </el-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  /** 操作按钮对齐方式：left 或 right */
  actionsAlign?: 'left' | 'right'
}

const props = withDefaults(defineProps<Props>(), {
  actionsAlign: 'left',
})

const justifyValue = computed(() => props.actionsAlign === 'right' ? 'end' : 'start')
</script>
