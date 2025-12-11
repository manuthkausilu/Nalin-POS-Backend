package com.shashi.possysytembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {
    private Long productId;
    private String barcode;
    private String productName;
    private Long categoryId;
    private Long brandId;
    private BigDecimal cost;
    private BigDecimal salePrice;
    private Integer qty;
    private Boolean isActive;
    private Boolean trackInventory;

}

