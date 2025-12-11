package com.shashi.possysytembackend.controller;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.BrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brands")
public class BrandController {
    private final BrandService brandService;
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> get(@PathVariable Long id) {
        Response response = brandService.getBrandById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        Response response = brandService.getAllBrands();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
