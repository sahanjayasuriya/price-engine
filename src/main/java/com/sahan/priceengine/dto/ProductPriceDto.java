package com.sahan.priceengine.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductPriceDto implements Serializable {

    private Double price;
    private Integer units;

}
