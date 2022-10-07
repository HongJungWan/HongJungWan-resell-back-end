package com.resell.resell.controller.dto;

import com.resell.resell.domain.product.Product;
import com.resell.resell.domain.product.common.Currency;
import com.resell.resell.domain.product.common.SizeClassification;
import com.resell.resell.domain.product.common.SizeUnit;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

import static com.resell.resell.controller.dto.BrandDto.*;

public class ProductDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    @AllArgsConstructor
    public static class SaveRequest {

        @NotBlank(message = "제품 한글명을 입력해주세요.")
        private String nameKor;

        @NotBlank(message = "제품 영문명을 입력해주세요.")
        private String nameEng;

        @NotBlank(message = "모델 넘버를 입력해주세요.")
        private String modelNumber;

        @NotBlank(message = "색상을 입력해주세요.")
        private String color;

        @NotNull(message = "출시일을 입력해주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate releaseDate;

        @Positive(message = "올바른 출시가를 입력해주세요.")
        @NotNull(message = "출시가를 입력해주세요.")
        private int releasePrice;

        @NotNull(message = "출시가 통화를 선택해주세요.")
        private Currency currency;

        @NotNull(message = "사이즈 분류를 선택해주세요.")
        private SizeClassification sizeClassification;

        @NotNull(message = "사이즈의 단위를 선택해주세요.")
        private SizeUnit sizeUnit;

        @Positive(message = "올바른 최소 사이즈를 입력해주세요.")
        @NotNull(message = "최소 사이즈를 입력해주세요.")
        private double minSize;

        @Positive(message = "올바른 최대 사이즈를 입력해주세요.")
        @NotNull(message = "최대 사이즈를 입력해주세요.")
        private double maxSize;

        @Positive(message = "올바른 사이즈 간격을 입력해주세요.")
        @NotNull(message = "사이즈 간격을 입력해주세요.")
        private double sizeGap;

        @NotNull(message = "브랜드를 선택해주세요.")
        private BrandInfo brand;

        private String originImagePath;

        private String thumbnailImagePath;

        private String resizedImagePath;

        public Product toEntity() {
            return Product.builder()
                    .nameKor(this.nameKor)
                    .nameEng(this.nameEng)
                    .modelNumber(this.modelNumber)
                    .color(this.color)
                    .releaseDate(this.releaseDate)
                    .releasePrice(this.releasePrice)
                    .currency(this.currency)
                    .sizeClassification(this.sizeClassification)
                    .sizeUnit(this.sizeUnit)
                    .minSize(this.minSize)
                    .maxSize(this.maxSize)
                    .sizeGap(this.sizeGap)
                    .brand(this.brand.toEntity())
                    .originImagePath(this.originImagePath)
                    .thumbnailImagePath(this.thumbnailImagePath)
                    .resizedImagePath(this.resizedImagePath)
                    .build();
        }

        public void setImagePath(String originImagePath, String thumbnailImagePath,
                                 String resizedImagePath) {
            this.originImagePath = originImagePath;
            this.thumbnailImagePath = thumbnailImagePath;
            this.resizedImagePath = resizedImagePath;
        }

        public void deleteImagePath() {
            setImagePath(null, null, null);
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class WishItemResponse {

        private Long id;
        private Long productId;
        private String nameKor;
        private String nameEng;

        @Builder
        public WishItemResponse(Long id, Long productId, String nameKor, String nameEng) {
            this.id = id;
            this.productId = productId;
            this.nameKor = nameKor;
            this.nameEng = nameEng;
        }
    }

}
