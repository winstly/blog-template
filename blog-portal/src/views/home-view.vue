<script setup lang="ts">
import HeroSection from '@/components/home/HeroSection.vue'
import ArticlesSection from '@/components/home/ArticlesSection.vue'
import LinksSection from '@/components/home/LinksSection.vue'
import AboutSection from '@/components/home/AboutSection.vue'
</script>

<template>
  <div class="home-view">
    <HeroSection />
    <ArticlesSection />
    <LinksSection />
    <AboutSection />
  </div>
</template>

<style scoped>
.home-view {
  min-height: 100vh;
}
</style>
