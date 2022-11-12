package com.resell.resell.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resell.resell.controller.dto.BrandDto.BrandInfo;
import com.resell.resell.controller.dto.TradeDto.ProductInfoByTrade;
import com.resell.resell.controller.dto.TradeDto.TradeBidResponse;
import com.resell.resell.controller.dto.TradeDto.TradeResource;
import com.resell.resell.controller.dto.UserDto.TradeUserInfo;
import com.resell.resell.domain.addressBook.AddressBook;
import com.resell.resell.domain.users.common.Account;
import com.resell.resell.service.SessionLoginService;
import com.resell.resell.service.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(TradeApiController.class)
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
public class TradeApiControllerTest {

    @MockBean
    private TradeService tradeService;

    @MockBean
    private SessionLoginService sessionLoginService;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .apply(sharedHttpSession())
                .build();
    }

    private TradeResource createTradeResource() {
        TradeUserInfo tradeUserInfo = TradeUserInfo
                .builder()
                .account(new Account("카카오뱅크", "123456789", "홍정완"))
                .addressBook(new AddressBook().getAddressList())
                .build();

        TradeBidResponse immediatePurchasePrice = TradeBidResponse
                .builder()
                .tradeId(11L)
                .productId(1L)
                .productSize(260.0)
                .price(30000L)
                .build();

        TradeBidResponse immediateSalePrice = TradeBidResponse
                .builder()
                .tradeId(11L)
                .productId(1L)
                .productSize(260.0)
                .price(20000L)
                .build();

        ProductInfoByTrade productInfoByTrade = ProductInfoByTrade
                .builder()
                .id(1L)
                .nameKor("맥북 신발")
                .nameEng("Mac Shoe")
                .modelNumber("MM1391-123")
                .color("WHITE/BLACK")
                .brand(createBrandInfo())
                .immediatePurchasePrice(immediatePurchasePrice)
                .immediateSalePrice(immediateSalePrice)
                .build();

        return TradeResource
                .builder()
                .productInfoByTrade(productInfoByTrade)
                .tradeUserInfo(tradeUserInfo)
                .build();
    }

    private BrandInfo createBrandInfo() {
        return BrandInfo.builder()
                .nameKor("나이키")
                .nameEng("Nike")
                .originImagePath("https://hongjungwan-brands-origin-temp.s3.ap-northeast-2.amazonaws.com/9a9cec61-4647-4cf5-968b-cc0e9a946730.jpg")
                .thumbnailImagePath("https://hongjungwan-brands-thumbnail-temp.s3.ap-northeast-2.amazonaws.com/9a9cec61-4647-4cf5-968b-cc0e9a946730.jpg")
                .build();
    }

    @DisplayName("판매 또는 구매 입찰 시 필요한 리소스 리턴")
    @Test
    public void obtainResourceForBid() throws Exception {

        String email = "hong43ok@gmail.com";
        Long productId = 1L;
        double size = 260.0;

        TradeResource tradeResource = createTradeResource();

        given(tradeService.getResourceForBid(email, productId, size)).willReturn(tradeResource);

        mockMvc.perform(get("/trades/{productId}", productId)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("size", "260.0"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("trade/getResource",
                        pathParameters(
                                parameterWithName("productId").description("물품 ID[PK]")
                        ),
                        requestParameters(
                                parameterWithName("size").description("신발 사이즈")
                        )
                ));
    }

}