package com.example.schooltrade.service;

import com.example.schooltrade.dto.GoodsDTO;
import com.example.schooltrade.dto.GoodsQueryDTO;
import com.example.schooltrade.vo.GoodsVO;
import com.example.schooltrade.vo.PageVO;

/**
 * 商品服务接口
 */
public interface GoodsService {

    /** 发布商品（存入后状态为 "待审核"） */
    void publish(Long sellerId, GoodsDTO dto);

    /** 编辑商品 */
    void update(Long userId, Long goodsId, GoodsDTO dto);

    /** 下架商品（在售 → 下架） */
    void offShelf(Long userId, Long goodsId);

    /** 撤销发布（待审核 → 删除，仅发布者可操作） */
    void withdraw(Long userId, Long goodsId);

    /** 删除历史商品记录（仅已下架/审核不通过） */
    void deleteGoods(Long userId, Long goodsId);

    /** 获取商品详情 */
    GoodsVO getDetail(Long goodsId, Long currentUserId);

    /** 分页搜索/筛选商品 */
    PageVO<GoodsVO> search(GoodsQueryDTO query);

    /** 获取我的发布 */
    PageVO<GoodsVO> getMyGoods(Long userId, Integer page, Integer size);

    /** 增加浏览量 */
    void increaseViewCount(Long goodsId);
}
