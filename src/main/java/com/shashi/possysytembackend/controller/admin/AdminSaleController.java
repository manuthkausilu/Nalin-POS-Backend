package com.shashi.possysytembackend.controller.admin;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/sales")
public class AdminSaleController {
    
    private final SaleService saleService;

    public AdminSaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteSale(@PathVariable Long id) {
        Response response = saleService.deleteSale(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
