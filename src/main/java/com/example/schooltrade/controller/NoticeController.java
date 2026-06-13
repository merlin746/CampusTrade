package com.example.schooltrade.controller;

import com.example.schooltrade.entity.Notice;
import com.example.schooltrade.service.NoticeService;
import com.example.schooltrade.vo.PageVO;
import com.example.schooltrade.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * 通知控制器
 */
@Tag(name = "通知接口", description = "系统通知")
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /**
     * 获取通知列表
     * GET /api/notice/list?page=1&size=20
     */
    @Operation(summary = "获取通知列表")
    @GetMapping("/list")
    public Result<PageVO<Notice>> list(@RequestAttribute("userId") Long userId,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "20") Integer size) {
        return Result.ok(noticeService.listByUser(userId, page, size));
    }

    /**
     * 标记已读
     * PUT /api/notice/{id}/read
     */
    @Operation(summary = "标记通知已读")
    @PutMapping("/{id}/read")
    public Result<?> markRead(@RequestAttribute("userId") Long userId,
                              @PathVariable Long id) {
        noticeService.markRead(userId, id);
        return Result.ok();
    }

    /**
     * 获取未读数量
     * GET /api/notice/unread-count
     */
    @Operation(summary = "获取未读通知数")
    @GetMapping("/unread-count")
    public Result<Long> unreadCount(@RequestAttribute("userId") Long userId) {
        return Result.ok(noticeService.unreadCount(userId));
    }
}
