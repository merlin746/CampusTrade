package com.example.schooltrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 用户实体
 * 对应数据库 user 表，存储所有用户（含管理员）的基本信息
 */
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;                // 用户ID

    private String username;        // 用户名（唯一）
    private String password;        // 密码 (BCrypt加密)
    private String email;           // 邮箱
    private String phone;           // 手机号
    private String avatar;          // 头像URL
    private String studentId;       // 学号
    private String idCardImage;     // 学生证/身份证图片URL

    /** 角色: 0=普通用户, 1=管理员 */
    private Integer role;

    /** 状态: 0=禁用, 1=正常 */
    private Integer status;

    /** 信誉分, 默认100, 范围0-1000 */
    private Integer creditScore;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public User() {}

    public User(Long id, String username, String password, String email, String phone, String avatar, String studentId, String idCardImage, Integer role, Integer status, Integer creditScore, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.studentId = studentId;
        this.idCardImage = idCardImage;
        this.role = role;
        this.status = status;
        this.creditScore = creditScore;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getIdCardImage() { return idCardImage; }
    public void setIdCardImage(String idCardImage) { this.idCardImage = idCardImage; }

    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

}