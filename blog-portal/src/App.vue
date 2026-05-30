<script setup lang="ts">
import NavMenu from '@/components/layout/NavMenu/NavMenu.vue'
import Footer from '@/components/layout/Footer/Footer.vue'
</script>

<template>
  <div id="app">
    <NavMenu />
    <main>
      <RouterView />
    </main>
    <Footer />
  </div>
</template>

<style scoped>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

main {
  flex: 1;
}
</style>
