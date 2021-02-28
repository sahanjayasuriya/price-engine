package com.sahan.priceengine.controller;

import com.sahan.priceengine.dto.ProductDto;
import com.sahan.priceengine.dto.ProductPriceDto;
import com.sahan.priceengine.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.sahan.priceengine.utility.AppUrl.*;

@RestController
@RequestMapping(API_CONTEXT_PATH + PRODUCT_API)
@Log4j2
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        try {
            return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(GET_PRODUCT_PRICE + "/{productId}")
    public ResponseEntity<List<ProductPriceDto>> getProductPrices(@PathVariable("productId") Long productId) {
        try {
            List<ProductPriceDto> productPrices = new ArrayList<>(50);
            for (int i = 1; i <= 50; i++) {
                ProductPriceDto productPrice = new ProductPriceDto();
                productPrice.setUnits(i);
                productPrice.setPrice(productService.getPriceForProductAndQuantity(productId, i));
                productPrices.add(productPrice);
            }
            return new ResponseEntity<>(productPrices, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(GET_PRODUCT_PRICE + "/{productId}/{quantity}")
    public ResponseEntity<Double> getProductPriceByProductAndQuantity(@PathVariable("productId") Long productId, @PathVariable("quantity") Integer quantity) {
        try {
            return new ResponseEntity<>(productService.getPriceForProductAndQuantity(productId, quantity), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }  catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
