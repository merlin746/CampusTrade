<template>
  <div class="admin-page fade-in-up">
    <div class="page-top">
      <h2 class="page-title">📦 商品管理</h2>
      <div class="toolbar">
        <div class="search-wrap">
          <el-icon class="search-icon"><Search /></el-icon>
          <el-input v-model="keyword" placeholder="搜索商品标题..." clearable class="search-input" @change="fetchGoods" />
        </div>
        <el-select v-model="filterStatus" placeholder="全部状态" clearable class="status-select" @change="fetchGoods">
          <el-option label="待审核" :value="0" />
          <el-option label="在售" :value="1" />
          <el-option label="已售出" :value="2" />
          <el-option label="已下架" :value="3" />
          <el-option label="审核不通过" :value="4" />
        </el-select>
      </div>
    </div>

    <div class="table-card">
      <el-table :data="goodsList" stripe style="width:100%" v-loading="loading" :header-cell-style="{background:'#f8fafc',color:'#1e293b',fontWeight:'700',fontSize:'12px'}">
        <el-table-column prop="id" label="ID" width="65" />
        <el-table-column label="图片" width="75">
          <template #default="{ row }">
            <div class="table-img">
              <img :src="row.images?.[0] || '/placeholder.png'" />
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column label="价格" width="100">
          <template #default="{ row }"><span class="table-price">¥{{ row.price }}</span></template>
        </el-table-column>
        <el-table-column prop="sellerName" label="卖家" width="110" />
        <el-table-column prop="categoryName" label="分类" width="90" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <span :class="['status-tag', `s-${row.status}`]">{{ statusMap[row.status] }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览" width="65" />
        <el-table-column prop="createTime" label="发布时间" width="155" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <button class="view-btn" @click="$router.push(`/goods/${row.id}`)">查看</button>
            <button
              v-if="row.status === 0 || row.status === 1"
              class="off-btn"
              @click="handleForceOff(row.id)"
            >下架</button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-area" v-if="total > 12">
        <el-pagination
          v-model:current-page="page"
          :total="total"
          :page-size="12"
          layout="prev, pager, next"
          background
          @current-change="fetchGoods"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { adminApi } from '@/api/admin'

const goodsList = ref([])
const total = ref(0)
const page = ref(1)
const keyword = ref('')
const filterStatus = ref(null)
const loading = ref(false)

const statusMap = { 0: '待审核', 1: '在售', 2: '已售出', 3: '已下架', 4: '审核不通过' }

async function fetchGoods() {
  loading.value = true
  const params = { page: page.value, size: 12, keyword: keyword.value }
  if (filterStatus.value !== null && filterStatus.value !== '') params.status = filterStatus.value
  const res = await adminApi.listGoods(params)
  goodsList.value = res.data.records || []
  total.value = res.data.total || 0
  loading.value = false
}
fetchGoods()

async function handleForceOff(id) {
  try {
    await ElMessageBox.confirm('确定要下架该商品吗？下架后将不再公开展示。', '强制下架', {
      confirmButtonText: '确认下架',
      type: 'warning',
    })
    await adminApi.forceOffShelf(id)
    ElMessage.success('已下架')
    fetchGoods()
  } catch { /* cancel */ }
}
</script>

<style scoped>
.admin-page { max-width: 1200px; }

.page-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 16px;
}

.page-title { font-size: 24px; font-weight: 800; }

.toolbar { display: flex; gap: 10px; }

.search-wrap { position: relative; width: 260px; }

.search-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 2;
  color: var(--text-muted);
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 14px !important;
  border: 2px solid var(--border) !important;
  box-shadow: none !important;
  padding-left: 40px !important;
}

.status-select :deep(.el-input__wrapper) {
  border-radius: 14px !important;
  border: 2px solid var(--border) !important;
  box-shadow: none !important;
}

.table-card {
  background: #fff;
  border-radius: 18px;
  border: 1px solid var(--border);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.table-img {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  overflow: hidden;
  background: #f1f5f9;
}

.table-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.table-price {
  font-weight: 700;
  color: #ef4444;
  font-size: 15px;
}

.status-tag {
  padding: 3px 10px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 700;
}

.s-0 { background: rgba(59,130,246,0.1); color: #3b82f6; }
.s-1 { background: rgba(16,185,129,0.1); color: #10b981; }
.s-2 { background: rgba(245,158,11,0.1); color: #f59e0b; }
.s-3, .s-4 { background: rgba(239,68,68,0.1); color: #ef4444; }

.view-btn {
  padding: 5px 14px;
  border-radius: 8px;
  border: 1.5px solid var(--border);
  background: #fff;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  font-family: inherit;
  color: var(--primary);
  transition: all 0.2s;
}

.view-btn:hover { background: rgba(99,102,241,0.06); border-color: var(--primary); }

.off-btn {
  padding: 5px 14px;
  border-radius: 8px;
  border: 1.5px solid #fecaca;
  background: #fff;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  font-family: inherit;
  color: #ef4444;
  transition: all 0.2s;
  margin-left: 8px;
}

.off-btn:hover { background: rgba(239,68,68,0.06); border-color: #ef4444; }

.pagination-area {
  display: flex;
  justify-content: center;
  padding: 20px;
}

@media (max-width: 768px) {
  .toolbar { width: 100%; flex-direction: column; }
  .search-wrap { width: 100%; }
}
</style>
