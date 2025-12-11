package com.shashi.possysytembackend.repository;

import com.shashi.possysytembackend.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    List<SaleItem> findBySale_SaleId(Long saleId);

    @Query(value = """
        SELECT 
            p.product_id, 
            p.product_name, 
            SUM(si.qty) as total_quantity,
            SUM(si.price * si.qty - COALESCE(si.discount, 0) * si.qty) as total_revenue
        FROM sale_item si
        JOIN product p ON si.product_id = p.product_id
        GROUP BY p.product_id, p.product_name
        ORDER BY total_revenue DESC
        LIMIT 10
    """, nativeQuery = true)
    List<Object[]> findTopSellingProducts();

    @Query(value = """
        SELECT 
            SUM((si.price - p.cost - COALESCE(si.discount, 0)) * si.qty) as totalProfit
        FROM sale_item si
        JOIN product p ON si.product_id = p.product_id
        JOIN sale s ON si.sale_id = s.sale_id
        WHERE s.sale_date BETWEEN :startDate AND :endDate
    """, nativeQuery = true)
    BigDecimal calculateTotalProfitBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}