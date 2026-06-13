package com.example.schooltrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.schooltrade.entity.Notice;
import com.example.schooltrade.mapper.NoticeMapper;
import com.example.schooltrade.service.NoticeService;
import com.example.schooltrade.vo.PageVO;
import org.springframework.stereotype.Service;

/**
 * 通知服务实现
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    public NoticeServiceImpl(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }

    @Override
    public void send(Long userId, Integer type, String title, String content, Long relatedId) {
        Notice notice = new Notice();
        notice.setUserId(userId);
        notice.setType(type);
        notice.setTitle(title);
        notice.setContent(content);
        notice.setRelatedId(relatedId);
        notice.setIsRead(0);
        noticeMapper.insert(notice);
    }

    @Override
    public PageVO<Notice> listByUser(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getUserId, userId)
               .orderByDesc(Notice::getCreateTime);

        Page<Notice> page = new Page<>(pageNum, pageSize);
        page = noticeMapper.selectPage(page, wrapper);

        return new PageVO<>(page.getRecords(), page.getTotal(),
                (int) page.getCurrent(), (int) page.getSize(), (int) page.getPages());
    }

    @Override
    public void markRead(Long userId, Long noticeId) {
        Notice notice = noticeMapper.selectById(noticeId);
        if (notice != null && notice.getUserId().equals(userId)) {
            notice.setIsRead(1);
            noticeMapper.updateById(notice);
        }
    }

    @Override
    public Long unreadCount(Long userId) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getUserId, userId).eq(Notice::getIsRead, 0);
        return noticeMapper.selectCount(wrapper);
    }
}
