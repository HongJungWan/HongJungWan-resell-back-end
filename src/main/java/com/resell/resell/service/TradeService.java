package com.resell.resell.service;

import com.resell.resell.controller.dto.TradeDto;
import com.resell.resell.controller.dto.TradeDto.ProductInfoByTrade;
import com.resell.resell.controller.dto.TradeDto.TradeResource;
import com.resell.resell.controller.dto.UserDto;
import com.resell.resell.controller.dto.UserDto.TradeUserInfo;
import com.resell.resell.domain.product.Product;
import com.resell.resell.domain.product.ProductRepository;
import com.resell.resell.domain.users.user.User;
import com.resell.resell.domain.users.user.UserRepository;
import com.resell.resell.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public TradeResource getResourceForBid(String email, Long productId, double size) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));
        Product product = productRepository.findById(productId).orElseThrow();

        return makeTradeResource(user, product, size);
    }

    private TradeResource makeTradeResource(User user, Product product, double size) {
        ProductInfoByTrade productInfoByTrade = product.toProductInfoByTrade(user, size);
        TradeUserInfo tradeUserInfo = user.createTradeUserInfo();
        
        return TradeResource.builder()
                .tradeUserInfo(tradeUserInfo)
                .productInfoByTrade(productInfoByTrade)
                .build();
    }

}