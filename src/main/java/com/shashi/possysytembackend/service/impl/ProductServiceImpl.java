package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.ProductDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.entity.Product;
import com.shashi.possysytembackend.exception.OurException;
import com.shashi.possysytembackend.repository.ProductRepository;
import com.shashi.possysytembackend.service.LogService;
import com.shashi.possysytembackend.service.ProductService;
import com.shashi.possysytembackend.repository.CategoryRepository;
import com.shashi.possysytembackend.repository.BrandRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final LogService logService;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, LogService logService, CategoryRepository categoryRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.logService = logService;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public Response createProduct(ProductDTO productDto) {
        Response response = new Response();
        try {
            Product product = new Product();
            product.setProductName(productDto.getProductName());
            product.setCategory(categoryRepository.findById(productDto.getCategoryId()).orElseThrow(() -> new OurException("Category not found")));
            if (productDto.getBrandId() != null) {
                product.setBrand(brandRepository.findById(productDto.getBrandId()).orElse(null));
            }
            product.setCost(productDto.getCost());
            product.setSalePrice(productDto.getSalePrice());
            product.setQty(productDto.getQty());
            product.setIsActive(true);
            product.setTrackInventory(productDto.getTrackInventory());

            // Barcode generate and set (ensure uniqueness)
            product.setBarcode(generateUniqueBarcode());

            Product savedProduct = productRepository.save(product);
            response.setStatusCode(201);
            response.setMessage("Product created successfully");
            response.setProductDTO(modelMapper.map(savedProduct, ProductDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating product: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getProductById(Long id) {
        Response response = new Response();
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new OurException("Product not found"));
            ProductDTO productDto = modelMapper.map(product, ProductDTO.class);
            response.setStatusCode(200);
            response.setMessage("Product fetch successfully");
            response.setProductDTO(productDto);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetch product: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllProducts() {
        Response response = new Response();
        try {
            List<Product> products = productRepository.findAll();
            List<ProductDTO> productDtos = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());
            Collections.reverse(productDtos); // Reverse the list
            response.setStatusCode(200);
            response.setMessage("Products fetched successfully");
            response.setProductDTOList(productDtos);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching products: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateProduct(Long id, ProductDTO productDto) {
        Response response = new Response();
        try {
            Product existingProduct = productRepository.findById(id).orElseThrow(() -> new OurException("Product not found"));
            existingProduct.setProductName(productDto.getProductName());
            existingProduct.setCategory(categoryRepository.findById(productDto.getCategoryId()).orElseThrow(() -> new OurException("Category not found")));
            if (productDto.getBrandId() != null) {
                existingProduct.setBrand(brandRepository.findById(productDto.getBrandId()).orElse(null));
            }
            existingProduct.setCost(productDto.getCost());
            existingProduct.setSalePrice(productDto.getSalePrice());
            existingProduct.setQty(productDto.getQty());
            existingProduct.setIsActive(productDto.getIsActive());
            existingProduct.setTrackInventory(productDto.getTrackInventory());
            Product updatedProduct = productRepository.save(existingProduct);
            response.setStatusCode(200);
            response.setMessage("Product updated successfully");
            response.setProductDTO(modelMapper.map(updatedProduct, ProductDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating product: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteProduct(Long id) {
        Response response = new Response();
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new OurException("Product not found"));
            productRepository.delete(product);
            response.setStatusCode(200);
            response.setMessage("Product deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting product: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getProductsWithLowQty() {
        Response response = new Response();
        try {
            List<Product> products = productRepository.findAllByQtyLessThanEqualAndIsActiveTrue(10);
            List<ProductDTO> productDtos = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Products with low quantity fetched successfully");
            response.setProductDTOList(productDtos);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching products with low quantity: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response findAllByActive() {
        Response response = new Response();
        try {
            List<Product> products = productRepository.findAllByIsActiveTrue();
            List<ProductDTO> productDTOS = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Products fetched successfully");
            response.setProductDTOList(productDTOS);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching Active products: " + e.getMessage());
        }
        return response;

    }

    @Override
    public Response getProductsByCategory(Long categoryId) {
        Response response = new Response();
        try {
            List<Product> products = productRepository.findByCategory_CategoryIdAndIsActiveTrue(categoryId.intValue());
            List<ProductDTO> productDtos = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Products by category fetched successfully");
            response.setProductDTOList(productDtos);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching products by category: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getProductsByBrand(Long brandId) {
        Response response = new Response();
        try {
            List<Product> products = productRepository.findByBrand_BrandIdAndIsActiveTrue(brandId.intValue());
            List<ProductDTO> productDtos = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Products by brand fetched successfully");
            response.setProductDTOList(productDtos);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching products by brand: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response searchProducts(String query) {
        Response response = new Response();
        try {
            List<Product> products = productRepository.searchProducts(query.toLowerCase());
            List<ProductDTO> productDtos = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Products search fetched successfully");
            response.setProductDTOList(productDtos);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error searching products: " + e.getMessage());
        }
        return response;
    }

    /**
     * Generates a unique barcode by checking the database for duplicates
     */
    public String generateUniqueBarcode() {
        final int MAX_ATTEMPTS = 200;
        int attempts = 0;
        while (attempts++ < MAX_ATTEMPTS) {
            String barcode = generateBarcode();
            // validate format: non-null, exactly 12 chars, all digits
            if (barcode == null || barcode.length() != 12 || !barcode.chars().allMatch(Character::isDigit)) {
                continue;
            }
            // check DB for existing barcode
            if (!productRepository.existsByBarcode(barcode)) {
                return barcode;
            }
            // small backoff to reduce contention on high collision rates
            try {
                Thread.sleep(5);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
        throw new RuntimeException("Unable to generate unique 12-digit barcode after " + MAX_ATTEMPTS + " attempts");
    }


    public static String generateBarcode() {
        java.security.SecureRandom random = new java.security.SecureRandom();
        StringBuilder barcode = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            barcode.append(random.nextInt(10)); // Appends a digit (0-9)
        }
        return barcode.toString();
    }

    // Add this method
    public Response getProductsByCategoryAndBrand(Long categoryId, Long brandId) {
        Response response = new Response();
        try {
            List<Product> products = productRepository.findByCategory_CategoryIdAndBrand_BrandIdAndIsActiveTrue(categoryId, brandId);
            List<ProductDTO> productDtos = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());
            System.out.println(productDtos);
            response.setStatusCode(200);
            response.setMessage("Products by category and brand fetched successfully");
            response.setProductDTOList(productDtos);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching products by category and brand: " + e.getMessage());
        }
        return response;
    }

}