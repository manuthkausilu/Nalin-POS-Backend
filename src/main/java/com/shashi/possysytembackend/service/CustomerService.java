package com.shashi.possysytembackend.service;

import com.shashi.possysytembackend.dto.CustomerDTO;
import com.shashi.possysytembackend.dto.Response;

public interface CustomerService {
    Response createCustomer(CustomerDTO customerDto);
    Response getCustomerById(Long id);
    Response getAllCustomers();
    Response updateCustomer(Long id, CustomerDTO customerDto);
    Response deleteCustomer(Long id);
}