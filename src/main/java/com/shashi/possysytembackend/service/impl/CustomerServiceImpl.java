package com.shashi.possysytembackend.service.impl;


import com.shashi.possysytembackend.dto.CustomerDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.entity.Customer;
import com.shashi.possysytembackend.repository.CustomerRepository;
import com.shashi.possysytembackend.service.CustomerService;
import com.shashi.possysytembackend.exception.OurException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response createCustomer(CustomerDTO customerDto) {
        Response response = new Response();
        try {
            // Check for unique phone
            if (customerDto.getPhone() != null && customerRepository.findAll().stream()
                    .anyMatch(c -> customerDto.getPhone().equals(c.getPhone()))) {
                throw new OurException("Phone number already exists");
            }
            // Check for unique email
            if (customerDto.getEmail() != null && customerRepository.findAll().stream()
                    .anyMatch(c -> customerDto.getEmail().equals(c.getEmail()))) {
                throw new OurException("Email already exists");
            }
            Customer customer = modelMapper.map(customerDto, Customer.class);
            Customer saved = customerRepository.save(customer);
            response.setStatusCode(201);
            response.setMessage("Customer created successfully");
            response.setCustomerDTO(modelMapper.map(saved, CustomerDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating customer: " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response getCustomerById(Long id) {
        Response response = new Response();
        try {
            Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
            response.setStatusCode(200);
            response.setMessage("Customer fetched successfully");
            response.setCustomerDTO(modelMapper.map(customer, CustomerDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching customer: " + e.getMessage());
        }
        return response;
    }
    

    @Override
    public Response getAllCustomers() {
        Response response = new Response();
        try {
            List<CustomerDTO> customerDTOs = customerRepository.findAll()
                    .stream()
                    .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Customers fetched successfully");
            response.setCustomerDTOList(customerDTOs);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching customers: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateCustomer(Long id, CustomerDTO customerDto) {
        Response response = new Response();
        try {
            Customer existing = customerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
            // Unique phone check (ignore current customer)
            if (customerDto.getPhone() != null && customerRepository.findAll().stream()
                    .anyMatch(c -> customerDto.getPhone().equals(c.getPhone()) && !c.getCustomerId().equals(id))) {
                throw new OurException("Phone number already exists");
            }
            // Unique email check (ignore current customer)
            if (customerDto.getEmail() != null && customerRepository.findAll().stream()
                    .anyMatch(c -> customerDto.getEmail().equals(c.getEmail()) && !c.getCustomerId().equals(id))) {
                throw new OurException("Email already exists");
            }
            customerDto.setCustomerId(existing.getCustomerId());
            modelMapper.map(customerDto, existing);
            Customer updatedCustomer = customerRepository.save(existing);
            response.setStatusCode(200);
            response.setMessage("Customer updated successfully");
            response.setCustomerDTO(modelMapper.map(updatedCustomer, CustomerDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating customer: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteCustomer(Long id) {
        Response response = new Response();
        try {
            customerRepository.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("Customer deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting customer: " + e.getMessage());
        }
        return response;
    }
}
