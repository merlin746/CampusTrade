<template>
  <div class="admin-page fade-in-up">
    <div class="page-top">
      <h2 class="page-title">👥 用户管理</h2>
      <div class="search-wrap">
        <el-icon class="search-icon"><Search /></el-icon>
        <el-input v-model="keyword" placeholder="搜索用户名或邮箱..." clearable class="search-input" @change="fetchUsers" />
      </div>
    </div>

    <div class="table-card">
      <el-table :data="users" stripe style="width:100%" v-loading="loading" :header-cell-style="{background:'#f8fafc',color:'#1e293b',fontWeight:'700',fontSize:'12px'}">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="username" label="用户名" width="140" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="studentId" label="学号" width="110" />
        <el-table-column prop="role" label="角色" width="90">
          <template #default="{ row }">
            <span :class="['role-tag', row.role === 1 ? 'admin' : 'user']">
              {{ row.role === 1 ? '管理员' : '用户' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <span :class="['status-dot', row.status === 1 ? 'on' : 'off']"></span>
            {{ row.status === 1 ? '正常' : '禁用' }}
          </template>
        </el-table-column>
        <el-table-column prop="creditScore" label="信誉分" width="80" />
        <el-table-column prop="createTime" label="注册时间" width="160" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <button
              v-if="row.role !== 1"
              :class="['action-btn', row.status === 1 ? 'danger' : 'success']"
              @click="toggleStatus(row.id)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </button>
            <span v-else class="no-action">—</span>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-area" v-if="total > 10">
        <el-pagination
          v-model:current-page="page"
          :total="total"
          :page-size="10"
          layout="prev, pager, next"
          background
          @current-change="fetchUsers"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { adminApi } from '@/api/admin'

const users = ref([])
const total = ref(0)
const page = ref(1)
const keyword = ref('')
const loading = ref(false)

async function fetchUsers() {
  loading.value = true
  const res = await adminApi.listUsers({ page: page.value, size: 10, keyword: keyword.value })
  users.value = res.data.records || []
  total.value = res.data.total || 0
  loading.value = false
}
fetchUsers()

async function toggleStatus(id) {
  try {
    await ElMessageBox.confirm('确定要切换该用户的状态吗？', '提示', { type: 'warning' })
    await adminApi.toggleUserStatus(id)
    ElMessage.success('操作成功')
    fetchUsers()
  } catch { /* cancel */ }
}
</script>

<style scoped>
.admin-page { max-width: 1100px; }

.page-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 16px;
}

.page-title { font-size: 24px; font-weight: 800; }

.search-wrap {
  position: relative;
  width: 300px;
}

.search-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 2;
  color: var(--text-muted);
  font-size: 16px;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 14px !important;
  border: 2px solid var(--border) !important;
  box-shadow: none !important;
  padding-left: 40px !important;
}

.search-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary) !important;
  box-shadow: 0 0 0 3px rgba(99,102,241,0.06) !important;
}

.table-card {
  background: #fff;
  border-radius: 18px;
  border: 1px solid var(--border);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.role-tag {
  padding: 3px 10px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 700;
}

.role-tag.admin { background: rgba(239,68,68,0.1); color: #ef4444; }
.role-tag.user { background: rgba(99,102,241,0.1); color: var(--primary); }

.status-dot {
  display: inline-block;
  width: 7px;
  height: 7px;
  border-radius: 50%;
  margin-right: 6px;
}

.status-dot.on { background: #10b981; box-shadow: 0 0 6px rgba(16,185,129,0.4); }
.status-dot.off { background: #ef4444; }

.action-btn {
  padding: 5px 14px;
  border-radius: 8px;
  border: none;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.2s;
}

.action-btn.danger { background: rgba(239,68,68,0.1); color: #ef4444; }
.action-btn.danger:hover { background: rgba(239,68,68,0.2); }
.action-btn.success { background: rgba(16,185,129,0.1); color: #10b981; }
.action-btn.success:hover { background: rgba(16,185,129,0.2); }

.no-action { color: var(--text-muted); }

.pagination-area {
  display: flex;
  justify-content: center;
  padding: 20px;
}

@media (max-width: 768px) {
  .search-wrap { width: 100%; }
}
</style>
