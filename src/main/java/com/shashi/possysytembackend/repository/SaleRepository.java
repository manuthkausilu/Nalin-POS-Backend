package com.shashi.possysytembackend.repository;

import com.shashi.possysytembackend.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);

    List<Sale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);

    @Query(value = """
            SELECT DATE(sale_date) as sale_date, 
                   COUNT(*) as total_sales,
                   SUM(total_amount) as revenue
            FROM sale
            WHERE sale_date BETWEEN :startDate AND :endDate
            GROUP BY DATE(sale_date)
            ORDER BY sale_date
        """, nativeQuery = true)
    List<Object[]> findDailySalesSummary(LocalDate startDate, LocalDate endDate);

    List<Sale> findAllByUser_UserId(Integer userUserId);
    long countBySaleDateBetween(LocalDateTime start, LocalDateTime end);
    long countByUser_UserIdAndSaleDateBetween(Integer userId, LocalDateTime start, LocalDateTime end);

    @Query(value = """
            SELECT DATE(sale_date) as date,
                   COUNT(*) as count,
                   SUM(total_amount) as revenue
            FROM sale
            WHERE MONTH(sale_date) = MONTH(CURRENT_DATE)
            AND YEAR(sale_date) = YEAR(CURRENT_DATE)
            GROUP BY DATE(sale_date)
            ORDER BY date
            """, nativeQuery = true)
    List<Object[]> findDailyRevenueForCurrentMonth();

    @Query(value = """
            SELECT MONTH(sale_date) as month,
                   COUNT(*) as count,
                   SUM(total_amount) as revenue
            FROM sale
            WHERE YEAR(sale_date) = YEAR(CURRENT_DATE)
            GROUP BY MONTH(sale_date)
            ORDER BY month
            """, nativeQuery = true)
    List<Object[]> findMonthlyRevenueForCurrentYear();

    @Query(value = """
            SELECT SUM(total_amount) as todayRevenue,
                   COUNT(*) as todayTransactions
            FROM sale
            WHERE DATE(sale_date) = CURRENT_DATE
            """, nativeQuery = true)
    Object[] findTodayRevenueAndTransactions();

    @Query(value = """
            SELECT SUM(total_amount) as monthRevenue,
                   COUNT(*) as monthTransactions
            FROM sale
            WHERE MONTH(sale_date) = MONTH(CURRENT_DATE)
            AND YEAR(sale_date) = YEAR(CURRENT_DATE)
            """, nativeQuery = true)
    Object[] findCurrentMonthRevenueAndTransactions();

    @Query(value = """
            SELECT s FROM Sale s
            WHERE MONTH(s.saleDate) = MONTH(CURRENT_DATE)
            AND YEAR(s.saleDate) = YEAR(CURRENT_DATE)
            """)
    List<Sale> findAllSalesForCurrentMonth();

    @Query(value = """
            SELECT s FROM Sale s
            WHERE YEAR(s.saleDate) = YEAR(CURRENT_DATE)
            """)
    List<Sale> findAllSalesForCurrentYear();
}