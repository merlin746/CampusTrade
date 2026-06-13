package com.example.schooltrade.controller;

import com.example.schooltrade.dto.GoodsDTO;
import com.example.schooltrade.dto.GoodsQueryDTO;
import com.example.schooltrade.service.GoodsService;
import com.example.schooltrade.vo.GoodsVO;
import com.example.schooltrade.vo.PageVO;
import com.example.schooltrade.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 商品控制器
 * 搜索和查看商品无需认证，发布/编辑/下架需要登录
 */
@Tag(name = "商品接口", description = "商品发布、搜索、详情")
@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    /**
     * 发布商品
     * POST /api/goods
     * 发布后商品状态为"待审核"，需要管理员审核通过后才上架
     */
    @Operation(summary = "发布商品")
    @PostMapping
    public Result<?> publish(@RequestAttribute("userId") Long userId,
                             @Valid @RequestBody GoodsDTO dto) {
        goodsService.publish(userId, dto);
        return Result.ok("发布成功，请等待管理员审核");
    }

    /**
     * 编辑商品
     * PUT /api/goods/{id}
     * 编辑后商品将重新进入待审核状态
     */
    @Operation(summary = "编辑商品")
    @PutMapping("/{id}")
    public Result<?> update(@RequestAttribute("userId") Long userId,
                            @PathVariable Long id,
                            @Valid @RequestBody GoodsDTO dto) {
        goodsService.update(userId, id, dto);
        return Result.ok("修改成功，商品需重新审核");
    }

    /**
     * 下架商品（在售 → 下架）
     * PUT /api/goods/{id}/off
     */
    @Operation(summary = "下架商品")
    @PutMapping("/{id}/off")
    public Result<?> offShelf(@RequestAttribute("userId") Long userId,
                              @PathVariable Long id) {
        goodsService.offShelf(userId, id);
        return Result.ok("已下架");
    }

    /**
     * 撤销发布（待审核 → 删除）
     * DELETE /api/goods/{id}/withdraw
     */
    @Operation(summary = "撤销发布")
    @DeleteMapping("/{id}/withdraw")
    public Result<?> withdraw(@RequestAttribute("userId") Long userId,
                              @PathVariable Long id) {
        goodsService.withdraw(userId, id);
        return Result.ok("已撤销");
    }

    /**
     * 删除商品记录（已下架/审核不通过）
     * DELETE /api/goods/{id}
     */
    @Operation(summary = "删除商品记录")
    @DeleteMapping("/{id}")
    public Result<?> deleteGoods(@RequestAttribute("userId") Long userId,
                                  @PathVariable Long id) {
        goodsService.deleteGoods(userId, id);
        return Result.ok("商品记录已删除");
    }

    /**
     * 获取商品详情
     * GET /api/goods/{id}
     * 公开接口，访问会增加浏览量
     */
    @Operation(summary = "获取商品详情")
    @GetMapping("/{id}")
    public Result<GoodsVO> detail(@PathVariable Long id,
                                  @RequestAttribute(value = "userId", required = false) Long userId) {
        goodsService.increaseViewCount(id);
        return Result.ok(goodsService.getDetail(id, userId));
    }

    /**
     * 搜索/筛选商品
     * GET /api/goods?keyword=xxx&categoryId=1&sortBy=0&page=1&size=12
     * 公开接口
     */
    @Operation(summary = "搜索/筛选商品")
    @GetMapping
    public Result<PageVO<GoodsVO>> search(@ModelAttribute GoodsQueryDTO query) {
        return Result.ok(goodsService.search(query));
    }

    /**
     * 获取我的发布
     * GET /api/goods/my?page=1&size=10
     */
    @Operation(summary = "获取我的发布")
    @GetMapping("/my")
    public Result<PageVO<GoodsVO>> myGoods(@RequestAttribute("userId") Long userId,
                                           @RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(goodsService.getMyGoods(userId, page, size));
    }
}
