package com.shashi.possysytembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockOverviewDTO {
    private long totalProductsQty;
    private double totalPrice;

}
