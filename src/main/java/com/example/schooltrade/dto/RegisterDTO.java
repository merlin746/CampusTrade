package com.example.schooltrade.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
/**
 * 用户注册请求
 */
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度为3-20个字符")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 30, message = "密码长度为6-30个字符")
    private String password;

    private String email;
    private String phone;
    private String studentId;

    public RegisterDTO() {}

    public RegisterDTO(String username, String password, String email, String phone, String studentId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.studentId = studentId;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

}