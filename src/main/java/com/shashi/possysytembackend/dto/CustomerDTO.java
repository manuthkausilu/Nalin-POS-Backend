package com.shashi.possysytembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {
    private Long customerId;
    private String customerName;
    private String phone;
    private String email;
    private String address;
}

