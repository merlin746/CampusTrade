package com.example.schooltrade.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品发布/编辑请求
 */
public class GoodsDTO {

    @NotBlank(message = "商品标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    private BigDecimal originalPrice;

    @NotNull(message = "请选择分类")
    private Long categoryId;

    /** 成色: 1=全新, 2=几乎全新, 3=有使用痕迹, 4=明显瑕疵 */
    @NotNull(message = "请选择成色")
    private Integer conditionLevel;

    private String address;
    private Integer contactType;
    private String contactInfo;

    /** 图片URL列表（上传后返回的地址） */
    private List<String> images;

    public GoodsDTO() {}

    public GoodsDTO(String title, String description, BigDecimal price, BigDecimal originalPrice, Long categoryId, Integer conditionLevel, String address, Integer contactType, String contactInfo, List<String> images) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.originalPrice = originalPrice;
        this.categoryId = categoryId;
        this.conditionLevel = conditionLevel;
        this.address = address;
        this.contactType = contactType;
        this.contactInfo = contactInfo;
        this.images = images;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Integer getConditionLevel() { return conditionLevel; }
    public void setConditionLevel(Integer conditionLevel) { this.conditionLevel = conditionLevel; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getContactType() { return contactType; }
    public void setContactType(Integer contactType) { this.contactType = contactType; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

}