<template>
  <div class="page-container">
    <div class="page-header fade-in-up">
      <h2 class="page-title">📋 我的订单</h2>
    </div>

    <div class="tab-bar fade-in-up stagger-1">
      <button :class="['tab-btn', { active: activeTab === 'bought' }]" @click="activeTab = 'bought'; fetchOrders()">
        🛒 我买到的
      </button>
      <button :class="['tab-btn', { active: activeTab === 'sold' }]" @click="activeTab = 'sold'; fetchOrders()">
        💰 我卖出的
      </button>
    </div>

    <div v-if="orders.length > 0" class="order-list">
      <div
        v-for="(order, idx) in orders"
        :key="order.id"
        :class="['order-card', `stagger-${Math.min(idx % 6 + 1, 6)}`]"
      >
        <div class="order-top">
          <span class="order-num">#{{ order.orderNo }}</span>
          <span :class="['order-status', `os-${order.status}`]">{{ statusMap[order.status] }}</span>
        </div>

        <div class="order-middle">
          <div class="order-goods" @click="$router.push(`/goods/${order.goodsId}`)">
            <div class="order-img">
              <img :src="order.goodsImage || '/placeholder.png'" :alt="order.goodsTitle" />
            </div>
            <div class="order-info">
              <h4 class="order-title">{{ order.goodsTitle }}</h4>
              <span class="order-price">¥{{ order.price }}</span>
            </div>
          </div>

          <div class="order-party">
            <el-avatar :size="32" class="party-avatar">
              {{ (activeTab === 'bought' ? order.sellerName : order.buyerName)?.charAt(0) }}
            </el-avatar>
            <div class="party-info">
              <span class="party-label">{{ activeTab === 'bought' ? '卖家' : '买家' }}</span>
              <span class="party-name">{{ activeTab === 'bought' ? order.sellerName : order.buyerName }}</span>
            </div>
          </div>
        </div>

        <div class="order-bottom">
          <span class="order-time">{{ order.createTime }}</span>
          <div class="order-actions">
            <template v-if="activeTab === 'bought'">
              <button v-if="order.status === 0" class="obtn danger" @click="handleCancel(order.id)">取消</button>
              <button v-if="order.status === 1" class="obtn success" @click="handleComplete(order.id)">确认收货</button>
              <button v-if="order.status === 2 && order.buyerReviewed === 0" class="obtn primary" @click="showReview(order)">评价卖家</button>
              <span v-if="order.status === 2 && order.buyerReviewed === 1" class="reviewed-tag">已评价 ✓</span>
              <button v-if="order.status === 2 || order.status === 3" class="obtn del" @click="handleDeleteOrder(order.id)">删除记录</button>
            </template>
            <template v-if="activeTab === 'sold'">
              <button v-if="order.status === 0" class="obtn success" @click="handleConfirm(order.id)">确认订单</button>
              <button v-if="order.status === 2 && order.sellerReviewed === 0" class="obtn primary" @click="showReview(order)">评价买家</button>
              <span v-if="order.status === 2 && order.sellerReviewed === 1" class="reviewed-tag">已评价 ✓</span>
              <button v-if="order.status === 2 || order.status === 3" class="obtn del" @click="handleDeleteOrder(order.id)">删除记录</button>
            </template>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="empty-state fade-in-scale">
      <span class="empty-icon">📋</span>
      <h3>暂无订单</h3>
      <p>去首页逛逛好物吧</p>
      <router-link to="/" class="btn-primary">去逛逛</router-link>
    </div>

    <!-- 评价弹窗 -->
    <el-dialog v-model="reviewVisible" title="✍️ 发表评价" width="440px" destroy-on-close>
      <div class="review-dialog">
        <div class="review-rate">
          <span class="review-label">评分</span>
          <el-rate v-model="reviewForm.score" size="large" />
        </div>
        <div class="review-input-wrap">
          <span class="review-label">评价内容</span>
          <textarea v-model="reviewForm.content" class="review-textarea" rows="3" placeholder="说说你的交易体验..."></textarea>
        </div>
        <label class="review-anon">
          <input type="checkbox" v-model="reviewForm.isAnonymous" />
          <span>匿名评价</span>
        </label>
      </div>
      <template #footer>
        <el-button @click="reviewVisible = false">取消</el-button>
        <button class="btn-primary" @click="submitReview" style="padding:10px 24px;font-size:14px;">提交评价</button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { orderApi } from '@/api/order'
