package com.shashi.possysytembackend.controller.admin;

import com.shashi.possysytembackend.dto.CategoryDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminCategoryController")
@RequestMapping("/api/admin/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Response> getAllCategories() {
        Response response = categoryService.getAllCategories();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody CategoryDTO dto) {
        Response response = categoryService.createCategory(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        Response response = categoryService.updateCategory(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        Response response = categoryService.deleteCategory(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
