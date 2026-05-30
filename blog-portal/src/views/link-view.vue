<script setup lang="ts">
import LinkList from '@/components/link/LinkList.vue'
import { mockFriendLinks, mockProfile, siteInfo, linkRequirements } from '@/mock'

const skills = [
  { name: 'Vue3', level: 90 },
  { name: 'TypeScript', level: 85 },
  { name: 'React', level: 75 },
  { name: 'Node.js', level: 70 },
  { name: 'Python', level: 60 },
  { name: 'C#', level: 65 },
]

const socialLinks = [
  { icon: 'fa-github', url: 'https://github.com', color: '#333' },
  { icon: 'fa-qq', url: 'https://wpa.qq.com/msgrd?v=3&uin=' + mockProfile.qq, color: '#12B7F5' },
  { icon: 'fa-envelope', url: 'mailto:' + mockProfile.email, color: '#EA4335' },
  { icon: 'fa-weixin', url: '#', color: '#07C160' },
]
</script>

<template>
  <div class="about-page">
    <!-- 简洁标题栏 -->
    <div class="page-banner">
      <div class="banner-content">
        <h1>关于我</h1>
        <p>{{ mockProfile.signature }}</p>
      </div>
    </div>

    <div class="page-content">
      <div class="container">
        <!-- 个人信息卡片 -->
        <div class="profile-card">
          <div class="profile-header">
            <div class="avatar">
              <img src="https://www.yanshisan.cn/logo.png" alt="avatar" />
            </div>
            <div class="profile-info">
              <h2>{{ mockProfile.nickname }}</h2>
              <p class="bio">{{ mockProfile.bio }}</p>
              <div class="location">
                <i class="fa fa-map-marker"></i>
                {{ mockProfile.location }}
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
                <li><strong>名称：</strong>{{ siteInfo.name }}</li>
                <li><strong>网址：</strong>{{ siteInfo.url }}</li>
                <li><strong>图标：</strong>{{ siteInfo.logo }}</li>
                <li><strong>描述：</strong>{{ siteInfo.description }}</li>
              </ul>
              <p class="note">{{ linkRequirements.note }}</p>
            </div>
          </div>

          <!-- 友链列表 -->
          <LinkList :links="mockFriendLinks" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.about-page {
  min-height: 100vh;
  background: #f5f7fa;
}

/* 简洁标题栏 */
.page-banner {
  background: #fff;
  border-bottom: 1px solid #f0f4f8;
}

.banner-content {
  max-width: 900px;
  margin: 0 auto;
  padding: 24px 20px;
}

.banner-content h1 {
  margin: 0 0 6px;
  font-size: 24px;
  font-weight: 600;
  color: #2d3748;
  display: flex;
  align-items: center;
  gap: 10px;
}

.banner-content h1::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 24px;
  background: #667eea;
  border-radius: 2px;
}

.banner-content p {
  margin: 0;
  font-size: 14px;
  color: #a0aec0;
  padding-left: 14px;
}

/* 页面内容 */
.page-content {
  padding: 20px 15px 60px;
}

.container {
  max-width: 900px;
  margin: 0 auto;
}

/* 个人信息卡片 */
.profile-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.profile-header {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  border: 2px solid #f0f4f8;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-info h2 {
  margin: 0 0 8px;
  font-size: 22px;
  color: #2d3748;
}

.bio {
  color: #718096;
  font-size: 14px;
  line-height: 1.6;
  margin: 0 0 8px;
}

.location {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #a0aec0;
  font-size: 13px;
}

.location i {
  color: #667eea;
  font-size: 12px;
}

/* 社交链接 */
.social-links {
  display: flex;
  gap: 10px;
  padding: 16px 0;
  border-top: 1px solid #f0f4f8;
  border-bottom: 1px solid #f0f4f8;
  margin-bottom: 20px;
}

.social-links a {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #f7fafc;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #718096;
  text-decoration: none;
  transition: all 0.3s;
}

.social-links a:hover {
  background: var(--hover-color);
  color: #fff;
}

/* 技能 */
.skills-section h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  color: #2d3748;
  margin: 0 0 16px;
}

.skills-section h3 i {
  color: #667eea;
  font-size: 14px;
}

.skill-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.skill-item {
  background: #f7fafc;
  border-radius: 8px;
  padding: 12px 14px;
}

.skill-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}

.skill-name {
  font-size: 13px;
  font-weight: 500;
  color: #2d3748;
}

.skill-level {
  font-size: 12px;
  color: #667eea;
  font-weight: 600;
}

.skill-bar {
  height: 4px;
  background: #e2e8f0;
  border-radius: 2px;
  overflow: hidden;
}

.skill-progress {
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
}

/* 友链部分 */
.link-section {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.section-header {
  margin-bottom: 16px;
}

.section-header h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  color: #2d3748;
  margin: 0;
}

.section-header h3 i {
  color: #667eea;
  font-size: 14px;
}

.link-notice {
  background: #f7fafc;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 20px;
}

.notice-header {
  margin-bottom: 12px;
}

.notice-header .title {
  font-size: 14px;
  font-weight: 600;
  color: #2d3748;
  padding-left: 10px;
  border-left: 3px solid #667eea;
}

.notice-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 12px;
}

.notice-tags span {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
}

.notice-tags .reject {
  background: #fff5f5;
  color: #e53e3e;
}

.notice-tags .accept {
  background: #f0fff4;
  color: #38a169;
}

.site-info {
  background: #fff;
  border-radius: 6px;
  padding: 12px;
}

.site-info > p {
  color: #718096;
  font-size: 13px;
  margin: 0 0 10px;
}

.site-info ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.site-info li {
  color: #4a5568;
  font-size: 13px;
  padding: 3px 0;
}

.site-info li strong {
  color: #2d3748;
}

.site-info .note {
  margin-top: 10px;
  color: #a0aec0;
  font-size: 12px;
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
    border-radius: 10px;
  }
}
</style>
