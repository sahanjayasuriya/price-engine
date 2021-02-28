package com.sahan.priceengine.service.impl;

import com.sahan.priceengine.entity.Parameter;
import com.sahan.priceengine.repository.ParameterRepository;
import com.sahan.priceengine.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParameterServiceImpl implements ParameterService {

    private final ParameterRepository parameterRepository;

    @Autowired
    public ParameterServiceImpl(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    @Override
    public Optional<Parameter> getParameter() {
        return parameterRepository.findFirstBy();
    }

}
