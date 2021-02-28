package com.sahan.priceengine.controller;

import com.sahan.priceengine.dto.ProductDto;
import com.sahan.priceengine.dto.ProductPriceDto;
import com.sahan.priceengine.entity.Product;
import com.sahan.priceengine.enums.ProductLabel;
import com.sahan.priceengine.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    private final double delta = 0.000001;

    @Autowired
    private ProductController productController;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        List<ProductDto> products = new ArrayList<>();
        ProductDto product1 = new ProductDto();
        product1.setId(1L);
        product1.setProductName("Penguin-ears");
        product1.setUnits(20);
        product1.setPrice(175.0);
        product1.setLabel(ProductLabel.RARE);

        ProductDto product2 = new ProductDto();
        product2.setId(1L);
        product2.setProductName("Horseshoe");
        product2.setUnits(5);
        product2.setPrice(825.0);
        product2.setLabel(ProductLabel.REGULAR);

        products.add(product1);
        products.add(product2);

        Mockito.when(productService.getProducts()).thenReturn(products);
        Mockito.when(productService.getPriceForProductAndQuantity(eq(2L), ArgumentMatchers.anyInt())).thenReturn(Double.valueOf(0));
        Mockito.when(productService.getPriceForProductAndQuantity(eq(2L), ArgumentMatchers.anyInt())).thenReturn(Double.valueOf(0));
        Mockito.when(productService.getPriceForProductAndQuantity(eq(3L), ArgumentMatchers.anyInt())).thenThrow(IllegalArgumentException.class);
        Mockito.when(productService.getPriceForProductAndQuantity(2L, -1)).thenThrow(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Get all products")
    void testGetAllProducts() {
        ResponseEntity<List<ProductDto>> products = productController.getAllProducts();
        Assertions.assertAll(
                () -> Assertions.assertEquals(products.getStatusCode(), HttpStatus.OK),
                () -> assertNotNull(products.getBody()),
                () -> Assertions.assertEquals(products.getBody().size(), 2)
        );
    }

    @Test
    @DisplayName("Get prices for 50 products")
    void testGetProductPrices() {
        ResponseEntity<List<ProductPriceDto>> productPrices = productController.getProductPrices(2L);
        Assertions.assertAll(
                () -> Assertions.assertEquals(productPrices.getStatusCode(), HttpStatus.OK),
                () -> assertNotNull(productPrices.getBody()),
                () -> Assertions.assertEquals(productPrices.getBody().size(), 50)
        );
    }

    @Test
    @DisplayName("Get prices for a product by product id and quantity")
    void testGetProductPriceByProductAndQuantity() {
        ResponseEntity<Double> price = productController.getProductPriceByProductAndQuantity(2L, 1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(price.getStatusCode(), HttpStatus.OK),
                () -> assertNotNull(price.getBody())
        );
    }

    @Test
    @DisplayName("Get prices for 50 products passing invalid product id")
    void testGetProductPricesPassingInvalidProductId() {
        ResponseEntity<List<ProductPriceDto>> productPrices = productController.getProductPrices(3L);
        Assertions.assertEquals(productPrices.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Get prices for a product passing invalid product id")
    void testGetProductPricePassingInvalidProductId() {
        ResponseEntity<Double> productPrices = productController.getProductPriceByProductAndQuantity(3L, 1);
        Assertions.assertEquals(productPrices.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Get prices for a product passing invalid quantity")
    void testGetProductPricePassingInvalidQuantity() {
        ResponseEntity<Double> productPrices = productController.getProductPriceByProductAndQuantity(2L, -1);
        Assertions.assertEquals(productPrices.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
