<template>
  <div class="admin-layout">
    <!-- 侧边栏 -->
    <aside class="admin-sidebar">
      <div class="sidebar-brand">
        <div class="brand-icon-box">
          <span class="brand-icon">🔄</span>
        </div>
        <div class="brand-text">
          <span class="brand-name">校园二手</span>
          <span class="brand-label">管理后台</span>
        </div>
      </div>

      <nav class="sidebar-nav">
        <router-link to="/admin/dashboard" :class="['nav-link', { active: $route.path === '/admin/dashboard' }]">
          <span class="nav-icon-box"><el-icon :size="18"><DataAnalysis /></el-icon></span>
          <span>仪表盘</span>
          <span v-if="$route.path === '/admin/dashboard'" class="active-indicator"></span>
        </router-link>
        <router-link to="/admin/users" :class="['nav-link', { active: $route.path === '/admin/users' }]">
          <span class="nav-icon-box"><el-icon :size="18"><UserFilled /></el-icon></span>
          <span>用户管理</span>
          <span v-if="$route.path === '/admin/users'" class="active-indicator"></span>
        </router-link>
        <router-link to="/admin/goods-pending" :class="['nav-link', { active: $route.path === '/admin/goods-pending' }]">
          <span class="nav-icon-box"><el-icon :size="18"><Checked /></el-icon></span>
          <span>商品审核</span>
          <span v-if="$route.path === '/admin/goods-pending'" class="active-indicator"></span>
        </router-link>
        <router-link to="/admin/goods" :class="['nav-link', { active: $route.path === '/admin/goods' }]">
          <span class="nav-icon-box"><el-icon :size="18"><Goods /></el-icon></span>
          <span>商品管理</span>
          <span v-if="$route.path === '/admin/goods'" class="active-indicator"></span>
        </router-link>
        <router-link to="/admin/categories" :class="['nav-link', { active: $route.path === '/admin/categories' }]">
          <span class="nav-icon-box"><el-icon :size="18"><Menu /></el-icon></span>
          <span>分类管理</span>
          <span v-if="$route.path === '/admin/categories'" class="active-indicator"></span>
        </router-link>
      </nav>

      <div class="sidebar-footer">
        <router-link to="/" class="back-link">
          <el-icon><HomeFilled /></el-icon>
          <span>返回前台</span>
        </router-link>
      </div>
    </aside>

    <!-- 主体 -->
    <div class="admin-main">
      <header class="admin-topbar">
        <div class="topbar-left">
          <h1 class="topbar-title">{{ $route.meta.title }}</h1>
        </div>
        <div class="topbar-right">
          <div class="admin-info">
            <el-avatar :size="34" :src="userStore.user?.avatar" class="tb-avatar">
              {{ userStore.user?.username?.charAt(0)?.toUpperCase() }}
            </el-avatar>
            <span class="tb-username">{{ userStore.user?.username }}</span>
            <span class="tb-badge">管理员</span>
          </div>
          <button class="logout-btn" @click="handleLogout">
            <el-icon><SwitchButton /></el-icon>
          </button>
        </div>
      </header>
      <div class="admin-content">
        <router-view v-slot="{ Component }">
          <transition name="page-fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

function handleLogout() {
  userStore.logout()
  router.push('/')
  ElMessage.success('已退出')
}
</script>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  background: #f0f2f5;
}

/* ========== 侧边栏 ========== */
.admin-sidebar {
  width: 250px;
  background: linear-gradient(180deg, #0f172a 0%, #1e293b 100%);
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 100;
  border-right: 1px solid rgba(255,255,255,0.05);
}

.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 24px 20px;
  border-bottom: 1px solid rgba(255,255,255,0.06);
}

.brand-icon-box {
  width: 42px;
  height: 42px;
  border-radius: 12px;
  background: linear-gradient(135deg, var(--primary), var(--primary-2));
  display: flex;
  align-items: center;
  justify-content: center;
}

.brand-icon { font-size: 20px; }

.brand-text {
  display: flex;
  flex-direction: column;
}

.brand-name {
  font-size: 16px;
  font-weight: 800;
  color: #fff;
  letter-spacing: 0.02em;
}

.brand-label {
  font-size: 11px;
  color: rgba(255,255,255,0.4);
  letter-spacing: 0.06em;
}

.sidebar-nav {
  flex: 1;
  padding: 16px 12px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 12px;
  color: rgba(255,255,255,0.55);
  font-size: 14px;
  font-weight: 500;
  transition: all 0.25s ease;
  position: relative;
  text-decoration: none;
}

.nav-link:hover {
  background: rgba(255,255,255,0.06);
  color: rgba(255,255,255,0.85);
}

.nav-link.active {
  background: rgba(99,102,241,0.15);
  color: #fff;
  font-weight: 700;
}

.active-indicator {
  position: absolute;
  right: -12px;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 28px;
  background: linear-gradient(180deg, var(--primary), var(--primary-2));
  border-radius: 2px 0 0 2px;
}

.nav-icon-box {
  width: 34px;
  height: 34px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255,255,255,0.04);
  transition: all 0.25s;
}

.nav-link.active .nav-icon-box {
  background: rgba(99,102,241,0.2);
}

.sidebar-footer {
  padding: 16px 12px;
  border-top: 1px solid rgba(255,255,255,0.06);
}

.back-link {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  border-radius: 12px;
  color: rgba(255,255,255,0.45);
  font-size: 14px;
  font-weight: 500;
  transition: all 0.25s;
  text-decoration: none;
}

.back-link:hover {
  background: rgba(255,255,255,0.05);
  color: rgba(255,255,255,0.75);
}

/* ========== 主区域 ========== */
.admin-main {
  flex: 1;
  margin-left: 250px;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 顶栏 */
.admin-topbar {
  height: 64px;
  background: #fff;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 28px;
  position: sticky;
  top: 0;
  z-index: 50;
}

.topbar-title {
  font-size: 18px;
  font-weight: 800;
  color: var(--text);
}

.topbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.admin-info {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 4px 14px 4px 4px;
  border-radius: 30px;
  background: var(--bg);
}

.tb-avatar {
  border: 2px solid var(--primary-light);
}

.tb-username {
  font-size: 14px;
  font-weight: 600;
}

.tb-badge {
  padding: 3px 10px;
  border-radius: 6px;
  background: linear-gradient(135deg, rgba(99,102,241,0.1), rgba(139,92,246,0.1));
  color: var(--primary);
  font-size: 11px;
  font-weight: 700;
}

.logout-btn {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  border: 1.5px solid #e5e7eb;
  background: #fff;
  color: var(--text-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.logout-btn:hover {
  border-color: #fecaca;
  color: #ef4444;
  background: #fef2f2;
}

/* 内容区 */
.admin-content {
  padding: 28px;
  flex: 1;
}

/* 过渡动画 */
.page-fade-enter-active,
.page-fade-leave-active {
  transition: all 0.3s var(--ease-out);
}

.page-fade-enter-from { opacity: 0; transform: translateY(10px); }
.page-fade-leave-to { opacity: 0; transform: translateY(-6px); }

@media (max-width: 768px) {
  .admin-sidebar { width: 72px; }
  .brand-text, .nav-link span:not(.nav-icon-box), .back-link span, .active-indicator { display: none; }
  .admin-main { margin-left: 72px; }
  .nav-link { justify-content: center; padding: 12px; }
}
</style>
