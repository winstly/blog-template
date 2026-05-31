<script setup lang="ts">
import { mockProfile } from '@/api/mock'
</script>

<template>
  <div class="footer__col">
    <div class="footer-contact">
      <h2 class="footer-contact__title">联系我</h2>
      <ul class="footer-contact__list">
        <li><i class="fa fa-map"></i>地址: {{ mockProfile.location }}</li>
        <li>
          <i class="fa fa-qq"></i>QQ:
          <a :href="'https://wpa.qq.com/msgrd?v=3&uin=' + mockProfile.qq" target="_blank" rel="noopener">
            {{ mockProfile.qq }}
          </a>
        </li>
        <li>
          <i class="fa fa-envelope"></i>邮箱:
          <a :href="'mailto:' + mockProfile.email">{{ mockProfile.email }}</a>
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.footer-contact {
  margin-top: var(--spacing-lg);
}

.footer-contact__title {
  color: var(--color-text-on-dark);
  font-size: var(--font-size-lg);
  margin-bottom: var(--spacing-md);
  text-transform: uppercase;
}

.footer-contact__list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-contact__list li {
  color: var(--color-text-secondary);
  position: relative;
  padding-left: 40px;
  font-family: Roboto, sans-serif;
  margin-bottom: var(--spacing-xs);
}

.footer-contact__list li:last-child {
  margin: 0;
}

.footer-contact__list li i {
  position: absolute;
  top: 50%;
  width: 30px;
  height: 30px;
  text-align: center;
  line-height: 30px;
  left: 0;
  margin-top: -15px;
  font-size: var(--font-size-base);
  color: var(--color-accent);
}

.footer-contact__list li a {
  color: var(--color-text-secondary);
  text-decoration: none;
  transition: color var(--transition-base);
}

.footer-contact__list li a:hover {
  color: var(--color-accent);
}
</style>
