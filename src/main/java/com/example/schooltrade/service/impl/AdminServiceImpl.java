package com.example.schooltrade.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.schooltrade.entity.*;
import com.example.schooltrade.exception.BusinessException;
import com.example.schooltrade.mapper.*;
import com.example.schooltrade.service.AdminService;
import com.example.schooltrade.service.GoodsService;
import com.example.schooltrade.service.NoticeService;
import com.example.schooltrade.vo.DashboardVO;
import com.example.schooltrade.vo.GoodsVO;
import com.example.schooltrade.vo.PageVO;
import com.example.schooltrade.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员服务实现
 * 仪表盘统计 / 用户管理 / 商品审核
 */
@Service
public class AdminServiceImpl implements AdminService {

    private final UserMapper userMapper;
    private final GoodsMapper goodsMapper;
    private final OrderMapper orderMapper;
    private final GoodsService goodsService;
    private final NoticeService noticeService;

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${file.allowed-extensions}")
    private String allowedExtensions;

    public AdminServiceImpl(UserMapper userMapper, GoodsMapper goodsMapper,
                            OrderMapper orderMapper, GoodsService goodsService,
                            NoticeService noticeService) {
        this.userMapper = userMapper;
        this.goodsMapper = goodsMapper;
        this.orderMapper = orderMapper;
        this.goodsService = goodsService;
        this.noticeService = noticeService;
    }

    @Override
    public DashboardVO dashboard() {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        DashboardVO vo = new DashboardVO();
        vo.setTotalUsers(userMapper.selectCount(null));
        vo.setTotalGoods(goodsMapper.selectCount(null));
        vo.setTotalOrders(orderMapper.selectCount(null));
        vo.setPendingGoods(goodsMapper.selectCount(
                new LambdaQueryWrapper<Goods>().eq(Goods::getStatus, 0)));
        vo.setTodayNewUsers(userMapper.selectCount(
                new LambdaQueryWrapper<User>().between(User::getCreateTime, todayStart, todayEnd)));
        vo.setTodayNewGoods(goodsMapper.selectCount(
                new LambdaQueryWrapper<Goods>().between(Goods::getCreateTime, todayStart, todayEnd)));
        vo.setTodayOrders(orderMapper.selectCount(
                new LambdaQueryWrapper<Order>().between(Order::getCreateTime, todayStart, todayEnd)));
        return vo;
    }

    @Override
    public PageVO<UserVO> listUsers(Integer pageNum, Integer pageSize, String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(User::getUsername, keyword)
                   .or().like(User::getEmail, keyword);
        }
        wrapper.orderByDesc(User::getCreateTime);

        Page<User> page = new Page<>(pageNum, pageSize);
        page = userMapper.selectPage(page, wrapper);

        List<UserVO> records = page.getRecords().stream().map(u -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(u, vo);
            return vo;
        }).collect(Collectors.toList());

        return new PageVO<>(records, page.getTotal(),
                (int) page.getCurrent(), (int) page.getSize(), (int) page.getPages());
    }

    @Override
    public void toggleUserStatus(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(404, "用户不存在");
        if (user.getRole() == 1) throw new BusinessException(400, "不能禁用管理员");
        user.setStatus(user.getStatus() == 1 ? 0 : 1);
        userMapper.updateById(user);
    }

    @Override
    public PageVO<?> listPendingGoods(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getStatus, 0).orderByAsc(Goods::getCreateTime);

        Page<Goods> page = new Page<>(pageNum, pageSize);
        page = goodsMapper.selectPage(page, wrapper);

        List<GoodsVO> records = page.getRecords().stream()
                .map(g -> goodsService.getDetail(g.getId(), null))
                .collect(Collectors.toList());

        return new PageVO<>(records, page.getTotal(),
                (int) page.getCurrent(), (int) page.getSize(), (int) page.getPages());
    }

    @Override
    @Transactional
    public void auditGoods(Long goodsId, Integer status, String reason) {
        // status: 1=通过, 4=不通过
        if (status != 1 && status != 4) {
            throw new BusinessException(400, "审核结果只能是 1(通过) 或 4(不通过)");
        }
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) throw new BusinessException(404, "商品不存在");
        if (goods.getStatus() != 0) throw new BusinessException(400, "商品非待审核状态");

        goods.setStatus(status);
        goodsMapper.updateById(goods);

        // 通知卖家审核结果
        String title = status == 1 ? "商品审核通过" : "商品审核不通过";
        String content = status == 1
                ? "您的商品《" + goods.getTitle() + "》已通过审核，已上架"
                : "您的商品《" + goods.getTitle() + "》审核不通过，原因: " + reason;

        noticeService.send(goods.getSellerId(), 3, title, content, goodsId);
    }

    @Override
    public PageVO<?> listAllGoods(Integer pageNum, Integer pageSize, String keyword, Integer status) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(Goods::getTitle, keyword);
        }
        if (status != null) {
            wrapper.eq(Goods::getStatus, status);
        }
        wrapper.orderByDesc(Goods::getCreateTime);

        Page<Goods> page = new Page<>(pageNum, pageSize);
        page = goodsMapper.selectPage(page, wrapper);

        List<GoodsVO> records = page.getRecords().stream()
                .map(g -> goodsService.getDetail(g.getId(), null))
                .collect(Collectors.toList());

        return new PageVO<>(records, page.getTotal(),
                (int) page.getCurrent(), (int) page.getSize(), (int) page.getPages());
    }

    @Override
    public void forceOffShelf(Long goodsId) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) throw new BusinessException(404, "商品不存在");
        // 允许下架【在售(1)】和【待审核(0)】状态的商品
        if (goods.getStatus() != 1 && goods.getStatus() != 0) {
            throw new BusinessException(400, "该商品状态下不允许下架操作");
        }
        goods.setStatus(3); // 已下架
        goodsMapper.updateById(goods);

        // 通知卖家
        noticeService.send(goods.getSellerId(), 3, "商品已被管理员下架",
                "您的商品《" + goods.getTitle() + "》已被管理员下架，如有疑问请联系管理员", goodsId);
    }

    @Override
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) throw new BusinessException(400, "文件为空");

        // 校验文件扩展名
        String ext = FileUtil.extName(file.getOriginalFilename());
        List<String> allowed = Arrays.asList(allowedExtensions.split(","));
        if (!allowed.contains(ext.toLowerCase())) {
            throw new BusinessException(400, "不支持的文件类型: " + ext);
        }

        // 生成唯一文件名并保存
        String fileName = IdUtil.fastSimpleUUID() + "." + ext;
        File destDir = new File(uploadPath);
        if (!destDir.exists()) destDir.mkdirs();

        try {
            File dest = new File(destDir, fileName);
            file.transferTo(dest);
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new BusinessException(500, "文件上传失败: " + e.getMessage());
        }
    }
}
