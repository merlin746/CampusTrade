package com.example.schooltrade.controller;

import com.example.schooltrade.dto.OrderDTO;
import com.example.schooltrade.service.OrderService;
import com.example.schooltrade.vo.OrderVO;
import com.example.schooltrade.vo.PageVO;
import com.example.schooltrade.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 * 订单状态流转: 买家创建(待确认) → 卖家确认 → 买家收货完成
 * 任意非终态都可以取消
 */
@Tag(name = "订单接口", description = "订单创建、确认、取消、完成")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 创建订单（买家下单）
     * POST /api/order
     * 下单后商品状态变为"已售出"
     */
    @Operation(summary = "创建订单")
    @PostMapping
    public Result<OrderVO> create(@RequestAttribute("userId") Long buyerId,
                                  @Valid @RequestBody OrderDTO dto) {
        return Result.ok("下单成功", orderService.create(buyerId, dto));
    }

    /**
     * 卖家确认订单
     * PUT /api/order/{id}/confirm
     */
    @Operation(summary = "卖家确认订单")
    @PutMapping("/{id}/confirm")
    public Result<?> confirm(@RequestAttribute("userId") Long sellerId,
                             @PathVariable Long id) {
        orderService.confirm(sellerId, id);
        return Result.ok("已确认");
    }

    /**
     * 取消订单
     * PUT /api/order/{id}/cancel?reason=xxx
     * 买家和卖家都可以取消
     */
    @Operation(summary = "取消订单")
    @PutMapping("/{id}/cancel")
    public Result<?> cancel(@RequestAttribute("userId") Long userId,
                            @PathVariable Long id,
                            @RequestParam(defaultValue = "") String reason) {
        orderService.cancel(userId, id, reason);
        return Result.ok("已取消");
    }

    /**
     * 买家确认收货，完成订单
     * PUT /api/order/{id}/complete
     */
    @Operation(summary = "确认收货")
    @PutMapping("/{id}/complete")
    public Result<?> complete(@RequestAttribute("userId") Long buyerId,
                              @PathVariable Long id) {
        orderService.complete(buyerId, id);
        return Result.ok("交易完成");
    }

    /**
     * 删除历史订单（仅已完成/已取消）
     * DELETE /api/order/{id}
     */
    @Operation(summary = "删除订单记录")
    @DeleteMapping("/{id}")
    public Result<?> deleteOrder(@RequestAttribute("userId") Long userId,
                                  @PathVariable Long id) {
        orderService.deleteOrder(userId, id);
        return Result.ok("订单记录已删除");
    }

    /**
     * 获取我买到的订单
     * GET /api/order/bought?page=1&size=10
     */
    @Operation(summary = "我买到的")
    @GetMapping("/bought")
    public Result<PageVO<OrderVO>> bought(@RequestAttribute("userId") Long userId,
                                          @RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(orderService.getBought(userId, page, size));
    }

    /**
     * 获取我卖出的订单
     * GET /api/order/sold?page=1&size=10
     */
    @Operation(summary = "我卖出的")
    @GetMapping("/sold")
    public Result<PageVO<OrderVO>> sold(@RequestAttribute("userId") Long userId,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(orderService.getSold(userId, page, size));
    }

    /**
     * 获取订单详情
     * GET /api/order/{id}
     */
    @Operation(summary = "获取订单详情")
    @GetMapping("/{id}")
    public Result<OrderVO> detail(@PathVariable Long id) {
        return Result.ok(orderService.getDetail(id));
    }
}
