package com.example.schooltrade.controller;

import com.example.schooltrade.service.AdminService;
import com.example.schooltrade.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通用文件上传控制器
 * 供用户在发布商品时上传图片
 */
@Tag(name = "文件接口", description = "图片上传")
@RestController
@RequestMapping("/api/upload")
public class FileController {

    private final AdminService adminService;

    public FileController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * 上传图片
     * POST /api/upload/image
     * 返回图片访问URL，用于商品发布时的图片列表
     */
    @Operation(summary = "上传图片")
    @PostMapping("/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = adminService.uploadFile(file);
        return Result.ok("上传成功", url);
    }
}
