package com.example.schooltrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 商品分类实体
 */
@TableName("category")
public class Category {

    @TableId(type = IdType.AUTO)
    private Long id;                // 分类ID

    private String name;            // 分类名称
    private String icon;            // 分类图标（emoji 或图标类名）
    private Integer sortOrder;      // 排序（越小越靠前）

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public Category() {}

    public Category(Long id, String name, String icon, Integer sortOrder, LocalDateTime createTime) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.sortOrder = sortOrder;
        this.createTime = createTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

}