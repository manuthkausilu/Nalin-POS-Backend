package com.shashi.possysytembackend.service;

import com.shashi.possysytembackend.dto.Response;

public interface DashboardService {
    Response getAdminDashboardStats();
    Response getMonthlyDailySalesChart();
    Response getYearlyMonthlySalesChart();
    Response getUserDashboardStats(Long userId);
}
