package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.InventoryAuditDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.entity.InventoryAudit;
import com.shashi.possysytembackend.repository.InventoryAuditRepository;
import com.shashi.possysytembackend.repository.ProductRepository;
import com.shashi.possysytembackend.repository.UserRepository;
import com.shashi.possysytembackend.service.InventoryAuditService;
import com.shashi.possysytembackend.exception.OurException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryAuditServiceImpl implements InventoryAuditService {
    private final InventoryAuditRepository inventoryAuditRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public InventoryAuditServiceImpl(InventoryAuditRepository inventoryAuditRepository, ModelMapper modelMapper, ProductRepository productRepository, UserRepository userRepository) {
        this.inventoryAuditRepository = inventoryAuditRepository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Response createInventoryAudit(InventoryAuditDTO auditDto) {
        Response response = new Response();
        try {
            InventoryAudit audit = new InventoryAudit();
            audit.setProduct(productRepository.findById(auditDto.getProductId()).orElseThrow(() -> new OurException("Product not found")));
            audit.setChangeQty(auditDto.getChangeQty());
            audit.setReason(auditDto.getReason());
            audit.setUser(userRepository.findById(auditDto.getUserId()).orElseThrow(() -> new OurException("User not found")));
            InventoryAudit saved = inventoryAuditRepository.save(audit);
            response.setStatusCode(201);
            response.setMessage("Inventory audit created successfully");
            response.setInventoryAuditDTO(modelMapper.map(saved, InventoryAuditDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating inventory audit: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getInventoryAuditById(Long id) {
        Response response = new Response();
        try {
            InventoryAudit audit = inventoryAuditRepository.findById(id).orElseThrow(() -> new OurException("Audit not found"));
            response.setStatusCode(200);
            response.setMessage("Inventory audit fetched successfully");
            response.setInventoryAuditDTO(modelMapper.map(audit, InventoryAuditDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching inventory audit: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllInventoryAudits() {
        Response response = new Response();
        try {
            List<InventoryAuditDTO> auditDTOs = inventoryAuditRepository.findAll().stream()
                    .map(a -> modelMapper.map(a, InventoryAuditDTO.class))
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Inventory audits fetched successfully");
            response.setInventoryAuditDTOList(auditDTOs);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching inventory audits: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateInventoryAudit(Long id, InventoryAuditDTO auditDto) {
        Response response = new Response();
        try {
            InventoryAudit existing = inventoryAuditRepository.findById(id).orElseThrow(() -> new OurException("Audit not found"));
            existing.setProduct(productRepository.findById(auditDto.getProductId()).orElseThrow(() -> new OurException("Product not found")));
            existing.setChangeQty(auditDto.getChangeQty());
            existing.setReason(auditDto.getReason());
            existing.setAuditDate(java.time.LocalDateTime.now());
            existing.setUser(userRepository.findById(auditDto.getUserId()).orElseThrow(() -> new OurException("User not found")));
            InventoryAudit updated = inventoryAuditRepository.save(existing);
            response.setStatusCode(200);
            response.setMessage("Inventory audit updated successfully");
            response.setInventoryAuditDTO(modelMapper.map(updated, InventoryAuditDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating inventory audit: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteInventoryAudit(Long id) {
        Response response = new Response();
        try {
            // Check if audit exists before deleting
            InventoryAudit audit = inventoryAuditRepository.findById(id)
                    .orElseThrow(() -> new OurException("Inventory audit not found"));
            inventoryAuditRepository.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("Inventory audit deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting inventory audit: " + e.getMessage());
        }
        return response;
    }
}
