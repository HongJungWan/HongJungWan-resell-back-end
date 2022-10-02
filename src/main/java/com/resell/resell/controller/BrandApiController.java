package com.resell.resell.controller;

import com.resell.resell.common.annotation.LoginCheck;
import com.resell.resell.controller.dto.BrandDto;
import com.resell.resell.controller.dto.BrandDto.SaveRequest;
import com.resell.resell.domain.users.common.UserLevel;
import com.resell.resell.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static com.resell.resell.controller.dto.BrandDto.*;

@RequiredArgsConstructor
@RequestMapping("/brands")
@RestController
public class BrandApiController {

    private final BrandService brandService;

    @LoginCheck(authority = UserLevel.ADMIN)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createBrand(@Valid @RequestPart SaveRequest requestDto, @RequestPart(required = false) MultipartFile brandImage) {
        brandService.saveBrand(requestDto, brandImage);

    }

    @GetMapping
    public List<BrandInfo> getBrandInfos() {
        return brandService.getBrandInfos();
    }

    @LoginCheck(authority = UserLevel.ADMIN)
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
    }

    @LoginCheck(authority = UserLevel.ADMIN)
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public void updateBrand(@PathVariable Long id,
                            @Valid @RequestPart SaveRequest requestDto,
                            @RequestPart(required = false) MultipartFile brandImage) {
        brandService.updateBrand(id, requestDto, brandImage);
    }

}
