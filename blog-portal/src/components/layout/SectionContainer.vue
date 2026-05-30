<script setup lang="ts">
interface Props {
  id?: string
  bgImage?: string
  bgColor?: string
  fullHeight?: boolean
  class?: string
}

const props = withDefaults(defineProps<Props>(), {
  fullHeight: false,
})

const sectionStyle = {
  backgroundImage: props.bgImage ? `url(${props.bgImage})` : undefined,
  backgroundColor: props.bgColor,
}
</script>

<template>
  <section
    :id="id"
    class="section"
    :class="[
      fullHeight ? 'section--full-height' : '',
      bgImage ? 'section--parallax' : '',
      props.class
    ]"
    :style="sectionStyle"
  >
    <div v-if="bgImage" class="section__overlay"></div>
    <slot></slot>
  </section>
</template>

<style scoped>
.section {
  overflow: hidden;
  display: table;
  table-layout: fixed;
  width: 100%;
  position: relative;
  padding: 100px 0;
}

.section--full-height {
  height: 100vh;
  padding: 0;
}

.section--parallax {
  background-position: center;
  background-attachment: fixed;
  background-repeat: no-repeat;
  background-size: cover;
}

.section__overlay {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.section :deep(.fp-tablecell) {
  display: table-cell;
  vertical-align: middle;
  width: 100%;
  height: 100%;
  position: relative;
  z-index: 1;
}
</style>
