<template>
  <div class="home-page">
    <!-- ========== 豪华英雄区 ========== -->
    <section class="hero-section">
      <div class="hero-bg">
        <div class="hero-orb orb-1"></div>
        <div class="hero-orb orb-2"></div>
        <div class="hero-orb orb-3"></div>
        <div class="hero-grid-pattern"></div>
      </div>
      <div class="hero-content">
        <div class="hero-badge fade-in-scale">
          <span class="badge-dot"></span>
          校园最值得信赖的二手交易平台
        </div>
        <h1 class="hero-title fade-in-up">
          让每一件<span class="text-gradient">闲置</span><br/>都找到<span class="text-gradient">新主人</span>
        </h1>
        <p class="hero-subtitle fade-in-up stagger-1">
          已为 {{ total > 999 ? Math.floor(total/1000)+'k+' : total }}+ 件好物找到归属，为同学们节省 ¥{{ total > 0 ? total * 85 : '∞' }}
        </p>

        <!-- 搜索框 -->
        <div class="hero-search fade-in-up stagger-2">
          <div class="search-wrapper">
            <el-icon class="search-icon"><Search /></el-icon>
            <input
              v-model="query.keyword"
              type="text"
              class="search-field"
              placeholder="搜索课本、电子产品、生活用品..."
              @keyup.enter="handleSearch"
            />
            <button class="search-submit" @click="handleSearch">
              <el-icon><Search /></el-icon>
              <span>搜索</span>
            </button>
          </div>
        </div>

        <!-- 热门搜索标签 -->
        <div class="hot-tags fade-in-up stagger-3">
          <span class="hot-label">🔥 热门:</span>
          <span class="hot-tag" @click="query.keyword='教材'; handleSearch()">教材</span>
          <span class="hot-tag" @click="query.keyword='手机'; handleSearch()">手机</span>
          <span class="hot-tag" @click="query.keyword='耳机'; handleSearch()">耳机</span>
          <span class="hot-tag" @click="query.keyword='自行车'; handleSearch()">自行车</span>
        </div>
      </div>

      <!-- 向下滚动提示 -->
      <div class="scroll-hint" v-if="goodsList.length > 0">
        <span>向下探索</span>
        <div class="scroll-mouse">
          <div class="scroll-wheel"></div>
        </div>
      </div>
    </section>

    <!-- ========== 内容区域 ========== -->
    <div class="page-container content-section">
      <!-- 分类选择 -->
      <div class="section-header fade-in-up">
        <div class="section-title-group">
          <h2 class="section-title">商品分类</h2>
          <div class="section-line"></div>
        </div>
      </div>

      <div class="category-scroll fade-in-up stagger-1">
        <div class="category-list">
          <button
            :class="['category-chip', { active: !query.categoryId }]"
            @click="query.categoryId = null; handleSearch()"
          >
            <span class="chip-icon">🌟</span>
            <span>全部</span>
          </button>
          <button
            v-for="cat in categories"
            :key="cat.id"
            :class="['category-chip', { active: query.categoryId === cat.id }]"
            @click="selectCategory(cat.id)"
          >
            <span class="chip-icon">{{ cat.icon || '📦' }}</span>
            <span>{{ cat.name }}</span>
          </button>
        </div>
      </div>

      <!-- 筛选 & 排序栏 -->
      <div class="toolbar fade-in-up stagger-2">
        <div class="sort-group">
          <button
            v-for="s in sortOptions"
            :key="s.value"
            :class="['sort-btn', { active: query.sortBy === s.value }]"
            @click="query.sortBy = s.value; handleSearch()"
          >
            {{ s.label }}
          </button>
        </div>
        <div class="result-info">
          <span class="result-count">{{ total }}</span> 件好物
        </div>
      </div>

      <!-- 商品网格 -->
      <div v-if="goodsList.length > 0" class="goods-grid">
        <div
          v-for="(item, idx) in goodsList"
          :key="item.id"
          :class="['goods-card-wrapper', `stagger-${Math.min(idx % 12 + 1, 12)}`]"
        >
          <div class="goods-card" @click="$router.push(`/goods/${item.id}`)">
            <div class="card-img-wrap">
              <img :src="item.images?.[0] || '/placeholder.png'" :alt="item.title" loading="lazy" />
              <div class="card-img-overlay">
                <button class="quick-view-btn">
                  <el-icon><Search /></el-icon>
                  查看详情
                </button>
              </div>
              <span class="condition-badge">{{ conditionLabels[item.conditionLevel] }}</span>
              <div class="card-shine"></div>
            </div>
            <div class="card-body">
              <h3 class="card-title">{{ item.title }}</h3>
              <p class="card-desc">{{ item.description?.substring(0, 50) || '物美价廉，值得拥有' }}</p>
              <div class="card-price-row">
                <div class="price-group">
                  <span class="card-price">¥{{ item.price }}</span>
                  <span v-if="item.originalPrice" class="card-original">¥{{ item.originalPrice }}</span>
                </div>
                <span class="card-discount" v-if="item.originalPrice && item.originalPrice > item.price">
                  {{ Math.round((1 - item.price / item.originalPrice) * 100) }}% OFF
                </span>
              </div>
              <div class="card-footer">
                <div class="seller-info">
                  <el-avatar :size="24" :src="item.sellerAvatar" class="seller-avatar">
                    {{ item.sellerName?.charAt(0) }}
                  </el-avatar>
                  <span class="seller-name">{{ item.sellerName }}</span>
                </div>
                <div class="card-stats">
                  <span class="stat" title="浏览量">
                    <el-icon><View /></el-icon> {{ item.viewCount || 0 }}
                  </span>
                  <span class="stat" title="收藏">
                    <el-icon><Star /></el-icon> {{ item.favoriteCount || 0 }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state fade-in-scale">
        <div class="empty-icon">📭</div>
        <h3>暂无商品</h3>
        <p>换个关键词试试，或者成为第一个发布商品的人吧</p>
        <router-link to="/publish" class="btn-primary" v-if="userStore.isLoggedIn">
          <el-icon><Plus /></el-icon> 发布商品
        </router-link>
      </div>

      <!-- 分页 -->
      <div v-if="total > query.size" class="pagination-area fade-in-up">
        <el-pagination
          v-model:current-page="query.page"
          :page-size="query.size"
          :total="total"
          layout="prev, pager, next"
          :pager-count="7"
          background
          @current-change="fetchGoods"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { goodsApi } from '@/api/goods'
import { categoryApi } from '@/api/category'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const categories = ref([])
const goodsList = ref([])
const total = ref(0)

const query = reactive({
  keyword: '',
  categoryId: null,
  sortBy: 0,
  page: 1,
  size: 12,
})

const sortOptions = [
  { label: '🕐 最新', value: 0 },
  { label: '💰 价格↑', value: 1 },
  { label: '💎 价格↓', value: 2 },
  { label: '🔥 最热', value: 3 },
]

const conditionLabels = { 1: '全新', 2: '几乎全新', 3: '有使用痕迹', 4: '明显瑕疵' }

onMounted(async () => {
  const [catRes] = await Promise.all([categoryApi.list()])
  categories.value = catRes.data || []
  fetchGoods()
})

async function fetchGoods() {
  const params = { ...query }
  if (!params.categoryId) delete params.categoryId
  const res = await goodsApi.search(params)
  goodsList.value = res.data.records || []
  total.value = res.data.total || 0
}

function handleSearch() {
  query.page = 1
  fetchGoods()
}

function selectCategory(id) {
  query.categoryId = query.categoryId === id ? null : id
  handleSearch()
}
</script>

<style scoped>
/* ================================================================
   英雄区域
   ================================================================ */
.hero-section {
  position: relative;
  min-height: 520px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: linear-gradient(180deg, #fafbff 0%, #f0f4ff 40%, var(--bg) 100%);
}

.hero-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.hero-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
  opacity: 0.4;
}

.orb-1 {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(139,92,246,0.3), transparent);
  top: -200px;
  right: -150px;
  animation: float 8s ease-in-out infinite;
}

