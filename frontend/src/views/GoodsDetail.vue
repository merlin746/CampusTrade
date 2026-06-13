<template>
  <div class="page-container" v-if="goods">
    <div class="detail-wrapper fade-in-up">
      <!-- 面包屑 -->
      <nav class="breadcrumb">
        <router-link to="/">← 返回首页</router-link>
        <span class="breadcrumb-divider">/</span>
        <span>{{ goods.categoryName }}</span>
        <span class="breadcrumb-divider">/</span>
        <span class="breadcrumb-current">{{ goods.title }}</span>
      </nav>

      <div class="detail-grid">
        <!-- 左侧图片区 -->
        <div class="detail-images">
          <div class="main-image-wrap">
            <div class="main-image">
              <img :src="currentImage || '/placeholder.png'" :alt="goods.title" />
              <div class="image-overlay"></div>
            </div>
            <span class="image-badge">{{ conditionMap[goods.conditionLevel] }}</span>
          </div>
          <div class="thumb-strip" v-if="goods.images?.length > 1">
            <button
              v-for="(img, i) in goods.images"
              :key="i"
              :class="['thumb-btn', { active: img === currentImage }]"
              @click="currentImage = img"
            >
              <img :src="img" :alt="`图${i + 1}`" />
            </button>
          </div>
        </div>

        <!-- 右侧信息区 -->
        <div class="detail-info">
          <div class="info-header">
            <h1 class="detail-title">{{ goods.title }}</h1>
            <span :class="['status-badge', `status-${goods.status}`]">
              {{ statusMap[goods.status] }}
            </span>
          </div>

          <div class="price-section">
            <div class="price-main">
              <span class="currency">¥</span>
              <span class="price-value">{{ goods.price }}</span>
            </div>
            <span v-if="goods.originalPrice" class="price-original">¥{{ goods.originalPrice }}</span>
            <span v-if="goods.originalPrice && goods.originalPrice > goods.price" class="discount-tag">
              省 ¥{{ (goods.originalPrice - goods.price).toFixed(0) }}
            </span>
          </div>

          <div class="info-grid">
            <div class="info-cell">
              <div class="info-cell-icon seller"><el-icon><User /></el-icon></div>
              <div class="info-cell-body">
                <span class="info-cell-label">卖家</span>
                <span class="info-cell-value">{{ goods.sellerName }}</span>
              </div>
            </div>
            <div class="info-cell">
              <div class="info-cell-icon category"><el-icon><Folder /></el-icon></div>
              <div class="info-cell-body">
                <span class="info-cell-label">分类</span>
                <span class="info-cell-value">{{ goods.categoryName }}</span>
              </div>
            </div>
            <div class="info-cell">
              <div class="info-cell-icon condition"><el-icon><Tag /></el-icon></div>
              <div class="info-cell-body">
                <span class="info-cell-label">成色</span>
                <span class="info-cell-value">{{ conditionMap[goods.conditionLevel] }}</span>
              </div>
            </div>
            <div class="info-cell">
              <div class="info-cell-icon location"><el-icon><Location /></el-icon></div>
              <div class="info-cell-body">
                <span class="info-cell-label">交易地点</span>
                <span class="info-cell-value">{{ goods.address || '校内' }}</span>
              </div>
            </div>
          </div>

          <!-- 操作按钮 -->
          <div class="action-buttons">
            <button
              class="buy-btn"
              :disabled="goods.status !== 1"
              @click="handleBuy"
            >
              <el-icon><ShoppingCart /></el-icon>
              {{ goods.status === 1 ? '立即购买' : statusMap[goods.status] }}
            </button>
            <button
              :class="['fav-btn', { favorited: goods.isFavorited }]"
              @click="toggleFavorite"
            >
              <el-icon><Star /></el-icon>
              <span>{{ goods.isFavorited ? '已收藏' : '收藏' }}</span>
              <span class="fav-count">{{ goods.favoriteCount }}</span>
            </button>
          </div>

          <!-- 统计数据 -->
          <div class="stat-row">
            <div class="stat-item">
              <el-icon><View /></el-icon>
              <span class="stat-num">{{ goods.viewCount }}</span>
              <span class="stat-label">浏览</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <el-icon><Star /></el-icon>
              <span class="stat-num">{{ goods.favoriteCount }}</span>
              <span class="stat-label">收藏</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <span class="stat-num">{{ goods.avgScore || '-' }}</span>
              <span class="stat-label">评分</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 商品描述 -->
      <div class="detail-card fade-in-up stagger-1">
        <div class="detail-card-header">
          <h3>📝 商品描述</h3>
          <div class="header-line"></div>
        </div>
        <div class="desc-content">
          {{ goods.description || '卖家很懒，没有写描述...' }}
        </div>
      </div>

      <!-- 发表评论 -->
      <div class="detail-card fade-in-up stagger-2">
        <div class="detail-card-header">
          <h3>✍️ 发表评论</h3>
          <div class="header-line"></div>
        </div>
        <template v-if="userStore.isLoggedIn">
          <div class="comment-form">
            <div class="comment-rate-row">
              <span class="comment-label">评分</span>
              <el-rate v-model="commentForm.score" size="large" show-score />
            </div>
            <textarea
              v-model="commentForm.content"
              class="comment-textarea"
              rows="3"
              placeholder="说说你对这件商品的看法..."
              maxlength="500"
            ></textarea>
            <div class="comment-actions">
              <label class="comment-anon">
                <input type="checkbox" v-model="commentForm.anonymous" />
                <span>匿名评论</span>
              </label>
              <button
                class="btn-primary comment-submit"
                :disabled="commentSubmitting"
                @click="submitComment"
              >
                <el-icon><ChatLineSquare /></el-icon>
                {{ commentSubmitting ? '提交中...' : '发表评论' }}
              </button>
            </div>
          </div>
        </template>
        <div v-else class="comment-login-hint">
          <router-link to="/login" class="comment-login-link">登录</router-link> 后即可发表评论
        </div>
      </div>

      <!-- 商品评价 -->
      <div class="detail-card fade-in-up stagger-3">
        <div class="detail-card-header">
          <h3>💬 全部评价 <span class="review-count">({{ reviewsTotal }})</span></h3>
          <div class="header-line"></div>
        </div>
        <div v-if="reviews.length > 0" class="review-list">
          <div v-for="r in reviews" :key="r.id" class="review-item">
            <div class="review-left">
              <el-avatar :size="40" :src="r.reviewerAvatar" class="review-avatar">
                {{ r.reviewerName?.charAt(0) }}
              </el-avatar>
            </div>
            <div class="review-right">
              <div class="review-top">
                <span class="review-name">{{ r.reviewerName }}</span>
                <el-rate :model-value="r.score" disabled size="small" />
              </div>
              <p class="review-text">{{ r.content || '用户未填写评价内容' }}</p>
              <span class="review-time">{{ r.createTime }}</span>
            </div>
          </div>
        </div>
        <div v-else class="no-reviews">
          <span class="no-reviews-icon">📭</span>
          <p>暂无评价，快来第一个评价吧</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { goodsApi } from '@/api/goods'
