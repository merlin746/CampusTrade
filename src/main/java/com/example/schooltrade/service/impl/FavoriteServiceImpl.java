package com.example.schooltrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.schooltrade.entity.Favorite;
import com.example.schooltrade.entity.Goods;
import com.example.schooltrade.exception.BusinessException;
import com.example.schooltrade.mapper.FavoriteMapper;
import com.example.schooltrade.mapper.GoodsMapper;
import com.example.schooltrade.service.FavoriteService;
import com.example.schooltrade.service.GoodsService;
import com.example.schooltrade.vo.GoodsVO;
import com.example.schooltrade.vo.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 收藏服务实现
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final GoodsMapper goodsMapper;
    private final GoodsService goodsService;

    public FavoriteServiceImpl(FavoriteMapper favoriteMapper, GoodsMapper goodsMapper,
                               GoodsService goodsService) {
        this.favoriteMapper = favoriteMapper;
        this.goodsMapper = goodsMapper;
        this.goodsService = goodsService;
    }

    @Override
    @Transactional
    public void add(Long userId, Long goodsId) {
        // 检查是否已收藏
        if (isFavorited(userId, goodsId)) {
            throw new BusinessException(400, "已收藏该商品");
        }
        // 检查商品是否存在
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) throw new BusinessException(404, "商品不存在");

        Favorite fav = new Favorite();
        fav.setUserId(userId);
        fav.setGoodsId(goodsId);
        favoriteMapper.insert(fav);

        // 更新商品收藏数
        goods.setFavoriteCount(goods.getFavoriteCount() + 1);
        goodsMapper.updateById(goods);
    }

    @Override
    @Transactional
    public void remove(Long userId, Long goodsId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .eq(Favorite::getGoodsId, goodsId);
        favoriteMapper.delete(wrapper);

        // 更新商品收藏数
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods != null && goods.getFavoriteCount() > 0) {
            goods.setFavoriteCount(goods.getFavoriteCount() - 1);
            goodsMapper.updateById(goods);
        }
    }

    @Override
    public PageVO<GoodsVO> getMyFavorites(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .orderByDesc(Favorite::getCreateTime);

        Page<Favorite> page = new Page<>(pageNum, pageSize);
        page = favoriteMapper.selectPage(page, wrapper);

        // 转换为商品VO列表
        java.util.List<GoodsVO> records = page.getRecords().stream()
                .map(fav -> goodsService.getDetail(fav.getGoodsId(), userId))
                .collect(java.util.stream.Collectors.toList());

        return new PageVO<>(records, page.getTotal(),
                (int) page.getCurrent(), (int) page.getSize(),
                (int) page.getPages());
    }

    @Override
    public boolean isFavorited(Long userId, Long goodsId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .eq(Favorite::getGoodsId, goodsId);
        return favoriteMapper.selectCount(wrapper) > 0;
    }

    @Override
    @Transactional
    public void clearAll(Long userId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        favoriteMapper.delete(wrapper);
    }
}
