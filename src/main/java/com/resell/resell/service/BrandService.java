package com.resell.resell.service;

import com.resell.resell.common.utils.file.FileNameUtils;
import com.resell.resell.controller.dto.BrandDto;
import com.resell.resell.controller.dto.BrandDto.SaveRequest;
import com.resell.resell.domain.brand.Brand;
import com.resell.resell.domain.brand.BrandRepository;
import com.resell.resell.exception.brand.DuplicateBrandNameException;
import com.resell.resell.service.storage.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import static com.resell.resell.controller.dto.BrandDto.*;

@RequiredArgsConstructor
@Service
public class BrandService {

    private final BrandRepository brandRepository;

    private final AwsS3Service awsS3Service;

    @Transactional
    @CacheEvict(value = "brands", allEntries = true)
    public void saveBrand(SaveRequest requestDto, @Nullable MultipartFile brandImage) {
        if (checkDuplicateName(requestDto)) {
            throw new DuplicateBrandNameException();
        }
        if (brandImage != null) {
            String originImagePath = awsS3Service.uploadBrandImage(brandImage);
            String thumbnailImagePath = FileNameUtils.toThumbnail(originImagePath);
            requestDto.setImagePath(originImagePath, thumbnailImagePath);
        }
        brandRepository.save(requestDto.toEntity());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "brands")
    public List<BrandInfo> getBrandInfos() {
        return brandRepository.findAll().stream()
                .map(Brand::toBrandInfo)
                .collect(Collectors.toList());
    }

    private boolean checkDuplicateName(SaveRequest requestDto) {
        if (brandRepository.existsByNameKor(requestDto.getNameKor())) {
            return true;
        } else if (brandRepository.existsByNameEng(requestDto.getNameEng())) {
            return true;
        }
        return false;
    }

}
