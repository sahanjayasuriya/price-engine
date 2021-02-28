package com.sahan.priceengine.controller;

import com.sahan.priceengine.dto.ProductDto;
import com.sahan.priceengine.dto.ProductPriceDto;
import com.sahan.priceengine.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.sahan.priceengine.utility.AppUrl.*;
import static com.sahan.priceengine.utility.Util.generateArray;

@RestController
@RequestMapping(API_CONTEXT_PATH + PRODUCTS_API)
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    }

    @GetMapping(GET_PRODUCT_PRICE)
    public ResponseEntity<ProductPriceDto> getProductPrice(@RequestParam(name = "productId") Long productId, @RequestParam(name = "quantity", required = false, defaultValue = "1") Integer quantity) {
        return new ResponseEntity<>(productService.getPriceForProductAndQuantity(productId, new Integer[]{quantity}).get(0), HttpStatus.OK);
    }

    @GetMapping(GET_PRODUCT_PRICE_LIST)
    public ResponseEntity<List<ProductPriceDto>> getProductPriceList(@RequestParam(name = "productId") Long productId, @RequestParam(name = "limit", required = false, defaultValue = "50") Integer limit) {
        return new ResponseEntity<>(productService.getPriceForProductAndQuantity(productId, generateArray(limit)), HttpStatus.OK);
    }

}
