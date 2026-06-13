package com.example.schooltrade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.schooltrade.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品 Mapper
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
}