import { favoriteApi } from '@/api/favorite'
import { reviewApi } from '@/api/review'
import { orderApi } from '@/api/order'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const goods = ref(null)
const currentImage = ref('')
const reviews = ref([])
const reviewsTotal = ref(0)

const commentSubmitting = ref(false)
const commentForm = reactive({ score: 5, content: '', anonymous: false })

const statusMap = { 0: '待审核', 1: '在售', 2: '已售出', 3: '已下架', 4: '审核不通过' }
const conditionMap = { 1: '全新', 2: '几乎全新', 3: '有使用痕迹', 4: '明显瑕疵' }

async function fetchReviews() {
  const revRes = await reviewApi.getByGoods(route.params.id, { page: 1, size: 20 })
  reviews.value = revRes.data.records || []
  reviewsTotal.value = revRes.data.total || 0
}

onMounted(async () => {
  const id = route.params.id
  const res = await goodsApi.detail(id)
  goods.value = res.data
  if (res.data.images?.length) currentImage.value = res.data.images[0]
  fetchReviews()
})

async function toggleFavorite() {
  if (!userStore.isLoggedIn) { router.push('/login'); return }
  try {
    if (goods.value.isFavorited) {
      await favoriteApi.remove(goods.value.id)
      goods.value.favoriteCount--
    } else {
      await favoriteApi.add(goods.value.id)
      goods.value.favoriteCount++
    }
    goods.value.isFavorited = !goods.value.isFavorited
  } catch { /* handled */ }
}

async function handleBuy() {
  if (!userStore.isLoggedIn) { router.push('/login'); return }
  // 跳转到支付页面，支付演示后自动创建订单
  router.push(`/pay/${goods.value.id}`)
}

