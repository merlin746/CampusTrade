<template>
  <div id="app">
    <!-- 主站豪华导航栏 -->
    <header v-if="!$route.path.startsWith('/admin')" class="main-header">
      <div class="header-glow"></div>
      <div class="header-inner">
        <router-link to="/" class="logo">
          <div class="logo-icon-box">
            <span class="logo-emoji">🔄</span>
          </div>
          <div class="logo-text-group">
            <span class="logo-title">校园二手</span>
            <span class="logo-subtitle">Campus Trade</span>
          </div>
        </router-link>

        <nav class="header-nav">
          <router-link to="/" class="nav-item" :class="{ active: $route.path === '/' }">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </router-link>
          <router-link to="/publish" class="nav-item" v-if="userStore.isLoggedIn">
            <el-icon><Plus /></el-icon>
            <span>发布</span>
          </router-link>
        </nav>

        <div class="header-actions">
          <template v-if="userStore.isLoggedIn">
            <router-link to="/publish" class="publish-btn">
              <el-icon><Plus /></el-icon>
              <span>发布商品</span>
            </router-link>
            <el-dropdown trigger="click" @command="handleMenu" placement="bottom-end">
              <div class="user-badge">
                <el-avatar :size="36" :src="userStore.user?.avatar" class="user-avatar">
                  {{ userStore.user?.username?.charAt(0)?.toUpperCase() }}
                </el-avatar>
                <span class="username">{{ userStore.user?.username }}</span>
                <el-icon class="dropdown-arrow"><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu class="luxury-dropdown">
                  <div class="dropdown-user-card">
                    <el-avatar :size="48" :src="userStore.user?.avatar" class="dropdown-avatar">
                      {{ userStore.user?.username?.charAt(0)?.toUpperCase() }}
                    </el-avatar>
                    <div class="dropdown-user-info">
                      <span class="dropdown-username">{{ userStore.user?.username }}</span>
                      <span class="dropdown-role">{{ userStore.isAdmin ? '管理员' : '普通用户' }}</span>
                    </div>
                  </div>
                  <div class="dropdown-divider"></div>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon> 个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="my-goods">
                    <el-icon><Goods /></el-icon> 我的发布
                  </el-dropdown-item>
                  <el-dropdown-item command="my-orders">
                    <el-icon><Document /></el-icon> 我的订单
                  </el-dropdown-item>
                  <el-dropdown-item command="my-favorites">
                    <el-icon><Star /></el-icon> 我的收藏
                  </el-dropdown-item>
                  <el-dropdown-item v-if="userStore.isAdmin" command="admin" class="admin-menu-item">
                    <el-icon><Setting /></el-icon> 管理后台
                  </el-dropdown-item>
                  <div class="dropdown-divider"></div>
                  <el-dropdown-item command="logout" class="logout-menu-item">
                    <el-icon><SwitchButton /></el-icon> 退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login" class="nav-link login-link">登录</router-link>
            <router-link to="/register" class="register-btn">
              <span>注册</span>
              <el-icon><ArrowRight /></el-icon>
            </router-link>
          </template>
        </div>
      </div>
    </header>

    <!-- 主内容区 -->
    <main :class="{ 'admin-main': $route.path.startsWith('/admin') }">
      <router-view v-slot="{ Component, route }">
        <transition name="page-fade" mode="out-in">
          <component :is="Component" :key="route.fullPath" />
        </transition>
      </router-view>
    </main>

    <!-- 豪华页脚 -->
    <footer v-if="!$route.path.startsWith('/admin')" class="main-footer">
      <div class="footer-inner">
        <div class="footer-brand">
          <span class="footer-logo">🔄</span>
          <span class="footer-name">校园二手交易平台</span>
        </div>
        <p class="footer-desc">让闲置物品流动起来，共建绿色校园</p>
        <div class="footer-divider"></div>
        <p class="footer-copy">© 2024 Campus Trade · 用心连接每一位同学</p>
      </div>
    </footer>

    <!-- 回到顶部 -->
    <transition name="fade">
      <button v-if="showBackTop" class="back-to-top" @click="scrollToTop">
        <el-icon><ArrowUp /></el-icon>
      </button>
    </transition>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const showBackTop = ref(false)

function handleScroll() {
  showBackTop.value = window.scrollY > 400
}

function scrollToTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => window.addEventListener('scroll', handleScroll))
onUnmounted(() => window.removeEventListener('scroll', handleScroll))

function handleMenu(command) {
  if (command === 'logout') {
    userStore.logout()
    router.push('/')
    ElMessage.success('已退出登录')
  } else if (command === 'admin') {
    router.push('/admin')
  } else {
    router.push(`/${command}`)
  }
}
</script>

<style scoped>
/* ========== 主站导航栏 ========== */
.main-header {
  position: sticky;
  top: 0;
  z-index: 1000;
  background: rgba(255,255,255,0.78);
  backdrop-filter: blur(24px) saturate(180%);
  -webkit-backdrop-filter: blur(24px) saturate(180%);
  border-bottom: 1px solid rgba(0,0,0,0.06);
  padding: 0 28px;
  height: 64px;
  transition: all 0.3s ease;
}

.header-glow {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, var(--primary), var(--primary-2), var(--primary), transparent);
  opacity: 0.6;
}

.header-inner {
  max-width: 1320px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

/* Logo */
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
}

.logo-icon-box {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  background: linear-gradient(135deg, var(--primary), var(--primary-2));
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(99,102,241,0.3);
}

