package com.sahan.priceengine.service.impl;

import com.sahan.priceengine.dto.ProductDto;
import com.sahan.priceengine.dto.ProductPriceDto;
import com.sahan.priceengine.entity.Parameter;
import com.sahan.priceengine.entity.Product;
import com.sahan.priceengine.enums.ProductLabel;
import com.sahan.priceengine.repository.ProductRepository;
import com.sahan.priceengine.service.ParameterService;
import com.sahan.priceengine.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class ProductServiceImplTest {

    private final double delta = 0.000001;

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ParameterService parameterService;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1L);
        product1.setProductName("Penguin-ears");
        product1.setUnits(20);
        product1.setPrice(175.0);
        product1.setLabel(ProductLabel.RARE);

        Product product2 = new Product();
        product2.setId(1L);
        product2.setProductName("Horseshoe");
        product2.setUnits(5);
        product2.setPrice(825.0);
        product2.setLabel(ProductLabel.REGULAR);

        products.add(product1);
        products.add(product2);

        Parameter parameter = new Parameter();
        parameter.setId(1L);
        parameter.setLaborPercentage(0.3);
        parameter.setCartonDiscount(0.1);
        parameter.setDiscountEligibleCartons(3);

        Mockito.when(productRepository.findAll()).thenReturn(products);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        Mockito.when(productRepository.findById(3L)).thenReturn(Optional.empty());

        Mockito.when(parameterService.getParameter()).thenReturn(Optional.of(parameter));

    }

    @Test
    @DisplayName("Get all products")
    void testGetProducts() {
        List<ProductDto> products = productService.getProducts();
        Assertions.assertAll(
                () -> Assertions.assertEquals(products.get(0).getProductName(), "Penguin-ears"),
                () -> Assertions.assertEquals(products.get(1).getProductName(), "Horseshoe")
        );
    }

    @Test
    @DisplayName("Get price of a product for a single unit")
    void testGetPriceForProductSingleQuantity() {
        ProductPriceDto price = productService.getPriceForProductAndQuantity(2L, 1);
        Assertions.assertEquals(price.getPrice(), 214.5, delta);
    }

    @Test
    @DisplayName("Get price of a product for single carton")
    void testGetPriceForProductSingleCarton() {
        ProductPriceDto price = productService.getPriceForProductAndQuantity(2L, 5);
        Assertions.assertEquals(price.getPrice(), 825.0, delta);
    }

    @Test
    @DisplayName("Get price of a product for single carton and two units")
    void testGetPriceForProductSingleCartonAndTwoUnits() {
        ProductPriceDto price = productService.getPriceForProductAndQuantity(2L, 7);
        Assertions.assertEquals(price.getPrice(), 1254.0, delta);
    }

    @Test
    @DisplayName("Get price of a product for discount eligible cartons")
    void testGetPriceForProductDiscountEligibleCartons() {
        ProductPriceDto price = productService.getPriceForProductAndQuantity(2L, 15);
        Assertions.assertEquals(price.getPrice(), 2227.5, delta);
    }

    @Test
    @DisplayName("Get price passing invalid product id")
    void testGetPricePassingInvalidProductId() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productService.getPriceForProductAndQuantity(3L, 1);
        });

        Assertions.assertEquals(exception.getMessage(), "Invalid product id");
    }

    @Test
    @DisplayName("Get price passing invalid quantity")
    void testGetPricePassingInvalidQuantity() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productService.getPriceForProductAndQuantity(2L, -1);
        });

        Assertions.assertEquals(exception.getMessage(), "Invalid quantity");
    }

    @Test
    @DisplayName("Get price passing invalid product id and quantity")
    void testGetPricePassingInvalidProductIdAndQuantity() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productService.getPriceForProductAndQuantity(3L, -1);
        });

        Assertions.assertEquals(exception.getMessage(), "Invalid quantity");
    }

}
