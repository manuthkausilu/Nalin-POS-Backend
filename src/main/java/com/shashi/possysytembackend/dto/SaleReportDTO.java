package com.shashi.possysytembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleReportDTO {

    private Long saleId;
    private LocalDateTime saleDate;
    private Integer itemCount;
    private BigDecimal originalTotal;
    private BigDecimal itemDiscounts;
    private BigDecimal orderDiscount;
    private BigDecimal totalDiscount;
    private BigDecimal totalAmount;
}
