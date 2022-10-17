package com.resell.resell.domain.product;

import com.resell.resell.controller.dto.ProductDto;
import com.resell.resell.domain.BaseTimeEntity;
import com.resell.resell.domain.brand.Brand;
import com.resell.resell.domain.product.common.Currency;
import com.resell.resell.domain.product.common.SizeClassification;
import com.resell.resell.domain.product.common.SizeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static com.resell.resell.controller.dto.ProductDto.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String nameKor;

    private String nameEng;

    @Column(unique = true)
    private String modelNumber;

    private String color;

    private LocalDate releaseDate;

    private int releasePrice;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private SizeClassification sizeClassification;

    @Enumerated(EnumType.STRING)
    private SizeUnit sizeUnit;

    private double minSize;

    private double maxSize;

    private double sizeGap;

    private String originImagePath;

    private String thumbnailImagePath;

    private String resizedImagePath;

    @ManyToOne(optional = false)
    @JoinColumn(name = "BRAND_ID")
    private Brand brand;

    public ProductInfoResponse toProductInfoResponse() {

        return ProductInfoResponse.builder()
                .id(this.id)
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
                .resizedImagePath(this.resizedImagePath)
                .brand(brand.toBrandInfo())
                .build();
    }

}
