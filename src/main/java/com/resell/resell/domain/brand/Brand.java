package com.resell.resell.domain.brand;

import com.resell.resell.controller.dto.BrandDto;
import com.resell.resell.controller.dto.BrandDto.BrandInfo;
import com.resell.resell.controller.dto.BrandDto.SaveRequest;
import com.resell.resell.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Brand extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nameKor;

    @Column(unique = true)
    private String nameEng;

    private String originImagePath;

    private String thumbnailImagePath;

    public BrandInfo toBrandInfo() {
        return BrandInfo.builder()
                .id(this.id)
                .nameKor(this.nameKor)
                .nameEng(this.nameEng)
                .originImagePath(this.originImagePath)
                .thumbnailImagePath(this.thumbnailImagePath)
                .build();
    }

    public void update(SaveRequest updatedBrand) {
        this.nameKor = updatedBrand.getNameKor();
        this.nameEng = updatedBrand.getNameEng();
        this.originImagePath = updatedBrand.getOriginImagePath();
        this.thumbnailImagePath = updatedBrand.getThumbnailImagePath();
    }

}
