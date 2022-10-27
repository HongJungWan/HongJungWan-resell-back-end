package com.resell.resell.domain.product;

import com.resell.resell.controller.dto.ProductDto;
import com.resell.resell.controller.dto.ProductDto.ThumbnailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchProductRepository {

    Page<ThumbnailResponse> findAllBySearchCondition(ProductDto.SearchCondition condition, Pageable pageable);

}