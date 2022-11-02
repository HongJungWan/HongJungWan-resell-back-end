package com.resell.resell.controller;

import com.resell.resell.common.annotation.CurrentUser;
import com.resell.resell.common.annotation.LoginCheck;
import com.resell.resell.controller.dto.TradeDto;
import com.resell.resell.controller.dto.TradeDto.TradeResource;
import com.resell.resell.domain.users.common.UserLevel;
import com.resell.resell.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradeApiController {

    private final TradeService tradeService;

    @LoginCheck(authority = UserLevel.AUTH)
    @GetMapping("/{productId}")
    public TradeResource obtainResourceForBid(@CurrentUser String email, @PathVariable Long productId, double size) {
        TradeResource resourceForBid = tradeService.getResourceForBid(email, productId, size);
        return resourceForBid;
    }

}