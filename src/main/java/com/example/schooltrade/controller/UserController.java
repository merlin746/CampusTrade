package com.example.schooltrade.controller;

import com.example.schooltrade.dto.PasswordDTO;
import com.example.schooltrade.service.UserService;
import com.example.schooltrade.vo.Result;
import com.example.schooltrade.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 处理用户资料查询、更新、密码修改
 */
@Tag(name = "用户接口", description = "个人资料管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取个人信息
     * GET /api/user/profile
     */
    @Operation(summary = "获取个人信息")
    @GetMapping("/profile")
    public Result<UserVO> profile(@RequestAttribute("userId") Long userId) {
        return Result.ok(userService.getById(userId));
    }

    /**
     * 更新个人信息
     * PUT /api/user/profile
     */
    @Operation(summary = "更新个人信息")
    @PutMapping("/profile")
    public Result<?> updateProfile(@RequestAttribute("userId") Long userId,
                                   @RequestBody UserVO vo) {
        userService.updateProfile(userId, vo);
        return Result.ok("更新成功");
    }

    /**
     * 修改密码
     * PUT /api/user/password
     */
    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<?> changePassword(@RequestAttribute("userId") Long userId,
                                    @Valid @RequestBody PasswordDTO dto) {
        userService.changePassword(userId, dto);
        return Result.ok("密码修改成功");
    }

    /**
     * 获取用户公开信息
     * GET /api/user/{id}
     */
    @Operation(summary = "获取用户公开信息")
    @GetMapping("/{id}")
    public Result<UserVO> getUser(@PathVariable Long id) {
        return Result.ok(userService.getById(id));
    }
}
