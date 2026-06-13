<template>
  <div class="admin-page fade-in-up">
    <div class="page-top">
      <h2 class="page-title">📂 分类管理</h2>
      <button class="btn-primary add-btn" @click="showAddDialog">
        <el-icon><Plus /></el-icon> 添加分类
      </button>
    </div>

    <div class="table-card">
      <el-table :data="categories" stripe style="width:100%" v-loading="loading" :header-cell-style="{background:'#f8fafc',color:'#1e293b',fontWeight:'700',fontSize:'12px'}">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="icon" label="图标" width="70" align="center">
          <template #default="{ row }"><span class="cat-icon">{{ row.icon || '📦' }}</span></template>
        </el-table-column>
        <el-table-column prop="name" label="名称" width="200" />
        <el-table-column prop="sortOrder" label="排序" width="90" />
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <button class="edit-btn" @click="showEditDialog(row)">
              <el-icon><Edit /></el-icon> 编辑
            </button>
            <button class="del-btn" @click="handleDelete(row.id)">
              <el-icon><Delete /></el-icon> 删除
            </button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="editing ? '✏️ 编辑分类' : '➕ 添加分类'" width="420px" destroy-on-close>
      <div class="dialog-form">
        <div class="form-group">
          <label>图标 Emoji</label>
          <el-input v-model="form.icon" placeholder="如：📚" size="large" class="luxury-input" />
        </div>
        <div class="form-group">
          <label>分类名称</label>
          <el-input v-model="form.name" placeholder="如：教材教辅" size="large" class="luxury-input" />
        </div>
        <div class="form-group">
          <label>排序值（越小越靠前）</label>
          <el-input-number v-model="form.sortOrder" :min="0" size="large" style="width:100%" />
        </div>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false" size="large">取消</el-button>
        <button class="btn-primary" @click="handleSave" style="padding:12px 28px;font-size:15px;">
          <el-icon><Check /></el-icon> 保存
        </button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { categoryApi } from '@/api/category'

const categories = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const editing = ref(false)
const editId = ref(null)
const form = reactive({ icon: '', name: '', sortOrder: 0 })

async function fetchCategories() {
  loading.value = true
  const res = await categoryApi.list()
  categories.value = res.data || []
  loading.value = false
}
fetchCategories()

function showAddDialog() {
  editing.value = false
  editId.value = null
  form.icon = ''
  form.name = ''
  form.sortOrder = categories.value.length + 1
  dialogVisible.value = true
}

function showEditDialog(row) {
  editing.value = true
  editId.value = row.id
  form.icon = row.icon
  form.name = row.name
  form.sortOrder = row.sortOrder
  dialogVisible.value = true
}

async function handleSave() {
  if (!form.name) { ElMessage.warning('请输入分类名称'); return }
  if (editing.value) {
    await categoryApi.update(editId.value, { ...form })
    ElMessage.success('更新成功')
  } else {
    await categoryApi.create({ ...form })
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  fetchCategories()
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('确定删除该分类？删除后不可恢复。', '确认删除', { confirmButtonText: '删除', type: 'warning' })
    await categoryApi.delete(id)
    ElMessage.success('删除成功')
    fetchCategories()
  } catch { /* cancel */ }
}
</script>

<style scoped>
.admin-page { max-width: 800px; }

.page-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title { font-size: 24px; font-weight: 800; }

.add-btn { font-size: 14px !important; padding: 10px 22px !important; }

.table-card {
  background: #fff;
  border-radius: 18px;
  border: 1px solid var(--border);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.cat-icon { font-size: 24px; }

.edit-btn, .del-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 5px 14px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  font-family: inherit;
  border: 1.5px solid var(--border);
  background: #fff;
  transition: all 0.2s;
}

.edit-btn { color: var(--primary); margin-right: 8px; }
.edit-btn:hover { background: rgba(99,102,241,0.06); border-color: var(--primary); }

.del-btn { color: var(--danger); }
.del-btn:hover { background: rgba(239,68,68,0.06); border-color: #ef4444; }

.dialog-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: var(--text);
  margin-bottom: 6px;
}

.luxury-input :deep(.el-input__wrapper) {
  border-radius: 12px !important;
  border: 2px solid var(--border) !important;
  box-shadow: none !important;
}
</style>
