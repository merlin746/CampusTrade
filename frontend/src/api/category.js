import request from '@/utils/request'

export const categoryApi = {
  list: () => request.get('/category/list'),
  create: (data) => request.post('/category', data),
  update: (id, data) => request.put(`/category/${id}`, data),
  delete: (id) => request.delete(`/category/${id}`),
}
