package com.shashi.possysytembackend.repository;

import com.shashi.possysytembackend.entity.InventoryAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryAuditRepository extends JpaRepository<InventoryAudit, Long> {
    List<InventoryAudit> findByProduct_ProductId(Long productId);
}