package com.example.schooltrade.vo;

import java.time.LocalDateTime;

/**
 * 用户信息响应（脱敏，不含密码）
 */
public class UserVO {

    private Long id;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String studentId;
    private Integer role;
    private Integer status;
    private Integer creditScore;
    private LocalDateTime createTime;

    public UserVO() {}

    public UserVO(Long id, String username, String email, String phone, String avatar, String studentId, Integer role, Integer status, Integer creditScore, LocalDateTime createTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.studentId = studentId;
        this.role = role;
        this.status = status;
        this.creditScore = creditScore;
        this.createTime = createTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

}