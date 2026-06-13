<template>
  <div class="auth-page">
    <!-- 装饰背景 -->
    <div class="auth-bg">
      <div class="auth-orb orb-1"></div>
      <div class="auth-orb orb-2"></div>
      <div class="auth-orb orb-3"></div>
      <div class="auth-grid"></div>
    </div>

    <div class="auth-card glass-card fade-in-scale">
      <div class="card-accent"></div>

      <div class="auth-header">
        <div class="auth-icon-box">
          <span class="auth-icon">✨</span>
        </div>
        <h2 class="auth-title">创建账号</h2>
        <p class="auth-subtitle">加入校园二手交易平台，开启闲置流转</p>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" size="large" class="auth-form">
        <el-form-item prop="username">
          <label class="form-label">用户名</label>
          <el-input
            v-model="form.username"
            placeholder="3-20个字符"
            :prefix-icon="User"
            class="luxury-input"
          />
        </el-form-item>
        <el-form-item prop="password">
          <label class="form-label">密码</label>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="6-30个字符"
            :prefix-icon="Lock"
            show-password
            class="luxury-input"
          />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <label class="form-label">确认密码</label>
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            :prefix-icon="Lock"
            show-password
            class="luxury-input"
          />
        </el-form-item>
        <el-form-item prop="email">
          <label class="form-label">邮箱 <span class="optional">选填</span></label>
          <el-input
            v-model="form.email"
            placeholder="your@email.com"
            :prefix-icon="Message"
            class="luxury-input"
          />
        </el-form-item>
        <el-form-item prop="studentId">
          <label class="form-label">学号 <span class="optional">选填</span></label>
          <el-input
            v-model="form.studentId"
            placeholder="如: 2024001"
            :prefix-icon="School"
            class="luxury-input"
          />
        </el-form-item>
        <el-form-item>
          <button type="button" class="auth-submit-btn" :disabled="loading" @click="handleRegister">
            <span v-if="!loading">注 册</span>
            <span v-else class="btn-loading">
              <span class="loading-dot"></span>
              <span class="loading-dot"></span>
              <span class="loading-dot"></span>
            </span>
          </button>
        </el-form-item>
      </el-form>

      <div class="auth-footer">
        <span>已有账号？</span>
        <router-link to="/login">立即登录 →</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { User, Lock, Message, School } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const formRef = ref(null)

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  studentId: '',
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 30, message: '密码长度为6-30个字符', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.password) callback(new Error('两次密码不一致'))
        else callback()
      },
      trigger: 'blur',
    },
  ],
}

async function handleRegister() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await userStore.register({
      username: form.username,
      password: form.password,
      email: form.email,
      studentId: form.studentId,
    })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch { /* handled */ }
  finally { loading.value = false }
}
</script>

<style scoped>
/* ========== 页面背景 ========== */
.auth-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 64px);
  padding: 24px;
  position: relative;
  overflow: hidden;
}

.auth-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
}

.auth-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
  opacity: 0.3;
}

.orb-1 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(139,92,246,0.4), transparent);
  top: -150px;
  right: -100px;
  animation: float 8s ease-in-out infinite;
}

.orb-2 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(99,102,241,0.35), transparent);
  bottom: -100px;
  left: -80px;
  animation: float 7s ease-in-out infinite reverse;
}

.orb-3 {
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, rgba(245,158,11,0.2), transparent);
  top: 50%;
  left: 50%;
  animation: float 6s ease-in-out infinite 1s;
}

.auth-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(99,102,241,0.04) 1px, transparent 1px),
    linear-gradient(90deg, rgba(99,102,241,0.04) 1px, transparent 1px);
  background-size: 50px 50px;
}

/* ========== 卡片 ========== */
.auth-card {
  position: relative;
  z-index: 1;
  width: 440px;
  padding: 44px 40px;
  overflow: hidden;
}

.card-accent {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--primary), var(--primary-2), var(--accent-2));
}

/* ========== 头部 ========== */
.auth-header {
  text-align: center;
  margin-bottom: 32px;
  padding-top: 8px;
}

.auth-icon-box {
  width: 72px;
  height: 72px;
  margin: 0 auto 16px;
  border-radius: 20px;
  background: linear-gradient(135deg, rgba(99,102,241,0.1), rgba(139,92,246,0.1));
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 24px rgba(99,102,241,0.12);
}

.auth-icon {
  font-size: 36px;
}

.auth-title {
  font-size: 26px;
  font-weight: 800;
  background: linear-gradient(135deg, var(--text), var(--primary-dark));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 6px;
}

.auth-subtitle {
  font-size: 14px;
  color: var(--text-secondary);
}

/* ========== 表单 ========== */
.auth-form :deep(.el-form-item) {
  margin-bottom: 18px;
}

.form-label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: var(--text);
  margin-bottom: 6px;
  letter-spacing: 0.02em;
}

.optional {
  font-weight: 400;
  color: var(--text-muted);
  font-size: 11px;
}

.luxury-input :deep(.el-input__wrapper) {
  border-radius: 12px !important;
  border: 2px solid var(--border) !important;
  box-shadow: none !important;
  transition: all 0.3s ease !important;
  padding: 8px 12px !important;
}

.luxury-input :deep(.el-input__wrapper:hover) {
  border-color: var(--primary-light) !important;
}

.luxury-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary) !important;
  box-shadow: 0 0 0 4px rgba(99,102,241,0.08) !important;
}

/* ========== 提交按钮 ========== */
.auth-submit-btn {
  width: 100%;
  height: 50px;
  border: none;
  border-radius: 14px;
  background: linear-gradient(135deg, var(--primary), var(--primary-2), var(--primary-dark));
  background-size: 200% 200%;
  color: #fff;
  font-size: 17px;
  font-weight: 700;
  letter-spacing: 0.06em;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.4s var(--ease-spring);
  position: relative;
  overflow: hidden;
  animation: gradientShift 4s ease infinite;
  margin-top: 4px;
}

.auth-submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(99,102,241,0.45), 0 0 60px rgba(139,92,246,0.12);
}

.auth-submit-btn:active {
  transform: scale(0.97);
}

.auth-submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.auth-submit-btn::after {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent 40%, rgba(255,255,255,0.12) 50%, transparent 60%);
  animation: shimmer 3s ease-in-out infinite;
}

.btn-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.loading-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #fff;
  animation: dotBounce 1.4s ease-in-out infinite both;
}

.loading-dot:nth-child(1) { animation-delay: -0.32s; }
.loading-dot:nth-child(2) { animation-delay: -0.16s; }

@keyframes dotBounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

/* ========== 底部 ========== */
.auth-footer {
  text-align: center;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid var(--border);
  font-size: 14px;
  color: var(--text-secondary);
}

.auth-footer a {
  color: var(--primary);
  margin-left: 6px;
  font-weight: 700;
  transition: color 0.2s;
}

.auth-footer a:hover {
  color: var(--primary-dark);
}

/* ========== 响应式 ========== */
@media (max-width: 500px) {
  .auth-card {
    width: 100%;
    padding: 32px 20px;
  }
}
</style>
