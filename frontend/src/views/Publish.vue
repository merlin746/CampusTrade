<template>
  <div class="page-container">
    <div class="publish-wrapper fade-in-up">
      <div class="publish-card">
        <div class="card-accent-bar"></div>
        <h2 class="publish-title">{{ isEdit ? '✏️ 编辑商品' : '🚀 发布商品' }}</h2>
        <p class="publish-desc">{{ isEdit ? '修改商品信息后需重新审核' : '填写信息，让好物找到新主人' }}</p>

        <el-form ref="formRef" :model="form" :rules="rules" label-position="top" size="large" class="publish-form">
          <el-form-item label="商品标题" prop="title">
            <el-input v-model="form.title" placeholder="给你的宝贝起个吸引人的标题~" maxlength="50" show-word-limit class="luxury-input" />
          </el-form-item>

          <div class="form-row-2">
            <el-form-item label="售价 ¥" prop="price" class="form-col">
              <el-input-number v-model="form.price" :precision="2" :min="0" placeholder="0.00" controls-position="right" style="width:100%" class="luxury-number" />
            </el-form-item>
            <el-form-item label="原价 ¥（选填）" class="form-col">
              <el-input-number v-model="form.originalPrice" :precision="2" :min="0" placeholder="0.00" controls-position="right" style="width:100%" class="luxury-number" />
            </el-form-item>
          </div>

          <div class="form-row-2">
            <el-form-item label="分类" prop="categoryId" class="form-col">
              <el-select v-model="form.categoryId" placeholder="选择分类" style="width:100%" class="luxury-select">
                <el-option v-for="c in categories" :key="c.id" :label="`${c.icon || ''} ${c.name}`" :value="c.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="成色" prop="conditionLevel" class="form-col">
              <div class="condition-group">
                <button
                  v-for="(label, val) in conditionMap"
                  :key="val"
                  type="button"
                  :class="['cond-btn', { active: form.conditionLevel === Number(val) }]"
                  @click="form.conditionLevel = Number(val)"
                >
                  {{ label }}
                </button>
              </div>
            </el-form-item>
          </div>

          <el-form-item label="商品描述" prop="description">
            <el-input v-model="form.description" type="textarea" :rows="4" placeholder="描述一下成色、使用情况、购买渠道等..." class="luxury-textarea" />
          </el-form-item>

          <div class="form-row-2">
            <el-form-item label="交易地点（选填）" class="form-col">
              <el-input v-model="form.address" placeholder="如：3号宿舍楼下" class="luxury-input" />
            </el-form-item>
            <el-form-item label="联系方式（选填）" class="form-col">
              <el-input v-model="form.contactInfo" placeholder="QQ/微信/手机号" class="luxury-input" />
            </el-form-item>
          </div>

          <!-- 图片上传 -->
          <el-form-item label="商品图片">
            <div class="upload-area">
              <el-upload
                :action="uploadUrl"
                list-type="picture-card"
                :on-success="handleUploadSuccess"
                :on-remove="handleRemove"
                :file-list="fileList"
                :headers="uploadHeaders"
                :before-upload="beforeUpload"
                multiple
              >
                <div class="upload-placeholder">
                  <el-icon :size="28"><Plus /></el-icon>
                  <span>上传</span>
                </div>
              </el-upload>
              <p class="upload-hint">📸 支持 jpg/png/gif/webp，最多9张，每张 ≤10MB</p>
            </div>
          </el-form-item>

          <div class="form-actions">
            <button type="button" class="btn-primary submit-btn" :disabled="submitting" @click="handleSubmit">
              <span v-if="!submitting">{{ isEdit ? '💾 保存修改' : '🚀 发布商品' }}</span>
              <span v-else>提交中...</span>
            </button>
            <button type="button" class="btn-outline" @click="$router.back()">取消</button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { goodsApi } from '@/api/goods'
import { categoryApi } from '@/api/category'
import { useUserStore } from '@/stores/user'
import { Plus } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const isEdit = ref(!!route.params.id)
const categories = ref([])
const submitting = ref(false)
const formRef = ref(null)
const fileList = ref([])
const uploadedImages = ref([])   // 独立追踪已上传图片URL
const uploadUrl = '/api/upload/image'
// 从 Pinia 内存获取 token，不从 localStorage 读（防多标签串号）
const uploadHeaders = computed(() => ({
  Authorization: 'Bearer ' + userStore.token
}))

const conditionMap = { 1: '全新', 2: '几乎全新', 3: '有使用痕迹', 4: '明显瑕疵' }

const form = reactive({
  title: '', price: null, originalPrice: null,
  categoryId: null, conditionLevel: 1,
  description: '', address: '', contactInfo: '',
})

