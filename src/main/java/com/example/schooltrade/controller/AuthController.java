package com.example.schooltrade.controller;

import com.example.schooltrade.dto.LoginDTO;
import com.example.schooltrade.dto.RegisterDTO;
import com.example.schooltrade.service.AuthService;
import com.example.schooltrade.vo.LoginVO;
import com.example.schooltrade.vo.Result;
import com.example.schooltrade.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 处理用户注册、登录、获取当前用户信息
 */
@Tag(name = "认证接口", description = "注册、登录、获取用户信息")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户注册
     * POST /api/auth/register
     * 无需认证，新用户默认为普通用户角色
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterDTO dto) {
        UserVO vo = authService.register(dto);
        return Result.ok("注册成功", vo);
    }

    /**
     * 用户登录
     * POST /api/auth/login
     * 验证用户名和密码，返回 JWT Token
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        LoginVO vo = authService.login(dto);
        return Result.ok("登录成功", vo);
    }

    /**
     * 获取当前登录用户信息
     * GET /api/auth/me
     * 需要携带有效的 JWT Token
     */
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public Result<UserVO> me(@RequestAttribute("userId") Long userId) {
        UserVO vo = authService.getCurrentUser(userId);
        return Result.ok(vo);
    }
}
