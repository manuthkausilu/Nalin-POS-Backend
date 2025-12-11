package com.shashi.possysytembackend.service;

import com.shashi.possysytembackend.dto.BrandDTO;
import com.shashi.possysytembackend.dto.Response;

import java.util.List;

public interface BrandService {
    Response createBrand(BrandDTO brandDto);
    Response getBrandById(Long id);
    Response getAllBrands();
    Response updateBrand(Long id, BrandDTO brandDto);
    Response deleteBrand(Long id);
}
