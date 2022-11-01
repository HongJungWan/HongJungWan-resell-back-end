package com.resell.resell.controller.dto;

import com.resell.resell.controller.dto.UserDto.TradeUserInfo;
import lombok.*;

public class TradeDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TradeResource {

        private TradeUserInfo tradeUserInfo;
        
        private ProductInfoByTrade ProductInfoByTrade;

        @Builder
        public TradeResource(TradeUserInfo tradeUserInfo, ProductInfoByTrade productInfoByTrade) {
            this.tradeUserInfo = tradeUserInfo;
            this.ProductInfoByTrade = productInfoByTrade;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TradeBidResponse {

        private Long tradeId;

        private Long productId;

        private double productSize;

        private Long price;

        @Builder
        public TradeBidResponse(Long tradeId, Long productId, double productSize, Long price) {
            this.tradeId = tradeId;
            this.productId = productId;
            this.productSize = productSize;
            this.price = price;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class ProductInfoByTrade {

        private Long id;
        private String nameKor;
        private String nameEng;
        private String modelNumber;
        private String color;
        private BrandDto.BrandInfo brand;
        private TradeBidResponse immediatePurchasePrice;
        private TradeBidResponse immediateSalePrice;
    }

}