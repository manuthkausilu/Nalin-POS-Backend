package com.shashi.possysytembackend.service;

import com.shashi.possysytembackend.dto.ProductDTO;
import com.shashi.possysytembackend.dto.Response;

public interface ProductService {
    Response createProduct(ProductDTO productDto);
    Response getProductById(Long id);
    Response getAllProducts();
    Response updateProduct(Long id, ProductDTO productDto);
    Response deleteProduct(Long id);
    Response getProductsWithLowQty();
    Response getProductsByCategory(Long categoryId);
    Response getProductsByBrand(Long brandId);
    Response searchProducts(String query);
    Response getProductsByCategoryAndBrand(Long categoryId, Long brandId);
    Response findAllByActive();
}
