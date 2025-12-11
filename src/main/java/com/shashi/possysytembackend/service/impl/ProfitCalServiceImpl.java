package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.entity.Sale;
import com.shashi.possysytembackend.repository.SaleRepository;
import com.shashi.possysytembackend.service.ProfitCalService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProfitCalServiceImpl implements ProfitCalService {

    private final SaleRepository saleRepository;

    public ProfitCalServiceImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public Map<String, BigDecimal> getNetProfit( LocalDateTime startDate, LocalDateTime endDate) {
        return calculateProfitDetails(startDate,endDate);
    }
        @Override
    public Response getNetProfitByPeriod(String period, LocalDate startDate, LocalDate endDate) {
        Response response = new Response();
        try {
            LocalDateTime start;
            LocalDateTime end;

            switch (period.toLowerCase()) {
                case "daily":
                    start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
                    end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
                    break;
                case "weekly":
                    end = LocalDateTime.now();
                    start = end.minusWeeks(1);
                    break;
                case "monthly":
                    end = LocalDateTime.now();
                    start = end.minusMonths(1);
                    break;
                case "custom":
                    start = startDate.atStartOfDay();
                    end = endDate.atTime(23, 59, 59);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid period: " + period);
            }

            // Calculate profit details once
            Map<String, BigDecimal> profitDetails = calculateProfitDetails(start, end);
            long totalSales = saleRepository.countBySaleDateBetween(start, end);

            Map<String, Object> result = new HashMap<>();
            result.put("period", period);
            result.put("startDate", start);
            result.put("endDate", end);
            result.put("netProfit", profitDetails.get("netProfit"));
            result.put("totalRevenue", profitDetails.get("totalRevenue"));
            result.put("totalCost", profitDetails.get("totalCost"));
            result.put("totalSales", totalSales);

            // Calculate profit margin
            result.put("profitMargin", profitDetails.get("totalRevenue").compareTo(BigDecimal.ZERO) > 0
                    ? profitDetails.get("netProfit")
                    .divide(profitDetails.get("totalRevenue"), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    : BigDecimal.ZERO);

            response.setStatusCode(200);
            response.setMessage("Net profit calculated successfully");
            response.setData(result);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error calculating net profit: " + e.getMessage());
        }
        return response;
    }

    /**
     * From `main` branch – calculates total revenue, cost, and net profit.
     */
    private Map<String, BigDecimal> calculateProfitDetails(LocalDateTime start, LocalDateTime end) {
        List<Sale> sales = saleRepository.findBySaleDateBetween(start, end);
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        for (Sale sale : sales) {
            if (sale.getTotalAmount() != null) {
                totalRevenue = totalRevenue.add(sale.getTotalAmount());
            }

            BigDecimal saleCost = BigDecimal.ZERO;

            if (sale.getSaleItems() != null) {
                for (var item : sale.getSaleItems()) {
                    BigDecimal cost = item.getProduct() != null && item.getProduct().getCost() != null
                            ? item.getProduct().getCost()
                            : BigDecimal.ZERO;
                    int qty = item.getQty() != null ? item.getQty() : 0;

                    BigDecimal itemCost = cost.multiply(BigDecimal.valueOf(qty));
                    saleCost = saleCost.add(itemCost);
                }
            }

            totalCost = totalCost.add(saleCost);
        }

        BigDecimal netProfit = totalRevenue.subtract(totalCost);

        Map<String, BigDecimal> result = new HashMap<>();
        result.put("netProfit", netProfit);
        result.put("totalRevenue", totalRevenue);
        result.put("totalCost", totalCost);
        return result;
    }

    /**
     * Utility method: revenue from totalAmount field.
     */
    private BigDecimal calculateTotalRevenue(LocalDateTime start, LocalDateTime end) {
        List<Sale> sales = saleRepository.findBySaleDateBetween(start, end);
        return sales.stream()
                .map(Sale::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}