.orb-2 {
  width: 350px;
  height: 350px;
  background: radial-gradient(circle, rgba(99,102,241,0.25), transparent);
  bottom: -100px;
  left: -100px;
  animation: float 7s ease-in-out infinite reverse;
}

.orb-3 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, rgba(245,158,11,0.2), transparent);
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: float 9s ease-in-out infinite;
}

.hero-grid-pattern {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(99,102,241,0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(99,102,241,0.03) 1px, transparent 1px);
  background-size: 60px 60px;
}

.hero-content {
  position: relative;
  z-index: 1;
  text-align: center;
  padding: 60px 24px;
  max-width: 800px;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 18px;
  border-radius: 30px;
  background: rgba(99,102,241,0.08);
  border: 1px solid rgba(99,102,241,0.15);
  color: var(--primary);
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 24px;
}

.badge-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--success);
  animation: pulseGlow 2s ease-in-out infinite;
}

.hero-title {
  font-size: clamp(32px, 5vw, 56px);
  font-weight: 900;
  line-height: 1.2;
  color: var(--text);
  margin-bottom: 16px;
  letter-spacing: -0.02em;
}

.hero-subtitle {
  font-size: 16px;
  color: var(--text-secondary);
  margin-bottom: 36px;
  line-height: 1.6;
}

