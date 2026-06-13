package com.example.schooltrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 商品图片实体
 */
@TableName("goods_image")
public class GoodsImage {

    @TableId(type = IdType.AUTO)
    private Long id;                // 图片ID

    private Long goodsId;           // 商品ID
    private String imageUrl;        // 图片URL
    private Integer sortOrder;      // 排序

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public GoodsImage() {}

    public GoodsImage(Long id, Long goodsId, String imageUrl, Integer sortOrder, LocalDateTime createTime) {
        this.id = id;
        this.goodsId = goodsId;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.createTime = createTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getGoodsId() { return goodsId; }
    public void setGoodsId(Long goodsId) { this.goodsId = goodsId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

}