package com.resell.resell.domain.cart;

import com.resell.resell.controller.dto.ProductDto.WishItemResponse;
import com.resell.resell.domain.product.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartProduct {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CART_ID")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Builder
    public CartProduct(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;
    }

    public Long getProductId() {
        return product.getId();
    }

    public WishItemResponse toWishItemDto() {
        return WishItemResponse.builder()
                .id(this.id)
                .productId(product.getId())
                .nameKor(product.getNameKor())
                .nameEng(product.getNameEng())
                .brand(product.getBrand())
                .build();
    }

}
