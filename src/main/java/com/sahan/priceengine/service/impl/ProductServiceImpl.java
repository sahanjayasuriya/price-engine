package com.sahan.priceengine.service.impl;

import com.sahan.priceengine.dto.ProductDto;
import com.sahan.priceengine.dto.ProductPriceDto;
import com.sahan.priceengine.entity.Parameter;
import com.sahan.priceengine.entity.Product;
import com.sahan.priceengine.repository.ProductRepository;
import com.sahan.priceengine.service.ParameterService;
import com.sahan.priceengine.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ParameterService parameterService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ParameterService parameterService) {
        this.productRepository = productRepository;
        this.parameterService = parameterService;
    }

    @Override
    public List<ProductDto> getProducts() {
        return productRepository.findAll().stream().map(this::copyProperties).collect(Collectors.toList());
    }

    @Override
    public List<ProductPriceDto> getPriceForProductAndQuantity(Long productId, Integer[] quantity) {
        List<ProductPriceDto> productPriceDtos = new ArrayList<>(quantity.length);
        Optional<Product> product = productRepository.findById(productId);
        Optional<Parameter> parameter = parameterService.getParameter();

        if (product.isEmpty()) {
            log.error("Invalid Product Id");
            throw new IllegalArgumentException("Invalid product id");
        }

        if (parameter.isEmpty()) {
            log.error("Parameters not found");
            throw new IllegalArgumentException("Parameters not found");
        }

        for (Integer i : quantity) {
            if (i <= 0) {
                log.error("Invalid Quantity");
                throw new IllegalArgumentException("Invalid quantity");
            }
            productPriceDtos.add(calculatePrice(product.get(), parameter.get(), i));
        }

        return productPriceDtos;
    }

    private ProductPriceDto calculatePrice(Product product, Parameter parameter, Integer quantity) {
        ProductPriceDto productPriceDto = new ProductPriceDto();

        Double unitPrice = (product.getPrice() + (product.getPrice() * parameter.getLaborPercentage())) / product.getUnits();

        Integer cartonCount = quantity / product.getUnits();
        productPriceDto.setPrice(productPriceDto.getPrice() + calculateCartonPrice(product.getPrice(), cartonCount, parameter));

        Integer unitCount = quantity % product.getUnits();
        productPriceDto.setPrice(productPriceDto.getPrice() + unitCount * unitPrice);

        productPriceDto.setProductName(product.getProductName());
        productPriceDto.setUnits(quantity);
        return productPriceDto;
    }

    private ProductDto copyProperties(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        return productDto;
    }

    private Double calculateCartonPrice(Double price, Integer cartonCount, Parameter parameter) {
        return cartonCount < parameter.getDiscountEligibleCartons() ?
                cartonCount * price :
                cartonCount * (price - price * parameter.getCartonDiscount());
    }
}
