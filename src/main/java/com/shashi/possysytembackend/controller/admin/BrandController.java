package com.shashi.possysytembackend.controller.admin;

import com.shashi.possysytembackend.dto.BrandDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.BrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("adminBrandController")
@RequestMapping("/api/admin/brands")
public class BrandController {
    private final BrandService brandService;
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody BrandDTO brandDTO) {
        Response response = brandService.createBrand(brandDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody BrandDTO brandDTO) {
        Response response = brandService.updateBrand(id, brandDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        Response response = brandService.deleteBrand(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
