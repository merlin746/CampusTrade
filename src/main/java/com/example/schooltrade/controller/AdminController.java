package com.example.schooltrade.controller;

import com.example.schooltrade.dto.AuditDTO;
import com.example.schooltrade.service.AdminService;
import com.example.schooltrade.vo.PageVO;
import com.example.schooltrade.vo.Result;
import com.example.schooltrade.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 管理员控制器
 * 所有接口需要 ROLE_ADMIN 权限
 */
@Tag(name = "管理员接口", description = "仪表盘、用户管理、商品审核")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ==================== 仪表盘 ====================

    /**
     * 仪表盘统计数据
     * GET /api/admin/dashboard
     */
    @Operation(summary = "仪表盘统计")
    @GetMapping("/dashboard")
    public Result<?> dashboard() {
        return Result.ok(adminService.dashboard());
    }

    // ==================== 用户管理 ====================

    /**
     * 用户列表
     * GET /api/admin/users?page=1&size=10&keyword=xxx
     */
    @Operation(summary = "用户列表")
    @GetMapping("/users")
    public Result<PageVO<UserVO>> listUsers(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(required = false) String keyword) {
        return Result.ok(adminService.listUsers(page, size, keyword));
    }

    /**
     * 切换用户状态（启用/禁用）
     * PUT /api/admin/users/{id}/toggle-status
     */
    @Operation(summary = "禁用/启用用户")
    @PutMapping("/users/{id}/toggle-status")
    public Result<?> toggleUserStatus(@PathVariable Long id) {
        adminService.toggleUserStatus(id);
        return Result.ok("操作成功");
    }

    // ==================== 商品审核 ====================

    /**
     * 待审核商品列表
     * GET /api/admin/goods/pending?page=1&size=12
     */
    @Operation(summary = "待审核商品列表")
    @GetMapping("/goods/pending")
    public Result<PageVO<?>> pendingGoods(@RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "12") Integer size) {
        return Result.ok(adminService.listPendingGoods(page, size));
    }

    /**
     * 审核商品
     * PUT /api/admin/goods/{id}/audit
     * status: 1=通过 (上架), 4=不通过
     */
    @Operation(summary = "审核商品")
    @PutMapping("/goods/{id}/audit")
    public Result<?> auditGoods(@PathVariable Long id,
                                @RequestBody AuditDTO dto) {
        adminService.auditGoods(id, dto.getStatus(), dto.getReason());
        return Result.ok("审核完成");
    }

    /**
     * 全部商品管理列表
     * GET /api/admin/goods?page=1&size=12&keyword=xxx&status=0
     */
    @Operation(summary = "商品管理列表")
    @GetMapping("/goods")
    public Result<PageVO<?>> listGoods(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "12") Integer size,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam(required = false) Integer status) {
        return Result.ok(adminService.listAllGoods(page, size, keyword, status));
    }

    /**
     * 管理员强制下架商品
     * PUT /api/admin/goods/{id}/off-shelf
     */
    @Operation(summary = "强制下架商品")
    @PutMapping("/goods/{id}/off-shelf")
    public Result<?> forceOffShelf(@PathVariable Long id) {
        adminService.forceOffShelf(id);
        return Result.ok("已下架");
    }

    // ==================== 文件上传 ====================

    /**
     * 上传图片
     * POST /api/admin/upload
     * 返回图片访问URL
     */
    @Operation(summary = "上传图片")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        String url = adminService.uploadFile(file);
        return Result.ok("上传成功", url);
    }
}
