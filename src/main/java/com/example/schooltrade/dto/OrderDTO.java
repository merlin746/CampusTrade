package com.example.schooltrade.dto;

import jakarta.validation.constraints.NotNull;
/**
 * 创建订单请求
 */
public class OrderDTO {

    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

    /** 可选: 买家留言或备注 */
    private String remark;

    public OrderDTO() {}

    public OrderDTO(Long goodsId, String remark) {
        this.goodsId = goodsId;
        this.remark = remark;
    }

    public Long getGoodsId() { return goodsId; }
    public void setGoodsId(Long goodsId) { this.goodsId = goodsId; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}