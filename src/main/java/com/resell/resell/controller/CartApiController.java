package com.resell.resell.controller;

import com.resell.resell.common.annotation.CurrentUser;
import com.resell.resell.common.annotation.LoginCheck;
import com.resell.resell.controller.dto.AddressDto;
import com.resell.resell.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.resell.resell.controller.dto.AddressDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("carts")
public class CartApiController {

    private final CartService cartService;

    @LoginCheck
    @PostMapping
    public void addWishList(@CurrentUser String email, @RequestBody IdRequest idRequest) {
        cartService.addWishList(email, idRequest);
    }

}