import { reviewApi } from '@/api/review'

const activeTab = ref('bought')
const orders = ref([])
const reviewVisible = ref(false)
const currentOrder = ref(null)
const reviewForm = reactive({ score: 5, content: '', isAnonymous: false, orderId: null })

const statusMap = { 0: '待确认', 1: '已确认', 2: '已完成', 3: '已取消' }

async function fetchOrders() {
  const api = activeTab.value === 'bought' ? orderApi.bought : orderApi.sold
  const res = await api({ page: 1, size: 50 })
  orders.value = res.data.records || []
}
fetchOrders()

async function handleConfirm(id) {
  try {
    await ElMessageBox.confirm('确认接受该订单？', '确认订单', { confirmButtonText: '确认', type: 'info' })
    await orderApi.confirm(id)
    ElMessage.success('已确认')
    fetchOrders()
  } catch { /* cancel */ }
}

async function handleCancel(id) {
  try {
    const { value } = await ElMessageBox.prompt('请输入取消原因', '取消订单', {
      confirmButtonText: '确认取消',
    })
    await orderApi.cancel(id, value || '买家主动取消')
    ElMessage.success('已取消')
    fetchOrders()
  } catch { /* cancel */ }
}

async function handleComplete(id) {
  try {
    await ElMessageBox.confirm('确认已收到货物，订单将完成？', '确认收货', { confirmButtonText: '确认', type: 'success' })
    await orderApi.complete(id)
    ElMessage.success('交易完成！')
    fetchOrders()
  } catch { /* cancel */ }
}

async function handleDeleteOrder(id) {
  try {
    await ElMessageBox.confirm('确定要删除这条订单记录吗？删除后不可恢复。', '删除记录', {
      confirmButtonText: '确认删除',
      type: 'warning',
    })
    await orderApi.deleteOrder(id)
    orders.value = orders.value.filter(o => o.id !== id)
    ElMessage.success('订单记录已删除')
  } catch { /* cancel */ }
}

function showReview(order) {
  currentOrder.value = order
  reviewForm.orderId = order.id
  reviewForm.score = 5
  reviewForm.content = ''
  reviewForm.isAnonymous = false
  reviewVisible.value = true
}

async function submitReview() {
  await reviewApi.create({
    orderId: reviewForm.orderId,
    score: reviewForm.score,
    content: reviewForm.content,
    isAnonymous: reviewForm.isAnonymous ? 1 : 0,
  })
  ElMessage.success('评价成功！')
  reviewVisible.value = false
  fetchOrders()
}
</script>

<style scoped>
.page-header { margin-bottom: 20px; }
.page-title { font-size: 24px; font-weight: 800; }

.tab-bar {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  background: #fff;
  padding: 6px;
  border-radius: 14px;
  border: 1px solid var(--border);
  width: fit-content;
}

.tab-btn {
  padding: 10px 24px;
  border-radius: 10px;
  border: none;
  background: transparent;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.25s ease;
  font-family: inherit;
}

.tab-btn.active {
  background: linear-gradient(135deg, var(--primary), var(--primary-2));
  color: #fff;
  box-shadow: 0 2px 10px rgba(99,102,241,0.3);
}

.order-card {
  background: #fff;
  border-radius: 18px;
  border: 1px solid var(--border);
  padding: 20px 24px;
  margin-bottom: 14px;
  transition: all 0.3s ease;
  animation: fadeInUp 0.4s var(--ease-out) both;
}

