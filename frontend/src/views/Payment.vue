<template>
  <div class="pay-page" v-if="goods">
    <!-- 顶部步骤条 -->
    <div class="pay-steps fade-in-up">
      <div class="step done">
        <div class="step-num">1</div>
        <span>确认商品</span>
      </div>
      <div class="step-line done"></div>
      <div class="step active">
        <div class="step-num">2</div>
        <span>选择支付</span>
      </div>
      <div class="step-line"></div>
      <div class="step">
        <div class="step-num">3</div>
        <span>下单完成</span>
      </div>
    </div>

    <div class="pay-layout fade-in-up stagger-1">
      <!-- 左侧：支付方式 -->
      <div class="pay-main">
        <div class="pay-card">
          <h3 class="card-hd">选择支付方式</h3>

          <div class="pay-methods">
            <button
              :class="['method-item', { active: payMethod === 'wechat' }]"
              @click="payMethod = 'wechat'"
            >
              <div class="method-icon wechat">
                <svg viewBox="0 0 24 24" width="36" height="36"><path fill="#07c160" d="M8.5 11a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zM8 14l2 2 4-4-1-1-3 3-1-1-1 1zM12 2C6.48 2 2 5.92 2 10.75c0 2.67 1.33 5.07 3.4 6.58L4.5 20l3.16-1.58c.82.25 1.7.4 2.62.47.08 0 .15-.01.23-.01h.76c5.56-.38 9.73-4.63 9.73-8.96C21 5.92 17.52 2 12 2z"/></svg>
              </div>
              <div class="method-body">
                <span class="method-name">微信支付</span>
                <span class="method-desc">推荐安装微信的用户使用</span>
              </div>
              <div class="method-check" v-if="payMethod === 'wechat'">
                <el-icon :size="20"><Check /></el-icon>
              </div>
            </button>

            <button
              :class="['method-item', { active: payMethod === 'alipay' }]"
              @click="payMethod = 'alipay'"
            >
              <div class="method-icon alipay">
                <svg viewBox="0 0 24 24" width="36" height="36"><path fill="#1677ff" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm4.5 6h-2.24c-.41 0-.77.23-.92.59l-1.61 3.37c-.16.34-.01.74.33.9.17.08.35.12.53.12.24 0 .47-.1.62-.29l1.69-2.06c.36-.44.9-.7 1.47-.7h.57c.65 0 1.25.32 1.62.86l.03.04c.41.57.52 1.29.3 1.94l-.85 2.5c-.22.64-.8 1.07-1.48 1.07H9.54c-.78 0-1.48-.48-1.72-1.18l-.04-.13c-.04-.13.06-.27.2-.27h1.69c.31 0 .56-.25.56-.56s-.25-.56-.56-.56H7.8c-.45 0-.88.26-1.07.67l-.01.03c-.24.5-.29 1.08-.12 1.6l.08.23c.52 1.52 1.96 2.55 3.59 2.55h4.67c1.5 0 2.86-.82 3.55-2.12l.42-.82c.72-1.4.56-3.05-.43-4.34-.8-1.05-2.09-1.8-3.59-1.8z"/></svg>
              </div>
              <div class="method-body">
                <span class="method-name">支付宝</span>
                <span class="method-desc">推荐安装支付宝的用户使用</span>
              </div>
              <div class="method-check" v-if="payMethod === 'alipay'">
                <el-icon :size="20"><Check /></el-icon>
              </div>
            </button>
          </div>
        </div>
      </div>

      <!-- 右侧：订单摘要 + 扫码 -->
      <div class="pay-side">
        <!-- 订单摘要 -->
        <div class="order-summary">
          <h3 class="card-hd">订单摘要</h3>
          <div class="summary-goods">
            <img :src="goods.images?.[0] || '/placeholder.png'" :alt="goods.title" class="summary-img" />
            <div class="summary-info">
              <h4 class="summary-title">{{ goods.title }}</h4>
              <div class="summary-meta">
                <span class="summary-seller">{{ goods.sellerName }}</span>
                <span class="summary-condition">{{ conditionMap[goods.conditionLevel] }}</span>
              </div>
            </div>
          </div>
          <div class="summary-price-row">
            <span class="summary-label">商品金额</span>
            <span class="summary-price">¥{{ goods.price }}</span>
          </div>
          <div class="summary-divider"></div>
          <div class="summary-total">
            <span>实付金额</span>
            <span class="total-price">¥{{ goods.price }}</span>
          </div>
        </div>

        <!-- 二维码 -->
        <div class="qr-card">
          <h3 class="card-hd">扫码支付</h3>
          <div class="qr-section" v-if="!paying">
            <div class="qr-box">
              <div class="qr-wrapper">
                <!-- 模拟二维码 -->
                <svg viewBox="0 0 200 200" class="qr-svg">
                  <rect width="200" height="200" fill="#fff"/>
                  <!-- 定位图案 -->
                  <rect x="10" y="10" width="60" height="60" rx="8" fill="#111"/>
                  <rect x="18" y="18" width="44" height="44" rx="4" fill="#fff"/>
                  <rect x="26" y="26" width="28" height="28" rx="2" fill="#111"/>

                  <rect x="130" y="10" width="60" height="60" rx="8" fill="#111"/>
                  <rect x="138" y="18" width="44" height="44" rx="4" fill="#fff"/>
                  <rect x="146" y="26" width="28" height="28" rx="2" fill="#111"/>

                  <rect x="10" y="130" width="60" height="60" rx="8" fill="#111"/>
                  <rect x="18" y="138" width="44" height="44" rx="4" fill="#fff"/>
                  <rect x="26" y="146" width="28" height="28" rx="2" fill="#111"/>

                  <!-- 随机数据块 -->
                  <rect x="80" y="20" width="10" height="10" fill="#111"/>
                  <rect x="100" y="20" width="10" height="10" fill="#111"/>
                  <rect x="90" y="40" width="10" height="10" fill="#111"/>
                  <rect x="130" y="80" width="10" height="10" fill="#111"/>
                  <rect x="150" y="90" width="10" height="10" fill="#111"/>
                  <rect x="140" y="110" width="10" height="10" fill="#111"/>
                  <rect x="80" y="100" width="10" height="10" fill="#111"/>
                  <rect x="110" y="120" width="10" height="10" fill="#111"/>
                  <rect x="70" y="130" width="10" height="10" fill="#111"/>
                  <rect x="80" y="150" width="10" height="10" fill="#111"/>
                  <rect x="120" y="150" width="10" height="10" fill="#111"/>
                  <rect x="100" y="170" width="10" height="10" fill="#111"/>
                  <rect x="90" y="170" width="10" height="10" fill="#111"/>
                  <rect x="170" y="140" width="10" height="10" fill="#111"/>
                  <rect x="160" y="170" width="10" height="10" fill="#111"/>
                  <rect x="90" y="180" width="10" height="10" fill="#111"/>
                  <rect x="170" y="170" width="10" height="10" fill="#111"/>
                  <rect x="20" y="80" width="10" height="10" fill="#111"/>
                  <rect x="40" y="100" width="10" height="10" fill="#111"/>
                  <rect x="30" y="120" width="10" height="10" fill="#111"/>
                  <rect x="50" y="170" width="10" height="10" fill="#111"/>
                  <rect x="60" y="180" width="10" height="10" fill="#111"/>
                  <rect x="100" y="90" width="10" height="10" fill="#111"/>
                  <rect x="120" y="80" width="10" height="10" fill="#111"/>
                  <rect x="150" y="120" width="10" height="10" fill="#111"/>
                </svg>
                <div class="qr-logo">
                  <svg v-if="payMethod === 'wechat'" viewBox="0 0 24 24" width="32" height="32"><path fill="#07c160" d="M8.5 11a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zM8 14l2 2 4-4-1-1-3 3-1-1-1 1zM12 2C6.48 2 2 5.92 2 10.75c0 2.67 1.33 5.07 3.4 6.58L4.5 20l3.16-1.58c.82.25 1.7.4 2.62.47.08 0 .15-.01.23-.01h.76c5.56-.38 9.73-4.63 9.73-8.96C21 5.92 17.52 2 12 2z"/></svg>
                  <svg v-else viewBox="0 0 24 24" width="32" height="32"><path fill="#1677ff" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm4.5 6h-2.24c-.41 0-.77.23-.92.59l-1.61 3.37c-.16.34-.01.74.33.9.17.08.35.12.53.12.24 0 .47-.1.62-.29l1.69-2.06c.36-.44.9-.7 1.47-.7h.57c.65 0 1.25.32 1.62.86l.03.04c.41.57.52 1.29.3 1.94l-.85 2.5c-.22.64-.8 1.07-1.48 1.07H9.54c-.78 0-1.48-.48-1.72-1.18l-.04-.13c-.04-.13.06-.27.2-.27h1.69c.31 0 .56-.25.56-.56s-.25-.56-.56-.56H7.8c-.45 0-.88.26-1.07.67l-.01.03c-.24.5-.29 1.08-.12 1.6l.08.23c.52 1.52 1.96 2.55 3.59 2.55h4.67c1.5 0 2.86-.82 3.55-2.12l.42-.82c.72-1.4.56-3.05-.43-4.34-.8-1.05-2.09-1.8-3.59-1.8z"/></svg>
                </div>
              </div>
            </div>
            <p class="qr-hint">请使用{{ payMethod === 'wechat' ? '微信' : '支付宝' }}扫一扫完成支付</p>
            <p class="qr-tip">扫码支付演示 · 实际支付请在正式环境中配置</p>
          </div>

          <!-- 支付成功动画 -->
          <div class="pay-success" v-else>
            <div class="success-check">
              <svg viewBox="0 0 80 80" width="80" height="80">
                <circle cx="40" cy="40" r="38" fill="none" stroke="#10b981" stroke-width="3" class="circle-check"/>
                <polyline points="22,42 35,55 58,27" fill="none" stroke="#10b981" stroke-width="4" stroke-linecap="round" stroke-linejoin="round" class="path-check"/>
              </svg>
            </div>
            <h3 class="success-text">支付成功</h3>
            <p class="success-sub">正在为您创建订单...</p>
          </div>

          <!-- 确认支付按钮 -->
          <button
            class="confirm-pay-btn"
            :disabled="paying"
            @click="handleConfirmPay"
          >
            <span v-if="!paying">
              确认支付 ¥{{ goods.price }}
            </span>
            <span v-else>
              <span class="spinner"></span> 处理中...
            </span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { goodsApi } from '@/api/goods'
