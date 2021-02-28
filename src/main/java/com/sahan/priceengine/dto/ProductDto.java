package com.sahan.priceengine.dto;

import com.sahan.priceengine.enums.ProductLabel;
import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String productName;
    private Double price;
    private Integer units;
    private ProductLabel label;
}
