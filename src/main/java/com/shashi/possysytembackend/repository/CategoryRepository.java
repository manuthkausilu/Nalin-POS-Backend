package com.shashi.possysytembackend.repository;
import com.shashi.possysytembackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    boolean existsByName(String name);
    boolean existsByNameAndCategoryIdNot(String name, Long id);
}
