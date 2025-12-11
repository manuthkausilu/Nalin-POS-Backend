package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.CategoryDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.entity.Category;
import com.shashi.possysytembackend.exception.OurException;
import com.shashi.possysytembackend.repository.CategoryRepository;
import com.shashi.possysytembackend.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response createCategory(CategoryDTO categoryDto) {
        Response response = new Response();
        try {
            if(categoryRepository.existsByName(categoryDto.getName())) {
                throw new OurException("Category with name " + categoryDto.getName() + " already exists");
            }
            Category category = modelMapper.map(categoryDto, Category.class);
            Category savedCategory = categoryRepository.save(category);
            response.setStatusCode(201);
            response.setMessage("Category created successfully");
            response.setCategoryDTO(modelMapper.map(savedCategory, CategoryDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating category: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getCategoryById(Long id) {
        Response response = new Response();
        try {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new OurException("Category not found"));
            response.setStatusCode(200);
            response.setMessage("Category fetching successfully");
            response.setCategoryDTO(modelMapper.map(category, CategoryDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetch category: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllCategories() {
        Response response = new Response();
        try {
            List<Category> categories = categoryRepository.findAll();
            List<CategoryDTO> categoryDTOs = categories.stream()
                    .map(category -> modelMapper.map(category, CategoryDTO.class))
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Categories fetched successfully");
            response.setCategoryDTOList(categoryDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching categories: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateCategory(Long id, CategoryDTO categoryDto) {
        System.out.println(id);
        System.out.println(categoryDto);
        Response response = new Response();
        try {
            Category existingCategory = categoryRepository.findById(id)
                    .orElseThrow(() -> new OurException("Category not found"));

            if (categoryRepository.existsByNameAndCategoryIdNot(categoryDto.getName(), id)) {
                throw new OurException("Category with name " + categoryDto.getName() + " already exists");
            }

            existingCategory.setName(categoryDto.getName());

            Category updatedCategory = categoryRepository.save(existingCategory);

            response.setStatusCode(200);
            response.setMessage("Category updated successfully");
            response.setCategoryDTO(modelMapper.map(updatedCategory, CategoryDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating category: " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response deleteCategory(Long id) {
        Response response = new Response();
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new OurException("Category not found"));
            categoryRepository.delete(category);
            response.setStatusCode(200);
            response.setMessage("Category deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting category: " + e.getMessage());
        }
        return response;
    }
}
