package com.example.schooltrade.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情/列表响应
 */
public class OrderVO {

    private Long id;
    private String orderNo;
    private Long goodsId;
    private String goodsTitle;
    private String goodsImage;      // 商品首图
    private Long buyerId;
    private String buyerName;
    private Long sellerId;
    private String sellerName;
    private BigDecimal price;
    private Integer status;
    private String cancelReason;
    private Integer buyerReviewed;
    private Integer sellerReviewed;
    private LocalDateTime completeTime;
    private LocalDateTime createTime;

    public OrderVO() {}

    public OrderVO(Long id, String orderNo, Long goodsId, String goodsTitle, String goodsImage, Long buyerId, String buyerName, Long sellerId, String sellerName, BigDecimal price, Integer status, String cancelReason, Integer buyerReviewed, Integer sellerReviewed, LocalDateTime completeTime, LocalDateTime createTime) {
        this.id = id;
        this.orderNo = orderNo;
        this.goodsId = goodsId;
        this.goodsTitle = goodsTitle;
        this.goodsImage = goodsImage;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.price = price;
        this.status = status;
        this.cancelReason = cancelReason;
        this.buyerReviewed = buyerReviewed;
        this.sellerReviewed = sellerReviewed;
        this.completeTime = completeTime;
        this.createTime = createTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public Long getGoodsId() { return goodsId; }
    public void setGoodsId(Long goodsId) { this.goodsId = goodsId; }

    public String getGoodsTitle() { return goodsTitle; }
    public void setGoodsTitle(String goodsTitle) { this.goodsTitle = goodsTitle; }

    public String getGoodsImage() { return goodsImage; }
    public void setGoodsImage(String goodsImage) { this.goodsImage = goodsImage; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }

    public Integer getBuyerReviewed() { return buyerReviewed; }
    public void setBuyerReviewed(Integer buyerReviewed) { this.buyerReviewed = buyerReviewed; }

    public Integer getSellerReviewed() { return sellerReviewed; }
    public void setSellerReviewed(Integer sellerReviewed) { this.sellerReviewed = sellerReviewed; }

    public LocalDateTime getCompleteTime() { return completeTime; }
    public void setCompleteTime(LocalDateTime completeTime) { this.completeTime = completeTime; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

}