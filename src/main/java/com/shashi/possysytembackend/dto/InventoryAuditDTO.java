package com.shashi.possysytembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryAuditDTO {
    private Long auditId;
    private Long productId;
    private Integer changeQty;
    private String reason;
    private LocalDateTime auditDate;
    private Integer userId;

}
