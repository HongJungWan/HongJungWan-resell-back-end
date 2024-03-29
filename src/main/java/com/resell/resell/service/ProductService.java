package com.resell.resell.service;

import com.resell.resell.common.utils.file.FileNameUtils;
import com.resell.resell.controller.dto.ProductDto;
import com.resell.resell.domain.product.Product;
import com.resell.resell.domain.product.ProductRepository;
import com.resell.resell.exception.product.DuplicateModelNumberException;
import com.resell.resell.exception.product.ProductNotFoundException;
import com.resell.resell.service.storage.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

import static com.resell.resell.controller.dto.ProductDto.*;
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

    @Transactional(readOnly = true)
    @Cacheable(value = "product", key = "#id")
    public ProductInfoResponse getProductInfo(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다.")).toProductInfoResponse();
    }

    @Transactional(readOnly = true)
    public Page<ThumbnailResponse> findProducts(SearchCondition condition, Pageable pageable) {
        return productRepository.findAllBySearchCondition(condition, pageable);
    }

    @CacheEvict(value = "product", key = "#id")
    @Transactional
    public void updateProduct(Long id, SaveRequest updatedProduct, @Nullable MultipartFile productImage) {
        Product savedProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다."));
        String savedImagePath = savedProduct.getOriginImagePath();
        String updatedImagePath = updatedProduct.getOriginImagePath();

        checkDuplicateUpdatedModelNumber(savedProduct.getModelNumber(),
                updatedProduct.getModelNumber());

        if (isDeleteSavedImage(savedImagePath, updatedImagePath, productImage)) {
            String key = FileNameUtils.getFileName(savedImagePath);
            awsS3Service.deleteProductImage(key);
            updatedProduct.deleteImagePath();
        }

        if (productImage != null) {
            String originImagePath = awsS3Service.uploadProductImage(productImage);
            String thumbnailImagePath = FileNameUtils.toThumbnail(originImagePath);
            String resizedImagePath = FileNameUtils.toResized(originImagePath);
            updatedProduct.setImagePath(originImagePath, thumbnailImagePath, resizedImagePath);
        }

        savedProduct.update(updatedProduct);
    }

    @CacheEvict(value = "product", key = "#id")
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다."));
        String path = product.getOriginImagePath();

        productRepository.deleteById(id);

        if (path != null) {
            String key = FileNameUtils.getFileName(path);
            awsS3Service.deleteProductImage(key);
        }
    }

    private void checkDuplicateUpdatedModelNumber(String modelNumber, String updatedModelNumber) {
        if (modelNumber.equals(updatedModelNumber)) {
            return;
        } else if (!productRepository.existsByModelNumber(updatedModelNumber)) {
            return;
        }
        throw new DuplicateModelNumberException();
    }

    private boolean isDeleteSavedImage(String savedImagePath, String updatedImagePath, MultipartFile productImage) {
        return ((updatedImagePath == null && savedImagePath != null) ||
                (savedImagePath != null && productImage != null));
    }

}