/* 搜索框 */
.hero-search {
  margin-bottom: 20px;
}

.search-wrapper {
  display: flex;
  align-items: center;
  max-width: 560px;
  margin: 0 auto;
  background: #fff;
  border-radius: 20px;
  border: 2px solid transparent;
  box-shadow: 0 4px 24px rgba(0,0,0,0.06), 0 0 0 1px rgba(0,0,0,0.04);
  transition: all 0.3s ease;
  overflow: hidden;
}

.search-wrapper:focus-within {
  border-color: var(--primary);
  box-shadow: 0 8px 32px rgba(99,102,241,0.15), 0 0 0 4px rgba(99,102,241,0.06);
}

.search-icon {
  font-size: 18px;
  color: var(--text-muted);
  margin-left: 18px;
}

.search-field {
  flex: 1;
  border: none;
  outline: none;
  padding: 16px 12px;
  font-size: 15px;
  background: transparent;
  color: var(--text);
  font-family: inherit;
}

.search-field::placeholder {
  color: var(--text-muted);
}

.search-submit {
  display: flex;
  align-items: center;
  gap: 6px;
  background: linear-gradient(135deg, var(--primary), var(--primary-2));
  color: #fff;
  border: none;
  padding: 12px 24px;
  margin: 5px 5px 5px 0;
  border-radius: 16px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.3s var(--ease-spring);
}

.search-submit:hover {
  box-shadow: 0 4px 16px rgba(99,102,241,0.4);
  transform: scale(1.03);
}

/* 热门标签 */
.hot-tags {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  flex-wrap: wrap;
}

.hot-label {
  font-size: 13px;
  color: var(--text-muted);
  font-weight: 500;
}

.hot-tag {
  padding: 5px 14px;
  border-radius: 20px;
  background: rgba(99,102,241,0.06);
  color: var(--primary);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.hot-tag:hover {
  background: rgba(99,102,241,0.15);
  transform: translateY(-2px);
}

/* 向下滚动提示 */
.scroll-hint {
  position: absolute;
  bottom: 24px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: var(--text-muted);
  font-size: 12px;
  animation: fadeInUp 1s var(--ease-out) 1s both;
}

.scroll-mouse {
  width: 22px;
  height: 34px;
  border: 2px solid var(--text-muted);
  border-radius: 11px;
  display: flex;
  justify-content: center;
  padding-top: 6px;
}

.scroll-wheel {
  width: 4px;
  height: 8px;
  background: var(--text-muted);
  border-radius: 2px;
  animation: scrollWheel 2s ease-in-out infinite;
}

@keyframes scrollWheel {
  0%, 100% { transform: translateY(0); opacity: 1; }
  50% { transform: translateY(8px); opacity: 0.3; }
}

/* ================================================================
   内容区
   ================================================================ */
.content-section {
  padding-top: 8px;
}

.section-header {
  margin-bottom: 16px;
}

.section-title-group {
  display: flex;
  align-items: center;
  gap: 16px;
}

.section-title {
  font-size: 22px;
  font-weight: 800;
  color: var(--text);
  white-space: nowrap;
}

.section-line {
  flex: 1;
  height: 2px;
  background: linear-gradient(90deg, var(--border), transparent);
  border-radius: 1px;
}

/* 分类滚动 */
.category-scroll {
  margin-bottom: 20px;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
  padding-bottom: 4px;
}

.category-scroll::-webkit-scrollbar {
  height: 0;
}

.category-list {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.category-chip {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  border-radius: 14px;
  border: 1.5px solid var(--border);
  background: #fff;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
  transition: all 0.3s var(--ease-smooth);
  white-space: nowrap;
  font-family: inherit;
}

.category-chip:hover {
  border-color: var(--primary-light);
  color: var(--primary);
  background: rgba(99,102,241,0.03);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(99,102,241,0.08);
}

.category-chip.active {
  background: linear-gradient(135deg, var(--primary), var(--primary-2));
  color: #fff;
  border-color: transparent;
  box-shadow: 0 4px 16px rgba(99,102,241,0.3);
  font-weight: 700;
  transform: translateY(-1px);
}

.chip-icon {
  font-size: 16px;
}

/* 工具栏 */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 12px;
}

.sort-group {
  display: flex;
  gap: 6px;
  background: #fff;
  padding: 4px;
  border-radius: 14px;
  box-shadow: var(--shadow-xs);
  border: 1px solid var(--border);
}

.sort-btn {
  padding: 8px 18px;
  border-radius: 11px;
  border: none;
  background: transparent;
  color: var(--text-secondary);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: inherit;
}

.sort-btn.active {
  background: linear-gradient(135deg, var(--primary), var(--primary-2));
  color: #fff;
  box-shadow: 0 2px 8px rgba(99,102,241,0.3);
  font-weight: 700;
}

.result-info {
  font-size: 14px;
  color: var(--text-secondary);
}

.result-count {
  font-weight: 800;
  color: var(--primary);
  font-size: 18px;
}

/* ================================================================
   商品卡片 - 豪华版
   ================================================================ */
.goods-card-wrapper {
  animation: fadeInUp 0.5s var(--ease-out) both;
}

.goods-card {
  background: #fff;
  border-radius: 20px;
  overflow: hidden;
  border: 1px solid var(--border);
  cursor: pointer;
  transition: all 0.4s var(--ease-out);
  position: relative;
}

.goods-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 24px 48px rgba(0,0,0,0.1), 0 8px 20px rgba(99,102,241,0.08);
  border-color: transparent;
}

