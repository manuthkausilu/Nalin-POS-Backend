package com.shashi.possysytembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaleItemReportDTO {

    private Long productId;
    private String productName;
    private Integer qty;
    private BigDecimal salePrice;
    private BigDecimal totalPrice;
    private BigDecimal discount;
    private BigDecimal totalAmount;
}