async function submitComment() {
  if (!commentForm.content.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  commentSubmitting.value = true
  try {
    await reviewApi.commentGoods(route.params.id, {
      score: commentForm.score,
      content: commentForm.content,
      isAnonymous: commentForm.anonymous ? 1 : 0,
    })
    ElMessage.success('评论发表成功！')
    commentForm.content = ''
    commentForm.score = 5
    commentForm.anonymous = false
    fetchReviews()
  } catch { /* handled */ }
  finally { commentSubmitting.value = false }
}
</script>

<style scoped>
/* ========== 面包屑 ========== */
.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 24px;
  font-size: 13px;
  color: var(--text-muted);
}

.breadcrumb a {
  color: var(--primary);
  font-weight: 500;
  transition: color 0.2s;
}

.breadcrumb a:hover { color: var(--primary-dark); }

.breadcrumb-divider { color: var(--text-muted); }

.breadcrumb-current {
  color: var(--text);
  font-weight: 600;
}

/* ========== 商品主体 ========== */
.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  margin-bottom: 32px;
}

@media (max-width: 768px) {
  .detail-grid { grid-template-columns: 1fr; gap: 24px; }
}

/* 图片 */
.main-image-wrap {
  position: relative;
  border-radius: 20px;
  overflow: hidden;
  background: #f1f5f9;
  box-shadow: var(--shadow-md);
}

.main-image {
  height: 440px;
  overflow: hidden;
}

.main-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s var(--ease-out);
}

.main-image:hover img {
  transform: scale(1.04);
}

.image-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, transparent 60%, rgba(0,0,0,0.03));
  pointer-events: none;
}

.image-badge {
  position: absolute;
  top: 16px;
  left: 16px;
  padding: 6px 16px;
  border-radius: 10px;
  background: rgba(99,102,241,0.92);
  backdrop-filter: blur(8px);
  color: #fff;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.03em;
}

.thumb-strip {
  display: flex;
  gap: 10px;
  margin-top: 12px;
}

.thumb-btn {
  width: 80px;
  height: 80px;
  border-radius: 12px;
  overflow: hidden;
  border: 2px solid transparent;
  cursor: pointer;
  padding: 0;
  background: #f1f5f9;
  transition: all 0.25s ease;
}

.thumb-btn img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.thumb-btn:hover { border-color: var(--primary-light); transform: translateY(-2px); }
.thumb-btn.active { border-color: var(--primary); box-shadow: 0 4px 12px rgba(99,102,241,0.3); }

/* 信息区 */
.detail-info {
  display: flex;
  flex-direction: column;
}

.info-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 20px;
}

.detail-title {
  font-size: 26px;
  font-weight: 800;
  color: var(--text);
  line-height: 1.3;
  letter-spacing: -0.01em;
}

.status-badge {
  padding: 6px 14px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 700;
  white-space: nowrap;
}

