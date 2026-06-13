package com.example.schooltrade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.schooltrade.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏 Mapper
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}
