package com.shashi.possysytembackend.service;

import com.shashi.possysytembackend.dto.Response;
import java.time.LocalDate;

public interface AnalyticsService {
    // Returns total sales, products, customers, etc.
    Response getOverview();

    // Returns top selling products (by quantity/revenue)
    Response getTopProducts();

    // Returns inventory status (low stock, out of stock, healthy)
    Response getInventoryStatus();

    // Returns customer statistics (new, returning, etc.)
    Response getCustomerStats();

    // Returns sales summary for a given period (daily, weekly, monthly, custom)
    Response getSalesSummaryByPeriod(String period, LocalDate startDate, LocalDate endDate);
}
