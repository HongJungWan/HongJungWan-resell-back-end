package com.resell.resell.controller;

import com.resell.resell.common.annotation.LoginCheck;
import com.resell.resell.domain.users.common.UserLevel;
import com.resell.resell.service.BrandService;
import com.resell.resell.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.resell.resell.controller.dto.ProductDto.SaveRequest;

@RequiredArgsConstructor
@RequestMapping("/products")
@RestController
public class ProductApiController {

    private final ProductService productService;

    private final BrandService brandService;

    @LoginCheck(authority = UserLevel.ADMIN)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createProduct(@Valid @RequestPart SaveRequest requestDto, @RequestPart(required = false) MultipartFile productImage) {
        brandService.checkBrandExist(requestDto.getBrand());
        productService.saveProduct(requestDto, productImage);
    }
}
