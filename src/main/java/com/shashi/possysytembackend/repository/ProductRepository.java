package com.shashi.possysytembackend.repository;

import com.shashi.possysytembackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_CategoryIdAndIsActiveTrue(Integer categoryId);
    List<Product> findByBrand_BrandIdAndIsActiveTrue(Integer brandId);
    boolean existsByBarcode(String barcode);
    long countProductsByIsActiveTrue();
    List<Product> findByQtyLessThanEqual(Integer qty);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.qty < 10 AND p.qty > 0")
    long countLowStock();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.qty = 0")
    long countOutOfStock();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.qty >= 10")
    long countHealthyStock();

    List<Product> findAllByIsActiveTrue();
    List<Product> findAllByQtyLessThanEqualAndIsActiveTrue(Integer qty);


    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE %:query% OR LOWER(p.barcode) LIKE %:query%")
    List<Product> searchProducts(String query);

    @Query("SELECT SUM(p.salePrice * p.qty) FROM Product p WHERE p.isActive = true")
    Double getTotalPriceOfAllProducts();

    @Query("SELECT SUM(p.qty) FROM Product p WHERE p.isActive = true")
    Long getTotalQuantityOfAllProducts();

    List<Product> findByCategory_CategoryIdAndBrand_BrandIdAndIsActiveTrue(Long categoryId, Long brandId);
}