.status-1 { background: rgba(16,185,129,0.1); color: #10b981; }
.status-0 { background: rgba(59,130,246,0.1); color: #3b82f6; }
.status-2 { background: rgba(245,158,11,0.1); color: #f59e0b; }
.status-3, .status-4 { background: rgba(239,68,68,0.1); color: #ef4444; }

/* 价格 */
.price-section {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 28px;
  padding: 20px 24px;
  background: linear-gradient(135deg, #fef2f2, #fff7ed);
  border-radius: 16px;
  border: 1px solid rgba(239,68,68,0.1);
}

.price-main {
  display: flex;
  align-items: baseline;
}

.currency {
  font-size: 22px;
  font-weight: 700;
  color: #ef4444;
}

.price-value {
  font-size: 38px;
  font-weight: 900;
  color: #ef4444;
  line-height: 1;
  font-variant-numeric: tabular-nums;
}

.price-original {
  font-size: 15px;
  color: var(--text-muted);
  text-decoration: line-through;
}

.discount-tag {
  padding: 4px 10px;
  border-radius: 6px;
  background: rgba(239,68,68,0.1);
  color: #ef4444;
  font-size: 12px;
  font-weight: 700;
}

/* 元数据网格 */
.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-bottom: 24px;
}

.info-cell {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  border-radius: 14px;
  background: var(--bg);
  transition: all 0.2s ease;
}

.info-cell:hover {
  background: rgba(99,102,241,0.04);
}

.info-cell-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.info-cell-icon.seller { background: rgba(99,102,241,0.1); color: var(--primary); }
.info-cell-icon.category { background: rgba(16,185,129,0.1); color: var(--success); }
.info-cell-icon.condition { background: rgba(245,158,11,0.1); color: var(--warning); }
.info-cell-icon.location { background: rgba(239,68,68,0.1); color: var(--danger); }

.info-cell-body {
  display: flex;
  flex-direction: column;
}

.info-cell-label {
  font-size: 11px;
  color: var(--text-muted);
  font-weight: 500;
}

.info-cell-value {
  font-size: 14px;
  font-weight: 600;
  color: var(--text);
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.buy-btn {
  flex: 1;
  height: 52px;
  border: none;
  border-radius: 16px;
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.3s var(--ease-spring);
  box-shadow: 0 4px 16px rgba(239,68,68,0.3);
}

.buy-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 28px rgba(239,68,68,0.45);
}

.buy-btn:disabled {
  background: var(--text-muted);
  box-shadow: none;
  cursor: not-allowed;
  opacity: 0.6;
}

.fav-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 0 24px;
  height: 52px;
  border-radius: 16px;
  border: 2px solid var(--border);
  background: #fff;
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.3s var(--ease-spring);
}

.fav-btn:hover { border-color: var(--warning); color: var(--warning); }
.fav-btn.favorited {
  border-color: var(--warning);
  background: rgba(245,158,11,0.05);
  color: var(--warning);
}

.fav-count {
  font-weight: 800;
  font-variant-numeric: tabular-nums;
}

/* 统计 */
.stat-row {
  display: flex;
  align-items: center;
  gap: 0;
  padding: 16px 20px;
  background: var(--bg);
  border-radius: 14px;
}

.stat-item {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  color: var(--text-secondary);
}

.stat-num {
  font-weight: 800;
  font-size: 16px;
  color: var(--text);
}

.stat-label {
  font-size: 12px;
  color: var(--text-muted);
}

.stat-divider {
  width: 1px;
  height: 28px;
  background: var(--border);
}

/* ========== 详情卡片 ========== */
.detail-card {
  background: #fff;
  border-radius: 20px;
  padding: 28px 32px;
  margin-bottom: 20px;
  border: 1px solid var(--border);
  box-shadow: var(--shadow-sm);
}

.detail-card-header {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 20px;
}

.detail-card-header h3 {
  font-size: 20px;
  font-weight: 800;
  white-space: nowrap;
}

.review-count {
  font-weight: 500;
  color: var(--text-muted);
  font-size: 15px;
}

.header-line {
  flex: 1;
  height: 2px;
  background: linear-gradient(90deg, var(--border), transparent);
  border-radius: 1px;
}

.desc-content {
  line-height: 1.9;
  color: var(--text-secondary);
  white-space: pre-wrap;
  font-size: 15px;
}

/* 评价 */
.review-item {
  display: flex;
  gap: 16px;
  padding: 18px 0;
  border-bottom: 1px solid var(--border-light);
}

.review-item:last-child { border-bottom: none; }

.review-avatar {
  border: 2px solid var(--primary-light);
}

.review-right { flex: 1; }

.review-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.review-name {
  font-weight: 600;
  font-size: 14px;
}

.review-text {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 6px;
  line-height: 1.6;
}

.review-time {
  font-size: 12px;
  color: var(--text-muted);
}

.no-reviews {
  text-align: center;
  padding: 32px;
  color: var(--text-muted);
}

.no-reviews-icon { font-size: 40px; display: block; margin-bottom: 8px; }

/* ========== 评论表单 ========== */
.comment-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.comment-rate-row {
  display: flex;
  align-items: center;
  gap: 16px;
}

.comment-label {
  font-size: 14px;
  font-weight: 600;
  color: var(--text);
}

.comment-textarea {
  width: 100%;
  border: 2px solid var(--border);
  border-radius: 14px;
  padding: 14px 16px;
  font-size: 14px;
  font-family: inherit;
  resize: vertical;
  outline: none;
  transition: border-color 0.25s;
  line-height: 1.6;
}

.comment-textarea:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 4px rgba(99,102,241,0.06);
}

.comment-textarea::placeholder { color: var(--text-muted); }

.comment-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment-anon {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: var(--text-secondary);
  cursor: pointer;
}

.comment-anon input[type="checkbox"] {
  width: 16px;
  height: 16px;
  accent-color: var(--primary);
  cursor: pointer;
}

.comment-submit {
  padding: 10px 28px !important;
  font-size: 14px !important;
}

.comment-login-hint {
  text-align: center;
  padding: 20px;
  color: var(--text-muted);
  font-size: 14px;
}

.comment-login-link {
  color: var(--primary);
  font-weight: 600;
}

.comment-login-link:hover { color: var(--primary-dark); }

@media (max-width: 768px) {
  .detail-title { font-size: 20px; }
  .price-value { font-size: 30px; }
  .info-grid { grid-template-columns: 1fr; }
  .detail-card { padding: 20px 16px; }
}
</style>
