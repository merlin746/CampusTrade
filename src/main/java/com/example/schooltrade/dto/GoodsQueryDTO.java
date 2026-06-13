package com.example.schooltrade.dto;

import java.math.BigDecimal;

/**
 * 商品搜索/筛选请求
 */
public class GoodsQueryDTO {

    private String keyword;         // 搜索关键词
    private Long categoryId;        // 分类ID
    private Integer conditionLevel; // 成色
    private BigDecimal minPrice;    // 最低价
    private BigDecimal maxPrice;    // 最高价
    private Integer sortBy;         // 排序: 0=最新, 1=价格升序, 2=价格降序, 3=最热

    private Integer page = 1;       // 页码
    private Integer size = 12;      // 每页大小

    public GoodsQueryDTO() {}

    public GoodsQueryDTO(String keyword, Long categoryId, Integer conditionLevel, BigDecimal minPrice, BigDecimal maxPrice, Integer sortBy, Integer page, Integer size) {
        this.keyword = keyword;
        this.categoryId = categoryId;
        this.conditionLevel = conditionLevel;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.sortBy = sortBy;
        this.page = page;
        this.size = size;
    }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Integer getConditionLevel() { return conditionLevel; }
    public void setConditionLevel(Integer conditionLevel) { this.conditionLevel = conditionLevel; }

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public Integer getSortBy() { return sortBy; }
    public void setSortBy(Integer sortBy) { this.sortBy = sortBy; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

}