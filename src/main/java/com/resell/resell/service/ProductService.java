package com.resell.resell.service;

import com.resell.resell.domain.product.ProductRepository;
import com.resell.resell.exception.product.DuplicateModelNumberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.resell.resell.controller.dto.ProductDto.SaveRequest;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void saveProduct(SaveRequest requestDto) {
        if (productRepository.existsByModelNumber(requestDto.getModelNumber())) {
            throw new DuplicateModelNumberException();
        }

        productRepository.save(requestDto.toEntity());
    }

}
