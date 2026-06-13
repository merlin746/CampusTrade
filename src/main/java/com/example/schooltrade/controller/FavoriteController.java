package com.example.schooltrade.controller;

import com.example.schooltrade.service.FavoriteService;
import com.example.schooltrade.vo.GoodsVO;
import com.example.schooltrade.vo.PageVO;
import com.example.schooltrade.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏控制器
 */
@Tag(name = "收藏接口", description = "商品收藏管理")
@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    /**
     * 收藏商品
     * POST /api/favorite/{goodsId}
     */
    @Operation(summary = "收藏商品")
    @PostMapping("/{goodsId}")
    public Result<?> add(@RequestAttribute("userId") Long userId,
                         @PathVariable Long goodsId) {
        favoriteService.add(userId, goodsId);
        return Result.ok("收藏成功");
    }

    /**
     * 取消收藏
     * DELETE /api/favorite/{goodsId}
     */
    @Operation(summary = "取消收藏")
    @DeleteMapping("/{goodsId}")
    public Result<?> remove(@RequestAttribute("userId") Long userId,
                            @PathVariable Long goodsId) {
        favoriteService.remove(userId, goodsId);
        return Result.ok("已取消收藏");
    }

    /**
     * 获取我的收藏
     * GET /api/favorite/list?page=1&size=12
     */
    @Operation(summary = "获取收藏列表")
    @GetMapping("/list")
    public Result<PageVO<GoodsVO>> list(@RequestAttribute("userId") Long userId,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "12") Integer size) {
        return Result.ok(favoriteService.getMyFavorites(userId, page, size));
    }

    /**
     * 判断是否已收藏
     * GET /api/favorite/check/{goodsId}
     */
    @Operation(summary = "判断是否已收藏")
    @GetMapping("/check/{goodsId}")
    public Result<Boolean> check(@RequestAttribute("userId") Long userId,
                                 @PathVariable Long goodsId) {
        return Result.ok(favoriteService.isFavorited(userId, goodsId));
    }

    /**
     * 清空所有收藏
     * DELETE /api/favorite/clear
     */
    @Operation(summary = "清空所有收藏")
    @DeleteMapping("/clear")
    public Result<?> clearAll(@RequestAttribute("userId") Long userId) {
        favoriteService.clearAll(userId);
        return Result.ok("已清空所有收藏");
    }
}
