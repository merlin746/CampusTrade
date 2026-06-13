<template>
  <div class="page-container">
    <div class="profile-wrapper fade-in-up">
      <!-- 个人头部卡片 -->
      <div class="profile-hero">
        <div class="hero-bg-glow"></div>
        <div class="hero-content-inner">
          <el-avatar :size="80" :src="profile.avatar" class="hero-avatar">
            {{ profile.username?.charAt(0)?.toUpperCase() }}
          </el-avatar>
          <div class="hero-info">
            <h2 class="hero-name">{{ profile.username }}</h2>
            <div class="hero-meta">
              <span class="credit-badge">
                ⭐ 信誉分 <strong>{{ profile.creditScore }}</strong>
              </span>
              <span class="hero-date">注册于 {{ profile.createTime?.substring(0, 10) }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Tab 内容 -->
      <div class="profile-content">
        <div class="profile-tabs">
          <button :class="['p-tab', { active: activeTab === 'info' }]" @click="activeTab = 'info'">📝 基本资料</button>
          <button :class="['p-tab', { active: activeTab === 'password' }]" @click="activeTab = 'password'">🔒 修改密码</button>
          <button :class="['p-tab', { active: activeTab === 'reviews' }]" @click="activeTab = 'reviews'">💬 我的评价</button>
        </div>

        <div class="tab-content">
          <!-- 基本资料 -->
          <div v-if="activeTab === 'info'" class="tab-panel fade-in-scale">
            <div class="form-group">
              <label class="form-group-label">用户名</label>
              <input class="form-input" :value="profile.username" disabled />
            </div>
            <div class="form-row">
              <div class="form-group">
                <label class="form-group-label">邮箱</label>
                <input class="form-input" v-model="profile.email" placeholder="your@email.com" />
              </div>
              <div class="form-group">
                <label class="form-group-label">手机号</label>
                <input class="form-input" v-model="profile.phone" placeholder="138xxxxxxxx" />
              </div>
            </div>
            <div class="form-group">
              <label class="form-group-label">学号</label>
              <input class="form-input" v-model="profile.studentId" placeholder="如: 2024001" />
            </div>
            <button class="btn-primary" @click="saveProfile" style="margin-top:8px;">
              <el-icon><Check /></el-icon> 保存修改
            </button>
          </div>

          <!-- 修改密码 -->
          <div v-if="activeTab === 'password'" class="tab-panel fade-in-scale">
            <el-form ref="pwdFormRef" :model="pwdForm" :rules="pwdRules" label-position="top">
              <el-form-item label="旧密码" prop="oldPassword">
                <el-input v-model="pwdForm.oldPassword" type="password" show-password class="luxury-input" placeholder="请输入旧密码" />
              </el-form-item>
              <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="pwdForm.newPassword" type="password" show-password class="luxury-input" placeholder="至少6个字符" />
              </el-form-item>
              <el-form-item>
                <button type="button" class="btn-primary" @click="changePassword">
                  <el-icon><Lock /></el-icon> 修改密码
                </button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 评价 -->
          <div v-if="activeTab === 'reviews'" class="tab-panel fade-in-scale">
            <div v-if="myReviews.length > 0" class="review-list">
              <div v-for="r in myReviews" :key="r.id" class="review-card">
                <div class="reviewer">
                  <el-avatar :size="40" :src="r.reviewerAvatar" class="reviewer-avatar">
                    {{ r.reviewerName?.charAt(0) }}
                  </el-avatar>
                  <div>
                    <span class="reviewer-name">{{ r.reviewerName }}</span>
                    <el-rate :model-value="r.score" disabled size="small" />
                  </div>
                </div>
                <p class="review-text">{{ r.content || '无评价内容' }}</p>
                <span class="review-date">{{ r.createTime }}</span>
              </div>
            </div>
            <div v-else class="empty-inner">
              <span>📭</span>
              <p>暂无评价</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import request from '@/utils/request'
import { reviewApi } from '@/api/review'

const userStore = useUserStore()
const activeTab = ref('info')
const pwdFormRef = ref(null)
const myReviews = ref([])

const profile = reactive({
  username: '', email: '', phone: '', studentId: '',
  avatar: '', creditScore: 0, createTime: '',
})

const pwdForm = reactive({ oldPassword: '', newPassword: '' })
const pwdRules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码至少6个字符', trigger: 'blur' },
  ],
}

onMounted(async () => {
  const res = await request.get('/user/profile')
  Object.assign(profile, res.data)
  const revRes = await reviewApi.getByUser(res.data.id, { page: 1, size: 20 })
  myReviews.value = revRes.data.records || []
})

