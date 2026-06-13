package com.example.schooltrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 评价实体
 * 买家卖家互评，评价后更新对方信誉分
 */
@TableName("review")
public class Review {

    @TableId(type = IdType.AUTO)
    private Long id;                // 评价ID

    private Long orderId;           // 订单ID
    private Long goodsId;           // 商品ID
    private Long reviewerId;        // 评价人ID
    private Long targetId;          // 被评价人ID

    /** 角色: 0=买家评卖家, 1=卖家评买家 */
    private Integer role;

    private Integer score;          // 评分 (1-5)
    private String content;         // 评价内容

    /** 是否匿名: 0=否, 1=是 */
    private Integer isAnonymous;

    /** 状态: 0=隐藏, 1=显示 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public Review() {}

    public Review(Long id, Long orderId, Long goodsId, Long reviewerId, Long targetId, Integer role, Integer score, String content, Integer isAnonymous, Integer status, LocalDateTime createTime) {
        this.id = id;
        this.orderId = orderId;
        this.goodsId = goodsId;
        this.reviewerId = reviewerId;
        this.targetId = targetId;
        this.role = role;
        this.score = score;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.status = status;
        this.createTime = createTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getGoodsId() { return goodsId; }
    public void setGoodsId(Long goodsId) { this.goodsId = goodsId; }

    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }

    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }

    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getIsAnonymous() { return isAnonymous; }
    public void setIsAnonymous(Integer isAnonymous) { this.isAnonymous = isAnonymous; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

}