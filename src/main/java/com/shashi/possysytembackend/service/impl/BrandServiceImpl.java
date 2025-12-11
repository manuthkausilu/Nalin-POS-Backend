package com.shashi.possysytembackend.service.impl;

import com.shashi.possysytembackend.dto.BrandDTO;
import com.shashi.possysytembackend.dto.Response;
import com.shashi.possysytembackend.entity.Brand;
import com.shashi.possysytembackend.exception.OurException;
import com.shashi.possysytembackend.repository.BrandRepository;
import com.shashi.possysytembackend.service.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

    public BrandServiceImpl(BrandRepository brandRepository, ModelMapper modelMapper) {
        this.brandRepository = brandRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response createBrand(BrandDTO brandDTO) {
        Response response = new Response();
        try {
            // Unique brand name check
            if (brandDTO.getBrandName() != null && brandRepository.findAll().stream()
                    .anyMatch(b -> brandDTO.getBrandName().equalsIgnoreCase(b.getBrandName()))) {
                throw new OurException("Brand name already exists");
            }
            Brand brand = modelMapper.map(brandDTO, Brand.class);
            brandRepository.save(brand);
            response.setStatusCode(201);
            response.setMessage("Brand created successfully");
            response.setBrandDTO(modelMapper.map(brand, BrandDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error creating brand: " + e.getMessage());
        }
        return  response;
    }

    @Override
    public Response getBrandById(Long id) {
        Response response = new Response();
        try {
            Brand brand = brandRepository.findById(id).orElseThrow(() -> new OurException("Brand not found"));
            response.setStatusCode(200);
            response.setMessage("Brand fetched successfully");
            response.setBrandDTO(modelMapper.map(brand, BrandDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetch brand: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBrands() {
        Response response = new Response();
        try {
            List<Brand> brands = brandRepository.findAll();
            List<BrandDTO> brandDTOs = brands.stream()
                    .map(brand -> modelMapper.map(brand, BrandDTO.class))
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Brands fetched successfully");
            response.setBrandDTOList(brandDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching brands: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateBrand(Long id, BrandDTO brandDto) {
        Response response = new Response();
        try {
            Brand existingBrand = brandRepository.findById(id).orElseThrow(() -> new OurException("Brand not found"));
            // Unique brand name check (ignore current brand)
            if (brandDto.getBrandName() != null && brandRepository.findAll().stream()
                    .anyMatch(b -> brandDto.getBrandName().equalsIgnoreCase(b.getBrandName()) && !b.getBrandId().equals(id))) {
                throw new OurException("Brand name already exists");
            }
            existingBrand.setBrandName(brandDto.getBrandName());
            Brand savedBrand = brandRepository.save(existingBrand);
            response.setStatusCode(200);
            response.setMessage("Brand updated successfully");
            response.setBrandDTO(modelMapper.map(savedBrand, BrandDTO.class));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating brand: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteBrand(Long id) {
        Response response = new Response();
        try {
            Brand brand = brandRepository.findById(id).orElseThrow(() -> new OurException("Brand not found"));
            brandRepository.delete(brand);
            response.setStatusCode(200);
            response.setMessage("Brand deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting brand: " + e.getMessage());
        }
        return response;
    }
}
