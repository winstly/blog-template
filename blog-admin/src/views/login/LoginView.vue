<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { User, Lock, Notebook } from '@element-plus/icons-vue'
import { useAuthStore } from '@/plugins/stores/auth'
import { useCanvasAnimation } from '@/composables/useCanvasAnimation'

const router = useRouter()
const authStore = useAuthStore()
const formRef = ref<FormInstance>()
const canvasRef = ref<HTMLCanvasElement>()

const { init: initCanvas } = useCanvasAnimation(canvasRef)

const form = reactive({
  username: '',
  password: ''
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

const isLoggingIn = ref(false)

onMounted(() => {
  initCanvas()
})

async function handleSubmit() {
  if (!formRef.value || isLoggingIn.value) return

  try {
    await formRef.value.validate()
    isLoggingIn.value = true

    const success = await authStore.login({
      username: form.username,
      password: form.password
    })

    if (success) {
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      isLoggingIn.value = false
      ElMessage.error('用户名或密码错误')
    }
  } catch (error) {
    isLoggingIn.value = false
    console.error('登录失败:', error)
  }
}
</script>

<template>
  <main class="login-page" role="main" aria-label="登录页面">
    <!-- 左侧：光影动画区域 -->
    <div class="login-visual" aria-hidden="true">
      <canvas ref="canvasRef" class="light-canvas" />

      <!-- 品牌内容 -->
      <div class="brand-content">
        <div class="brand-logo">
          <div class="logo-icon">
            <el-icon :size="48" color="#3b82f6">
              <Notebook />
            </el-icon>
          </div>
          <h1 class="brand-title">Blog Admin</h1>
        </div>
        <p class="brand-tagline">内容创作 · 灵感管理 · 数据洞察</p>
        <div class="brand-features">
          <div class="feature-item">
            <span class="feature-dot" />
            <span>文章管理</span>
          </div>
          <div class="feature-item">
            <span class="feature-dot" />
            <span>数据分析</span>
          </div>
          <div class="feature-item">
            <span class="feature-dot" />
            <span>内容运营</span>
          </div>
        </div>
      </div>

      <!-- 装饰性文字 -->
      <div class="decorative-text">BLOG ADMIN</div>
    </div>

    <!-- 右侧：登录表单 -->
    <section class="login-form-section" aria-label="登录表单区域">
      <div class="form-container">
        <header class="form-header">
          <h1 class="form-title" id="login-title">欢迎回来</h1>
          <p class="form-subtitle" id="login-desc">请登录您的管理员账户</p>
        </header>

        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          class="login-form"
          aria-labelledby="login-title"
          aria-describedby="login-desc"
          @keyup.enter="handleSubmit"
        >
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              :prefix-icon="User"
              size="large"
              clearable
              class="custom-input"
              aria-label="用户名"
              aria-required="true"
              autocomplete="username"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              size="large"
              show-password
              clearable
              class="custom-input"
              aria-label="密码"
              aria-required="true"
              autocomplete="current-password"
            />
          </el-form-item>

          <el-form-item class="form-actions">
            <el-button
              type="primary"
              class="login-button"
              size="large"
              :loading="isLoggingIn"
              aria-label="登录"
              @click="handleSubmit"
              @keydown.enter="handleSubmit"
              @keydown.space.prevent="handleSubmit"
            >
              {{ isLoggingIn ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <aside class="demo-credentials" aria-label="演示账号信息">
          <div class="divider" role="separator" aria-hidden="true">
            <span>演示账号</span>
          </div>
          <div class="credentials-info">
            <code aria-label="演示账号: admin, 密码: admin123">admin / admin123</code>
          </div>
        </aside>
      </div>

      <!-- 底部版权 -->
      <footer class="login-footer">
        <p>&copy; 2024 Blog Admin. All rights reserved.</p>
      </footer>
    </section>
  </main>
</template>

<style scoped>
.login-page {
  display: flex;
  min-height: 100vh;
  background: var(--color-gray-50);
}

.login-visual {
  position: relative;
  flex: 1;
  min-height: 100vh;
  overflow: hidden;
  background: linear-gradient(135deg, #1a1f2e 0%, #141824 50%, #0d1017 100%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.light-canvas {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.brand-content {
  position: relative;
  z-index: 2;
  text-align: center;
  color: #fff;
  padding: 2rem;
}

.brand-logo {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.logo-icon {
  width: 80px;
  height: 80px;
  background: rgba(59, 130, 246, 0.1);
  border: 2px solid rgba(59, 130, 246, 0.3);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  animation: logoGlow 3s ease-in-out infinite;
}

@keyframes logoGlow {
  0%, 100% {
    box-shadow: 0 0 20px rgba(59, 130, 246, 0.2),
                0 0 40px rgba(59, 130, 246, 0.1),
                inset 0 0 20px rgba(59, 130, 246, 0.05);
  }
  50% {
    box-shadow: 0 0 30px rgba(59, 130, 246, 0.3),
                0 0 60px rgba(59, 130, 246, 0.15),
                inset 0 0 30px rgba(59, 130, 246, 0.1);
  }
}

.brand-title {
  font-family: 'Montserrat', -apple-system, BlinkMacSystemFont, sans-serif;
  font-size: 2.5rem;
  font-weight: 700;
  letter-spacing: 0.05em;
  background: linear-gradient(135deg, #fff 0%, #a0aec0 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.brand-tagline {
  font-size: 1rem;
  color: rgba(255, 255, 255, 0.6);
  letter-spacing: 0.3em;
  margin-bottom: 3rem;
  font-weight: 300;
}

.brand-features {
  display: flex;
  justify-content: center;
  gap: 2rem;
  flex-wrap: wrap;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: rgba(255, 255, 255, 0.7);
}

.feature-dot {
  width: 6px;
  height: 6px;
  background: #3b82f6;
  border-radius: 50%;
  animation: dotPulse 2s ease-in-out infinite;
}

.feature-item:nth-child(2) .feature-dot { animation-delay: 0.3s; }
.feature-item:nth-child(3) .feature-dot { animation-delay: 0.6s; }

@keyframes dotPulse {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.5; transform: scale(0.8); }
}

.decorative-text {
  position: absolute;
  bottom: 2rem;
  left: 50%;
  transform: translateX(-50%);
  font-family: 'Montserrat', sans-serif;
  font-size: 0.75rem;
  letter-spacing: 1em;
  color: rgba(255, 255, 255, 0.1);
  z-index: 2;
  white-space: nowrap;
}

.login-form-section {
  width: 480px;
  min-height: 100vh;
  background: #fff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 2rem;
  position: relative;
}

.form-container {
  width: 100%;
  max-width: 360px;
}

.form-header {
  text-align: center;
  margin-bottom: 2.5rem;
}

.form-title {
  font-family: 'Montserrat', -apple-system, sans-serif;
  font-size: 1.75rem;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 0.5rem;
}

.form-subtitle {
  font-size: 0.875rem;
  color: #718096;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 1.5rem;
}

.login-form :deep(.el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 0 0 1px #e2e8f0 inset;
  transition: all 0.3s ease;
  padding: 4px 16px;
}

.login-form :deep(.el-input__wrapper):hover,
.login-form :deep(.el-input__wrapper).is-focus {
  box-shadow: 0 0 0 2px #3b82f6 inset;
}

.login-form :deep(.el-input__inner) {
  height: 48px;
  font-size: 0.9375rem;
}

.login-form :deep(.el-input__inner)::placeholder {
  color: #a0aec0;
}

.login-form :deep(.el-input__icon) {
  color: #a0aec0;
  font-size: 1.1rem;
}

.form-actions {
  margin-top: 2rem;
  margin-bottom: 0 !important;
}

.login-button {
  width: 100%;
  height: 52px;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  letter-spacing: 0.1em;
}

.demo-credentials {
  margin-top: 2rem;
}

.divider {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent, #e2e8f0, transparent);
}

.divider span {
  font-size: 0.75rem;
  color: #a0aec0;
  text-transform: uppercase;
  letter-spacing: 0.1em;
}

.credentials-info {
  text-align: center;
}

.credentials-info code {
  display: inline-block;
  padding: 0.5rem 1rem;
  background: #f7fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-family: 'Fira Code', monospace;
  font-size: 0.8125rem;
  color: #4a5568;
  letter-spacing: 0.05em;
}

.login-footer {
  position: absolute;
  bottom: 1.5rem;
  left: 0;
  right: 0;
  text-align: center;
}

.login-footer p {
  font-size: 0.75rem;
  color: #a0aec0;
}

@media (max-width: 900px) {
  .login-page {
    flex-direction: column;
  }

  .login-visual {
    min-height: 40vh;
    padding: 2rem;
  }

  .brand-title {
    font-size: 2rem;
  }

  .brand-tagline {
    font-size: 0.875rem;
    letter-spacing: 0.2em;
  }

  .decorative-text {
    display: none;
  }

  .login-form-section {
    width: 100%;
    min-height: 60vh;
    padding: 2rem;
  }
}

@media (max-width: 480px) {
  .brand-features {
    flex-direction: column;
    gap: 0.75rem;
  }

  .login-form-section {
    padding: 1.5rem;
  }
}
</style>
