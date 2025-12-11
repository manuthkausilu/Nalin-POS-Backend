package com.shashi.possysytembackend.controller.admin.analytics;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.RevenueAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/analytics/revenue")
@CrossOrigin
public class RevenueAnalyticsController {
    private final RevenueAnalyticsService revenueAnalyticsService;

    public RevenueAnalyticsController(RevenueAnalyticsService revenueAnalyticsService) {
        this.revenueAnalyticsService = revenueAnalyticsService;
    }

    @GetMapping("/today")
    public ResponseEntity<Response> getTodayRevenue() {
        Response response = revenueAnalyticsService.getTodayRevenueAndProfit();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/month/daily")
    public ResponseEntity<Response> getCurrentMonthDailyStats() {
        Response response = revenueAnalyticsService.getCurrentMonthDailyStats();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/year/monthly")
    public ResponseEntity<Response> getCurrentYearMonthlyStats() {
        Response response = revenueAnalyticsService.getCurrentYearMonthlyStats();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
