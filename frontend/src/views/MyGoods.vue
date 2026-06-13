<template>
  <div class="page-container">
    <div class="page-header fade-in-up">
      <div class="page-header-left">
        <h2 class="page-title">📦 我的发布</h2>
        <span class="page-subtitle">管理你发布的商品</span>
      </div>
      <router-link to="/publish" class="btn-primary">
        <el-icon><Plus /></el-icon>
        <span>发布新商品</span>
      </router-link>
    </div>

    <div v-if="goodsList.length > 0" class="goods-grid">
      <div
        v-for="(item, idx) in goodsList"
        :key="item.id"
        :class="['goods-card-wrapper', `stagger-${Math.min(idx % 8 + 1, 8)}`]"
      >
        <div class="goods-card">
          <div class="card-img-wrap" @click="$router.push(`/goods/${item.id}`)">
            <img :src="item.images?.[0] || '/placeholder.png'" :alt="item.title" loading="lazy" />
            <span :class="['status-tag', `status-${item.status}`]">{{ statusMap[item.status] }}</span>
            <div class="card-img-overlay">
              <button class="overlay-btn">查看详情</button>
            </div>
          </div>
          <div class="card-body">
            <h3 class="card-title" @click="$router.push(`/goods/${item.id}`)">{{ item.title }}</h3>
            <div class="card-price-row">
              <span class="card-price">¥{{ item.price }}</span>
              <span class="card-time">{{ item.createTime?.substring(0, 10) }}</span>
            </div>
            <div class="card-actions">
              <!-- 待审核：可编辑 + 撤销发布 -->
              <template v-if="item.status === 0">
                <button class="action-btn edit" @click="$router.push(`/edit-goods/${item.id}`)">
                  <el-icon><Edit /></el-icon> 编辑
                </button>
                <button class="action-btn withdraw" @click="handleWithdraw(item.id)">
                  <el-icon><Delete /></el-icon> 撤销发布
                </button>
              </template>
              <!-- 在售：可下架 -->
              <button v-if="item.status === 1" class="action-btn edit" @click="$router.push(`/edit-goods/${item.id}`)">
                <el-icon><Edit /></el-icon> 编辑
              </button>
              <button
                v-if="item.status === 1"
                class="action-btn danger"
                @click="handleOff(item.id)"
              >
                <el-icon><Close /></el-icon> 下架
              </button>
              <!-- 审核不通过：可编辑重发 + 删除记录 -->
              <template v-if="item.status === 4">
                <button class="action-btn edit" @click="$router.push(`/edit-goods/${item.id}`)">
                  <el-icon><Edit /></el-icon> 重新编辑
                </button>
                <button class="action-btn del" @click="handleDeleteGoods(item.id)">
                  <el-icon><Delete /></el-icon> 删除记录
                </button>
              </template>
              <!-- 已下架：可删除 -->
              <button v-if="item.status === 3" class="action-btn del" @click="handleDeleteGoods(item.id)">
                <el-icon><Delete /></el-icon> 删除记录
              </button>
              <!-- 已售出：仅查看 -->
              <span v-if="item.status === 2" class="no-action">已售出</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="empty-state fade-in-scale">
      <span class="empty-icon">📭</span>
      <h3>还没有发布任何商品</h3>
      <p>发布你的第一件闲置物品吧</p>
      <router-link to="/publish" class="btn-primary">
        <el-icon><Plus /></el-icon> 发布商品
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { goodsApi } from '@/api/goods'

const goodsList = ref([])
const statusMap = { 0: '待审核', 1: '在售', 2: '已售出', 3: '已下架', 4: '审核不通过' }

onMounted(async () => {
  const res = await goodsApi.myGoods({ page: 1, size: 50 })
  goodsList.value = res.data.records || []
})

async function handleOff(id) {
  try {
    await ElMessageBox.confirm('确定要下架该商品吗？下架后将不再公开展示。', '确认下架', {
      confirmButtonText: '确认下架',
      type: 'warning',
    })
    await goodsApi.offShelf(id)
    ElMessage.success('已下架')
    const item = goodsList.value.find(g => g.id === id)
    if (item) item.status = 3
  } catch { /* cancel */ }
}

