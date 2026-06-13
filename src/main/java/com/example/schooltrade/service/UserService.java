package com.example.schooltrade.service;

import com.example.schooltrade.dto.PasswordDTO;
import com.example.schooltrade.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /** 获取用户详情 */
    UserVO getById(Long userId);

    /** 更新用户资料 */
    void updateProfile(Long userId, UserVO vo);

    /** 修改密码 */
    void changePassword(Long userId, PasswordDTO dto);

    /** 获取用户信誉分 */
    Integer getCreditScore(Long userId);
}
