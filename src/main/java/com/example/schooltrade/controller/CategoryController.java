package com.example.schooltrade.controller;

import com.example.schooltrade.entity.Category;
import com.example.schooltrade.service.CategoryService;
import com.example.schooltrade.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 * 获取分类列表无需认证，管理分类需要管理员权限
 */
@Tag(name = "分类接口", description = "商品分类管理")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 获取所有分类
     * GET /api/category/list
     * 公开接口，无需认证
     */
    @Operation(summary = "获取所有分类")
    @GetMapping("/list")
    public Result<List<Category>> list() {
        return Result.ok(categoryService.listAll());
    }

    /**
     * 新增分类（管理员）
     * POST /api/category
     */
    @Operation(summary = "新增分类")
    @PostMapping
    public Result<?> create(@RequestBody Category category) {
        categoryService.create(category);
        return Result.ok("创建成功");
    }

    /**
     * 更新分类（管理员）
     * PUT /api/category/{id}
     */
    @Operation(summary = "更新分类")
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryService.update(category);
        return Result.ok("更新成功");
    }

    /**
     * 删除分类（管理员）
     * DELETE /api/category/{id}
     */
    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.ok("删除成功");
    }
}
