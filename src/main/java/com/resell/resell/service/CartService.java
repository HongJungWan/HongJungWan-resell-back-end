package com.resell.resell.service;

import com.resell.resell.domain.cart.CartProduct;
import com.resell.resell.domain.cart.CartProductRepository;
import com.resell.resell.domain.cart.CartRepository;
import com.resell.resell.domain.product.Product;
import com.resell.resell.domain.product.ProductRepository;
import com.resell.resell.domain.users.user.User;
import com.resell.resell.domain.users.user.UserRepository;
import com.resell.resell.exception.product.ProductNotFoundException;
import com.resell.resell.exception.user.DuplicateCartItemException;
import com.resell.resell.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.resell.resell.controller.dto.AddressDto.*;
import static com.resell.resell.controller.dto.ProductDto.*;
import static com.resell.resell.controller.dto.ProductDto.IdRequest;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;

    private final CartProductRepository cartProductRepository;

    private final ProductRepository productRepository;

    @Transactional
    public void addWishList(String email, IdRequest idRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));

        Product product = productRepository.findById(idRequest.getId()).orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다."));

        CartProduct cartProduct = new CartProduct(user.getCart(), product);
        // cart가 null 이라.. JIRA에 올리기
        CartProduct cartItem = cartProductRepository.save(cartProduct);

        if (user.checkCartItemDuplicate(cartItem)) {
            throw new DuplicateCartItemException("장바구니에 중복 상품을 추가했습니다.");
        }

        user.addCartItem(cartItem);
    }

    @Transactional(readOnly = true)
    public Set<WishItemResponse> getWishList(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));

        return user.getWishList();
    }

    @Transactional
    public void deleteWishList(IdRequest idRequest) {
        cartProductRepository.deleteById(idRequest.getId());
    }

}
