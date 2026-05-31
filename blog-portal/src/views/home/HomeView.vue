<script setup lang="ts">
import HeroSection from './HeroSection.vue'
import ArticlesSection from './ArticlesSection.vue'
import LinksSection from './LinksSection.vue'
import AboutSection from './AboutSection.vue'
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
