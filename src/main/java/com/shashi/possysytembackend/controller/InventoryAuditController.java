package com.shashi.possysytembackend.controller;

import com.shashi.possysytembackend.dto.InventoryAuditDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.InventoryAuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory-audits")
public class  InventoryAuditController {
    private final InventoryAuditService inventoryAuditService;
    public InventoryAuditController(InventoryAuditService inventoryAuditService) {
        this.inventoryAuditService = inventoryAuditService;
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody InventoryAuditDTO dto) {
        Response response = inventoryAuditService.createInventoryAudit(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> get(@PathVariable Long id) {
        Response response = inventoryAuditService.getInventoryAuditById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        Response response = inventoryAuditService.getAllInventoryAudits();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody InventoryAuditDTO dto) {
        Response response = inventoryAuditService.updateInventoryAudit(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        Response response = inventoryAuditService.deleteInventoryAudit(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
