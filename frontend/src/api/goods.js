import request from '@/utils/request'

export const goodsApi = {
  // 搜索商品
  search: (params) => request.get('/goods', { params }),
  // 获取详情
  detail: (id) => request.get(`/goods/${id}`),
  // 发布商品
  publish: (data) => request.post('/goods', data),
  // 编辑商品
  update: (id, data) => request.put(`/goods/${id}`, data),
  // 下架商品
  offShelf: (id) => request.put(`/goods/${id}/off`),
  // 撤销发布（仅待审核状态可操作）
  withdraw: (id) => request.delete(`/goods/${id}/withdraw`),
  // 删除商品记录（仅已下架/审核不通过）
  deleteGoods: (id) => request.delete(`/goods/${id}`),
  // 我的发布
  myGoods: (params) => request.get('/goods/my', { params }),
}
