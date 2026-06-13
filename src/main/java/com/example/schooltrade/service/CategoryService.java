package com.example.schooltrade.service;

import com.example.schooltrade.entity.Category;
import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {

    /** 获取所有分类（按排序字段升序） */
    List<Category> listAll();

    /** 管理员创建分类 */
    void create(Category category);

    /** 管理员更新分类 */
    void update(Category category);

    /** 管理员删除分类 */
    void delete(Long id);
}
