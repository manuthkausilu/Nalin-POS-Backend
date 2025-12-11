package com.shashi.possysytembackend.controller.admin.analytics;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/analytics")
@CrossOrigin
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/overview")
    public ResponseEntity<Response> getOverview() {
        Response response = analyticsService.getOverview();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/top-products")
    public ResponseEntity<Response> getTopProducts() {
        Response response = analyticsService.getTopProducts();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/inventory-status")
    public ResponseEntity<Response> getInventoryStatus() {
        Response response = analyticsService.getInventoryStatus();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/customer-stats")
    public ResponseEntity<Response> getCustomerStats() {
        Response response = analyticsService.getCustomerStats();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

