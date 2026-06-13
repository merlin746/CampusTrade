package com.example.schooltrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 收藏实体
 */
@TableName("favorite")
public class Favorite {

    @TableId(type = IdType.AUTO)
    private Long id;                // 收藏ID

    private Long userId;            // 用户ID
    private Long goodsId;           // 商品ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public Favorite() {}

    public Favorite(Long id, Long userId, Long goodsId, LocalDateTime createTime) {
        this.id = id;
        this.userId = userId;
        this.goodsId = goodsId;
        this.createTime = createTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getGoodsId() { return goodsId; }
    public void setGoodsId(Long goodsId) { this.goodsId = goodsId; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

}