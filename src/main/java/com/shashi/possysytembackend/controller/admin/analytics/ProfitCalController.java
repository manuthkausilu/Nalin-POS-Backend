package com.shashi.possysytembackend.controller.admin.analytics;

import com.shashi.possysytembackend.service.ProfitCalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/admin/analytics/profit")
public class ProfitCalController {
    private final ProfitCalService profitCalService;

    public ProfitCalController(ProfitCalService profitCalService) {
        this.profitCalService = profitCalService;
    }

    @GetMapping("/daily")
    public ResponseEntity<?> getDailyProfitSummary() {
        return ResponseEntity.ok(profitCalService.getNetProfitByPeriod("daily", null, null));
    }

    @GetMapping("/weekly")
    public ResponseEntity<?> getWeeklyProfitSummary() {
        return ResponseEntity.ok(profitCalService.getNetProfitByPeriod("weekly", null, null));
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyProfitSummary() {
        return ResponseEntity.ok(profitCalService.getNetProfitByPeriod("monthly", null, null));
    }

    @GetMapping("/custom")
    public ResponseEntity<?> getCustomProfitSummary(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    ) {
        LocalDate start = parseToLocalDate(startDate);
        LocalDate end = parseToLocalDate(endDate);
        return ResponseEntity.ok(profitCalService.getNetProfitByPeriod("custom", start, end));
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
