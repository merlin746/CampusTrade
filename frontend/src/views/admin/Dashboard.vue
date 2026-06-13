<template>
  <div class="dashboard">
    <div class="dash-header fade-in-up">
      <h2 class="dash-title">📊 数据概览</h2>
      <span class="dash-date">{{ today }}</span>
    </div>

    <!-- 核心统计 -->
    <div class="stat-grid fade-in-up stagger-1">
      <div class="stat-card purple">
        <div class="stat-icon-wrap">
          <el-icon :size="26"><User /></el-icon>
        </div>
        <div class="stat-body">
          <span class="stat-num">{{ data.totalUsers }}</span>
          <span class="stat-label">总用户数</span>
        </div>
        <div class="stat-bg-icon"><el-icon :size="60"><User /></el-icon></div>
      </div>
      <div class="stat-card amber">
        <div class="stat-icon-wrap">
          <el-icon :size="26"><Goods /></el-icon>
        </div>
        <div class="stat-body">
          <span class="stat-num">{{ data.totalGoods }}</span>
          <span class="stat-label">总商品数</span>
        </div>
        <div class="stat-bg-icon"><el-icon :size="60"><Goods /></el-icon></div>
      </div>
      <div class="stat-card green">
        <div class="stat-icon-wrap">
          <el-icon :size="26"><Document /></el-icon>
        </div>
        <div class="stat-body">
          <span class="stat-num">{{ data.totalOrders }}</span>
          <span class="stat-label">总订单数</span>
        </div>
        <div class="stat-bg-icon"><el-icon :size="60"><Document /></el-icon></div>
      </div>
      <div class="stat-card red">
        <div class="stat-icon-wrap">
          <el-icon :size="26"><WarningFilled /></el-icon>
        </div>
        <div class="stat-body">
          <span class="stat-num">{{ data.pendingGoods }}</span>
          <span class="stat-label">待审核商品</span>
        </div>
        <div class="stat-bg-icon"><el-icon :size="60"><WarningFilled /></el-icon></div>
      </div>
    </div>

    <!-- 今日数据 -->
    <div class="section-header fade-in-up stagger-4">
      <h3>📅 今日数据</h3>
      <div class="section-line"></div>
    </div>

    <div class="today-grid fade-in-up stagger-5">
      <div class="today-card">
        <div class="today-ring new-users">
          <span class="ring-value">{{ data.todayNewUsers }}</span>
          <span class="ring-label">新增用户</span>
        </div>
      </div>
      <div class="today-card">
        <div class="today-ring new-goods">
          <span class="ring-value">{{ data.todayNewGoods }}</span>
          <span class="ring-label">新增商品</span>
        </div>
      </div>
      <div class="today-card">
        <div class="today-ring new-orders">
          <span class="ring-value">{{ data.todayOrders }}</span>
          <span class="ring-label">新订单数</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { adminApi } from '@/api/admin'

const today = new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' })

const data = ref({
  totalUsers: 0, totalGoods: 0, totalOrders: 0,
  pendingGoods: 0, todayNewUsers: 0, todayNewGoods: 0, todayOrders: 0,
})

onMounted(async () => {
  const res = await adminApi.dashboard()
  data.value = res.data
})
</script>

<style scoped>
.dashboard { max-width: 1100px; }

.dash-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
}

.dash-title { font-size: 24px; font-weight: 800; }
.dash-date { font-size: 14px; color: var(--text-muted); }

/* 统计卡片 */
.stat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
}

.stat-card {
  background: #fff;
  border-radius: 20px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 18px;
  position: relative;
  overflow: hidden;
  border: 1px solid var(--border);
  transition: all 0.35s var(--ease-out);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 36px rgba(0,0,0,0.08);
}

.stat-bg-icon {
  position: absolute;
  right: -10px;
  bottom: -10px;
  opacity: 0.06;
  color: currentColor;
}

.stat-icon-wrap {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-card.purple .stat-icon-wrap { background: rgba(99,102,241,0.1); color: var(--primary); }
.stat-card.purple .stat-bg-icon { color: var(--primary); }
.stat-card.purple .stat-num { color: var(--primary); }

.stat-card.amber .stat-icon-wrap { background: rgba(245,158,11,0.1); color: var(--warning); }
.stat-card.amber .stat-bg-icon { color: var(--warning); }
.stat-card.amber .stat-num { color: var(--warning); }

.stat-card.green .stat-icon-wrap { background: rgba(16,185,129,0.1); color: var(--success); }
.stat-card.green .stat-bg-icon { color: var(--success); }
.stat-card.green .stat-num { color: var(--success); }

.stat-card.red .stat-icon-wrap { background: rgba(239,68,68,0.1); color: var(--danger); }
.stat-card.red .stat-bg-icon { color: var(--danger); }
.stat-card.red .stat-num { color: var(--danger); }

.stat-body {
  display: flex;
  flex-direction: column;
  position: relative;
  z-index: 1;
}

.stat-num {
  font-size: 34px;
  font-weight: 900;
  line-height: 1;
  font-variant-numeric: tabular-nums;
}

.stat-label {
  font-size: 13px;
  color: var(--text-muted);
  margin-top: 4px;
  font-weight: 500;
}

/* 今日 */
.section-header {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 20px;
}

.section-header h3 {
  font-size: 18px;
  font-weight: 700;
  white-space: nowrap;
}

.section-line {
  flex: 1;
  height: 2px;
  background: linear-gradient(90deg, var(--border), transparent);
  border-radius: 1px;
}

.today-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.today-card {
  background: #fff;
  border-radius: 20px;
  padding: 32px 24px;
  border: 1px solid var(--border);
  display: flex;
  justify-content: center;
  transition: all 0.3s ease;
}

.today-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 28px rgba(0,0,0,0.05);
}

.today-ring {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 4px solid;
}

.today-ring.new-users { border-color: var(--primary); }
.today-ring.new-goods { border-color: var(--warning); }
.today-ring.new-orders { border-color: var(--success); }

.ring-value {
  font-size: 32px;
  font-weight: 900;
  line-height: 1;
}

.ring-label {
  font-size: 12px;
  color: var(--text-muted);
  margin-top: 4px;
  font-weight: 500;
}

.today-ring.new-users .ring-value { color: var(--primary); }
.today-ring.new-goods .ring-value { color: var(--warning); }
.today-ring.new-orders .ring-value { color: var(--success); }

@media (max-width: 768px) {
  .today-grid { grid-template-columns: 1fr; }
}
</style>
