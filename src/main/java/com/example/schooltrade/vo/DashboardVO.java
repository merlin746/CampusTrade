package com.example.schooltrade.vo;

/**
 * 管理员仪表盘统计
 */
public class DashboardVO {

    private Long totalUsers;        // 总用户数
    private Long totalGoods;        // 总商品数
    private Long totalOrders;       // 总订单数
    private Long pendingGoods;      // 待审核商品数
    private Long todayNewUsers;     // 今日新增用户
    private Long todayNewGoods;     // 今日新增商品
    private Long todayOrders;       // 今日订单数

    public DashboardVO() {}

    public DashboardVO(Long totalUsers, Long totalGoods, Long totalOrders, Long pendingGoods, Long todayNewUsers, Long todayNewGoods, Long todayOrders) {
        this.totalUsers = totalUsers;
        this.totalGoods = totalGoods;
        this.totalOrders = totalOrders;
        this.pendingGoods = pendingGoods;
        this.todayNewUsers = todayNewUsers;
        this.todayNewGoods = todayNewGoods;
        this.todayOrders = todayOrders;
    }

    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public Long getTotalGoods() { return totalGoods; }
    public void setTotalGoods(Long totalGoods) { this.totalGoods = totalGoods; }

    public Long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }

    public Long getPendingGoods() { return pendingGoods; }
    public void setPendingGoods(Long pendingGoods) { this.pendingGoods = pendingGoods; }

    public Long getTodayNewUsers() { return todayNewUsers; }
    public void setTodayNewUsers(Long todayNewUsers) { this.todayNewUsers = todayNewUsers; }

    public Long getTodayNewGoods() { return todayNewGoods; }
    public void setTodayNewGoods(Long todayNewGoods) { this.todayNewGoods = todayNewGoods; }

    public Long getTodayOrders() { return todayOrders; }
    public void setTodayOrders(Long todayOrders) { this.todayOrders = todayOrders; }

}