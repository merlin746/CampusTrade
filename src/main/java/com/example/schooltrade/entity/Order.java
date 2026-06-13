package com.example.schooltrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体
 * 状态流转: 0(待确认) -> 1(已确认) -> 2(已完成), 任意非终态可取消到状态3
 */
@TableName("orders")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;                // 订单ID

    private String orderNo;         // 订单号 (格式: OT + yyyyMMddHHmmss + 随机数)
    private Long goodsId;           // 商品ID
    private Long buyerId;           // 买家ID
    private Long sellerId;          // 卖家ID
    private BigDecimal price;       // 成交价格

    /** 状态: 0=待确认, 1=已确认, 2=已完成, 3=已取消 */
    private Integer status;

    private String cancelReason;    // 取消原因

    /** 买家是否已评价: 0=未评, 1=已评 */
    private Integer buyerReviewed;

    /** 卖家是否已评价: 0=未评, 1=已评 */
    private Integer sellerReviewed;

    private LocalDateTime completeTime;  // 完成时间

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public Order() {}

    public Order(Long id, String orderNo, Long goodsId, Long buyerId, Long sellerId, BigDecimal price, Integer status, String cancelReason, Integer buyerReviewed, Integer sellerReviewed, LocalDateTime completeTime, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.orderNo = orderNo;
        this.goodsId = goodsId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.price = price;
        this.status = status;
        this.cancelReason = cancelReason;
        this.buyerReviewed = buyerReviewed;
        this.sellerReviewed = sellerReviewed;
        this.completeTime = completeTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public Long getGoodsId() { return goodsId; }
    public void setGoodsId(Long goodsId) { this.goodsId = goodsId; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

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

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

}