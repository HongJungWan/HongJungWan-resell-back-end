package com.resell.resell.domain.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "cart", orphanRemoval = true)
    private Set<CartProduct> wishList = new HashSet<>();

    public void addCartProducts(CartProduct cartItem) {
        wishList.add(cartItem);
    }

}
