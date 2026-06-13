package com.example.schooltrade.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

/**
 * 评价请求
 * - 有orderId: 订单评价（买卖双方互评）
 * - 有goodsId但没有orderId: 直接商品评论（无需下单）
 */
public class ReviewDTO {

    /** 订单ID（订单评价时必填，直接评论时可选） */
    private Long orderId;

    /** 商品ID（直接评论时必填） */
    private Long goodsId;

    @NotNull(message = "评分不能为空")
    @Range(min = 1, max = 5, message = "评分范围为1-5")
    private Integer score;

    private String content;

    /** 是否匿名: 0=否, 1=是 */
    private Integer isAnonymous;

    public ReviewDTO() {}

    public ReviewDTO(Long orderId, Long goodsId, Integer score, String content, Integer isAnonymous) {
        this.orderId = orderId;
        this.goodsId = goodsId;
        this.score = score;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getGoodsId() { return goodsId; }
    public void setGoodsId(Long goodsId) { this.goodsId = goodsId; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getIsAnonymous() { return isAnonymous; }
    public void setIsAnonymous(Integer isAnonymous) { this.isAnonymous = isAnonymous; }

}