package com.shashi.possysytembackend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter @Setter
public class SaleItemViewDTO {
    private Long saleItemId;
    private Long saleId;
    private String productName;
    private String barcode;
    private Integer qty;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private BigDecimal discount;


}