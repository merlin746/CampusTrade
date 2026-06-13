import request from '@/utils/request'

export const orderApi = {
  create: (data) => request.post('/order', data),
  confirm: (id) => request.put(`/order/${id}/confirm`),
  cancel: (id, reason) => request.put(`/order/${id}/cancel`, null, { params: { reason } }),
  complete: (id) => request.put(`/order/${id}/complete`),
  deleteOrder: (id) => request.delete(`/order/${id}`),
  bought: (params) => request.get('/order/bought', { params }),
  sold: (params) => request.get('/order/sold', { params }),
  detail: (id) => request.get(`/order/${id}`),
}
