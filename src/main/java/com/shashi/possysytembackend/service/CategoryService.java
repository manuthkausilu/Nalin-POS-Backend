package com.shashi.possysytembackend.service;

import com.shashi.possysytembackend.dto.CategoryDTO;
import com.shashi.possysytembackend.dto.Response;

import java.util.List;

public interface CategoryService {
    Response createCategory(CategoryDTO categoryDto);
    Response getCategoryById(Long id);
    Response getAllCategories();
    Response updateCategory(Long id, CategoryDTO categoryDto);
    Response deleteCategory(Long id);
}
