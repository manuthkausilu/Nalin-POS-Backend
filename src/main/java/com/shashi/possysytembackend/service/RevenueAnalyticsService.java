package com.shashi.possysytembackend.service;

import com.shashi.possysytembackend.dto.Response;

public interface RevenueAnalyticsService {
    Response getTodayRevenueAndProfit();
    Response getCurrentMonthDailyStats();
    Response getCurrentYearMonthlyStats();
}
