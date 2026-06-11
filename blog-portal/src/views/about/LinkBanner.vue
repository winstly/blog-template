<script setup lang="ts">
import { onMounted } from 'vue';
import { useSiteData } from '@/composables';

const { profile, fetchProfile } = useSiteData();

onMounted(() => {
  fetchProfile();
});
</script>

<template>
  <div class="about-banner">
    <header class="banner-arrow">
      <a class="arrow-left">
        <svg width="0" height="0">
          <path fill="#fff" d="M58.9103319,3.8342148C63.9275825,-1.29368407,72.0647057,-1.29107495,77.1639611,3.92063726C82.2279116,9.09626594,82.198021,17.5181678,77.2485188,22.5768242L31.318494,69.5197703L77.2485188,116.462716C82.2657693,121.590615,82.2632165,129.907191,77.1639611,135.118903C72.1000106,140.294532,63.8598341,140.263982,58.9103319,135.205326L3.8112137,78.891075C-1.25273677,73.7154463,-1.2880417,65.3601778,3.8112137,60.1484655L58.9103319,3.8342148Z"></path>
        </svg>
      </a>
      <a class="arrow-right">
        <svg width="0" height="0">
          <path fill="#fff" d="M58.9103319,3.8342148 C63.9275825,-1.29368407 72.0647057,-1.29107495 77.1639611,3.92063726 C82.2279116,9.09626594 82.198021,17.5181678 77.2485188,22.5768242 L31.318494,69.5197703 L77.2485188,116.462716 C82.2657693,121.590615 82.2632165,129.907191 77.1639611,135.118903 C72.1000106,140.294532 63.8598341,140.263982 58.9103319,135.205326 L3.8112137,78.891075 C-1.25273677,73.7154463,-1.2880417,65.3601778 3.8112137,60.1484655 L58.9103319,3.8342148 Z"></path>
        </svg>
      </a>
    </header>
    <div class="banner-title">
      <h1>关于我</h1>
      <p>{{ profile?.signature ?? '' }}</p>
    </div>
  </div>
</template>

<style scoped>
.about-banner {
  height: 260px;
  background: var(--gradient-accent);
  color: var(--color-text-primary);
  position: relative;
  overflow: hidden;
}

.banner-arrow {
  position: absolute;
  width: 100%;
  height: 260px;
  overflow: hidden;
  display: flex;
  justify-content: space-between;
  padding: 0 var(--spacing-lg);
}

.banner-arrow a {
  display: flex;
  align-items: center;
  opacity: 0.6;
  transition: opacity var(--transition-base);
}

.banner-arrow a:hover {
  opacity: 1;
}

.banner-title {
  position: relative;
  z-index: 9;
  padding-top: 70px;
  color: var(--color-text-on-dark);
  text-align: center;
}

.banner-title h1 {
  font-size: var(--font-size-4xl);
  margin: 0 0 var(--spacing-sm);
}

.banner-title p {
  font-size: var(--font-size-xl);
  margin: 0;
}
</style>