async function handleWithdraw(id) {
  try {
    await ElMessageBox.confirm('确定要撤销发布吗？该商品将被彻底删除。', '撤销发布', {
      confirmButtonText: '确认撤销',
      type: 'warning',
    })
    await goodsApi.withdraw(id)
    ElMessage.success('已撤销')
    goodsList.value = goodsList.value.filter(g => g.id !== id)
  } catch { /* cancel */ }
}

async function handleDeleteGoods(id) {
  try {
    await ElMessageBox.confirm('确定要删除该商品记录吗？删除后不可恢复。', '删除记录', {
      confirmButtonText: '确认删除',
      type: 'warning',
    })
    await goodsApi.deleteGoods(id)
    ElMessage.success('商品记录已删除')
    goodsList.value = goodsList.value.filter(g => g.id !== id)
  } catch { /* cancel */ }
}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
  flex-wrap: wrap;
  gap: 16px;
}

.page-header-left { display: flex; flex-direction: column; gap: 2px; }
.page-title { font-size: 24px; font-weight: 800; color: var(--text); }
.page-subtitle { font-size: 13px; color: var(--text-muted); }

/* 卡片 */
.goods-card-wrapper { animation: fadeInUp 0.5s var(--ease-out) both; }

.goods-card {
  background: #fff;
  border-radius: 18px;
  overflow: hidden;
  border: 1px solid var(--border);
  transition: all 0.35s var(--ease-out);
}

.goods-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 20px 40px rgba(0,0,0,0.08), 0 8px 16px rgba(99,102,241,0.04);
}

.card-img-wrap {
  position: relative;
  height: 200px;
  overflow: hidden;
  cursor: pointer;
  background: #f1f5f9;
}

.card-img-wrap img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s var(--ease-out);
}

.goods-card:hover .card-img-wrap img { transform: scale(1.08); }

.status-tag {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 4px 12px;
  border-radius: 8px;
  font-size: 11px;
  font-weight: 700;
  backdrop-filter: blur(8px);
}

.status-0 { background: rgba(59,130,246,0.9); color: #fff; }
.status-1 { background: rgba(16,185,129,0.9); color: #fff; }
.status-2 { background: rgba(245,158,11,0.9); color: #fff; }
.status-3, .status-4 { background: rgba(239,68,68,0.9); color: #fff; }

.card-img-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.35);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.goods-card:hover .card-img-overlay { opacity: 1; }

.overlay-btn {
  padding: 8px 20px;
  border-radius: 20px;
  border: 2px solid #fff;
  background: transparent;
  color: #fff;
  font-weight: 600;
  font-size: 13px;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.2s;
  transform: translateY(8px);
}

.goods-card:hover .overlay-btn { transform: translateY(0); }
.overlay-btn:hover { background: #fff; color: var(--primary); }

.card-body { padding: 16px 18px; }

.card-title {
  font-size: 15px;
  font-weight: 700;
  color: var(--text);
  cursor: pointer;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.card-price { font-size: 22px; font-weight: 800; color: #ef4444; }
.card-time { font-size: 12px; color: var(--text-muted); }

.card-actions {
  display: flex;
  gap: 8px;
  padding-top: 12px;
  border-top: 1px solid var(--border-light);
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 7px 16px;
  border-radius: 10px;
  border: 1.5px solid var(--border);
  background: #fff;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.2s ease;
}

.action-btn.edit:hover { border-color: var(--primary); color: var(--primary); background: rgba(99,102,241,0.04); }
.action-btn.danger:hover { border-color: var(--danger); color: var(--danger); background: rgba(239,68,68,0.04); }
.action-btn.withdraw { color: var(--danger); }
.action-btn.withdraw:hover { border-color: var(--danger); color: #fff; background: var(--danger); }
.action-btn.del { color: var(--text-muted); }
.action-btn.del:hover { border-color: #9ca3af; color: #6b7280; background: #f9fafb; }

.no-action { font-size: 12px; color: var(--text-muted); }

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 80px 20px;
}
.empty-icon { font-size: 60px; display: block; margin-bottom: 12px; }
.empty-state h3 { font-size: 20px; font-weight: 700; margin-bottom: 6px; }
.empty-state p { color: var(--text-secondary); margin-bottom: 24px; }

@media (max-width: 768px) {
  .page-header { flex-direction: column; align-items: stretch; }
}
</style>
