package com.shashi.possysytembackend.controller;

import com.shashi.possysytembackend.dto.CustomerDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody CustomerDTO dto) {
        Response response = customerService.createCustomer(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> get(@PathVariable Long id) {
        Response response = customerService.getCustomerById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<Response> getAll() {
        Response response = customerService.getAllCustomers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody CustomerDTO dto) {
        Response response = customerService.updateCustomer(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        Response response = customerService.deleteCustomer(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