const rules = {
  title: [{ required: true, message: '请输入商品标题', trigger: 'blur' }],
  price: [{ required: true, message: '请输入售价', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  conditionLevel: [{ required: true, message: '请选择成色', trigger: 'change' }],
}

onMounted(async () => {
  const res = await categoryApi.list()
  categories.value = res.data || []
  if (isEdit.value) {
    const goodsRes = await goodsApi.detail(route.params.id)
    const g = goodsRes.data
    Object.assign(form, {
      title: g.title, price: g.price, originalPrice: g.originalPrice,
      categoryId: g.categoryId, conditionLevel: g.conditionLevel,
      description: g.description, address: g.address, contactInfo: g.contactInfo,
    })
    if (g.images) {
      uploadedImages.value = [...g.images]
      fileList.value = g.images.map((url, i) => ({ name: `image${i}`, url }))
    }
  }
})

function handleUploadSuccess(response) {
  // response = {code:200, message:"success", data:"/uploads/xxx.jpg"}
  uploadedImages.value.push(response.data)
}
function handleRemove(uploadFile) {
  const url = uploadFile.url || uploadFile.response?.data
  uploadedImages.value = uploadedImages.value.filter(u => u !== url)
}
function beforeUpload(file) {
  const allowed = ['image/jpeg', 'image/png', 'image/gif', 'image/webp']
  if (!allowed.includes(file.type)) { ElMessage.error('不支持的格式'); return false }
  if (file.size > 10 * 1024 * 1024) { ElMessage.error('图片不能超过10MB'); return false }
  return true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    const data = { ...form, images: uploadedImages.value }
    if (isEdit.value) {
      await goodsApi.update(route.params.id, data)
      ElMessage.success('修改成功，商品需重新审核')
    } else {
      await goodsApi.publish(data)
      ElMessage.success('发布成功，请等待管理员审核')
    }
    router.push('/my-goods')
  } catch { /* handled */ }
  finally { submitting.value = false }
}
</script>

<style scoped>
.publish-wrapper { max-width: 780px; margin: 0 auto; }

.publish-card {
  background: #fff;
  border-radius: 24px;
  padding: 40px;
  border: 1px solid var(--border);
  position: relative;
  overflow: hidden;
  box-shadow: var(--shadow-md);
}

.card-accent-bar {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 5px;
  background: linear-gradient(90deg, var(--primary), var(--primary-2), var(--accent-2));
}

.publish-title { font-size: 28px; font-weight: 800; margin-bottom: 6px; margin-top: 8px; }
.publish-desc { color: var(--text-secondary); font-size: 14px; margin-bottom: 32px; }

.publish-form :deep(.el-form-item__label) {
  font-weight: 600 !important;
  font-size: 13px !important;
  color: var(--text) !important;
}

.luxury-input :deep(.el-input__wrapper),
.luxury-number :deep(.el-input__wrapper),
.luxury-select :deep(.el-input__wrapper) {
  border-radius: 12px !important;
  border: 2px solid var(--border) !important;
  box-shadow: none !important;
  transition: all 0.25s !important;
}

.luxury-input :deep(.el-input__wrapper.is-focus),
.luxury-number :deep(.el-input__wrapper.is-focus),
.luxury-select :deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary) !important;
  box-shadow: 0 0 0 3px rgba(99,102,241,0.06) !important;
}

.luxury-textarea :deep(.el-textarea__inner) {
  border-radius: 14px !important;
  border: 2px solid var(--border) !important;
  font-size: 14px;
  font-family: inherit;
  transition: all 0.25s;
}
.luxury-textarea :deep(.el-textarea__inner:focus) {
  border-color: var(--primary) !important;
  box-shadow: 0 0 0 3px rgba(99,102,241,0.06) !important;
}

.form-row-2 {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

@media (max-width: 600px) { .form-row-2 { grid-template-columns: 1fr; } }

/* 成色选择 */
.condition-group {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.cond-btn {
  padding: 8px 16px;
  border-radius: 10px;
  border: 2px solid var(--border);
  background: #fff;
  font-size: 13px;
  font-weight: 600;
  color: var(--text-secondary);
  cursor: pointer;
  font-family: inherit;
  transition: all 0.25s;
}

.cond-btn:hover { border-color: var(--primary-light); color: var(--primary); }

.cond-btn.active {
  background: linear-gradient(135deg, var(--primary), var(--primary-2));
  color: #fff;
  border-color: transparent;
  box-shadow: 0 2px 10px rgba(99,102,241,0.3);
}

/* 上传 */
.upload-area {
  width: 100%;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  color: var(--text-muted);
  font-size: 12px;
}

.upload-hint {
  font-size: 12px;
  color: var(--text-muted);
  margin-top: 10px;
}

/* 提交按钮 */
.form-actions {
  display: flex;
  gap: 14px;
  margin-top: 8px;
}

.submit-btn { font-size: 16px !important; padding: 14px 40px !important; }

@media (max-width: 600px) {
  .publish-card { padding: 24px 16px; border-radius: 18px; }
  .publish-title { font-size: 22px; }
}
</style>
