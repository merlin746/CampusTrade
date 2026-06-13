<template>
  <div class="admin-page fade-in-up">
    <div class="page-top">
      <h2 class="page-title">✅ 商品审核</h2>
      <span class="pending-count">{{ goodsList.length }} 件待审核</span>
    </div>

    <div v-if="goodsList.length > 0" class="audit-list">
      <div v-for="item in goodsList" :key="item.id" class="audit-card">
        <div class="audit-main">
          <div class="audit-image" @click="previewImage = item.images?.[0]">
            <img :src="item.images?.[0] || '/placeholder.png'" :alt="item.title" />
          </div>
          <div class="audit-body">
            <h3 class="audit-title">{{ item.title }}</h3>
            <p class="audit-desc">{{ item.description?.substring(0, 120) || '无描述' }}</p>
            <div class="audit-meta-grid">
              <span><strong>¥{{ item.price }}</strong> 售价</span>
              <span>{{ item.sellerName }} 卖家</span>
              <span>{{ item.categoryName }} 分类</span>
              <span>{{ conditionMap[item.conditionLevel] }}</span>
              <span class="meta-full">{{ item.createTime?.substring(0, 16) }}</span>
            </div>
          </div>
        </div>

        <div class="audit-gallery" v-if="item.images?.length > 1">
          <img v-for="(img, i) in item.images.slice(1, 5)" :key="i" :src="img" class="gallery-thumb" @click="previewImage = img" />
          <span v-if="item.images.length > 5" class="more-images">+{{ item.images.length - 5 }} 张</span>
        </div>

        <div class="audit-actions">
          <button class="approve-btn" @click="handleAudit(item.id, 1)">
            <el-icon><Check /></el-icon> 审核通过
          </button>
          <button class="reject-btn" @click="showReject(item.id)">
            <el-icon><Close /></el-icon> 审核不通过
          </button>
          <button class="off-btn" @click="handleForceOff(item.id)">
            <el-icon><Remove /></el-icon> 直接下架
          </button>
        </div>
      </div>
    </div>

    <div v-else class="empty-state fade-in-scale">
      <span class="empty-icon">🎉</span>
      <h3>没有待审核商品</h3>
      <p>所有商品都已处理完毕</p>
    </div>

    <!-- 拒绝弹窗 -->
    <el-dialog v-model="rejectVisible" title="❌ 审核不通过" width="420px" destroy-on-close>
      <div class="reject-form">
        <label class="reject-label">拒绝原因</label>
        <textarea v-model="rejectReason" class="reject-textarea" rows="3" placeholder="如：图片不清晰、描述不实、价格异常..."></textarea>
      </div>
      <template #footer>
        <el-button @click="rejectVisible = false">取消</el-button>
        <button class="btn-danger" @click="doReject">确认拒绝</button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { adminApi } from '@/api/admin'

const goodsList = ref([])
const rejectVisible = ref(false)
const rejectReason = ref('')
const currentRejectId = ref(null)
const previewImage = ref(null)

const conditionMap = { 1: '全新', 2: '几乎全新', 3: '有使用痕迹', 4: '明显瑕疵' }

async function fetchGoods() {
  const res = await adminApi.pendingGoods({ page: 1, size: 50 })
  goodsList.value = res.data.records || []
}
fetchGoods()

async function handleAudit(id, status) {
  try {
    await ElMessageBox.confirm(
      status === 1 ? '确认审核通过？商品将立即上架展示。' : '确认拒绝该商品？',
      '审核确认',
      { confirmButtonText: status === 1 ? '确认通过' : '确认拒绝', type: status === 1 ? 'success' : 'warning' }
    )
    await adminApi.auditGoods(id, { status, reason: '' })
    ElMessage.success(status === 1 ? '✅ 审核通过，商品已上架' : '已拒绝')
    fetchGoods()
  } catch { /* cancel */ }
}

function showReject(id) {
  currentRejectId.value = id
  rejectReason.value = ''
  rejectVisible.value = true
}

