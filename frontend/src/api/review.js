import request from '@/utils/request'

export const reviewApi = {
  // 创建评价（支持订单评价和直接商品评论）
  create: (data) => request.post('/review', data),
  // 直接评论商品（无需订单）
  commentGoods: (goodsId, data) => request.post('/review', { goodsId, ...data }),
  getByGoods: (goodsId, params) => request.get(`/review/goods/${goodsId}`, { params }),
  getByUser: (userId, params) => request.get(`/review/user/${userId}`, { params }),
  stats: (goodsId) => request.get(`/review/stats/${goodsId}`),
}
