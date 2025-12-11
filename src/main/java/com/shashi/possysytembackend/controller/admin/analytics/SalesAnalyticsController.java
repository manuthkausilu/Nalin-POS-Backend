package com.shashi.possysytembackend.controller.admin.analytics;

import com.shashi.possysytembackend.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestController("adminSalesAnalyticsController")
@RequestMapping("/api/admin/analytics/sales")
public class SalesAnalyticsController {
    private final AnalyticsService analyticsService;
    public SalesAnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/daily")
    public ResponseEntity<?> getDailySalesSummary() {
        return ResponseEntity.ok(analyticsService.getSalesSummaryByPeriod("daily", null, null));
    }

    @GetMapping("/weekly")
    public ResponseEntity<?> getWeeklySalesSummary() {
        return ResponseEntity.ok(analyticsService.getSalesSummaryByPeriod("weekly", null, null));
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlySalesSummary() {
        return ResponseEntity.ok(analyticsService.getSalesSummaryByPeriod("monthly", null, null));
    }

    @GetMapping("/custom")
    public ResponseEntity<?> getCustomSalesSummary(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    ) {
        LocalDate start = parseToLocalDate(startDate);
        LocalDate end = parseToLocalDate(endDate);
        return ResponseEntity.ok(analyticsService.getSalesSummaryByPeriod("custom", start, end));
    }

    // Helper method to parse both date and date-time strings to LocalDate
    private LocalDate parseToLocalDate(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(value).toLocalDate();
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format: " + value);
            }
        }
    }
}
