package com.example.schooltrade.service;

import com.example.schooltrade.dto.ReviewDTO;
import com.example.schooltrade.vo.PageVO;
import com.example.schooltrade.vo.ReviewVO;

import java.util.List;

/**
 * 评价服务接口
 */
public interface ReviewService {

    /** 创建评价（买家评卖家 或 卖家评买家） */
    void create(Long reviewerId, ReviewDTO dto);

    /** 获取商品的评价列表 */
    PageVO<ReviewVO> getByGoods(Long goodsId, Integer page, Integer size);

    /** 获取用户的评价列表 */
    PageVO<ReviewVO> getByUser(Long userId, Integer page, Integer size);

    /** 获取评价统计 (平均分、各星级数量) */
    List<Long> getScoreStats(Long goodsId);
}
