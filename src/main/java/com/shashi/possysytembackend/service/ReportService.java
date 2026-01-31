package com.shashi.possysytembackend.service;

import com.shashi.possysytembackend.dto.SaleItemReportDTO;
import com.shashi.possysytembackend.dto.SaleReportDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public interface ReportService {
    List<SaleReportDTO> getDailySaleReport(LocalDate date);
    List<SaleReportDTO> getMonthlySaleReport(YearMonth month);
    List<SaleReportDTO> getYearlySaleReport(int year);
    List<SaleReportDTO> getSaleReportForRange(LocalDateTime start, LocalDateTime end);

    List<SaleItemReportDTO> getDailySaleItemReport(LocalDate date);
    List<SaleItemReportDTO> getMonthlySaleItemReport(YearMonth month);
    List<SaleItemReportDTO> getYearlySaleItemReport(int year);
    List<SaleItemReportDTO> getSaleItemReportForRange(LocalDateTime start, LocalDateTime end);
}