import { orderApi } from '@/api/order'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const goods = ref(null)
const payMethod = ref('wechat')
const paying = ref(false)

const conditionMap = { 1: '全新', 2: '几乎全新', 3: '有使用痕迹', 4: '明显瑕疵' }

onMounted(async () => {
  try {
    const res = await goodsApi.detail(route.params.goodsId)
    if (!res.data || res.data.status !== 1) {
      ElMessage.error('该商品已下架或已售出')
      router.push('/')
      return
    }
    if (res.data.sellerName === userStore.user?.username) {
      ElMessage.warning('不能购买自己的商品')
      router.back()
      return
    }
    goods.value = res.data
  } catch {
    ElMessage.error('商品不存在')
    router.push('/')
  }
})

async function handleConfirmPay() {
  paying.value = true

  // 模拟支付过程
  await new Promise(resolve => setTimeout(resolve, 2000))

  try {
    // 创建订单
    await orderApi.create({ goodsId: goods.value.id })
    ElMessage.success('下单成功！请等待卖家确认')
    router.push('/my-orders')
  } catch {
    ElMessage.error('下单失败')
  } finally {
    paying.value = false
  }
}
</script>

<style scoped>
.pay-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 32px 24px 60px;
  min-height: calc(100vh - 64px);
  animation: pageEnter 0.5s var(--ease-out);
}

