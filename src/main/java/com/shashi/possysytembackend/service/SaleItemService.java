package com.shashi.possysytembackend.service;

import com.shashi.possysytembackend.dto.SaleItemDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.dto.SaleItemViewDTO;
import java.util.List;

public interface SaleItemService {
    Response createSaleItem(SaleItemDTO saleItemDto);
    Response getSaleItemById(Long id);
    Response getAllSaleItems();
    Response updateSaleItem(Long id, SaleItemDTO saleItemDto);
    Response deleteSaleItem(Long id);
    Response getSaleItemsBySaleId(Long saleId);
    Response getSaleItemViewsBySaleId(Long saleId);
}