.order-card:hover {
  box-shadow: 0 8px 24px rgba(0,0,0,0.06);
  border-color: transparent;
}

.order-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 14px;
  border-bottom: 1px solid var(--border-light);
}

.order-num { font-size: 12px; color: var(--text-muted); font-family: monospace; }
.order-status { padding: 4px 14px; border-radius: 8px; font-size: 12px; font-weight: 700; }
.os-0 { background: rgba(245,158,11,0.1); color: #f59e0b; }
.os-1 { background: rgba(59,130,246,0.1); color: #3b82f6; }
.os-2 { background: rgba(16,185,129,0.1); color: #10b981; }
.os-3 { background: rgba(239,68,68,0.1); color: #ef4444; }

.order-middle {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.order-goods {
  display: flex;
  gap: 14px;
  cursor: pointer;
  flex: 1;
  min-width: 0;
}

.order-img {
  width: 72px;
  height: 72px;
  border-radius: 14px;
  overflow: hidden;
  flex-shrink: 0;
  background: #f1f5f9;
}

.order-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.order-goods:hover .order-img img { transform: scale(1.08); }

.order-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 0;
}

.order-title {
  font-size: 15px;
  font-weight: 700;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.order-price { font-size: 18px; font-weight: 700; color: #ef4444; }

.order-party {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 16px;
  background: var(--bg);
  border-radius: 12px;
  flex-shrink: 0;
}

.party-avatar { border: 2px solid var(--primary-light); }

.party-info {
  display: flex;
  flex-direction: column;
}

.party-label { font-size: 11px; color: var(--text-muted); }
.party-name { font-size: 14px; font-weight: 600; }

.order-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 14px;
  border-top: 1px solid var(--border-light);
}

.order-time { font-size: 12px; color: var(--text-muted); }

.order-actions { display: flex; gap: 8px; }

.obtn {
  padding: 7px 18px;
  border-radius: 10px;
  border: none;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.2s ease;
}

.obtn.primary { background: linear-gradient(135deg, var(--primary), var(--primary-2)); color: #fff; box-shadow: 0 2px 8px rgba(99,102,241,0.3); }
.obtn.success { background: linear-gradient(135deg, #10b981, #059669); color: #fff; box-shadow: 0 2px 8px rgba(16,185,129,0.3); }
.obtn.danger { background: #fff; color: var(--danger); border: 1.5px solid #fecaca; }
.obtn.del { background: #fff; color: var(--text-muted); border: 1.5px solid var(--border); }
.obtn.del:hover { border-color: #9ca3af; color: #6b7280; }
.obtn:hover { transform: translateY(-1px); }
.reviewed-tag { font-size: 13px; color: var(--success); font-weight: 600; align-self: center; }

.empty-state {
  text-align: center;
  padding: 80px 20px;
}
.empty-icon { font-size: 60px; display: block; margin-bottom: 12px; }
.empty-state h3 { font-size: 20px; font-weight: 700; margin-bottom: 6px; }
.empty-state p { color: var(--text-secondary); margin-bottom: 24px; }

/* 评价弹窗 */
.review-dialog {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.review-rate {
  display: flex;
  align-items: center;
  gap: 16px;
}
.review-label {
  font-size: 14px;
  font-weight: 600;
  color: var(--text);
}
.review-textarea {
  width: 100%;
  border: 2px solid var(--border);
  border-radius: 12px;
  padding: 12px;
  font-size: 14px;
  font-family: inherit;
  resize: vertical;
  outline: none;
  transition: border-color 0.2s;
}
.review-textarea:focus { border-color: var(--primary); }
.review-anon {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: var(--text-secondary);
  cursor: pointer;
}

@media (max-width: 768px) {
  .order-middle { flex-direction: column; gap: 12px; }
  .order-party { width: 100%; }
}
</style>
