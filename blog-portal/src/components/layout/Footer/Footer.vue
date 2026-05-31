<script setup lang="ts">
import { mockProfile } from '@/api/mock'
import FooterProfile from './FooterProfile.vue'
import FooterLinks from './FooterLinks.vue'
import FooterContact from './FooterContact.vue'

const currentYear = new Date().getFullYear()
</script>

<template>
  <footer class="footer">
    <!-- Footer Top -->
    <div class="footer__top">
      <div class="footer__container">
        <div class="footer__row">
          <FooterProfile />
          <FooterLinks />
          <FooterContact />
        </div>
      </div>
    </div>

    <!-- Copyright -->
    <div class="footer__copyright">
      <div class="footer__container">
        <div class="footer__row">
          <div class="footer__col footer__col--full">
            <p class="footer__copyright-text">
              Copyright &copy; 2018-{{ currentYear }} {{ mockProfile.nickname }} All Rights Reserved V.3.1.3 蜀ICP备18008600号
            </p>
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  margin-top: auto;
  flex-shrink: 0;
}

.footer__container {
  width: 90%;
  padding-right: var(--spacing-sm);
  padding-left: var(--spacing-sm);
  margin-right: auto;
  margin-left: auto;
}

.footer__top {
  background: var(--color-dark-bg, #232328);
  padding: var(--spacing-3xl) 0 90px;
}

.footer__row {
  display: flex;
  flex-wrap: wrap;
  margin: 0 calc(var(--spacing-sm) * -1);
}

.footer__col {
  flex: 0 0 33.333%;
  max-width: 33.333%;
  padding: 0 var(--spacing-sm);
}

.footer__col--full {
  flex: 0 0 100%;
  max-width: 100%;
}

.footer__copyright {
  padding: var(--spacing-xs) 0;
  background: var(--color-darker-bg, #1D1D21);
}

.footer__copyright-text {
  color: var(--color-text-muted, #787977);
  font-size: var(--font-size-sm);
  margin: 0;
  text-align: center;
}

/* Responsive */
@media screen and (max-width: 768px) {
  .footer__col {
    flex: 0 0 100%;
    max-width: 100%;
  }
}
</style>
