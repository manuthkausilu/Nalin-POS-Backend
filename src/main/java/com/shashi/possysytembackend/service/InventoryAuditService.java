package com.shashi.possysytembackend.service;

import com.shashi.possysytembackend.dto.InventoryAuditDTO;
import com.shashi.possysytembackend.dto.Response;

public interface InventoryAuditService {
    Response createInventoryAudit(InventoryAuditDTO auditDto);
    Response getInventoryAuditById(Long id);
    Response getAllInventoryAudits();
    Response updateInventoryAudit(Long id, InventoryAuditDTO auditDto);
    Response deleteInventoryAudit(Long id);
}
