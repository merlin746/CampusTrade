package com.example.schooltrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 通知实体（系统消息简化版）
 */
@TableName("notice")
public class Notice {

    @TableId(type = IdType.AUTO)
    private Long id;                // 通知ID

    private Long userId;            // 接收用户ID

    /** 类型: 1=系统通知, 2=订单通知, 3=审核通知 */
    private Integer type;

    private String title;           // 通知标题
    private String content;         // 通知内容

    /** 是否已读: 0=未读, 1=已读 */
    private Integer isRead;

    private Long relatedId;         // 关联的订单/商品ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public Notice() {}

    public Notice(Long id, Long userId, Integer type, String title, String content, Integer isRead, Long relatedId, LocalDateTime createTime) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.isRead = isRead;
        this.relatedId = relatedId;
        this.createTime = createTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getIsRead() { return isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }

    public Long getRelatedId() { return relatedId; }
    public void setRelatedId(Long relatedId) { this.relatedId = relatedId; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

}