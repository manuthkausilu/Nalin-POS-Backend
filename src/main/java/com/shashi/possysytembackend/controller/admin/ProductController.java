package com.shashi.possysytembackend.controller.admin;

import com.shashi.possysytembackend.dto.ProductDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminProductController")
@RequestMapping("/api/admin/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody ProductDTO dto) {
        Response response = productService.createProduct(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        Response response = productService.updateProduct(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        Response response = productService.deleteProduct(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
