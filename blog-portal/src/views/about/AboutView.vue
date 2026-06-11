<script setup lang="ts">
import { ref, onMounted } from 'vue';
import LinkList from './LinkList.vue';
import { siteService } from '@/api/services';
import { useSiteData } from '@/composables';
import type { FriendLink } from '@/api/types';

const { profile, fetchProfile } = useSiteData();

const friendLinks = ref<FriendLink[]>([]);

const skills = [
  { name: 'Vue3', level: 90 },
  { name: 'TypeScript', level: 85 },
  { name: 'React', level: 75 },
  { name: 'Node.js', level: 70 },
  { name: 'Python', level: 60 },
  { name: 'C#', level: 65 },
];

onMounted(async () => {
  fetchProfile();
  try {
    friendLinks.value = await siteService.getFriendLinks();
  } catch (e) {
    console.error('Failed to fetch friend links:', e);
  }
});

const socialLinks = ref<{ icon: string; url: string; color: string }[]>([]);

onMounted(async () => {
  await fetchProfile();
  if (profile.value) {
    socialLinks.value = [
      { icon: 'fa-github', url: 'https://github.com', color: '#333' },
      { icon: 'fa-qq', url: 'https://wpa.qq.com/msgrd?v=3&uin=' + profile.value.qq, color: '#12B7F5' },
      { icon: 'fa-envelope', url: 'mailto:' + profile.value.email, color: '#EA4335' },
      { icon: 'fa-weixin', url: '#', color: '#07C160' },
    ];
  }
});
</script>

<template>
  <div class="about-page">
    <!-- 简洁标题栏 -->
    <div class="page-banner">
      <div class="banner-content">
        <h1>关于我</h1>
        <p>{{ profile?.signature ?? '' }}</p>
      </div>
    </div>

    <div class="page-content">
      <div class="container">
        <!-- 个人信息卡片 -->
        <div class="profile-card">
          <div class="profile-header">
            <div class="avatar">
              <img :src="profile?.avatar ?? 'https://www.yanshisan.cn/logo.png'" alt="avatar" />
            </div>
            <div class="profile-info">
              <h2>{{ profile?.nickname ?? '' }}</h2>
              <p class="bio">{{ profile?.bio ?? '' }}</p>
              <div class="location">
                <i class="fa fa-map-marker"></i>
                {{ profile?.location ?? '' }}
              </div>
            </div>
          </div>

          <!-- 社交链接 -->
          <div class="social-links">
            <a
              v-for="link in socialLinks"
              :key="link.icon"
              :href="link.url"
              target="_blank"
              :style="{ '--hover-color': link.color }"
            >
              <i :class="['fa', link.icon]"></i>
            </a>
          </div>

          <!-- 技能条 -->
          <div class="skills-section">
            <h3><i class="fa fa-code"></i> 技能</h3>
            <div class="skill-list">
              <div v-for="skill in skills" :key="skill.name" class="skill-item">
                <div class="skill-info">
                  <span class="skill-name">{{ skill.name }}</span>
                  <span class="skill-level">{{ skill.level }}%</span>
                </div>
                <div class="skill-bar">
                  <div class="skill-progress" :style="{ width: skill.level + '%' }"></div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 友链申请说明 -->
        <div class="link-section">
          <div class="section-header">
            <h3><i class="fa fa-link"></i> 友情链接</h3>
          </div>

          <div class="link-notice">
            <div class="notice-header">
              <span class="title">申请说明</span>
            </div>
            <div class="notice-tags">
              <span class="reject"><i class="fa fa-times-circle"></i> 经常宕机</span>
              <span class="reject"><i class="fa fa-times-circle"></i> 不合法规</span>
              <span class="reject"><i class="fa fa-times-circle"></i> 插边球站</span>
              <span class="reject"><i class="fa fa-times-circle"></i> 红标报毒</span>
              <span class="accept"><i class="fa fa-check-circle"></i> 原创优先</span>
              <span class="accept"><i class="fa fa-check-circle"></i> 技术优先</span>
            </div>
            <div class="site-info">
              <p>交换友链可在留言板留言，请将本站加入友链：</p>
              <ul>
                <li><strong>名称：</strong>{{ profile?.nickname ?? '' }}</li>
                <li><strong>网址：</strong>https://www.yanshisan.cn</li>
                <li><strong>描述：</strong>{{ profile?.signature ?? '' }}</li>
              </ul>
              <p class="note">申请提交后若无其它原因将在24小时内审核，如超过时间还未通过，请私信我。</p>
            </div>
          </div>

          <!-- 友链列表 -->
          <LinkList :links="friendLinks" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.about-page {
  min-height: 100vh;
  background: var(--color-background);
}

/* 简洁标题栏 */
.page-banner {
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
}

.banner-content {
  max-width: 900px;
  margin: 0 auto;
  padding: var(--spacing-2xl) var(--spacing-xl);
}

