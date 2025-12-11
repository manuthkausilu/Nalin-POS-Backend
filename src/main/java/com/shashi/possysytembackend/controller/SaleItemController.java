package com.shashi.possysytembackend.controller;

import com.shashi.possysytembackend.dto.SaleItemDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.SaleItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sale-items")
public class SaleItemController {
    private final SaleItemService saleItemService;
    public SaleItemController(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody SaleItemDTO dto) {
        Response response = saleItemService.createSaleItem(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> get(@PathVariable Long id) {
        Response response = saleItemService.getSaleItemById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        Response response = saleItemService.getAllSaleItems();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody SaleItemDTO dto) {
        Response response = saleItemService.updateSaleItem(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        Response response = saleItemService.deleteSaleItem(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/by-sale/{saleId}")
    public ResponseEntity<Response> getBySaleId(@PathVariable Long saleId) {
        Response response = saleItemService.getSaleItemsBySaleId(saleId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/by-sale/{saleId}/view")
    public ResponseEntity<Response> getBySaleIdView(@PathVariable Long saleId) {
        Response response = saleItemService.getSaleItemViewsBySaleId(saleId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
