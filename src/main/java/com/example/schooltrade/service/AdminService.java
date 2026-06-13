package com.example.schooltrade.service;

import com.example.schooltrade.vo.DashboardVO;
import com.example.schooltrade.vo.PageVO;
import com.example.schooltrade.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 管理员服务接口
 */
public interface AdminService {

    /** 仪表盘统计数据 */
    DashboardVO dashboard();

    /** 用户管理：分页查询所有用户 */
    PageVO<UserVO> listUsers(Integer page, Integer size, String keyword);

    /** 用户管理：切换用户状态（启用/禁用） */
    void toggleUserStatus(Long userId);

    /** 商品审核：分页查询待审核商品 */
    PageVO<?> listPendingGoods(Integer page, Integer size);

    /** 商品审核：审核商品（通过/拒绝） */
    void auditGoods(Long goodsId, Integer status, String reason);

    /** 商品管理：分页查询所有商品 */
    PageVO<?> listAllGoods(Integer page, Integer size, String keyword, Integer status);

    /** 管理员强制下架商品（任意在售/待审商品） */
    void forceOffShelf(Long goodsId);

    /** 文件上传 */
    String uploadFile(MultipartFile file);
}
