package com.shashi.possysytembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor
@Data
public class SaleItemDTO {
    private Long saleItemId;
    private Long saleId;
    private Long productId;
    private Integer qty;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private BigDecimal discount;

}

