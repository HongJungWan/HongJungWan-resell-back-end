package com.resell.resell.domain.cart;

import com.resell.resell.domain.product.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * CART에 여러 PRODUCT를 담을 수 있다. PRODUCT 또한 여러 CART에 포함될 수 있다. 따라서 ManyToMany
 * ManyToMany는 실무에서 잘 사용하지 않는다. 한계가 명확하다.
 * 그렇기에 정규화를 통해 1:N , N:1로 처리한다.
 * CartProduct는 중간 테이블 역할
 */
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
    
}
