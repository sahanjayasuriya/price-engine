package com.sahan.priceengine.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductPriceDto {
    private String productName;
    private Double price = 0.0;
    private Integer units;

}
