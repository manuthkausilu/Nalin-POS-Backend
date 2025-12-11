package com.shashi.possysytembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaleDTO {
    private Long saleId;
    private LocalDateTime saleDate;
    private BigDecimal totalAmount;
    private BigDecimal totalDiscount;
    private String paymentMethod;
    private Integer userId;
    private Long customerId;
    private List<SaleItemDTO> saleItems;
    private BigDecimal originalTotal;
    private BigDecimal itemDiscounts;
    private BigDecimal subtotal;
    private BigDecimal orderDiscountPercentage;
    private BigDecimal orderDiscount;
    private BigDecimal paymentAmount;
    private BigDecimal balance;
}
