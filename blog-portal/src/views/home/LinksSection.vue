<script setup lang="ts">
import SectionContainer from '@/components/layout/SectionContainer.vue'
</script>

<template>
  <SectionContainer
    bg-image="/images/links-bg.jpg"
  >
    <div class="fp-tablecell">
      <div class="warp-box">
        <div class="warp">
          <div class="inner">
            <div class="links">
              <ul>
                <li
                  v-motion
                  :initial="{ opacity: 0, x: -50 }"
                  :visible="{ opacity: 1, x: 0, transition: { duration: 800 } }"
                >
                  <RouterLink to="/about">关于我</RouterLink>
                </li>
                <li
                  v-motion
                  :initial="{ opacity: 0, x: 50 }"
                  :visible="{ opacity: 1, x: 0, transition: { duration: 800 } }"
                >
                  <RouterLink to="/diary">心情日记</RouterLink>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </SectionContainer>
</template>

<style scoped>
.warp-box {
  width: 100%;
  height: 100%;
  position: relative;
}

.warp {
  display: table;
  margin: 0 auto;
  height: 100%;
  position: relative;
}

.inner {
  text-align: center;
}

.links ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.links li {
  display: inline-block;
  width: 200px;
  height: 46px;
  margin: var(--spacing-sm);
  border: 1px solid var(--color-text-light);
  box-sizing: border-box;
  transition: all var(--transition-slow);
  position: relative;
  text-align: center;
  overflow: hidden;
  line-height: 46px;
}

.links li a {
  display: block;
  text-align: center;
  color: var(--color-text-on-dark);
  text-decoration: none;
  font-size: var(--font-size-sm);
  transition: all var(--transition-slow);
  position: relative;
  z-index: 2;
}

.links li::after {
  content: '';
  width: 0;
  background-color: var(--color-primary);
  height: 100%;
  position: absolute;
  left: 0;
  top: 0;
  z-index: 1;
  transition: all var(--transition-slow);
}

.links li:hover {
  border-color: var(--color-text-on-dark);
}

.links li:hover::after {
  width: 100%;
}
</style>