async function saveProfile() {
  await request.put('/user/profile', {
    email: profile.email, phone: profile.phone,
    avatar: profile.avatar, studentId: profile.studentId,
  })
  ElMessage.success('保存成功')
  userStore.refreshUser()
}

async function changePassword() {
  const valid = await pwdFormRef.value.validate().catch(() => false)
  if (!valid) return
  await request.put('/user/password', pwdForm)
  ElMessage.success('密码修改成功')
  pwdForm.oldPassword = ''
  pwdForm.newPassword = ''
}
</script>

<style scoped>
.profile-wrapper { max-width: 800px; margin: 0 auto; }

/* 个人头部 */
.profile-hero {
  position: relative;
  background: #fff;
  border-radius: 24px;
  padding: 32px;
  margin-bottom: 20px;
  border: 1px solid var(--border);
  overflow: hidden;
}

.hero-bg-glow {
  position: absolute;
  right: -40px;
  top: -40px;
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, rgba(99,102,241,0.08), transparent);
  border-radius: 50%;
  pointer-events: none;
}

.hero-content-inner {
  display: flex;
  align-items: center;
  gap: 24px;
  position: relative;
  z-index: 1;
}

.hero-avatar {
  border: 3px solid var(--primary-light);
  box-shadow: 0 8px 24px rgba(99,102,241,0.15);
}

.hero-name { font-size: 24px; font-weight: 800; margin-bottom: 6px; }

.hero-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
  font-size: 13px;
  color: var(--text-secondary);
}

.credit-badge {
  padding: 3px 12px;
  background: linear-gradient(135deg, rgba(99,102,241,0.08), rgba(139,92,246,0.08));
  border-radius: 20px;
  color: var(--primary);
}

.credit-badge strong { color: var(--primary-dark); }

.hero-date { align-self: center; }

/* Tab切换 */
.profile-tabs {
  display: flex;
  gap: 6px;
  margin-bottom: 24px;
  background: #fff;
  padding: 6px;
  border-radius: 14px;
  border: 1px solid var(--border);
  width: fit-content;
}

.p-tab {
  padding: 10px 20px;
  border-radius: 10px;
  border: none;
  background: transparent;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.25s;
  font-family: inherit;
}

.p-tab.active {
  background: linear-gradient(135deg, var(--primary), var(--primary-2));
  color: #fff;
  box-shadow: 0 2px 10px rgba(99,102,241,0.3);
}

.tab-content {
  background: #fff;
  border-radius: 20px;
  padding: 28px 32px;
  border: 1px solid var(--border);
}

.tab-panel { animation: fadeInScale 0.35s var(--ease-out) both; }

/* 表单 */
.form-group {
  margin-bottom: 18px;
}

.form-group-label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: var(--text);
  margin-bottom: 6px;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid var(--border);
  border-radius: 12px;
  font-size: 14px;
  font-family: inherit;
  outline: none;
  transition: all 0.25s;
  background: #fff;
  color: var(--text);
}

.form-input:focus { border-color: var(--primary); box-shadow: 0 0 0 3px rgba(99,102,241,0.06); }
.form-input:disabled { background: var(--bg); color: var(--text-muted); cursor: not-allowed; }

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

@media (max-width: 600px) { .form-row { grid-template-columns: 1fr; } }

.luxury-input :deep(.el-input__wrapper) {
  border-radius: 12px !important;
  border: 2px solid var(--border) !important;
  box-shadow: none !important;
}
.luxury-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary) !important;
  box-shadow: 0 0 0 3px rgba(99,102,241,0.06) !important;
}

/* 评价 */
.review-card {
  padding: 18px 0;
  border-bottom: 1px solid var(--border-light);
}
.review-card:last-child { border-bottom: none; }

.reviewer { display: flex; align-items: center; gap: 12px; margin-bottom: 10px; }
.reviewer-avatar { border: 2px solid var(--primary-light); }
.reviewer-name { display: block; font-weight: 600; font-size: 14px; margin-bottom: 2px; }
.review-text { color: var(--text-secondary); font-size: 14px; line-height: 1.6; margin-bottom: 4px; }
.review-date { font-size: 12px; color: var(--text-muted); }

.empty-inner { text-align: center; padding: 40px; color: var(--text-muted); font-size: 48px; }
.empty-inner p { font-size: 14px; margin-top: 8px; }

@media (max-width: 600px) {
  .profile-hero { padding: 20px; }
  .hero-content-inner { flex-direction: column; text-align: center; }
  .hero-meta { justify-content: center; }
  .tab-content { padding: 20px 16px; }
}
</style>
