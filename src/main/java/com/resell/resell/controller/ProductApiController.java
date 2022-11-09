package com.resell.resell.controller;

import com.resell.resell.common.annotation.LoginCheck;
import com.resell.resell.domain.product.common.OrderStandard;
import com.resell.resell.domain.users.common.UserLevel;
import com.resell.resell.service.BrandService;
import com.resell.resell.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.resell.resell.controller.dto.ProductDto.*;
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

    @GetMapping("/{id}")
    public ProductInfoResponse getProductInfo(@PathVariable Long id) {
        ProductInfoResponse productInfoResponse = productService.getProductInfo(id);
        return productInfoResponse;
    }

    @GetMapping
    public Page<ThumbnailResponse> getProductsThumbnail(SearchCondition condition, Pageable pageable) {
        return productService.findProducts(condition, pageable);
    }

    @GetMapping("/order-standards")
    public OrderStandard[] getProductOrderStandards() {
        return OrderStandard.values();
    }

    @LoginCheck(authority = UserLevel.ADMIN)
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public void updateProduct(@PathVariable Long id,
                              @Valid @RequestPart SaveRequest requestDto,
                              @RequestPart(required = false) MultipartFile productImage) {
        productService.updateProduct(id, requestDto, productImage);
    }

    @LoginCheck(authority = UserLevel.ADMIN)
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
