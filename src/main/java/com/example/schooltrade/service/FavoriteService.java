package com.example.schooltrade.service;

import com.example.schooltrade.vo.GoodsVO;
import com.example.schooltrade.vo.PageVO;

/**
 * 收藏服务接口
 */
public interface FavoriteService {

    /** 收藏商品 */
    void add(Long userId, Long goodsId);

    /** 取消收藏 */
    void remove(Long userId, Long goodsId);

    /** 获取我的收藏列表 */
    PageVO<GoodsVO> getMyFavorites(Long userId, Integer page, Integer size);

    /** 判断是否已收藏 */
    boolean isFavorited(Long userId, Long goodsId);

    /** 清空用户所有收藏 */
    void clearAll(Long userId);
}
