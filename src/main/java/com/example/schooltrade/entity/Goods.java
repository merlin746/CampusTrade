package com.example.schooltrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体
 * status 流转: 0(待审核) -> 1(在售) / 4(审核不通过) -> 2(已售出) / 3(已下架)
 */
@TableName("goods")
public class Goods {

    @TableId(type = IdType.AUTO)
    private Long id;                // 商品ID

    private String title;           // 商品标题
    private String description;     // 商品描述
    private BigDecimal price;       // 售价
    private BigDecimal originalPrice; // 原价
    private Long categoryId;        // 分类ID
    private Long sellerId;          // 卖家ID

    /** 成色: 1=全新, 2=几乎全新, 3=有使用痕迹, 4=明显瑕疵 */
    private Integer conditionLevel;

    /** 状态: 0=待审核, 1=在售, 2=已售出, 3=已下架, 4=审核不通过 */
    private Integer status;

    private Integer viewCount;      // 浏览量
    private Integer favoriteCount;  // 收藏数
    private Integer reviewCount;    // 评论数
    private BigDecimal avgScore;    // 平均评分 (1-5)
    private String address;         // 交易地点

    /** 联系方式: 0=站内, 1=QQ, 2=微信, 3=手机号 */
    private Integer contactType;
    private String contactInfo;     // 联系方式详情

    /** 举报状态: 0=正常, 1=被举报 */
    private Integer isReported;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public Goods() {}

    public Goods(Long id, String title, String description, BigDecimal price, BigDecimal originalPrice, Long categoryId, Long sellerId, Integer conditionLevel, Integer status, Integer viewCount, Integer favoriteCount, Integer reviewCount, BigDecimal avgScore, String address, Integer contactType, String contactInfo, Integer isReported, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.originalPrice = originalPrice;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.conditionLevel = conditionLevel;
        this.status = status;
        this.viewCount = viewCount;
        this.favoriteCount = favoriteCount;
        this.reviewCount = reviewCount;
        this.avgScore = avgScore;
        this.address = address;
        this.contactType = contactType;
        this.contactInfo = contactInfo;
        this.isReported = isReported;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public Integer getConditionLevel() { return conditionLevel; }
    public void setConditionLevel(Integer conditionLevel) { this.conditionLevel = conditionLevel; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public Integer getFavoriteCount() { return favoriteCount; }
    public void setFavoriteCount(Integer favoriteCount) { this.favoriteCount = favoriteCount; }

    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }

    public BigDecimal getAvgScore() { return avgScore; }
    public void setAvgScore(BigDecimal avgScore) { this.avgScore = avgScore; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getContactType() { return contactType; }
    public void setContactType(Integer contactType) { this.contactType = contactType; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public Integer getIsReported() { return isReported; }
    public void setIsReported(Integer isReported) { this.isReported = isReported; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

}