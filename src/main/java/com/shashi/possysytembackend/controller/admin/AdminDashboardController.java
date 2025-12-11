package com.shashi.possysytembackend.controller.admin;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin
public class AdminDashboardController {
    private final DashboardService dashboardService;

    public AdminDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Response> getDashboardStats() {
        Response response = dashboardService.getAdminDashboardStats();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/daily-sales-chart")
    public ResponseEntity<Response> getDailySalesChart() {
        Response response = dashboardService.getMonthlyDailySalesChart();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/monthly-sales-chart")
    public ResponseEntity<Response> getMonthlySalesChart() {
        Response response = dashboardService.getYearlyMonthlySalesChart();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