/* ========== 步骤条 ========== */
.pay-steps {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0;
  margin-bottom: 40px;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: #cbd5e1;
  font-size: 13px;
  font-weight: 500;
}

.step.done { color: var(--primary); }
.step.active { color: var(--text); }

.step-num {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #e5e7eb;
  color: #9ca3af;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
  font-size: 15px;
  transition: all 0.3s;
}

.step.done .step-num { background: linear-gradient(135deg,#6366f1,#8b5cf6); color: #fff; }
.step.active .step-num { background: var(--text); color: #fff; }

.step-line {
  width: 80px;
  height: 2px;
  background: #e5e7eb;
  margin: 0 8px;
  margin-top: -18px;
  transition: background 0.3s;
}

.step-line.done { background: var(--primary); }

/* ========== 布局 ========== */
.pay-layout {
  display: grid;
  grid-template-columns: 1fr 400px;
  gap: 24px;
}

@media (max-width: 860px) {
  .pay-layout { grid-template-columns: 1fr; }
}

/* ========== 支付方式 ========== */
.pay-card, .order-summary, .qr-card {
  background: #fff;
  border-radius: 18px;
  border: 1px solid var(--border);
  padding: 24px;
}

.card-hd {
  font-size: 17px;
  font-weight: 700;
  margin-bottom: 20px;
}

.pay-methods {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.method-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 18px 20px;
  border-radius: 14px;
  border: 2px solid var(--border);
  background: #fff;
  cursor: pointer;
  transition: all 0.25s;
  font-family: inherit;
  text-align: left;
}

.method-item:hover { border-color: var(--primary-light); background: #fafbff; }

.method-item.active {
  border-color: var(--primary);
  background: rgba(99,102,241,0.04);
  box-shadow: 0 4px 16px rgba(99,102,241,0.08);
}

.method-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.method-icon.wechat { background: rgba(7,193,96,0.08); }
.method-icon.alipay { background: rgba(22,119,255,0.08); }

.method-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.method-name {
  font-size: 15px;
  font-weight: 700;
  color: var(--text);
}

.method-desc {
  font-size: 12px;
  color: var(--text-muted);
}

.method-check {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary), var(--primary-2));
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* ========== 订单摘要 ========== */
.order-summary { margin-bottom: 20px; }

.summary-goods {
  display: flex;
  gap: 14px;
  margin-bottom: 18px;
  padding-bottom: 18px;
  border-bottom: 1px solid var(--border-light);
}

.summary-img {
  width: 72px;
  height: 72px;
  border-radius: 12px;
  object-fit: cover;
  background: #f1f5f9;
  flex-shrink: 0;
}

.summary-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 0;
}

.summary-title {
  font-size: 14px;
  font-weight: 700;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.summary-meta {
  display: flex;
  gap: 10px;
  font-size: 12px;
  color: var(--text-muted);
}

.summary-price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.summary-label { font-size: 14px; color: var(--text-secondary); }
.summary-price { font-size: 16px; font-weight: 700; color: var(--text); }

.summary-divider {
  height: 1px;
  background: var(--border-light);
  margin: 10px 0;
}

.summary-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  color: var(--text-secondary);
}

.total-price {
  font-size: 24px;
  font-weight: 900;
  color: #ef4444;
}

/* ========== 二维码 ========== */
.qr-card { text-align: center; }

.qr-section {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.qr-box {
  margin-bottom: 16px;
}

.qr-wrapper {
  position: relative;
  width: 180px;
  height: 180px;
  margin: 0 auto;
  padding: 8px;
  border: 2px solid #e5e7eb;
  border-radius: 18px;
  background: #fff;
}

.qr-svg {
  width: 100%;
  height: 100%;
  border-radius: 8px;
}

.qr-logo {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 48px;
  height: 48px;
  border-radius: 10px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.qr-hint {
  font-size: 14px;
  font-weight: 600;
  color: var(--text);
  margin-bottom: 6px;
}

.qr-tip {
  font-size: 12px;
  color: var(--text-muted);
  margin-bottom: 8px;
}

/* ========== 支付成功动画 ========== */
.pay-success {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 0;
}

.success-check { margin-bottom: 16px; }

.circle-check {
  stroke-dasharray: 240;
  stroke-dashoffset: 240;
  animation: drawCircle 0.6s ease forwards;
}

.path-check {
  stroke-dasharray: 50;
  stroke-dashoffset: 50;
  animation: drawCheck 0.4s ease 0.4s forwards;
}

@keyframes drawCircle {
  to { stroke-dashoffset: 0; }
}

@keyframes drawCheck {
  to { stroke-dashoffset: 0; }
}

.success-text {
  font-size: 20px;
  font-weight: 800;
  color: #10b981;
  margin-bottom: 4px;
}

.success-sub {
  font-size: 14px;
  color: var(--text-muted);
}

/* ========== 确认支付按钮 ========== */
.confirm-pay-btn {
  width: 100%;
  height: 48px;
  border: none;
  border-radius: 14px;
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  font-family: inherit;
  margin-top: 20px;
  transition: all 0.3s var(--ease-spring);
  box-shadow: 0 4px 16px rgba(239,68,68,0.3);
  display: flex;
  align-items: center;
  justify-content: center;
}

.confirm-pay-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 28px rgba(239,68,68,0.45);
}

.confirm-pay-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255,255,255,0.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
  margin-right: 8px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
