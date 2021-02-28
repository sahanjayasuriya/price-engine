package com.sahan.priceengine.service.impl;

import com.sahan.priceengine.dto.ProductDto;
import com.sahan.priceengine.entity.Parameter;
import com.sahan.priceengine.entity.Product;
import com.sahan.priceengine.repository.ProductRepository;
import com.sahan.priceengine.service.ParameterService;
import com.sahan.priceengine.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sahan.priceengine.utility.AppConstant.DOUBLE_ZERO;

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
    public Double getPriceForProductAndQuantity(Long productId, Integer quantity) {
        Double price = DOUBLE_ZERO;
        Optional<Product> product = productRepository.findById(productId);
        Optional<Parameter> parameter = parameterService.getParameter();

        if (quantity <= 0) {
            log.error("Invalid Quantity");
            throw new IllegalArgumentException("Invalid quantity");
        }

        if (product.isEmpty()) {
            log.error("Invalid Product Id");
            throw new IllegalArgumentException("Invalid product id");
        }

        if (parameter.isEmpty()) {
            log.error("Parameters not found");
            throw new IllegalArgumentException("Parameters not found");
        }

        Double unitPrice = (product.get().getPrice() + (product.get().getPrice() * parameter.get().getLaborPercentage())) / product.get().getUnits();

        Integer cartonCount = quantity / product.get().getUnits();
        price += calculateCartonPrice(product.get().getPrice(), cartonCount, parameter.get());

        Integer unitCount = quantity % product.get().getUnits();
        price += unitCount * unitPrice;

        return price;
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
