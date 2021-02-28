package com.sahan.priceengine.service;

import com.sahan.priceengine.dto.ProductDto;
import com.sahan.priceengine.dto.ProductPriceDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> getProducts();

    ProductPriceDto getPriceForProductAndQuantity(Long productId, Integer quantity);

}
