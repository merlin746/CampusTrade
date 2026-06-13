package com.example.schooltrade.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品详情/列表响应
 */
public class GoodsVO {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Long categoryId;
    private String categoryName;
    private Long sellerId;
    private String sellerName;
    private String sellerAvatar;
    private Integer conditionLevel;
    private Integer status;
    private Integer viewCount;
    private Integer favoriteCount;
    private Integer reviewCount;
    private BigDecimal avgScore;
    private String address;
    private Integer contactType;
    private String contactInfo;
    private Integer isReported;

    /** 是否被当前用户收藏 */
    private Boolean isFavorited;

    /** 商品图片列表 */
    private List<String> images;

    private LocalDateTime createTime;

    public GoodsVO() {}

    public GoodsVO(Long id, String title, String description, BigDecimal price, BigDecimal originalPrice, Long categoryId, String categoryName, Long sellerId, String sellerName, String sellerAvatar, Integer conditionLevel, Integer status, Integer viewCount, Integer favoriteCount, Integer reviewCount, BigDecimal avgScore, String address, Integer contactType, String contactInfo, Integer isReported, Boolean isFavorited, List<String> images, LocalDateTime createTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.originalPrice = originalPrice;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.sellerAvatar = sellerAvatar;
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
        this.isFavorited = isFavorited;
        this.images = images;
        this.createTime = createTime;
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

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public String getSellerAvatar() { return sellerAvatar; }
    public void setSellerAvatar(String sellerAvatar) { this.sellerAvatar = sellerAvatar; }

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

    public Boolean getIsFavorited() { return isFavorited; }
    public void setIsFavorited(Boolean isFavorited) { this.isFavorited = isFavorited; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

}