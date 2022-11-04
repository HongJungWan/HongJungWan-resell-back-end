package com.resell.resell.domain.product;

import com.resell.resell.controller.dto.ProductDto;
import com.resell.resell.controller.dto.TradeDto;
import com.resell.resell.controller.dto.TradeDto.ProductInfoByTrade;
import com.resell.resell.controller.dto.TradeDto.TradeBidResponse;
import com.resell.resell.domain.BaseTimeEntity;
import com.resell.resell.domain.brand.Brand;
import com.resell.resell.domain.product.common.Currency;
import com.resell.resell.domain.product.common.SizeClassification;
import com.resell.resell.domain.product.common.SizeUnit;
import com.resell.resell.domain.trade.Trade;
import com.resell.resell.domain.trade.TradeStatus;
import com.resell.resell.domain.users.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

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

    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trade> trades = new ArrayList<>();

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

    public void update(SaveRequest updatedProduct) {
        this.nameKor = updatedProduct.getNameKor();
        this.nameEng = updatedProduct.getNameEng();
        this.modelNumber = updatedProduct.getModelNumber();
        this.color = updatedProduct.getColor();
        this.releaseDate = updatedProduct.getReleaseDate();
        this.releasePrice = updatedProduct.getReleasePrice();
        this.currency = updatedProduct.getCurrency();
        this.sizeClassification = updatedProduct.getSizeClassification();
        this.sizeUnit = updatedProduct.getSizeUnit();
        this.minSize = updatedProduct.getMinSize();
        this.maxSize = updatedProduct.getMaxSize();
        this.sizeGap = updatedProduct.getSizeGap();
        this.brand = updatedProduct.getBrand().toEntity();
        this.originImagePath = updatedProduct.getOriginImagePath();
        this.thumbnailImagePath = updatedProduct.getThumbnailImagePath();
        this.resizedImagePath = updatedProduct.getResizedImagePath();
    }

    private TradeBidResponse getLowestPrice(User currentUser, double size) {
        return trades.stream()
                .filter(lowestPriceFilter(currentUser, size))
                .sorted(Comparator.comparing(Trade::getPrice))
                .map(trade -> trade.toTradeBidResponse())
                .findFirst()
                .orElse(null);
    }

    private Predicate<Trade> lowestPriceFilter(User currentUser, double size) {
        return v -> v.getStatus() == TradeStatus.PRE_CONCLUSION && v.getBuyer() == null
                && v.getProductSize() == size && v.getPublisherId() != currentUser.getId();
    }

    private TradeBidResponse getHighestPrice(User currentUser, double size) {
        return trades.stream()
                .filter(highestPriceFilter(currentUser, size))
                .sorted(Comparator.comparing(Trade::getPrice).reversed())
                .map(trade -> trade.toTradeBidResponse())
                .findFirst()
                .orElse(null);
    }

    private Predicate<Trade> highestPriceFilter(User currentUser, double size) {
        return v -> v.getStatus() == TradeStatus.PRE_CONCLUSION && v.getSeller() == null
                && v.getProductSize() == size && v.getPublisherId() != currentUser.getId();
    }

    public ProductInfoByTrade toProductInfoByTrade(User currentUser, double size) {
        return ProductInfoByTrade.builder()
                .id(this.id)
                .nameKor(this.nameKor)
                .nameEng(this.nameEng)
                .modelNumber(this.modelNumber)
                .color(this.color)
                .brand(brand.toBrandInfo())
                .immediatePurchasePrice(getLowestPrice(currentUser, size))
                .immediateSalePrice(getHighestPrice(currentUser, size))
                .build();
    }

}
