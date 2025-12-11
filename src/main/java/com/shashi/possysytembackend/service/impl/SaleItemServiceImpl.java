package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.SaleItemDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.entity.SaleItem;
import com.shashi.possysytembackend.repository.SaleItemRepository;
import com.shashi.possysytembackend.service.SaleItemService;
import com.shashi.possysytembackend.repository.SaleRepository;
import com.shashi.possysytembackend.repository.ProductRepository;
import com.shashi.possysytembackend.exception.OurException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import com.shashi.possysytembackend.dto.SaleItemViewDTO;

@Service
public class SaleItemServiceImpl implements SaleItemService {
    private final SaleItemRepository saleItemRepository;
    private final ModelMapper modelMapper;
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    public SaleItemServiceImpl(SaleItemRepository saleItemRepository, ModelMapper modelMapper, SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleItemRepository = saleItemRepository;
        this.modelMapper = modelMapper;
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Response createSaleItem(SaleItemDTO saleItemDto) {
        Response response = new Response();
        try {
            // Check product quantity
            var product = productRepository.findById(saleItemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            if (saleItemDto.getQty() == null || saleItemDto.getQty() <= 0) {
                throw new OurException("Sale quantity must be greater than zero");
            }
            if (product.getQty() == null || product.getQty() < saleItemDto.getQty()) {
                throw new OurException("Insufficient product quantity");
            }

            SaleItem saleItem = new SaleItem();
            saleItem.setSale(saleRepository.findById(saleItemDto.getSaleId()).orElseThrow(() -> new RuntimeException("Sale not found")));
            saleItem.setProduct(product);
            saleItem.setQty(saleItemDto.getQty());
            saleItem.setPrice(saleItemDto.getPrice());
            saleItem.setDiscount(saleItemDto.getDiscount());

            // Deduct product quantity
            product.setQty(product.getQty() - saleItemDto.getQty());
            productRepository.save(product);

            SaleItem saved = saleItemRepository.save(saleItem);

            SaleItemDTO dto = new SaleItemDTO();
            dto.setSaleItemId(saved.getSaleItemId());
            dto.setSaleId(saved.getSale().getSaleId());
            dto.setProductId(saved.getProduct().getProductId());
            dto.setQty(saved.getQty());
            dto.setPrice(saved.getPrice());
            dto.setDiscount(saved.getDiscount());

            response.setStatusCode(201);
            response.setMessage("SaleItem created successfully");
            response.setSaleItemDTO(dto);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating sale item: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getSaleItemById(Long id) {
        Response response = new Response();
        try {
            SaleItem saleItem = saleItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("SaleItem not found"));

            SaleItemDTO dto = new SaleItemDTO();
            dto.setSaleItemId(saleItem.getSaleItemId());
            dto.setSaleId(saleItem.getSale().getSaleId());
            dto.setProductId(saleItem.getProduct().getProductId());
            dto.setQty(saleItem.getQty());
            dto.setPrice(saleItem.getPrice());
            dto.setDiscount(saleItem.getDiscount());

            response.setStatusCode(200);
            response.setMessage("SaleItem fetched successfully");
            response.setSaleItemDTO(dto);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching sale item: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllSaleItems() {
        Response response = new Response();
        try {
            List<SaleItemDTO> saleItemDTOs = saleItemRepository.findAll().stream()
                    .map(si -> {
                        SaleItemDTO dto = new SaleItemDTO();
                        dto.setSaleItemId(si.getSaleItemId());
                        dto.setSaleId(si.getSale().getSaleId());
                        dto.setProductId(si.getProduct().getProductId());
                        dto.setQty(si.getQty());
                        dto.setPrice(si.getPrice());
                        dto.setDiscount(si.getDiscount());
                        return dto;
                    })
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("SaleItems fetched successfully");
            response.setSaleItemDTOList(saleItemDTOs);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching sale items: " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    public Response updateSaleItem(Long id, SaleItemDTO saleItemDto) {
        Response response = new Response();
        try {
            SaleItem existing = saleItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("SaleItem not found"));

            var newProduct = productRepository.findById(saleItemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            var oldProduct = existing.getProduct();

            // Validate qty
            if (saleItemDto.getQty() == null || saleItemDto.getQty() <= 0) {
                throw new OurException("Sale quantity must be greater than zero");
            }

            // If product changed, restore old product qty and check new product qty
            if (!oldProduct.getProductId().equals(newProduct.getProductId())) {
                // Restore old product qty
                oldProduct.setQty(oldProduct.getQty() + existing.getQty());
                productRepository.save(oldProduct);

                // Check new product qty
                if (newProduct.getQty() == null || newProduct.getQty() < saleItemDto.getQty()) {
                    throw new OurException("Insufficient product quantity");
                }
                // Deduct from new product
                newProduct.setQty(newProduct.getQty() - saleItemDto.getQty());
                productRepository.save(newProduct);

                existing.setProduct(newProduct);
            } else {
                // If product is same, adjust qty difference
                int qtyDiff = saleItemDto.getQty() - existing.getQty();
                if (qtyDiff > 0) {
                    if (newProduct.getQty() == null || newProduct.getQty() < qtyDiff) {
                        throw new OurException("Insufficient product quantity");
                    }
                    newProduct.setQty(newProduct.getQty() - qtyDiff);
                } else if (qtyDiff < 0) {
                    newProduct.setQty(newProduct.getQty() - qtyDiff); // since qtyDiff is negative, this adds back
                }
                productRepository.save(newProduct);
            }

            existing.setSale(saleRepository.findById(saleItemDto.getSaleId())
                    .orElseThrow(() -> new RuntimeException("Sale not found")));
            existing.setQty(saleItemDto.getQty());
            existing.setPrice(saleItemDto.getPrice());
            existing.setDiscount(saleItemDto.getDiscount());

            SaleItem saved = saleItemRepository.save(existing);

            SaleItemDTO dto = new SaleItemDTO();
            dto.setSaleItemId(saved.getSaleItemId());
            dto.setSaleId(saved.getSale().getSaleId());
            dto.setProductId(saved.getProduct().getProductId());
            dto.setQty(saved.getQty());
            dto.setPrice(saved.getPrice());
            dto.setDiscount(saved.getDiscount());

            response.setStatusCode(200);
            response.setMessage("SaleItem updated successfully");
            response.setSaleItemDTO(dto);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating sale item: " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    public Response deleteSaleItem(Long id) {
        Response response = new Response();
        try {
            SaleItem saleItem = saleItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("SaleItem not found"));

            // Restore product quantity when deleting sale item
            var product = saleItem.getProduct();
            if (product != null && saleItem.getQty() != null) {
                product.setQty(product.getQty() + saleItem.getQty());
                productRepository.save(product);
            }

            saleItemRepository.delete(saleItem);
            response.setStatusCode(200);
            response.setMessage("SaleItem deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting sale item: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getSaleItemsBySaleId(Long saleId) {
        Response response = new Response();
        try {
            List<SaleItemDTO> saleItemDTOs = saleItemRepository.findAll().stream()
                .filter(si -> si.getSale().getSaleId().equals(saleId))
                .map(si -> {
                    SaleItemDTO dto = new SaleItemDTO();
                    dto.setSaleItemId(si.getSaleItemId());
                    dto.setSaleId(si.getSale().getSaleId());
                    dto.setProductId(si.getProduct().getProductId());
                    dto.setQty(si.getQty());
                    dto.setPrice(si.getPrice());
                    dto.setDiscount(si.getDiscount());
                    return dto;
                })
                .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("SaleItems fetched successfully by saleId");
            response.setSaleItemDTOList(saleItemDTOs);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching sale items by saleId: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getSaleItemViewsBySaleId(Long saleId) {
        Response response = new Response();
        try {
            List<SaleItemViewDTO> saleItemViewDTOs = saleItemRepository.findAll().stream()
                .filter(si -> si.getSale().getSaleId().equals(saleId))
                .map(si -> {
                    SaleItemViewDTO dto = new SaleItemViewDTO();
                    dto.setSaleItemId(si.getSaleItemId());
                    dto.setSaleId(si.getSale().getSaleId());
                    dto.setProductName(si.getProduct().getProductName());
                    dto.setBarcode(si.getProduct().getBarcode());
                    dto.setQty(si.getQty());
                    dto.setPrice(si.getPrice());
                    dto.setTotalPrice(si.getTotalPrice());
                    dto.setDiscount(si.getDiscount());
                    return dto;
                })
                .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("SaleItems fetched successfully by saleId");
            // You may want to add a new field to Response for this DTO list, or reuse saleItemDTOList if type-safe
            response.setSaleItemDTOList((List) saleItemViewDTOs); // unchecked cast, adjust Response if needed
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching sale items by saleId: " + e.getMessage());
        }
        return response;
    }
}
