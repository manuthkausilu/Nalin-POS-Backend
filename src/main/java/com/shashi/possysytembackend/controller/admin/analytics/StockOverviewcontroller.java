package com.shashi.possysytembackend.controller.admin.analytics;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.StockOverviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/analytics/stock-overview")
public class StockOverviewcontroller {

    private final StockOverviewService stockOverviewservice;

    public StockOverviewcontroller(StockOverviewService stockOverviewservice) {
        this.stockOverviewservice = stockOverviewservice;
    }

    @GetMapping
    public ResponseEntity<Response> getStockOverview() {
        Response response = stockOverviewservice.getStockOverview();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
