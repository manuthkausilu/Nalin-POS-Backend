package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.entity.Sale;
import com.shashi.possysytembackend.repository.SaleRepository;
import com.shashi.possysytembackend.service.ProfitCalService;
import com.shashi.possysytembackend.service.RevenueAnalyticsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class RevenueAnalyticsServiceImpl implements RevenueAnalyticsService {
    private final SaleRepository saleRepository;
    private final ProfitCalService profitCalService;

    @Override
    public Response getTodayRevenueAndProfit() {
        Response response = new Response();
        try {
            // Get today's stats using ProfitCalService
            Response profitResponse = profitCalService.getNetProfitByPeriod("daily", null, null);
            if (profitResponse.getStatusCode() != 200) {
                return profitResponse;
            }

            Map<String, Object> profitData = (Map<String, Object>) profitResponse.getData();

            Map<String, Object> result = new HashMap<>();
            result.put("revenue", profitData.get("totalRevenue"));
            result.put("profit", profitData.get("netProfit"));
            result.put("transactions", profitData.get("totalSales"));

            response.setStatusCode(200);
            response.setMessage("Today's revenue and profit fetched successfully");
            response.setData(result);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching revenue and profit: " + e.getMessage());
        }
        return response;
    }



    @Override
    public Response getCurrentMonthDailyStats() {
        Response response = new Response();
        try {
            List<Sale> sales = saleRepository.findAllSalesForCurrentMonth();
            Map<LocalDate, List<Sale>> salesByDay = new HashMap<>();
            for (Sale sale : sales) {
                LocalDate date = sale.getSaleDate().toLocalDate();
                salesByDay.computeIfAbsent(date, k -> new ArrayList<>()).add(sale);
            }

            List<Map<String, Object>> dailyData = new ArrayList<>();
            for (LocalDate date : salesByDay.keySet()) {
                List<Sale> daySales = salesByDay.get(date);
                BigDecimal totalRevenue = BigDecimal.ZERO;
                BigDecimal totalCost = BigDecimal.ZERO;
                long totalTransactions = daySales.size();

                for (Sale sale : daySales) {
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
                            saleCost = saleCost.add(cost.multiply(BigDecimal.valueOf(qty)));
                        }
                    }
                    totalCost = totalCost.add(saleCost);
                }
                BigDecimal netProfit = totalRevenue.subtract(totalCost);

                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", date);
                dayData.put("revenue", totalRevenue);
                dayData.put("profit", netProfit);
                dayData.put("transactions", totalTransactions);
                dailyData.add(dayData);
            }

            response.setStatusCode(200);
            response.setMessage("Monthly daily stats fetched successfully");
            response.setData(Collections.singletonMap("dailyStats", dailyData));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching monthly stats: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getCurrentYearMonthlyStats() {
        Response response = new Response();
        try {
            List<Sale> sales = saleRepository.findAllSalesForCurrentYear();
            Map<Integer, List<Sale>> salesByMonth = new HashMap<>();
            for (Sale sale : sales) {
                int month = sale.getSaleDate().getMonthValue();
                salesByMonth.computeIfAbsent(month, k -> new ArrayList<>()).add(sale);
            }

            List<Map<String, Object>> monthlyData = new ArrayList<>();
            for (int month = 1; month <= 12; month++) {
                List<Sale> monthSales = salesByMonth.getOrDefault(month, Collections.emptyList());
                BigDecimal totalRevenue = BigDecimal.ZERO;
                BigDecimal totalCost = BigDecimal.ZERO;
                long totalTransactions = monthSales.size();

                for (Sale sale : monthSales) {
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
                            saleCost = saleCost.add(cost.multiply(BigDecimal.valueOf(qty)));
                        }
                    }
                    totalCost = totalCost.add(saleCost);
                }
                BigDecimal netProfit = totalRevenue.subtract(totalCost);

                Map<String, Object> monthData = new HashMap<>();
                monthData.put("month", month);
                monthData.put("revenue", totalRevenue);
                monthData.put("profit", netProfit);
                monthData.put("transactions", totalTransactions);
                monthlyData.add(monthData);
            }

            response.setStatusCode(200);
            response.setMessage("Yearly monthly stats fetched successfully");
            response.setData(Collections.singletonMap("monthlyStats", monthlyData));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching yearly stats: " + e.getMessage());
        }
        return response;
    }

    private Map<String, Object> convertToHourlyStatsMap(List<Object[]> stats) {
        List<Map<String, Object>> hourlyData = new ArrayList<>();
        for (Object[] row : stats) {
            Map<String, Object> hourData = new HashMap<>();
            hourData.put("hour", row[0]);
            hourData.put("count", row[1]);
            hourData.put("revenue", row[2]);
            hourlyData.add(hourData);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("hourlyStats", hourlyData);
        return result;
    }

    private Map<String, Object> convertToDailyStatsMap(List<Object[]> stats) {
        List<Map<String, Object>> dailyData = new ArrayList<>();
        for (Object[] row : stats) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", row[0]);
            dayData.put("count", row[1]);
            dayData.put("revenue", row[2]);
            dailyData.add(dayData);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("dailyStats", dailyData);
        return result;
    }

    private Map<String, Object> convertToMonthlyStatsMap(List<Object[]> stats) {
        List<Map<String, Object>> monthlyData = new ArrayList<>();
        for (Object[] row : stats) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", row[0]);
            monthData.put("count", row[1]);
            monthData.put("revenue", row[2]);
            monthlyData.add(monthData);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("monthlyStats", monthlyData);
        return result;
    }
}
