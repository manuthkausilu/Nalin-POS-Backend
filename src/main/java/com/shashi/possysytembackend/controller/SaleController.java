package com.shashi.possysytembackend.controller;

import com.shashi.possysytembackend.dto.SaleDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.dto.SaleItemDTO;
import com.shashi.possysytembackend.service.SaleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody SaleDTO dto, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        dto.setUserId(Integer.valueOf(userId));
        
        // // Validate new required fields
        // if (dto.getOriginalTotal() == null || dto.getItemDiscounts() == null || 
        //     dto.getSubtotal() == null || dto.getOrderDiscountPercentage() == null || 
        //     dto.getOrderDiscount() == null) {
        //     Response response = new Response();
        //     response.setStatusCode(400);
        //     response.setMessage("Missing required fields");
        //     return ResponseEntity.badRequest().body(response);
        // }
        
        Response response = saleService.createSale(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> get(@PathVariable Long id) {
        Response response = saleService.getSaleById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        Response response = saleService.getAllSales();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody SaleDTO dto) {
        // // Validate new required fields
        // if (dto.getOriginalTotal() == null || dto.getItemDiscounts() == null || 
        //     dto.getSubtotal() == null || dto.getOrderDiscountPercentage() == null || 
        //     dto.getOrderDiscount() == null) {
        //     Response response = new Response();
        //     response.setStatusCode(400);
        //     response.setMessage("Missing required fields");
        //     return ResponseEntity.badRequest().body(response);
        // }
        
        Response response = saleService.updateSale(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Response> getByUserId(@PathVariable Long id) {
        Response response = saleService.getSalesByUserId(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/date-range")
    public ResponseEntity<Response> getSalesByDateRange(
            @RequestParam String rangeType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Response response = saleService.getSalesByDateRange(rangeType, startDate, endDate);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
