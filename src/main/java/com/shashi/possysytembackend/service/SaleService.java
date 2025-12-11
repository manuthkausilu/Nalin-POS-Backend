package com.shashi.possysytembackend.service;

import com.shashi.possysytembackend.dto.SaleDTO;
import com.shashi.possysytembackend.dto.Response;
import java.util.List;

public interface SaleService {
    Response createSale(SaleDTO saleDto);
    Response getSaleById(Long id);
    Response getAllSales();
    Response updateSale(Long id, SaleDTO saleDto);
    Response deleteSale(Long id);
    Response getSalesByUserId(Long userId);
    Response getSalesByDateRange(String rangeType, String startDate, String endDate);
}
