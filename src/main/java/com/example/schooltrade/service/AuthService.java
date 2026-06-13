package com.example.schooltrade.service;

import com.example.schooltrade.dto.LoginDTO;
import com.example.schooltrade.dto.RegisterDTO;
import com.example.schooltrade.vo.LoginVO;
import com.example.schooltrade.vo.UserVO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /** 用户注册，返回注册成功的用户信息 */
    UserVO register(RegisterDTO dto);

    /** 用户登录，返回 Token 和用户信息 */
    LoginVO login(LoginDTO dto);

    /** 根据 Token 获取当前登录用户信息 */
    UserVO getCurrentUser(Long userId);
}
