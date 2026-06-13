<template>
  <div class="page-container">
    <div class="page-header fade-in-up">
      <div class="page-header-left">
        <h2 class="page-title">⭐ 我的收藏</h2>
        <span class="page-subtitle">{{ goodsList.length }} 件心动好物</span>
      </div>
      <button v-if="goodsList.length > 0" class="clear-all-btn" @click="handleClearAll">
        <el-icon><Delete /></el-icon> 清空全部
      </button>
    </div>

    <div v-if="goodsList.length > 0" class="goods-grid">
      <div
        v-for="(item, idx) in goodsList"
        :key="item.id"
        :class="['goods-card-wrapper', `stagger-${Math.min(idx % 10 + 1, 10)}`]"
      >
        <div class="goods-card">
          <div class="card-img-wrap" @click="$router.push(`/goods/${item.id}`)">
            <img :src="item.images?.[0] || '/placeholder.png'" :alt="item.title" loading="lazy" />
            <div class="card-img-overlay">
              <button class="overlay-btn">查看详情</button>
            </div>
          </div>
          <div class="card-body">
            <h3 class="card-title" @click="$router.push(`/goods/${item.id}`)">{{ item.title }}</h3>
            <div class="card-bottom">
              <span class="card-price">¥{{ item.price }}</span>
              <button class="remove-btn" @click.stop="handleRemove(item.id)" title="取消收藏">
                <el-icon><Delete /></el-icon>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="empty-state fade-in-scale">
      <span class="empty-icon">💔</span>
      <h3>还没有收藏任何商品</h3>
      <p>去首页发现喜欢的好物吧</p>
      <router-link to="/" class="btn-primary">去逛逛</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { favoriteApi } from '@/api/favorite'
import { Delete } from '@element-plus/icons-vue'

const goodsList = ref([])

onMounted(async () => {
  const res = await favoriteApi.list({ page: 1, size: 50 })
  goodsList.value = res.data.records || []
})

async function handleRemove(goodsId) {
  await favoriteApi.remove(goodsId)
  goodsList.value = goodsList.value.filter(g => g.id !== goodsId)
  ElMessage.success('已取消收藏')
}

async function handleClearAll() {
  try {
    await ElMessageBox.confirm('确定要清空所有收藏吗？此操作不可恢复。', '清空收藏', {
      confirmButtonText: '确认清空',
      type: 'warning',
    })
    await favoriteApi.clearAll()
    goodsList.value = []
    ElMessage.success('已清空所有收藏')
  } catch { /* cancel */ }
}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
}
.page-header-left { display: flex; flex-direction: column; gap: 2px; }
.page-title { font-size: 24px; font-weight: 800; }
.page-subtitle { font-size: 13px; color: var(--text-muted); }

.clear-all-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  border-radius: 12px;
  border: 1.5px solid #fecaca;
  background: #fff;
  color: #ef4444;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.25s;
}
.clear-all-btn:hover {
  background: #fef2f2;
  border-color: #ef4444;
}

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
  transform: translateY(8px);
  transition: all 0.3s;
}

.goods-card:hover .overlay-btn { transform: translateY(0); }
.overlay-btn:hover { background: #fff; color: var(--primary); }

.card-body { padding: 16px 18px; }

.card-title {
  font-size: 15px;
  font-weight: 700;
  margin-bottom: 12px;
  cursor: pointer;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-price { font-size: 22px; font-weight: 800; color: #ef4444; }

.remove-btn {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  border: 1.5px solid var(--border);
  background: #fff;
  color: var(--text-muted);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.remove-btn:hover {
  border-color: #ef4444;
  color: #ef4444;
  background: rgba(239,68,68,0.04);
}

.empty-state {
  text-align: center;
  padding: 80px 20px;
}
.empty-icon { font-size: 60px; display: block; margin-bottom: 12px; }
.empty-state h3 { font-size: 20px; font-weight: 700; margin-bottom: 6px; }
.empty-state p { color: var(--text-secondary); margin-bottom: 24px; }
</style>
