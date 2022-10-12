package com.resell.resell.service;

import com.resell.resell.common.utils.file.FileNameUtils;
import com.resell.resell.domain.product.ProductRepository;
import com.resell.resell.exception.product.DuplicateModelNumberException;
import com.resell.resell.service.storage.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

import static com.resell.resell.controller.dto.ProductDto.SaveRequest;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final AwsS3Service awsS3Service;

    @Transactional
    public void saveProduct(SaveRequest requestDto, @Nullable MultipartFile productImage) {
        if (productRepository.existsByModelNumber(requestDto.getModelNumber())) {
            throw new DuplicateModelNumberException();
        }
        if (productImage != null) {
            String originImagePath = awsS3Service.uploadProductImage(productImage);
            String thumbnailImagePath = FileNameUtils.toThumbnail(originImagePath);
            String resizedImagePath = FileNameUtils.toResized(originImagePath);
            requestDto.setImagePath(originImagePath, thumbnailImagePath, resizedImagePath);
        }
        productRepository.save(requestDto.toEntity());
    }

}
