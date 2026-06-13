package com.example.schooltrade.service.impl;

import com.example.schooltrade.dto.PasswordDTO;
import com.example.schooltrade.entity.User;
import com.example.schooltrade.exception.BusinessException;
import com.example.schooltrade.mapper.UserMapper;
import com.example.schooltrade.service.UserService;
import com.example.schooltrade.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserVO getById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(404, "用户不存在");
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @Override
    public void updateProfile(Long userId, UserVO vo) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(404, "用户不存在");
        // 只允许修改非敏感字段
        user.setEmail(vo.getEmail());
        user.setPhone(vo.getPhone());
        user.setAvatar(vo.getAvatar());
        user.setStudentId(vo.getStudentId());
        userMapper.updateById(user);
    }

    @Override
    public void changePassword(Long userId, PasswordDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(404, "用户不存在");
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException(400, "旧密码不正确");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.updateById(user);
    }

    @Override
    public Integer getCreditScore(Long userId) {
        User user = userMapper.selectById(userId);
        return user != null ? user.getCreditScore() : 0;
    }
}
