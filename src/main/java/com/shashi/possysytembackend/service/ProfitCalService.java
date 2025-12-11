package com.shashi.possysytembackend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import com.shashi.possysytembackend.dto.Response;

public interface ProfitCalService {
    Response getNetProfitByPeriod(String period, LocalDate startDate, LocalDate endDate);
    Map<String, BigDecimal> getNetProfit(LocalDateTime startDate, LocalDateTime endDate);

}