.logo-emoji {
  font-size: 20px;
}

.logo-text-group {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.logo-title {
  font-size: 18px;
  font-weight: 800;
  background: linear-gradient(135deg, var(--primary-dark), var(--primary-2));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.logo-subtitle {
  font-size: 10px;
  font-weight: 500;
  color: var(--text-muted);
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

/* 导航菜单 */
.header-nav {
  display: flex;
  align-items: center;
  gap: 4px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
  transition: all 0.2s ease;
}

.nav-item:hover {
  background: rgba(99,102,241,0.06);
  color: var(--primary);
}

.nav-item.active {
  background: rgba(99,102,241,0.08);
  color: var(--primary);
  font-weight: 600;
}

/* 右侧操作区 */
.header-actions {
  display: flex;
  align-items: center;
  gap: 14px;
}

.publish-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 18px;
  border-radius: 10px;
  background: linear-gradient(135deg, var(--primary), var(--primary-2));
  color: #fff;
  font-size: 13px;
  font-weight: 600;
  transition: all 0.3s var(--ease-spring);
  box-shadow: 0 2px 10px rgba(99,102,241,0.25);
}

.publish-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(99,102,241,0.4);
}

/* 用户头像区 */
.user-badge {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 12px 4px 4px;
  border-radius: 32px;
  background: rgba(99,102,241,0.04);
  border: 1px solid transparent;
  transition: all 0.2s ease;
}

.user-badge:hover {
  background: rgba(99,102,241,0.08);
  border-color: rgba(99,102,241,0.15);
}

.user-avatar {
  border: 2px solid var(--primary-light);
}

.username {
  font-size: 14px;
  font-weight: 600;
  color: var(--text);
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dropdown-arrow {
  font-size: 12px;
  color: var(--text-muted);
  transition: transform 0.2s;
}

/* 下拉菜单 */
.luxury-dropdown {
  border-radius: 14px !important;
  box-shadow: 0 20px 50px rgba(0,0,0,0.12), 0 0 0 1px rgba(0,0,0,0.04) !important;
  padding: 8px !important;
  overflow: hidden;
  min-width: 220px !important;
}

.dropdown-user-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 14px;
}

.dropdown-avatar {
  border: 2px solid var(--primary-light);
}

.dropdown-user-info {
  display: flex;
  flex-direction: column;
}

.dropdown-username {
  font-weight: 700;
  font-size: 15px;
  color: var(--text);
}

.dropdown-role {
  font-size: 12px;
  color: var(--text-muted);
}

.dropdown-divider {
  height: 1px;
  background: var(--border);
  margin: 4px 0;
}

.admin-menu-item {
  color: var(--accent-2) !important;
  font-weight: 600 !important;
}

.logout-menu-item {
  color: var(--danger) !important;
}

/* 未登录按钮 */
.login-link {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.2s;
}

.login-link:hover {
  background: rgba(0,0,0,0.04);
  color: var(--text);
}

.register-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 20px;
  border-radius: 10px;
  background: linear-gradient(135deg, var(--primary), var(--primary-2));
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  box-shadow: 0 2px 10px rgba(99,102,241,0.25);
  transition: all 0.3s var(--ease-spring);
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(99,102,241,0.4);
}

/* ========== 页面过渡动画 ========== */
.page-fade-enter-active,
.page-fade-leave-active {
  transition: all 0.35s var(--ease-out);
}

.page-fade-enter-from {
  opacity: 0;
  transform: translateY(12px);
}

.page-fade-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}

/* ========== 页脚 ========== */
.main-footer {
  background: linear-gradient(180deg, var(--bg) 0%, #f1f5f9 100%);
  border-top: 1px solid var(--border);
  padding: 40px 24px;
}

.footer-inner {
  max-width: 600px;
  margin: 0 auto;
  text-align: center;
}

.footer-brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 8px;
}

.footer-logo {
  font-size: 24px;
}

.footer-name {
  font-size: 18px;
  font-weight: 700;
  background: linear-gradient(135deg, var(--primary-dark), var(--primary-2));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.footer-desc {
  color: var(--text-secondary);
  font-size: 14px;
  margin-bottom: 16px;
}

.footer-divider {
  width: 60px;
  height: 2px;
  margin: 0 auto 16px;
  background: linear-gradient(90deg, var(--primary-light), var(--primary-2));
  border-radius: 1px;
}

.footer-copy {
  color: var(--text-muted);
  font-size: 12px;
}

/* ========== 回到顶部 ========== */
.back-to-top {
  position: fixed;
  bottom: 32px;
  right: 32px;
  z-index: 99;
  width: 48px;
  height: 48px;
  border-radius: 16px;
  border: none;
  background: linear-gradient(135deg, var(--primary), var(--primary-2));
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 24px rgba(99,102,241,0.35);
  transition: all 0.3s var(--ease-spring);
}

.back-to-top:hover {
  transform: translateY(-4px) scale(1.05);
  box-shadow: 0 12px 32px rgba(99,102,241,0.5);
}

.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

/* ========== 管理后台主区域 ========== */
.admin-main {
  min-height: 100vh;
  background: #f0f2f5;
}

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .main-header {
    padding: 0 14px;
    height: 56px;
  }

  .header-nav {
    display: none;
  }

  .logo-title {
    font-size: 16px;
  }

  .publish-btn span {
    display: none;
  }

  .publish-btn {
    padding: 8px 12px;
    border-radius: 10px;
  }
}
</style>
