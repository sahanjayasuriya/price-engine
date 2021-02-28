package com.sahan.priceengine.service.impl;

import com.sahan.priceengine.dto.ProductDto;
import com.sahan.priceengine.entity.Parameter;
import com.sahan.priceengine.repository.ParameterRepository;
import com.sahan.priceengine.service.ParameterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class ParameterServiceImplTest {

    private final double delta = 0.000001;

    @Autowired
    private ParameterService parameterService;

    @MockBean
    private ParameterRepository parameterRepository;

    @BeforeEach
    void setUp() {
        Parameter parameter = new Parameter();
        parameter.setId(1L);
        parameter.setLaborPercentage(0.3);
        parameter.setCartonDiscount(0.1);
        parameter.setDiscountEligibleCartons(3);

        Mockito.when(parameterRepository.findFirstBy()).thenReturn(java.util.Optional.of(parameter));
    }

    @Test
    @DisplayName("Get parameters")
    void testGetParameters() {
        Optional<Parameter> parameter = parameterService.getParameter();
        Assertions.assertAll(
                () -> Assertions.assertEquals(parameter.get().getLaborPercentage(), 0.3, delta),
                () -> Assertions.assertEquals(parameter.get().getDiscountEligibleCartons().intValue(), 3),
                () -> Assertions.assertEquals(parameter.get().getCartonDiscount(), 0.1, delta)
        );
    }

}
