package com.example.schooltrade.service;

import com.example.schooltrade.entity.Notice;
import com.example.schooltrade.vo.PageVO;

/**
 * 通知服务接口
 */
public interface NoticeService {

    /** 发送通知 */
    void send(Long userId, Integer type, String title, String content, Long relatedId);

    /** 获取用户的通知列表 */
    PageVO<Notice> listByUser(Long userId, Integer page, Integer size);

    /** 标记已读 */
    void markRead(Long userId, Long noticeId);

    /** 获取未读数量 */
    Long unreadCount(Long userId);
}
