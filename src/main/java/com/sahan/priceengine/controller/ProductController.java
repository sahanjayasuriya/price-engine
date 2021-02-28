package com.sahan.priceengine.controller;

import com.sahan.priceengine.dto.ProductDto;
import com.sahan.priceengine.dto.ProductPriceDto;
import com.sahan.priceengine.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.sahan.priceengine.utility.AppUrl.*;

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
    public ResponseEntity<? extends Object> getProductPrices(@RequestParam(name = "productId") Long productId, @RequestParam(name = "quantity", required = false) Integer quantity) {
        if(quantity != null) {
            return new ResponseEntity<>(productService.getPriceForProductAndQuantity(productId, quantity), HttpStatus.OK);
        }
        List<ProductPriceDto> productPrices = new ArrayList<>(50);
        for (int i = 1; i <= 50; i++) {
            ProductPriceDto productPrice = new ProductPriceDto();
            productPrice.setUnits(i);
            productPrice.setPrice(productService.getPriceForProductAndQuantity(productId, i).getPrice());
            productPrices.add(productPrice);
        }
        return new ResponseEntity<>(productPrices, HttpStatus.OK);
    }

}
