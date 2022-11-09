package com.resell.resell.controller;

import com.resell.resell.common.annotation.CurrentUser;
import com.resell.resell.common.annotation.LoginCheck;
import com.resell.resell.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.resell.resell.controller.dto.ProductDto.*;
import static com.resell.resell.controller.dto.ProductDto.IdRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("carts")
public class CartApiController {

    private final CartService cartService;

    // 장바구니에 상품 담기
    @LoginCheck
    @PostMapping
    public void addWishList(@CurrentUser String email, @RequestBody IdRequest idRequest) {
        cartService.addWishList(email, idRequest);
    }

    @LoginCheck
    @GetMapping
    public Set<WishItemResponse> getWishList(@CurrentUser String email) {
        return cartService.getWishList(email);
    }

    @LoginCheck
    @DeleteMapping
    public void deleteWishList(@RequestBody IdRequest idRequest) {
        cartService.deleteWishList(idRequest);
    }

}
