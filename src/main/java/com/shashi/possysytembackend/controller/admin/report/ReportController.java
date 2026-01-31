package com.shashi.possysytembackend.controller.admin.report;

import com.shashi.possysytembackend.dto.SaleItemReportDTO;
import com.shashi.possysytembackend.dto.SaleReportDTO;
import com.shashi.possysytembackend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales/daily")
    public ResponseEntity<List<SaleReportDTO>> getDailySales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportService.getDailySaleReport(date));
    }

    @GetMapping("/sales/monthly")
    public ResponseEntity<List<SaleReportDTO>> getMonthlySales(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(reportService.getMonthlySaleReport(YearMonth.of(year, month)));
    }

    @GetMapping("/sales/yearly")
    public ResponseEntity<List<SaleReportDTO>> getYearlySales(@RequestParam int year) {
        return ResponseEntity.ok(reportService.getYearlySaleReport(year));
    }

    @GetMapping("/sales/range")
    public ResponseEntity<List<SaleReportDTO>> getSalesInRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(reportService.getSaleReportForRange(start, end));
    }

    @GetMapping("/sale-items/daily")
    public ResponseEntity<List<SaleItemReportDTO>> getDailySaleItems(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportService.getDailySaleItemReport(date));
    }

    @GetMapping("/sale-items/monthly")
    public ResponseEntity<List<SaleItemReportDTO>> getMonthlySaleItems(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(reportService.getMonthlySaleItemReport(YearMonth.of(year, month)));
    }

    @GetMapping("/sale-items/yearly")
    public ResponseEntity<List<SaleItemReportDTO>> getYearlySaleItems(@RequestParam int year) {
        return ResponseEntity.ok(reportService.getYearlySaleItemReport(year));
    }

    @GetMapping("/sale-items/range")
    public ResponseEntity<List<SaleItemReportDTO>> getSaleItemsInRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(reportService.getSaleItemReportForRange(start, end));
    }
}
