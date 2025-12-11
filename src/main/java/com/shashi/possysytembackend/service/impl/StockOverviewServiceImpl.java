package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.dto.StockOverviewDTO;
import com.shashi.possysytembackend.exception.OurException;
import com.shashi.possysytembackend.repository.ProductRepository;
import com.shashi.possysytembackend.service.StockOverviewService;
import org.springframework.stereotype.Service;

@Service
public class StockOverviewServiceImpl implements StockOverviewService {

    private final ProductRepository productRepository;

    public StockOverviewServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Response getStockOverview() {
        Response response = new Response();

        try {
            StockOverviewDTO stockOverviewDTO = new StockOverviewDTO();

            Double totalPriceNullable = productRepository.getTotalPriceOfAllProducts();
            Long totalQtyNullable = productRepository.getTotalQuantityOfAllProducts();

            double totalPrice = (totalPriceNullable == null) ? 0.0 : totalPriceNullable;
            long totalProductsQty = (totalQtyNullable == null) ? 0 : totalQtyNullable;

            stockOverviewDTO.setTotalPrice(totalPrice);
            stockOverviewDTO.setTotalProductsQty(totalProductsQty);
            response.setStockOverviewDTO(stockOverviewDTO);
            response.setStatusCode(200);
            response.setMessage("Stock overview fetched successfully");
        } catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Something went wrong"+ e.getMessage());
        }
        return response;
    }
}
