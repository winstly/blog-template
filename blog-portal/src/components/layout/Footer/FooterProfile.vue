<script setup lang="ts">
import { onMounted } from 'vue';
import { useSiteData } from '@/composables';

const { profile, fetchProfile } = useSiteData();

onMounted(() => {
  fetchProfile();
});
</script>

<template>
  <div class="footer__col">
    <div class="footer-profile">
      <div class="footer-profile__logo">
        <h2>{{ profile?.nickname ?? '' }}</h2>
      </div>
      <p class="footer-profile__signature">{{ profile?.signature ?? '' }}</p>
    </div>
  </div>
</template>

<style scoped>
.footer-profile {
  margin-top: var(--spacing-lg);
}

.footer-profile__logo {
  margin-bottom: var(--spacing-md);
}

.footer-profile__logo h2 {
  color: var(--color-text-on-dark);
  font-size: var(--font-size-lg);
  margin: 0;
  text-transform: uppercase;
}

.footer-profile__signature {
  color: var(--color-text-secondary);
}
</style>
