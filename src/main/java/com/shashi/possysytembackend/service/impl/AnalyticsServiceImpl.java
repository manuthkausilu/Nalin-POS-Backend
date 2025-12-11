package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.entity.Product;
import com.shashi.possysytembackend.entity.Sale;
import com.shashi.possysytembackend.repository.ProductRepository;
import com.shashi.possysytembackend.repository.SaleRepository;
import com.shashi.possysytembackend.repository.SaleItemRepository;
import com.shashi.possysytembackend.repository.CustomerRepository;
import com.shashi.possysytembackend.service.AnalyticsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SaleItemRepository saleItemRepository;

    public AnalyticsServiceImpl(SaleRepository saleRepository,
                                ProductRepository productRepository,
                                CustomerRepository customerRepository,
                                SaleItemRepository saleItemRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.saleItemRepository = saleItemRepository;
    }

    @Override
    public Response getOverview() {
        Response response = new Response();
        try {
            Map<String, Object> overview = new HashMap<>();
            overview.put("totalSales", saleRepository.count());
            overview.put("totalProducts", productRepository.count());
            overview.put("totalCustomers", customerRepository.count());
            response.setStatusCode(200);
            response.setMessage("Overview fetched successfully");
            response.setData(overview);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching overview: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getTopProducts() {
        Response response = new Response();
        try {
            List<Object[]> topProducts = saleItemRepository.findTopSellingProducts();
            List<Map<String, Object>> result = topProducts.stream()
                .map(row -> {
                    Map<String, Object> product = new HashMap<>();
                    product.put("productId", row[0]);
                    product.put("productName", row[1]);
                    product.put("totalQuantity", row[2]);
                    product.put("totalRevenue", row[3]);
                    return product;
                })
                .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Top products fetched successfully");
            response.setData(result);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching top products: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getInventoryStatus() {
        Response response = new Response();
        try {
            Map<String, Object> inventory = new HashMap<>();
            long lowStock = productRepository.countLowStock();
            long outOfStock = productRepository.countOutOfStock();
            long healthy = productRepository.countHealthyStock();
            long total = productRepository.count();
            inventory.put("lowStock", lowStock);
            inventory.put("outOfStock", outOfStock);
            inventory.put("healthy", healthy);
            inventory.put("total", total);
            response.setStatusCode(200);
            response.setMessage("Inventory status fetched successfully");
            response.setData(inventory);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching inventory status: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getCustomerStats() {
        Response response = new Response();
        try {
            // If you have a createdAt field in Customer, implement countByCreatedAtBetween
            // Otherwise, just return total customers
            long totalCustomers = customerRepository.count();
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCustomers", totalCustomers);
            response.setStatusCode(200);
            response.setMessage("Customer stats fetched successfully");
            response.setData(stats);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching customer stats: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getSalesSummaryByPeriod(String period, LocalDate startDate, LocalDate endDate) {
        Response response = new Response();
        try {
            LocalDateTime start;
            LocalDateTime end = LocalDateTime.now();
            switch(period.toLowerCase()) {
                case "daily":
                    start = end.minusDays(1);
                    break;
                case "weekly":
                    start = end.minusWeeks(1);
                    break;
                case "monthly":
                    start = end.minusMonths(1);
                    break;
                case "custom":
                    start = startDate.atStartOfDay();
                    end = endDate.atTime(23, 59, 59);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid period");
            }
            List<Sale> sales = saleRepository.findBySaleDateBetween(start, end);
            BigDecimal totalRevenue = sales.stream()
                .map(Sale::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            long totalTransactions = sales.size();
            Map<String, Object> summary = new HashMap<>();
            summary.put("period", period);
            summary.put("startDate", start);
            summary.put("endDate", end);
            summary.put("totalRevenue", totalRevenue);
            summary.put("totalTransactions", totalTransactions);
            summary.put("averageTransactionValue",
                totalTransactions > 0 ? totalRevenue.divide(BigDecimal.valueOf(totalTransactions), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
            response.setStatusCode(200);
            response.setMessage("Sales summary fetched successfully");
            response.setData(summary);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching sales summary: " + e.getMessage());
        }
        return response;
    }
}
