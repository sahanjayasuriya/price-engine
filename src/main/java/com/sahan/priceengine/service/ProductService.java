package com.sahan.priceengine.service;

import com.sahan.priceengine.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> getProducts();

    Double getPriceForProductAndQuantity(Long productId, Integer quantity);

}
