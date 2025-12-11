package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.repository.ProductRepository;
import com.shashi.possysytembackend.repository.SaleRepository;
import com.shashi.possysytembackend.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    public DashboardServiceImpl(SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Response getAdminDashboardStats() {
        Response response = new Response();
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0);

            Map<String, Object> stats = new HashMap<>();
            
            // Get total sales count for current month
            long monthSalesCount = saleRepository.countBySaleDateBetween(startOfMonth, now);
            
            // Get today's sales count
            long todaySalesCount = saleRepository.countBySaleDateBetween(startOfDay, now);
            
            // Get product counts
            long totalProducts = productRepository.count();
            long activeProducts = productRepository.countProductsByIsActiveTrue();

            // Get revenue data
            Object[] todayRevenue = saleRepository.findTodayRevenueAndTransactions();
            Object[] monthRevenue = saleRepository.findCurrentMonthRevenueAndTransactions();

            stats.put("monthlySalesCount", monthSalesCount);
            stats.put("todaySalesCount", todaySalesCount);
            stats.put("totalProducts", totalProducts);
            stats.put("activeProducts", activeProducts);
            stats.put("todayRevenue", todayRevenue[0] != null ? todayRevenue[0] : BigDecimal.ZERO);
            stats.put("monthlyRevenue", monthRevenue[0] != null ? monthRevenue[0] : BigDecimal.ZERO);

            response.setStatusCode(200);
            response.setMessage("Dashboard stats fetched successfully");
            response.setData(stats);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching dashboard stats: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMonthlyDailySalesChart() {
        Response response = new Response();
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            
            List<Map<String, Object>> dailySales = new ArrayList<>();
            LocalDateTime currentDate = startOfMonth;
            
            while (!currentDate.isAfter(now)) {
                LocalDateTime nextDay = currentDate.plusDays(1);
                long salesCount = saleRepository.countBySaleDateBetween(currentDate, nextDay);
                
                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", currentDate.toLocalDate());
                dayData.put("salesCount", salesCount);
                dailySales.add(dayData);
                
                currentDate = nextDay;
            }

            response.setStatusCode(200);
            response.setMessage("Daily sales chart data fetched successfully");
            response.setData(dailySales);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching daily sales chart: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getYearlyMonthlySalesChart() {
        Response response = new Response();
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfYear = now.withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            
            List<Map<String, Object>> monthlySales = new ArrayList<>();
            LocalDateTime currentMonth = startOfYear;
            
            while (!currentMonth.isAfter(now)) {
                LocalDateTime nextMonth = currentMonth.plusMonths(1);
                long salesCount = saleRepository.countBySaleDateBetween(currentMonth, nextMonth);
                
                Map<String, Object> monthData = new HashMap<>();
                monthData.put("month", currentMonth.getMonth().toString());
                monthData.put("salesCount", salesCount);
                monthlySales.add(monthData);
                
                currentMonth = nextMonth;
            }

            response.setStatusCode(200);
            response.setMessage("Monthly sales chart data fetched successfully");
            response.setData(monthlySales);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching monthly sales chart: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserDashboardStats(Long userId) {
        Response response = new Response();
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0);

            Map<String, Object> stats = new HashMap<>();
            
            // Get user's total sales count for current month
            long monthSalesCount = saleRepository.countByUser_UserIdAndSaleDateBetween(
                Math.toIntExact(userId), startOfMonth, now);
            
            // Get user's today's sales count
            long todaySalesCount = saleRepository.countByUser_UserIdAndSaleDateBetween(
                Math.toIntExact(userId), startOfDay, now);

            stats.put("monthlySalesCount", monthSalesCount);
            stats.put("todaySalesCount", todaySalesCount);

            response.setStatusCode(200);
            response.setMessage("User dashboard stats fetched successfully");
            response.setData(stats);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching user dashboard stats: " + e.getMessage());
        }
        return response;
    }
}
