package com.shashi.possysytembackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int statusCode;
    private String message;

    private String token;
    private String role;
    private String expirationTime;

    private UserDTO userDTO;
    private List<UserDTO> userDTOList;

    private BrandDTO brandDTO;
    private List<BrandDTO> brandDTOList;

    private ProductDTO productDTO;
    private List<ProductDTO> productDTOList;

    private CategoryDTO categoryDTO;
    private List<CategoryDTO> categoryDTOList;

    private CustomerDTO customerDTO;
    private List<CustomerDTO> customerDTOList;

    private SaleDTO saleDTO;
    private List<SaleDTO> saleDTOList;

    private SaleItemDTO saleItemDTO;
    private List<SaleItemDTO> saleItemDTOList;

    private LogDTO logDTO;
    private List<LogDTO> logDTOList;

    private StockOverviewDTO stockOverviewDTO;

    private InventoryAuditDTO inventoryAuditDTO;
    private List<InventoryAuditDTO> inventoryAuditDTOList;
    private Object data;

}
