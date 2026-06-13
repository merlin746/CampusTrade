import request from '@/utils/request'

export const favoriteApi = {
  add: (goodsId) => request.post(`/favorite/${goodsId}`),
  remove: (goodsId) => request.delete(`/favorite/${goodsId}`),
  list: (params) => request.get('/favorite/list', { params }),
  check: (goodsId) => request.get(`/favorite/check/${goodsId}`),
  clearAll: () => request.delete('/favorite/clear'),
}
