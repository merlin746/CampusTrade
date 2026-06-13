import request from '@/utils/request'

export const adminApi = {
  dashboard: () => request.get('/admin/dashboard'),
  listUsers: (params) => request.get('/admin/users', { params }),
  toggleUserStatus: (id) => request.put(`/admin/users/${id}/toggle-status`),
  pendingGoods: (params) => request.get('/admin/goods/pending', { params }),
  auditGoods: (id, data) => request.put(`/admin/goods/${id}/audit`, data),
  forceOffShelf: (id) => request.put(`/admin/goods/${id}/off-shelf`),
  listGoods: (params) => request.get('/admin/goods', { params }),
  upload: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/admin/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },
}
