package com.shashi.possysytembackend.controller;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> get(@PathVariable Long id) {
        Response response = productService.getProductById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        Response response = productService.findAllByActive();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/low-qty")
    public ResponseEntity<Response> getProductsWithLowQty() {
        Response response = productService.getProductsWithLowQty();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Response> getByCategory(@PathVariable Long categoryId) {
        Response response = productService.getProductsByCategory(categoryId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<Response> getByBrand(@PathVariable Long brandId) {
        Response response = productService.getProductsByBrand(brandId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Response> searchProducts(@RequestParam("q") String query) {
        Response response = productService.searchProducts(query);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/active")
    public ResponseEntity<Response> ActiveProducts() {
        Response response = productService.findAllByActive();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/by-category-brand")
    public ResponseEntity<Response> getByCategoryAndBrand(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("brandId") Long brandId) {
        Response response = productService.getProductsByCategoryAndBrand(categoryId, brandId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
