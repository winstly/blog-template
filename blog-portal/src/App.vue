<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import NavMenu from '@/components/layout/NavMenu/NavMenu.vue'
import Footer from '@/components/layout/Footer/Footer.vue'
import AppLoading from '@/components/AppLoading.vue'

const router = useRouter()
const isLoading = ref(true)
const minDisplayTime = 400
const loadingStartTime = ref(Date.now())

router.beforeEach(() => {
  isLoading.value = true
  loadingStartTime.value = Date.now()
})

router.afterEach(() => {
  const elapsed = Date.now() - loadingStartTime.value
  const remaining = Math.max(0, minDisplayTime - elapsed)

  setTimeout(() => {
    isLoading.value = false
  }, remaining)
})

onMounted(() => {
  setTimeout(() => {
    isLoading.value = false
  }, minDisplayTime)
})
</script>

<template>
  <AppLoading :visible="isLoading" />
  <div class="app">
    <NavMenu />
    <main>
      <RouterView />
    </main>
    <Footer />
  </div>
</template>

<style scoped>
.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

main {
  flex: 1 0 auto;
}
</style>
