package com.shashi.possysytembackend.controller;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.DashboardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
public class UserDashboardController {
    private final DashboardService dashboardService;

    public UserDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Response> getUserDashboardStats(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        Response response = dashboardService.getUserDashboardStats(Long.valueOf(userId));
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