.card-img-wrap {
  position: relative;
  height: 220px;
  overflow: hidden;
  background: #f1f5f9;
}

.card-img-wrap img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s var(--ease-out);
}

.goods-card:hover .card-img-wrap img {
  transform: scale(1.1);
}

.card-img-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.goods-card:hover .card-img-overlay {
  opacity: 1;
}

.quick-view-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 22px;
  border-radius: 30px;
  border: 2px solid #fff;
  background: rgba(255,255,255,0.2);
  backdrop-filter: blur(4px);
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.3s ease;
  transform: translateY(8px);
}

.goods-card:hover .quick-view-btn {
  transform: translateY(0);
}

.quick-view-btn:hover {
  background: #fff;
  color: var(--primary);
}

.condition-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  padding: 4px 12px;
  border-radius: 8px;
  background: rgba(99,102,241,0.92);
  backdrop-filter: blur(8px);
  color: #fff;
  font-size: 11px;
  font-weight: 600;
  letter-spacing: 0.02em;
}

.card-shine {
  position: absolute;
  top: 0;
  left: -100%;
  width: 50%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.15), transparent);
  transform: skewX(-20deg);
  transition: left 0.6s ease;
}

.goods-card:hover .card-shine {
  left: 150%;
}

.card-body {
  padding: 16px 18px;
}

.card-title {
  font-size: 16px;
  font-weight: 700;
  color: var(--text);
  margin-bottom: 6px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-desc {
  font-size: 13px;
  color: var(--text-secondary);
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-price-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
}

.price-group {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.card-price {
  font-size: 22px;
  font-weight: 800;
  color: #ef4444;
  font-variant-numeric: tabular-nums;
}

.card-original {
  font-size: 13px;
  color: var(--text-muted);
  text-decoration: line-through;
}

.card-discount {
  padding: 3px 10px;
  border-radius: 6px;
  background: rgba(239,68,68,0.08);
  color: #ef4444;
  font-size: 12px;
  font-weight: 700;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid var(--border-light);
}

.seller-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.seller-avatar {
  border: 2px solid var(--primary-light);
}

.seller-name {
  font-size: 13px;
  font-weight: 500;
  color: var(--text-secondary);
}

.card-stats {
  display: flex;
  align-items: center;
  gap: 12px;
}

.stat {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: var(--text-muted);
}

.stat .el-icon {
  font-size: 14px;
}

/* ================================================================
   空状态
   ================================================================ */
.empty-state {
  text-align: center;
  padding: 80px 20px;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-state h3 {
  font-size: 20px;
  font-weight: 700;
  color: var(--text);
  margin-bottom: 8px;
}

.empty-state p {
  color: var(--text-secondary);
  margin-bottom: 24px;
}

/* 分页 */
.pagination-area {
  display: flex;
  justify-content: center;
  margin-top: 48px;
  padding-bottom: 24px;
}

/* ================================================================
   响应式
   ================================================================ */
@media (max-width: 768px) {
  .hero-section {
    min-height: 400px;
  }

  .hero-content {
    padding: 40px 16px;
  }

  .hero-title {
    font-size: 28px;
  }

  .search-field {
    padding: 14px 10px;
    font-size: 14px;
  }

  .search-submit span {
    display: none;
  }

  .search-submit {
    padding: 12px;
    margin: 4px 4px 4px 0;
    border-radius: 14px;
  }

  .sort-group {
    width: 100%;
    overflow-x: auto;
  }

  .card-img-wrap {
    height: 180px;
  }
}
</style>
