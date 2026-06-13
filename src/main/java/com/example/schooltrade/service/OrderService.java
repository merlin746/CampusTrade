package com.example.schooltrade.service;

import com.example.schooltrade.dto.OrderDTO;
import com.example.schooltrade.vo.OrderVO;
import com.example.schooltrade.vo.PageVO;

/**
 * 订单服务接口
 */
public interface OrderService {

    /** 买家下单 */
    OrderVO create(Long buyerId, OrderDTO dto);

    /** 卖家确认订单 */
    void confirm(Long sellerId, Long orderId);

    /** 买家/卖家取消订单 */
    void cancel(Long userId, Long orderId, String reason);

    /** 买家确认收货，完成订单 */
    void complete(Long buyerId, Long orderId);

    /** 获取我买到的 */
    PageVO<OrderVO> getBought(Long buyerId, Integer page, Integer size);

    /** 获取我卖出的 */
    PageVO<OrderVO> getSold(Long sellerId, Integer page, Integer size);

    /** 获取订单详情 */
    OrderVO getDetail(Long orderId);

    /** 删除已完成/已取消的历史订单 */
    void deleteOrder(Long userId, Long orderId);
}
