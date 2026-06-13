package com.example.schooltrade.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.schooltrade.dto.OrderDTO;
import com.example.schooltrade.entity.Goods;
import com.example.schooltrade.entity.GoodsImage;
import com.example.schooltrade.entity.Order;
import com.example.schooltrade.entity.User;
import com.example.schooltrade.exception.BusinessException;
import com.example.schooltrade.mapper.GoodsImageMapper;
import com.example.schooltrade.mapper.GoodsMapper;
import com.example.schooltrade.mapper.OrderMapper;
import com.example.schooltrade.mapper.UserMapper;
import com.example.schooltrade.service.NoticeService;
import com.example.schooltrade.service.OrderService;
import com.example.schooltrade.vo.OrderVO;
import com.example.schooltrade.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现
 * 核心业务：下单、确认、取消、完成，以及订单状态流转
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final GoodsMapper goodsMapper;
    private final GoodsImageMapper goodsImageMapper;
    private final UserMapper userMapper;
    private final NoticeService noticeService;

    public OrderServiceImpl(OrderMapper orderMapper, GoodsMapper goodsMapper,
                            GoodsImageMapper goodsImageMapper, UserMapper userMapper,
                            NoticeService noticeService) {
        this.orderMapper = orderMapper;
        this.goodsMapper = goodsMapper;
        this.goodsImageMapper = goodsImageMapper;
        this.userMapper = userMapper;
        this.noticeService = noticeService;
    }

    @Override
    @Transactional
    public OrderVO create(Long buyerId, OrderDTO dto) {
        Goods goods = goodsMapper.selectById(dto.getGoodsId());
        if (goods == null) throw new BusinessException(404, "商品不存在");
        if (goods.getStatus() != 1) throw new BusinessException(400, "商品已下架或已售出");
        if (goods.getSellerId().equals(buyerId)) throw new BusinessException(400, "无法购买自己的商品");

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setGoodsId(goods.getId());
        order.setBuyerId(buyerId);
        order.setSellerId(goods.getSellerId());
        order.setPrice(goods.getPrice());
        order.setStatus(0);  // 待确认

        orderMapper.insert(order);

        // 商品状态改为已售出（预留）
        goods.setStatus(2);
        goodsMapper.updateById(goods);

        // 发送通知给卖家
        User buyer = userMapper.selectById(buyerId);
        noticeService.send(goods.getSellerId(), 2, "新订单",
                "用户 " + (buyer != null ? buyer.getUsername() : "") + " 购买了您的商品《" + goods.getTitle() + "》",
                order.getId());

        return convertToVO(order);
    }

    @Override
    @Transactional
    public void confirm(Long sellerId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException(404, "订单不存在");
        if (!order.getSellerId().equals(sellerId)) throw new BusinessException(403, "无权操作");
        if (order.getStatus() != 0) throw new BusinessException(400, "当前状态无法确认");

        order.setStatus(1);  // 已确认
        orderMapper.updateById(order);

        noticeService.send(order.getBuyerId(), 2, "订单已确认",
                "卖家已确认您的订单 " + order.getOrderNo(), orderId);
    }

    @Override
    @Transactional
    public void cancel(Long userId, Long orderId, String reason) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException(404, "订单不存在");
        if (!order.getBuyerId().equals(userId) && !order.getSellerId().equals(userId)) {
            throw new BusinessException(403, "无权操作");
        }
        if (order.getStatus() == 2 || order.getStatus() == 3) {
            throw new BusinessException(400, "当前状态无法取消");
        }

        order.setStatus(3);  // 已取消
        order.setCancelReason(reason);
        orderMapper.updateById(order);

        // 恢复商品为在售
        Goods goods = goodsMapper.selectById(order.getGoodsId());
        if (goods != null) {
            goods.setStatus(1);
            goodsMapper.updateById(goods);
        }

        // 通知对方
        Long targetId = order.getBuyerId().equals(userId) ? order.getSellerId() : order.getBuyerId();
        noticeService.send(targetId, 2, "订单已取消",
                "订单 " + order.getOrderNo() + " 已取消，原因: " + reason, orderId);
    }

    @Override
    @Transactional
    public void complete(Long buyerId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException(404, "订单不存在");
        if (!order.getBuyerId().equals(buyerId)) throw new BusinessException(403, "无权操作");
        if (order.getStatus() != 1) throw new BusinessException(400, "当前状态无法完成");

        order.setStatus(2);  // 已完成
        order.setCompleteTime(LocalDateTime.now());
        orderMapper.updateById(order);

        noticeService.send(order.getSellerId(), 2, "交易完成",
                "买家已确认收货，订单 " + order.getOrderNo() + " 交易完成", orderId);
    }

    @Override
    public PageVO<OrderVO> getBought(Long buyerId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getBuyerId, buyerId).orderByDesc(Order::getCreateTime);
        return queryPage(pageNum, pageSize, wrapper);
    }

    @Override
    public PageVO<OrderVO> getSold(Long sellerId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getSellerId, sellerId).orderByDesc(Order::getCreateTime);
        return queryPage(pageNum, pageSize, wrapper);
    }

    @Override
    public OrderVO getDetail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException(404, "订单不存在");
        return convertToVO(order);
    }

    private PageVO<OrderVO> queryPage(Integer pageNum, Integer pageSize, LambdaQueryWrapper<Order> wrapper) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        page = orderMapper.selectPage(page, wrapper);
        List<OrderVO> records = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return new PageVO<>(records, page.getTotal(),
                (int) page.getCurrent(), (int) page.getSize(), (int) page.getPages());
    }

    private OrderVO convertToVO(Order order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);

        // 商品信息
        Goods goods = goodsMapper.selectById(order.getGoodsId());
        if (goods != null) {
            vo.setGoodsTitle(goods.getTitle());
            // 取第一张图片
            LambdaQueryWrapper<GoodsImage> imgWrapper = new LambdaQueryWrapper<>();
            imgWrapper.eq(GoodsImage::getGoodsId, goods.getId()).orderByAsc(GoodsImage::getSortOrder).last("LIMIT 1");
            GoodsImage img = goodsImageMapper.selectOne(imgWrapper);
            vo.setGoodsImage(img != null ? img.getImageUrl() : null);
        }

        // 用户信息
        User buyer = userMapper.selectById(order.getBuyerId());
        vo.setBuyerName(buyer != null ? buyer.getUsername() : "");
        User seller = userMapper.selectById(order.getSellerId());
        vo.setSellerName(seller != null ? seller.getUsername() : "");

        return vo;
    }

    @Override
    public void deleteOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException(404, "订单不存在");
        if (!order.getBuyerId().equals(userId) && !order.getSellerId().equals(userId)) {
            throw new BusinessException(403, "无权删除此订单");
        }
        // 只允许删除【已完成(2)】或【已取消(3)】的订单
        if (order.getStatus() != 2 && order.getStatus() != 3) {
            throw new BusinessException(400, "只能删除已完成或已取消的订单");
        }
        orderMapper.deleteById(orderId);
    }

    /** 生成订单号: OT + 年月日时分秒 + 4位随机数 */
    private String generateOrderNo() {
        return "OT" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(4);
    }
}
