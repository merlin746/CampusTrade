package com.example.schooltrade.controller;

import com.example.schooltrade.dto.ReviewDTO;
import com.example.schooltrade.service.ReviewService;
import com.example.schooltrade.vo.PageVO;
import com.example.schooltrade.vo.Result;
import com.example.schooltrade.vo.ReviewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评价控制器
 */
@Tag(name = "评价接口", description = "商品评价管理")
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 创建评价
     * POST /api/review
     * 评价后自动更新商品平均分和被评价人信誉分
     */
    @Operation(summary = "创建评价")
    @PostMapping
    public Result<?> create(@RequestAttribute("userId") Long reviewerId,
                            @Valid @RequestBody ReviewDTO dto) {
        reviewService.create(reviewerId, dto);
        return Result.ok("评价成功");
    }

    /**
     * 获取商品的评价列表
     * GET /api/review/goods/{goodsId}?page=1&size=10
     */
    @Operation(summary = "获取商品评价")
    @GetMapping("/goods/{goodsId}")
    public Result<PageVO<ReviewVO>> goodsReviews(@PathVariable Long goodsId,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(reviewService.getByGoods(goodsId, page, size));
    }

    /**
     * 获取用户的评价列表
     * GET /api/review/user/{userId}?page=1&size=10
     */
    @Operation(summary = "获取用户评价")
    @GetMapping("/user/{userId}")
    public Result<PageVO<ReviewVO>> userReviews(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "1") Integer page,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(reviewService.getByUser(userId, page, size));
    }

    /**
     * 获取商品评分统计
     * GET /api/review/stats/{goodsId}
     * 返回各星级评价数量 [1星, 2星, 3星, 4星, 5星]
     */
    @Operation(summary = "获取商品评分统计")
    @GetMapping("/stats/{goodsId}")
    public Result<List<Long>> scoreStats(@PathVariable Long goodsId) {
        return Result.ok(reviewService.getScoreStats(goodsId));
    }
}
