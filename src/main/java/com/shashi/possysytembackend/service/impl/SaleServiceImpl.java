package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.SaleDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.dto.SaleItemDTO;
import com.shashi.possysytembackend.entity.Product;
import com.shashi.possysytembackend.entity.Sale;
import com.shashi.possysytembackend.entity.SaleItem;
import com.shashi.possysytembackend.repository.*;
import com.shashi.possysytembackend.service.SaleService;
import com.shashi.possysytembackend.exception.OurException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final SaleItemRepository saleItemRepository;


    private SaleDTO convertToDTO(Sale sale) {
        SaleDTO dto = new SaleDTO();
        dto.setSaleId(sale.getSaleId());
        dto.setTotalAmount(sale.getTotalAmount());
        dto.setTotalDiscount(sale.getTotalDiscount());
        dto.setPaymentMethod(sale.getPaymentMethod().name());
        dto.setSaleDate(sale.getSaleDate());
        dto.setUserId(sale.getUser().getUserId());
        if (sale.getCustomer() != null) {
            dto.setCustomerId(sale.getCustomer().getCustomerId());
        }
        dto.setOriginalTotal(sale.getOriginalTotal());
        dto.setItemDiscounts(sale.getItemDiscounts());
        dto.setSubtotal(sale.getSubtotal());
        dto.setOrderDiscountPercentage(sale.getOrderDiscountPercentage());
        dto.setOrderDiscount(sale.getOrderDiscount());
        dto.setPaymentAmount(sale.getPaymentAmount());
        dto.setBalance(sale.getBalance());
        return dto;
    }

    @Override
    @Transactional
    public Response createSale(SaleDTO saleDto) {
        Response response = new Response();
        try {
            // Begin transactimon
            Sale sale = new Sale();
            sale.setTotalAmount(saleDto.getTotalAmount());
            sale.setTotalDiscount(saleDto.getTotalDiscount());
            sale.setPaymentMethod(Sale.PaymentMethod.valueOf(saleDto.getPaymentMethod()));
            sale.setUser(userRepository.findById(saleDto.getUserId())
                .orElseThrow(() -> new OurException("User not found")));

            if (saleDto.getCustomerId() != null) {
                sale.setCustomer(customerRepository.findById(saleDto.getCustomerId())
                    .orElse(null));
            }
            sale.setOriginalTotal(saleDto.getOriginalTotal());
            sale.setItemDiscounts(saleDto.getItemDiscounts());
            sale.setSubtotal(saleDto.getSubtotal());
            sale.setOrderDiscountPercentage(saleDto.getOrderDiscountPercentage());
            sale.setOrderDiscount(saleDto.getOrderDiscount());
            sale.setPaymentAmount(saleDto.getPaymentAmount());
            sale.setBalance(saleDto.getBalance());

            // Save the sale first to get the ID
            Sale saved = saleRepository.save(sale);

            // Process sale items and update inventory
            if (saleDto.getSaleItems() != null && !saleDto.getSaleItems().isEmpty()) {
                for (SaleItemDTO saleItemDto : saleDto.getSaleItems()) {
                    // Get product
                    Product product = productRepository.findById(saleItemDto.getProductId())
                        .orElseThrow(() -> new OurException("Product not found: " + saleItemDto.getProductId()));

                    // Check inventory
                    if (product.getQty() < saleItemDto.getQty()) {
                        throw new OurException("Insufficient inventory for product: " + product.getProductName());
                    }

                    // Update inventory
                    product.setQty(product.getQty() - saleItemDto.getQty());
                    productRepository.save(product);

                    // Create sale item
                    SaleItem saleItem = new SaleItem();
                    saleItem.setSale(saved);
                    saleItem.setProduct(product);
                    saleItem.setQty(saleItemDto.getQty());
                    saleItem.setPrice(saleItemDto.getPrice());
                    saleItem.setTotalPrice(saleItemDto.getTotalPrice());
                    saleItem.setDiscount(saleItemDto.getDiscount());
                    saleItemRepository.save(saleItem);
                }
            }

            response.setStatusCode(201);
            response.setMessage("Sale created successfully and inventory updated");
            response.setSaleDTO(convertToDTO(saved));

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating sale: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getSaleById(Long id) {
        //        Response response = new Response();
        //        try {
        //            List<Sale> sales = saleRepository.findAllByUser_UserId(Math.toIntExact(id));
        //            List<SaleDTO> saleDTOs = sales.stream()
        //                    .map(this::convertToDTO)
        //                    .collect(Collectors.toList());
        //            response.setStatusCode(200);
        //            response.setMessage("Sale fetched successfully");
        //            response.setSaleDTOList( saleDTOs);
        //        } catch (Exception e) {
        //            response.setStatusCode(500);
        //            response.setMessage("Error fetching sale: " + e.getMessage());
        //        }
        //        return response;

        Response response = new Response();
        try {
            Sale sale = saleRepository.findById(id)
                    .orElseThrow(() -> new OurException("Sale not found"));
            SaleDTO dto = convertToDTO(sale);
            response.setStatusCode(200);
            response.setMessage("Sale fetched successfully");
            response.setSaleDTO(dto);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching sale: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllSales() {
        Response response = new Response();
        try {
            List<Sale> sales = saleRepository.findAll();
            Collections.reverse(sales);
            List<SaleDTO> saleDTOs = sales.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Sales fetched successfully");
            response.setSaleDTOList(saleDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching sales: " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    public Response updateSale(Long id, SaleDTO saleDto) {
        Response response = new Response();
        try {
            Sale existing = saleRepository.findById(id).orElseThrow(() -> new OurException("Sale not found"));
            // Set local date/time for saleDate
            existing.setSaleDate(java.time.LocalDateTime.now());
            existing.setTotalAmount(saleDto.getTotalAmount());
            existing.setTotalDiscount(saleDto.getTotalDiscount());
            existing.setPaymentMethod(Sale.PaymentMethod.valueOf(saleDto.getPaymentMethod()));
            existing.setUser(userRepository.findById(saleDto.getUserId()).orElseThrow(() -> new OurException("User not found")));
            if (saleDto.getCustomerId() != null) {
                existing.setCustomer(customerRepository.findById(saleDto.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found")));
            }
            existing.setOriginalTotal(saleDto.getOriginalTotal());
            existing.setItemDiscounts(saleDto.getItemDiscounts());
            existing.setSubtotal(saleDto.getSubtotal());
            existing.setOrderDiscountPercentage(saleDto.getOrderDiscountPercentage());
            existing.setOrderDiscount(saleDto.getOrderDiscount());

            Sale saved = saleRepository.save(existing);
            response.setStatusCode(200);
            response.setMessage("Sale updated successfully");
            response.setSaleDTO(modelMapper.map(saved, SaleDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating sale: " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    public Response deleteSale(Long id) {
        Response response = new Response();
        try {
            // Find the sale
            Sale sale = saleRepository.findById(id)
                    .orElseThrow(() -> new OurException("Sale not found"));

            // First restore inventory quantities
            if (sale.getSaleItems() != null) {
                for (SaleItem saleItem : sale.getSaleItems()) {
                    Product product = saleItem.getProduct();
                    if (product != null && saleItem.getQty() != null) {
                        // Restore the quantity back to inventory
                        product.setQty(product.getQty() + saleItem.getQty());
                        productRepository.save(product);
                    }
                }
            }

            // Delete the sale (this will cascade delete sale items due to JPA relationship)
            saleRepository.delete(sale);

            response.setStatusCode(200);
            response.setMessage("Sale and related items deleted successfully, inventory restored");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting sale: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getSalesByUserId(Long userId) {
        Response response = new Response();
        try {
            List<Sale> sales = saleRepository.findAllByUser_UserId(Math.toIntExact(userId));
            Collections.reverse(sales);
            List<SaleDTO> saleDTOs = sales.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Sales retrieved successfully");
            response.setSaleDTOList(saleDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving sales: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getSalesByDateRange(String rangeType, String startDate, String endDate) {
        Response response = new Response();
        try {
            LocalDateTime start;
            LocalDateTime end;

            LocalDate today = LocalDate.now();

            switch (rangeType.toLowerCase()) {
                case "today":
                    start = today.atStartOfDay();
                    end = today.atTime(LocalTime.MAX);
                    break;
                case "lastweek":
                    start = today.minusWeeks(1).with(java.time.DayOfWeek.MONDAY).atStartOfDay();
                    end = today.with(java.time.DayOfWeek.SUNDAY).atTime(LocalTime.MAX);
                    break;
                case "lastmonth":
                    start = today.minusMonths(1).withDayOfMonth(1).atStartOfDay();
                    end = today.withDayOfMonth(today.lengthOfMonth()).atTime(LocalTime.MAX);
                    break;
                case "custom":
                    start = LocalDate.parse(startDate).atStartOfDay();
                    end = LocalDate.parse(endDate).atTime(LocalTime.MAX);
                    break;
                default:
                    response.setStatusCode(400);
                    response.setMessage("Invalid rangeType");
                    return response;
            }

            List<Sale> sales = saleRepository.findBySaleDateBetween(start, end);
            List<SaleDTO> saleDTOs = sales.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Sales fetched successfully");
            response.setSaleDTOList(saleDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching sales: " + e.getMessage());
        }
        return response;
    }
}