.banner-content h1 {
  margin: 0 0 6px;
  font-size: var(--font-size-3xl);
  font-weight: 600;
  color: var(--color-text-dark);
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.banner-content h1::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 24px;
  background: var(--color-accent-secondary);
  border-radius: 2px;
}

.banner-content p {
  margin: 0;
  font-size: var(--font-size-base);
  color: var(--color-text-light);
  padding-left: 14px;
}

/* 页面内容 */
.page-content {
  padding: var(--spacing-xl) 15px 60px;
}

.container {
  max-width: 900px;
  margin: 0 auto;
}

/* 个人信息卡片 */
.profile-card {
  background: var(--color-surface);
  border-radius: var(--radius-xl);
  padding: var(--spacing-2xl);
  margin-bottom: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
}

.profile-header {
  display: flex;
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-xl);
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  border: 2px solid var(--color-border);
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-info h2 {
  margin: 0 0 var(--spacing-sm);
  font-size: 22px;
  color: var(--color-text-dark);
}

.bio {
  color: var(--color-text-muted);
  font-size: var(--font-size-base);
  line-height: 1.6;
  margin: 0 0 var(--spacing-sm);
}

.location {
  display: flex;
  align-items: center;
  gap: 5px;
  color: var(--color-text-light);
  font-size: var(--font-size-sm);
}

.location i {
  color: var(--color-accent-secondary);
  font-size: var(--font-size-xs);
}

/* 社交链接 */
.social-links {
  display: flex;
  gap: var(--spacing-sm);
  padding: var(--spacing-lg) 0;
  border-top: 1px solid var(--color-border);
  border-bottom: 1px solid var(--color-border);
  margin-bottom: var(--spacing-xl);
}

.social-links a {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: var(--color-surface-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-text-muted);
  text-decoration: none;
  transition: all var(--transition-base);
}

.social-links a:hover {
  background: var(--hover-color);
  color: var(--color-text-on-dark);
}

/* 技能 */
.skills-section h3 {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-md);
  color: var(--color-text-dark);
  margin: 0 0 var(--spacing-lg);
}

.skills-section h3 i {
  color: var(--color-accent-secondary);
  font-size: var(--font-size-base);
}

.skill-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--spacing-md);
}

.skill-item {
  background: var(--color-surface-secondary);
  border-radius: var(--radius-md);
  padding: var(--spacing-md) 14px;
}

.skill-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}

.skill-name {
  font-size: var(--font-size-sm);
  font-weight: 500;
  color: var(--color-text-dark);
}

.skill-level {
  font-size: var(--font-size-xs);
  color: var(--color-accent-secondary);
  font-weight: 600;
}

.skill-bar {
  height: 4px;
  background: var(--color-border-dark);
  border-radius: 2px;
  overflow: hidden;
}

.skill-progress {
  height: 100%;
  background: var(--gradient-accent);
  border-radius: 2px;
}

/* 友链部分 */
.link-section {
  background: var(--color-surface);
  border-radius: var(--radius-xl);
  padding: var(--spacing-2xl);
  box-shadow: var(--shadow-sm);
}

.section-header {
  margin-bottom: var(--spacing-lg);
}

.section-header h3 {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-lg);
  color: var(--color-text-dark);
  margin: 0;
}

.section-header h3 i {
  color: var(--color-accent-secondary);
  font-size: var(--font-size-base);
}

.link-notice {
  background: var(--color-surface-secondary);
  border-radius: var(--radius-md);
  padding: var(--spacing-lg);
  margin-bottom: var(--spacing-xl);
}

.notice-header {
  margin-bottom: var(--spacing-md);
}

.notice-header .title {
  font-size: var(--font-size-base);
  font-weight: 600;
  color: var(--color-text-dark);
  padding-left: var(--spacing-sm);
  border-left: 3px solid var(--color-accent-secondary);
}

.notice-tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-md);
}

.notice-tags span {
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-xs);
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--radius-xl);
  font-size: var(--font-size-xs);
}

.notice-tags .reject {
  background: var(--color-error-bg);
  color: var(--color-error-text);
}

.notice-tags .accept {
  background: var(--color-success-bg);
  color: var(--color-success-text);
}

.site-info {
  background: var(--color-surface);
  border-radius: var(--radius-sm);
  padding: var(--spacing-md);
}

.site-info > p {
  color: var(--color-text-muted);
  font-size: var(--font-size-sm);
  margin: 0 0 var(--spacing-sm);
}

.site-info ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.site-info li {
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
  padding: 3px 0;
}

.site-info li strong {
  color: var(--color-text-dark);
}

.site-info .note {
  margin-top: var(--spacing-sm);
  color: var(--color-text-light);
  font-size: var(--font-size-xs);
}

/* 响应式 */
@media screen and (max-width: 768px) {
  .profile-header {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .location {
    justify-content: center;
  }

  .social-links {
    justify-content: center;
  }

  .skill-list {
    grid-template-columns: 1fr;
  }

  .profile-card,
  .link-section {
    padding: 18px;
    border-radius: var(--radius-lg);
  }
}
</style>
