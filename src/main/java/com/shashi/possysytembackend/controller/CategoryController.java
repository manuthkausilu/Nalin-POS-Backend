package com.shashi.possysytembackend.controller;

import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> get(@PathVariable Long id) {
        Response response = categoryService.getCategoryById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        Response response = categoryService.getAllCategories();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
