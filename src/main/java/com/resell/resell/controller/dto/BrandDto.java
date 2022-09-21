package com.resell.resell.controller.dto;

import com.resell.resell.domain.brand.Brand;
import lombok.*;

import javax.validation.constraints.NotBlank;

public class BrandDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SaveRequest {

        @NotBlank(message = "브랜드 한글명을 입력해주세요.")
        private String nameKor;
        @NotBlank(message = "브랜드 영문명을 입력해주세요.")
        private String nameEng;

        private String originImagePath;

        private String thumbnailImagePath;

        public void setImagePath(String originImagePath, String thumbnailImagePath) {
            this.originImagePath = originImagePath;
            this.thumbnailImagePath = thumbnailImagePath;
        }

        public Brand toEntity() {
            return Brand.builder()
                    .nameKor(this.nameKor)
                    .nameEng(this.nameEng)
                    .originImagePath(this.originImagePath)
                    .thumbnailImagePath(this.thumbnailImagePath)
                    .build();
        }
    }

}
