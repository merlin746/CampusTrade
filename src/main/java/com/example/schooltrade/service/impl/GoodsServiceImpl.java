package com.example.schooltrade.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.schooltrade.dto.GoodsDTO;
import com.example.schooltrade.dto.GoodsQueryDTO;
import com.example.schooltrade.entity.*;
import com.example.schooltrade.exception.BusinessException;
import com.example.schooltrade.mapper.*;
import com.example.schooltrade.service.GoodsService;
import com.example.schooltrade.vo.GoodsVO;
import com.example.schooltrade.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务实现
 * 核心业务：商品的发布、编辑、搜索、详情查询
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    private final GoodsMapper goodsMapper;
    private final GoodsImageMapper goodsImageMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final FavoriteMapper favoriteMapper;

    public GoodsServiceImpl(GoodsMapper goodsMapper, GoodsImageMapper goodsImageMapper,
                            CategoryMapper categoryMapper, UserMapper userMapper,
                            FavoriteMapper favoriteMapper) {
        this.goodsMapper = goodsMapper;
        this.goodsImageMapper = goodsImageMapper;
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    @Transactional
    public void publish(Long sellerId, GoodsDTO dto) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(dto, goods);
        goods.setSellerId(sellerId);
        goods.setStatus(0);  // 待审核
        goods.setViewCount(0);
        goods.setFavoriteCount(0);
        goods.setReviewCount(0);
        goodsMapper.insert(goods);

        // 保存图片
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            for (int i = 0; i < dto.getImages().size(); i++) {
                GoodsImage img = new GoodsImage();
                img.setGoodsId(goods.getId());
                img.setImageUrl(dto.getImages().get(i));
                img.setSortOrder(i);
                goodsImageMapper.insert(img);
            }
        }
    }

    @Override
    @Transactional
    public void update(Long userId, Long goodsId, GoodsDTO dto) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) throw new BusinessException(404, "商品不存在");
        if (!goods.getSellerId().equals(userId)) {
            throw new BusinessException(403, "只能编辑自己的商品");
        }
        BeanUtils.copyProperties(dto, goods);
        goods.setStatus(0); // 重新编辑后需要重新审核
        goodsMapper.updateById(goods);

        // 更新图片：先删旧图，再插新图
        LambdaQueryWrapper<GoodsImage> imgWrapper = new LambdaQueryWrapper<>();
        imgWrapper.eq(GoodsImage::getGoodsId, goodsId);
        goodsImageMapper.delete(imgWrapper);

        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            for (int i = 0; i < dto.getImages().size(); i++) {
                GoodsImage img = new GoodsImage();
                img.setGoodsId(goodsId);
                img.setImageUrl(dto.getImages().get(i));
                img.setSortOrder(i);
                goodsImageMapper.insert(img);
            }
        }
    }

    @Override
    public void offShelf(Long userId, Long goodsId) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) throw new BusinessException(404, "商品不存在");
        if (!goods.getSellerId().equals(userId)) {
            throw new BusinessException(403, "只能下架自己的商品");
        }
        if (goods.getStatus() != 1) {
            throw new BusinessException(400, "只有【在售】状态的商品才能下架");
        }
        goods.setStatus(3);  // 已下架
        goodsMapper.updateById(goods);
    }

    @Override
    @Transactional
    public void withdraw(Long userId, Long goodsId) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) throw new BusinessException(404, "商品不存在");
        if (!goods.getSellerId().equals(userId)) {
            throw new BusinessException(403, "只能撤销自己的商品");
        }
        if (goods.getStatus() != 0) {
            throw new BusinessException(400, "只有【待审核】状态的商品才能撤销发布");
        }
        // 删除商品的图片记录
        LambdaQueryWrapper<GoodsImage> imgWrapper = new LambdaQueryWrapper<>();
        imgWrapper.eq(GoodsImage::getGoodsId, goodsId);
        goodsImageMapper.delete(imgWrapper);
        // 删除商品
        goodsMapper.deleteById(goodsId);
    }

    @Override
    @Transactional
    public void deleteGoods(Long userId, Long goodsId) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) throw new BusinessException(404, "商品不存在");
        if (!goods.getSellerId().equals(userId)) {
            throw new BusinessException(403, "只能删除自己的商品记录");
        }
        // 只允许删除【已下架(3)】或【审核不通过(4)】的商品
        if (goods.getStatus() != 3 && goods.getStatus() != 4) {
            throw new BusinessException(400, "只能删除已下架或审核不通过的商品记录");
        }
        // 删除图片记录
        LambdaQueryWrapper<GoodsImage> imgWrapper = new LambdaQueryWrapper<>();
        imgWrapper.eq(GoodsImage::getGoodsId, goodsId);
        goodsImageMapper.delete(imgWrapper);
        // 删除商品
        goodsMapper.deleteById(goodsId);
    }

    @Override
    public GoodsVO getDetail(Long goodsId, Long currentUserId) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) throw new BusinessException(404, "商品不存在");

        GoodsVO vo = convertToVO(goods);

        // 判断当前用户是否已收藏
        if (currentUserId != null) {
            LambdaQueryWrapper<Favorite> favWrapper = new LambdaQueryWrapper<>();
            favWrapper.eq(Favorite::getUserId, currentUserId)
                       .eq(Favorite::getGoodsId, goodsId);
            vo.setIsFavorited(favoriteMapper.selectCount(favWrapper) > 0);
        } else {
            vo.setIsFavorited(false);
        }

        return vo;
    }

    @Override
    public PageVO<GoodsVO> search(GoodsQueryDTO query) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        // 只查询在售商品
        wrapper.eq(Goods::getStatus, 1);

        // 关键词搜索（模糊匹配标题）
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.like(Goods::getTitle, query.getKeyword());
        }
        // 分类筛选
        if (query.getCategoryId() != null) {
            wrapper.eq(Goods::getCategoryId, query.getCategoryId());
        }
        // 成色筛选
        if (query.getConditionLevel() != null) {
            wrapper.eq(Goods::getConditionLevel, query.getConditionLevel());
        }
        // 价格区间
        if (query.getMinPrice() != null) {
            wrapper.ge(Goods::getPrice, query.getMinPrice());
        }
        if (query.getMaxPrice() != null) {
            wrapper.le(Goods::getPrice, query.getMaxPrice());
        }

        // 排序
        if (query.getSortBy() != null) {
            switch (query.getSortBy()) {
                case 1 -> wrapper.orderByAsc(Goods::getPrice);
                case 2 -> wrapper.orderByDesc(Goods::getPrice);
                case 3 -> wrapper.orderByDesc(Goods::getViewCount);
                default -> wrapper.orderByDesc(Goods::getCreateTime);
            }
        } else {
            wrapper.orderByDesc(Goods::getCreateTime);
        }

        // 分页
        Page<Goods> page = new Page<>(query.getPage(), query.getSize());
        page = goodsMapper.selectPage(page, wrapper);

        List<GoodsVO> records = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageVO<>(records, page.getTotal(),
                (int) page.getCurrent(), (int) page.getSize(),
                (int) page.getPages());
    }

    @Override
    public PageVO<GoodsVO> getMyGoods(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getSellerId, userId)
               .orderByDesc(Goods::getCreateTime);

        Page<Goods> page = new Page<>(pageNum, pageSize);
        page = goodsMapper.selectPage(page, wrapper);

        List<GoodsVO> records = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageVO<>(records, page.getTotal(),
                (int) page.getCurrent(), (int) page.getSize(),
                (int) page.getPages());
    }

    @Override
    public void increaseViewCount(Long goodsId) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods != null) {
            goods.setViewCount(goods.getViewCount() + 1);
            goodsMapper.updateById(goods);
        }
    }

    /**
     * 将 Goods 实体转换为 GoodsVO，附带分类名、卖家名、图片列表
     */
    private GoodsVO convertToVO(Goods goods) {
        GoodsVO vo = new GoodsVO();
        BeanUtils.copyProperties(goods, vo);

        // 分类名称
        Category category = categoryMapper.selectById(goods.getCategoryId());
        if (category != null) vo.setCategoryName(category.getName());

        // 卖家信息
        User seller = userMapper.selectById(goods.getSellerId());
        if (seller != null) {
            vo.setSellerName(seller.getUsername());
            vo.setSellerAvatar(seller.getAvatar());
        }

        // 商品图片
        LambdaQueryWrapper<GoodsImage> imgWrapper = new LambdaQueryWrapper<>();
        imgWrapper.eq(GoodsImage::getGoodsId, goods.getId())
                  .orderByAsc(GoodsImage::getSortOrder);
        List<GoodsImage> images = goodsImageMapper.selectList(imgWrapper);
        vo.setImages(images.stream().map(GoodsImage::getImageUrl).collect(Collectors.toList()));

        return vo;
    }
}