async function doReject() {
  await adminApi.auditGoods(currentRejectId.value, { status: 4, reason: rejectReason.value })
  ElMessage.success('已拒绝')
  rejectVisible.value = false
  fetchGoods()
}

async function handleForceOff(id) {
  try {
    await ElMessageBox.confirm('确定要直接下架该商品吗？无需通知卖家审核结果。', '强制下架', {
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
.admin-page { max-width: 900px; }

.page-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title { font-size: 24px; font-weight: 800; }
.pending-count {
  padding: 6px 18px;
  background: rgba(245,158,11,0.1);
  color: var(--warning);
  border-radius: 20px;
  font-size: 13px;
  font-weight: 700;
}

.audit-card {
  background: #fff;
  border-radius: 18px;
  padding: 24px;
  margin-bottom: 16px;
  border: 1px solid var(--border);
  transition: all 0.3s ease;
}

.audit-card:hover {
  box-shadow: 0 8px 24px rgba(0,0,0,0.06);
}

.audit-main {
  display: flex;
  gap: 20px;
  margin-bottom: 16px;
}

.audit-image {
  width: 170px;
  height: 170px;
  border-radius: 14px;
  overflow: hidden;
  flex-shrink: 0;
  cursor: pointer;
  background: #f1f5f9;
}

.audit-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.audit-image:hover img { transform: scale(1.05); }

.audit-body { flex: 1; min-width: 0; }

.audit-title {
  font-size: 20px;
  font-weight: 800;
  margin-bottom: 8px;
}

.audit-desc {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 14px;
  line-height: 1.6;
}

.audit-meta-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 12px 20px;
  font-size: 13px;
  color: var(--text-secondary);
}

.audit-meta-grid strong { color: #ef4444; }
.meta-full { width: 100%; }

.audit-gallery {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
  flex-wrap: wrap;
  align-items: center;
}

.gallery-thumb {
  width: 72px;
  height: 72px;
  border-radius: 10px;
  object-fit: cover;
  cursor: pointer;
  border: 2px solid transparent;
  transition: all 0.2s;
}

.gallery-thumb:hover { border-color: var(--primary); }

.more-images {
  font-size: 13px;
  color: var(--text-muted);
  font-weight: 500;
}

.audit-actions {
  display: flex;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid var(--border-light);
}

.approve-btn, .reject-btn, .off-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 28px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  font-family: inherit;
  border: none;
  transition: all 0.3s var(--ease-spring);
}

.approve-btn {
  background: linear-gradient(135deg, #10b981, #059669);
  color: #fff;
  box-shadow: 0 4px 14px rgba(16,185,129,0.3);
}

.approve-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(16,185,129,0.4);
}

.reject-btn {
  background: #fff;
  color: var(--danger);
  border: 2px solid #fecaca;
}

.reject-btn:hover {
  background: #fef2f2;
  border-color: #ef4444;
}

.off-btn {
  background: #fff;
  color: #6b7280;
  border: 2px solid #e5e7eb;
}

.off-btn:hover {
  background: #f3f4f6;
  border-color: #9ca3af;
  color: #374151;
}

.empty-state {
  text-align: center;
  padding: 80px 20px;
}
.empty-icon { font-size: 60px; display: block; margin-bottom: 12px; }
.empty-state h3 { font-size: 20px; font-weight: 700; margin-bottom: 6px; }
.empty-state p { color: var(--text-secondary); }

.reject-label { display: block; font-size: 13px; font-weight: 600; margin-bottom: 8px; }
.reject-textarea {
  width: 100%;
  border: 2px solid var(--border);
  border-radius: 12px;
  padding: 12px;
  font-size: 14px;
  font-family: inherit;
  resize: vertical;
  outline: none;
}
.reject-textarea:focus { border-color: var(--danger); }

.btn-danger {
  padding: 10px 24px;
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  font-family: inherit;
}

@media (max-width: 600px) {
  .audit-main { flex-direction: column; }
  .audit-image { width: 100%; height: 200px; }
}
</style>
