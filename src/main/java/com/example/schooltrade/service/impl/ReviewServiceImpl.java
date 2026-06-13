package com.example.schooltrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.schooltrade.dto.ReviewDTO;
import com.example.schooltrade.entity.*;
import com.example.schooltrade.exception.BusinessException;
import com.example.schooltrade.mapper.*;
import com.example.schooltrade.service.NoticeService;
import com.example.schooltrade.service.ReviewService;
import com.example.schooltrade.vo.PageVO;
import com.example.schooltrade.vo.ReviewVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评价服务实现
 * 买家卖家互评，评价后更新商品平均分和被评价人信誉分
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final OrderMapper orderMapper;
    private final GoodsMapper goodsMapper;
    private final UserMapper userMapper;
    private final NoticeService noticeService;

    public ReviewServiceImpl(ReviewMapper reviewMapper, OrderMapper orderMapper,
                             GoodsMapper goodsMapper, UserMapper userMapper,
                             NoticeService noticeService) {
        this.reviewMapper = reviewMapper;
        this.orderMapper = orderMapper;
        this.goodsMapper = goodsMapper;
        this.userMapper = userMapper;
        this.noticeService = noticeService;
    }

    @Override
    @Transactional
    public void create(Long reviewerId, ReviewDTO dto) {
        // ===== 模式1: 直接商品评论（无订单ID） =====
        if (dto.getOrderId() == null && dto.getGoodsId() != null) {
            createDirectReview(reviewerId, dto);
            return;
        }

        // ===== 模式2: 订单评价（需订单ID） =====
        Order order = orderMapper.selectById(dto.getOrderId());
        if (order == null) throw new BusinessException(404, "订单不存在");
        if (order.getStatus() != 2) throw new BusinessException(400, "订单未完成，无法评价");

        // 判断评价角色: 评价人是买家还是卖家
        boolean isBuyer = order.getBuyerId().equals(reviewerId);
        boolean isSeller = order.getSellerId().equals(reviewerId);
        if (!isBuyer && !isSeller) throw new BusinessException(403, "无权评价此订单");

        // 检查是否已评价过
        if (isBuyer && order.getBuyerReviewed() == 1) throw new BusinessException(400, "您已评价过");
        if (isSeller && order.getSellerReviewed() == 1) throw new BusinessException(400, "您已评价过");

        Long targetId = isBuyer ? order.getSellerId() : order.getBuyerId();

        Review review = new Review();
        review.setOrderId(order.getId());
        review.setGoodsId(order.getGoodsId());
        review.setReviewerId(reviewerId);
        review.setTargetId(targetId);
        review.setRole(isBuyer ? 0 : 1);  // 0=买家评卖家, 1=卖家评买家
        review.setScore(dto.getScore());
        review.setContent(dto.getContent());
        review.setIsAnonymous(dto.getIsAnonymous() != null ? dto.getIsAnonymous() : 0);
        review.setStatus(1);
        reviewMapper.insert(review);

        // 更新订单评价标记
        if (isBuyer) order.setBuyerReviewed(1);
        else order.setSellerReviewed(1);
        orderMapper.updateById(order);

        // 更新商品平均评分
        updateGoodsAvgScore(order.getGoodsId());

        // 更新被评价人信誉分（+评分）
        User target = userMapper.selectById(targetId);
        if (target != null) {
            int delta = dto.getScore() - 3;  // 3分为基准，高分加分，低分扣分
            target.setCreditScore(Math.max(0, Math.min(1000, target.getCreditScore() + delta * 5)));
            userMapper.updateById(target);
        }

        // 通知被评价人
        noticeService.send(targetId, 2, "收到新评价",
                "您在订单 " + order.getOrderNo() + " 中收到了 " + dto.getScore() + " 星评价", order.getId());
    }

    /**
     * 直接商品评论（无需下单即可评论商品）
     */
    private void createDirectReview(Long reviewerId, ReviewDTO dto) {
        Goods goods = goodsMapper.selectById(dto.getGoodsId());
        if (goods == null) throw new BusinessException(404, "商品不存在");
        if (goods.getStatus() != 1 && goods.getStatus() != 2) {
            throw new BusinessException(400, "该商品暂不支持评论");
        }

        Review review = new Review();
        review.setOrderId(0L);  // 0表示直接评论，无订单关联
        review.setGoodsId(dto.getGoodsId());
        review.setReviewerId(reviewerId);
        review.setTargetId(goods.getSellerId());  // 评价指向卖家
        review.setRole(2);  // 2=直接评论(非订单评价)
        review.setScore(dto.getScore());
        review.setContent(dto.getContent());
        review.setIsAnonymous(dto.getIsAnonymous() != null ? dto.getIsAnonymous() : 0);
        review.setStatus(1);
        reviewMapper.insert(review);

        // 更新商品平均评分
        updateGoodsAvgScore(dto.getGoodsId());

        // 通知卖家收到新评论
        User reviewer = userMapper.selectById(reviewerId);
        String reviewerName = dto.getIsAnonymous() == 1 ? "匿名用户" : (reviewer != null ? reviewer.getUsername() : "用户");
        noticeService.send(goods.getSellerId(), 2, "商品新评论",
                reviewerName + " 对您的商品《" + goods.getTitle() + "》发表了 " + dto.getScore() + " 星评论", dto.getGoodsId());
    }

    @Override
    public PageVO<ReviewVO> getByGoods(Long goodsId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getGoodsId, goodsId)
               .eq(Review::getStatus, 1)
               .orderByDesc(Review::getCreateTime);

        Page<Review> page = new Page<>(pageNum, pageSize);
        page = reviewMapper.selectPage(page, wrapper);

        List<ReviewVO> records = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageVO<>(records, page.getTotal(),
                (int) page.getCurrent(), (int) page.getSize(), (int) page.getPages());
    }

    @Override
    public PageVO<ReviewVO> getByUser(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getTargetId, userId)
               .eq(Review::getStatus, 1)
               .orderByDesc(Review::getCreateTime);

        Page<Review> page = new Page<>(pageNum, pageSize);
        page = reviewMapper.selectPage(page, wrapper);

        List<ReviewVO> records = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageVO<>(records, page.getTotal(),
                (int) page.getCurrent(), (int) page.getSize(), (int) page.getPages());
    }

    @Override
    public List<Long> getScoreStats(Long goodsId) {
        // 返回1-5星各有多少个评价 [1星数, 2星数, 3星数, 4星数, 5星数]
        List<Long> stats = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Review::getGoodsId, goodsId)
                   .eq(Review::getScore, i)
                   .eq(Review::getStatus, 1);
            stats.add(reviewMapper.selectCount(wrapper));
        }
        return stats;
    }

    private void updateGoodsAvgScore(Long goodsId) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getGoodsId, goodsId).eq(Review::getStatus, 1);
        List<Review> reviews = reviewMapper.selectList(wrapper);
        if (reviews.isEmpty()) return;

        double avg = reviews.stream().mapToInt(Review::getScore).average().orElse(0);
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods != null) {
            goods.setAvgScore(BigDecimal.valueOf(avg).setScale(1, RoundingMode.HALF_UP));
            goods.setReviewCount(reviews.size());
            goodsMapper.updateById(goods);
        }
    }

    private ReviewVO convertToVO(Review review) {
        ReviewVO vo = new ReviewVO();
        BeanUtils.copyProperties(review, vo);

        User reviewer = userMapper.selectById(review.getReviewerId());
        if (reviewer != null) {
            // 匿名评价时隐藏评价人信息
            if (review.getIsAnonymous() == 1) {
                vo.setReviewerName("匿名用户");
                vo.setReviewerAvatar(null);
            } else {
                vo.setReviewerName(reviewer.getUsername());
                vo.setReviewerAvatar(reviewer.getAvatar());
            }
        }

        return vo;
    }
}
