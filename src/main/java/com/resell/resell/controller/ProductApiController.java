package com.resell.resell.controller;

import com.resell.resell.common.annotation.LoginCheck;
import com.resell.resell.domain.users.common.UserLevel;
import com.resell.resell.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.resell.resell.controller.dto.ProductDto.SaveRequest;

@RequiredArgsConstructor
@RequestMapping("/products")
@RestController
public class ProductApiController {

    private final ProductService productService;

    @LoginCheck(authority = UserLevel.ADMIN)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createProduct(@Valid @RequestParam SaveRequest requestDto) {
        productService.saveProduct(requestDto);
    }

}
