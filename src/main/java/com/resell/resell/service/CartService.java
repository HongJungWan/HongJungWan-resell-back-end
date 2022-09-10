package com.resell.resell.service;

import com.resell.resell.controller.dto.AddressDto;
import com.resell.resell.domain.cart.CartProduct;
import com.resell.resell.domain.cart.CartProductRepository;
import com.resell.resell.domain.product.Product;
import com.resell.resell.domain.product.ProductRepository;
import com.resell.resell.domain.users.user.User;
import com.resell.resell.domain.users.user.UserRepository;
import com.resell.resell.exception.user.DuplicateCartItemException;
import com.resell.resell.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.resell.resell.controller.dto.AddressDto.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;

    private final CartProductRepository cartProductRepository;

    private final ProductRepository productRepository;

    @Transactional
    public void addWishList(String email, IdRequest idRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));

        Product product = productRepository.findById(idRequest.getId()).orElseThrow();
        CartProduct cartItem = cartProductRepository.save(new CartProduct(user.getCart(), product));

        if (user.checkCartItemDuplicate(cartItem)) {
            throw new DuplicateCartItemException("장바구니 중복입니다.");
        }

        user.addCartItem(cartItem);
    }
}
