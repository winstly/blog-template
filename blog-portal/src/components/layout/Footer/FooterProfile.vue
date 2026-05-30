<script setup lang="ts">
import { mockProfile } from '@/mock'
</script>

<template>
  <div class="footer__col">
    <div class="footer-profile">
      <div class="footer-profile__logo">
        <h2>{{ mockProfile.nickname }}</h2>
      </div>
      <p class="footer-profile__signature">{{ mockProfile.signature }}</p>
      <div>
        <a href="#" class="footer-profile__btn">About Me</a>
      </div>
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

.footer-profile__btn {
  margin-top: var(--spacing-md);
  display: inline-block;
  padding: var(--spacing-xs) var(--spacing-2xl);
  background: transparent;
  border-radius: var(--radius-sm);
  border: 2px solid var(--color-accent);
  color: var(--color-accent);
  cursor: pointer;
  transition: all var(--transition-base);
  text-decoration: none;
}

.footer-profile__btn:hover {
  background: var(--color-accent);
  color: var(--color-text-on-dark);
  border-color: var(--color-accent);
}
</style>
