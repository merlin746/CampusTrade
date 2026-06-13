package com.example.schooltrade.vo;

import java.time.LocalDateTime;

/**
 * 评价列表响应
 */
public class ReviewVO {

    private Long id;
    private Long orderId;
    private Long goodsId;
    private Long reviewerId;
    private String reviewerName;
    private String reviewerAvatar;
    private Integer role;
    private Integer score;
    private String content;
    private Integer isAnonymous;
    private LocalDateTime createTime;

    public ReviewVO() {}

    public ReviewVO(Long id, Long orderId, Long goodsId, Long reviewerId, String reviewerName, String reviewerAvatar, Integer role, Integer score, String content, Integer isAnonymous, LocalDateTime createTime) {
        this.id = id;
        this.orderId = orderId;
        this.goodsId = goodsId;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.reviewerAvatar = reviewerAvatar;
        this.role = role;
        this.score = score;
        this.content = content;
        this.isAnonymous = isAnonymous;
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

    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }

    public String getReviewerAvatar() { return reviewerAvatar; }
    public void setReviewerAvatar(String reviewerAvatar) { this.reviewerAvatar = reviewerAvatar; }

    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getIsAnonymous() { return isAnonymous; }
    public void setIsAnonymous(Integer isAnonymous) { this.isAnonymous = isAnonymous; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

}