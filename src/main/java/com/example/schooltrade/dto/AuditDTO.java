package com.example.schooltrade.dto;

/**
 * 管理员审核商品请求
 */
public class AuditDTO {

    /** 审核结果: 1=通过(上架), 4=不通过 */
    private Integer status;

    /** 审核不通过时的原因 */
    private String reason;

    public AuditDTO() {}

    public AuditDTO(Integer status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

}