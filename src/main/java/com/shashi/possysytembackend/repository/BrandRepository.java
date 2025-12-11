package com.shashi.possysytembackend.repository;

import com.shashi.possysytembackend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByBrandName(String brandName);
}
