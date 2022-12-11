package com.resell.resell.controller;

import static com.resell.resell.controller.dto.ProductDto.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.resell.resell.controller.ProductApiController;
import com.resell.resell.controller.dto.BrandDto;
import com.resell.resell.controller.dto.BrandDto.BrandInfo;
import com.resell.resell.controller.dto.TradeDto;
import com.resell.resell.controller.dto.TradeDto.TradeBidResponse;
import com.resell.resell.controller.dto.TradeDto.TradeCompleteInfo;
import com.resell.resell.domain.product.common.Currency;
import com.resell.resell.domain.product.common.OrderStandard;
import com.resell.resell.domain.product.common.SizeClassification;
import com.resell.resell.domain.product.common.SizeUnit;
import com.resell.resell.service.BrandService;
import com.resell.resell.service.ProductService;
import com.resell.resell.service.SessionLoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(ProductApiController.class)
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
class ProductApiControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    BrandService brandService;

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

    private BrandInfo createBrandInfo() {
        return BrandInfo.builder()
                .nameKor("나이키")
                .nameEng("Nike")
                .originImagePath("https://hongjungwan-brands-origin-temp.s3.ap-northeast-2.amazonaws.com/9a9cec61-4647-4cf5-968b-cc0e9a946730.jpg")
                .thumbnailImagePath("https://hongjungwan-brands-thumbnail-temp.s3.ap-northeast-2.amazonaws.com/9a9cec61-4647-4cf5-968b-cc0e9a946730.jpg")
                .build();
    }

    private SaveRequest createSaveRequest() {
        return SaveRequest.builder()
                .nameKor("덩크 로우")
                .nameEng("Dunk Low")
                .modelNumber("DD1391-100")
                .color("WHITE/BLACK")
                .releaseDate(LocalDate.of(2022, 11, 12))
                .releasePrice(119000)
                .currency(Currency.KRW)
                .sizeClassification(SizeClassification.MENS)
                .sizeUnit(SizeUnit.MM)
                .minSize(240)
                .maxSize(320)
                .sizeGap(5)
                .brand(createBrandInfo())
                .originImagePath("https://hongjungwan-brands-origin-temp.s3.ap-northeast-2.amazonaws.com/9a9cec61-4647-4cf5-968b-cc0e9a946730.jpg")
                .build();
    }

    private ProductInfoResponse createProductInfo() {
        return ProductInfoResponse.builder()
                .id(1L)
                .nameKor("덩크 로우")
                .nameEng("Dunk Low")
                .modelNumber("DD1391-100")
                .color("WHITE/BLACK")
                .releaseDate(LocalDate.of(2022, 11, 12))
                .releasePrice(119000)
                .currency(Currency.KRW)
                .sizeClassification(SizeClassification.MENS)
                .sizeUnit(SizeUnit.MM)
                .minSize(240)
                .maxSize(320)
                .sizeGap(5)
                .brand(createBrandInfo())
                .resizedImagePath("https://hongjungwan-brands-origin-temp.s3.ap-northeast-2.amazonaws.com/9a9cec61-4647-4cf5-968b-cc0e9a946730.jpg")
                .purchaseBids(createPurchaseBids())
                .saleBids(createSales())
                .tradeCompleteInfos(createCompleteTrades())
                .build();
    }

    private List<TradeCompleteInfo> createCompleteTrades() {
        List<TradeCompleteInfo> list = new ArrayList<>();
        TradeCompleteInfo tradeCompleteInfo = TradeCompleteInfo.builder()
                .completeTime(LocalDateTime.now())
                .price(300000L)
                .productSize(280.0)
                .build();
        list.add(tradeCompleteInfo);
        return list;
    }

    private ThumbnailResponse createProductThumbnail() {
        return ThumbnailResponse.builder()
                .id(99L)
                .productThumbnailImagePath("https://hongjungwan-product-origin-temp.s3.ap-northeast-2.amazonaws.com/ec6e5843-8776-4c60-8132-6283b31c18c7.jpg")
                .brandThumbnailImagePath("https://hongjungwan-brands-origin-temp.s3.ap-northeast-2.amazonaws.com/9a9cec61-4647-4cf5-968b-cc0e9a946730.jpg")
                .nameKor("맥")
                .nameEng("Mac")
                .lowestPrice(500000L)
                .build();
    }

    private Pageable createPageable() {
        return PageRequest.of(0, 10);
    }

    private Page<ThumbnailResponse> createProductThumbnailsPage() {
        List<ThumbnailResponse> thumbnailList = new ArrayList<>();
        thumbnailList.add(createProductThumbnail());
        Pageable pageable = createPageable();

        return new PageImpl<>(thumbnailList, pageable, 1);
    }

    private SearchCondition createSearchCondition() {
        return SearchCondition.builder()
                .brandId(1L)
                .keyword("ka")
                .orderStandard(OrderStandard.LOW_PRICE).build();
    }

    private List<TradeBidResponse> createPurchaseBids() {

        List<TradeBidResponse> purchaseBids = new ArrayList<>();

        TradeBidResponse tradeBidResponse = TradeBidResponse.builder()
                .tradeId(5L)
                .productId(1L)
                .productSize(260.0)
                .price(300000L)
                .build();

        purchaseBids.add(tradeBidResponse);

        return purchaseBids;
    }

    private List<TradeBidResponse> createSales() {

        List<TradeBidResponse> saleBids = new ArrayList<>();

        TradeBidResponse tradeBidResponse = TradeBidResponse.builder()
                .tradeId(5L)
                .productId(1L)
                .productSize(260.0)
                .price(200000L)
                .build();

        saleBids.add(tradeBidResponse);

        return saleBids;
    }


    private MockMultipartFile createImageFile() {
        return new MockMultipartFile("productImage", "productImage", MediaType.IMAGE_PNG_VALUE,
                "sample".getBytes());
    }

    private MockMultipartFile convertMultipartFile(Object dto)
            throws JsonProcessingException {
        return new MockMultipartFile("requestDto", "requestDto", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(dto).getBytes(
                        StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("상품 등록")
    public void createProduct() throws Exception {
        SaveRequest saveRequest = createSaveRequest();
        MockMultipartFile requestDto = convertMultipartFile(saveRequest);
        MockMultipartFile productImage = createImageFile();

        doNothing().when(productService).saveProduct(saveRequest, productImage);

        mockMvc.perform(
                        multipart("/products")
                                .file(requestDto)
                                .file(productImage)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())

                .andDo(document("products/create",
                        requestPartFields("requestDto",
                                fieldWithPath("nameKor").type(JsonFieldType.STRING).description("저장할 상품의 한글명"),
                                fieldWithPath("nameEng").type(JsonFieldType.STRING).description("저장할 상품의 영문명"),
                                fieldWithPath("modelNumber").type(JsonFieldType.STRING)
                                        .description("저장할 상품의 모델 넘버"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("저장할 상품의 색상"),
                                fieldWithPath("releaseDate").type(JsonFieldType.STRING)
                                        .description("저장할 상품의 발매일"),
                                fieldWithPath("releasePrice").type(JsonFieldType.NUMBER)
                                        .description("저장할 상품의 발매가"),
                                fieldWithPath("currency").type(JsonFieldType.STRING)
                                        .description("저장할 상품의 발매 통화"),
                                fieldWithPath("sizeClassification").type(JsonFieldType.STRING)
                                        .description("저장할 상품의 사이즈 분류"),
                                fieldWithPath("sizeUnit").type(JsonFieldType.STRING)
                                        .description("저장할 상품의 사이즈 단위"),
                                fieldWithPath("minSize").type(JsonFieldType.NUMBER)
                                        .description("저장할 상품의 최소 사이즈"),
                                fieldWithPath("maxSize").type(JsonFieldType.NUMBER)
                                        .description("저장할 상품의 최대 사이즈"),
                                fieldWithPath("sizeGap").type(JsonFieldType.NUMBER)
                                        .description("저장할 상품의 사이즈 간격"),
                                fieldWithPath("brand").type(JsonFieldType.OBJECT).description("저장할 상품의 브랜드"),
                                fieldWithPath("brand.id").ignored(),
                                fieldWithPath("brand.nameKor").ignored(),
                                fieldWithPath("brand.nameEng").ignored(),
                                fieldWithPath("brand.originImagePath").ignored(),
                                fieldWithPath("brand.thumbnailImagePath").ignored(),
                                fieldWithPath("originImagePath").ignored(),
                                fieldWithPath("thumbnailImagePath").ignored(),
                                fieldWithPath("resizedImagePath").ignored()),
                        requestParts(
                                partWithName("requestDto").ignored(),
                                partWithName("productImage").description("저장할 상품의 이미지 파일").optional())
                ));
    }

    @Test
    @DisplayName("상품 상세 정보 조회 - ID")
    void getProductInfo() throws Exception {
        ProductInfoResponse productInfo = createProductInfo();
        Long id = productInfo.getId();
        given(productService.getProductInfo(id)).willReturn(productInfo);

        mockMvc.perform(
                        get("/products/{id}", id)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("products/get/details",
                        pathParameters(
                                parameterWithName("id").description("조회할 상품의 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("조회한 상품의 ID"),
                                fieldWithPath("nameKor").type(JsonFieldType.STRING).description("조회한 상품의 한글명"),
                                fieldWithPath("nameEng").type(JsonFieldType.STRING).description("조회한 상품의 영문명"),
                                fieldWithPath("modelNumber").type(JsonFieldType.STRING)
                                        .description("조회한 상품의 모델 넘버"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("조회한 상품의 색상"),
                                fieldWithPath("releaseDate").type(JsonFieldType.STRING)
                                        .description("조회한 상품의 출시일"),
                                fieldWithPath("releasePrice").type(JsonFieldType.NUMBER)
                                        .description("조회한 상품의 출시가"),
                                fieldWithPath("currency").type(JsonFieldType.STRING)
                                        .description("조회한 상품의 발매 통화"),
                                fieldWithPath("sizeClassification").type(JsonFieldType.STRING)
                                        .description("조회한 상품의 사이즈 분류"),
                                fieldWithPath("sizeUnit").type(JsonFieldType.STRING)
                                        .description("조회한 상품의 사이즈 단위"),
                                fieldWithPath("minSize").type(JsonFieldType.NUMBER)
                                        .description("조회한 상품의 최소 사이즈"),
                                fieldWithPath("maxSize").type(JsonFieldType.NUMBER)
                                        .description("조회한 상품의 최대 사이즈"),
                                fieldWithPath("sizeGap").type(JsonFieldType.NUMBER)
                                        .description("조회한 상품의 사이즈 간격"),
                                fieldWithPath("brand").type(JsonFieldType.OBJECT).description("조회한 상품의 브랜드"),
                                fieldWithPath("brand.id").ignored(),
                                fieldWithPath("brand.nameKor").ignored(),
                                fieldWithPath("brand.nameEng").ignored(),
                                fieldWithPath("brand.originImagePath").ignored(),
                                fieldWithPath("brand.thumbnailImagePath").ignored(),
                                fieldWithPath("resizedImagePath").type(JsonFieldType.STRING)
                                        .description("조회한 상품의 이미지 경로"),
                                fieldWithPath("saleBids.[].tradeId").type(JsonFieldType.NUMBER)
                                        .description("판매 입찰 ID"),
                                fieldWithPath("saleBids.[].productId").type(JsonFieldType.NUMBER)
                                        .description("판매 입찰 productID"),
                                fieldWithPath("saleBids.[].productSize").type(JsonFieldType.NUMBER)
                                        .description("판매 입찰 물품 사이즈"),
                                fieldWithPath("saleBids.[].price").type(JsonFieldType.NUMBER)
                                        .description("판매 입찰 물품 가격"),
                                fieldWithPath("purchaseBids.[].tradeId").type(JsonFieldType.NUMBER)
                                        .description("구매 입찰  ID"),
                                fieldWithPath("purchaseBids.[].productId").type(JsonFieldType.NUMBER)
                                        .description("구매 입찰  productId"),
                                fieldWithPath("purchaseBids.[].productSize").type(JsonFieldType.NUMBER)
                                        .description("구매 입찰 물품 사이즈"),
                                fieldWithPath("purchaseBids.[].price").type(JsonFieldType.NUMBER)
                                        .description("구매 입찰 물품 가격"),
                                fieldWithPath("tradeCompleteInfos.[].productSize").type(JsonFieldType.NUMBER)
                                        .description("완료된 거래의 물품 사이즈"),
                                fieldWithPath("tradeCompleteInfos.[].price").type(JsonFieldType.NUMBER)
                                        .description("완료된 거래의 물품 가격"),
                                fieldWithPath("tradeCompleteInfos.[].completeTime").type(JsonFieldType.STRING)
                                        .description("거래 완료 시간")
                        )
                ));
    }

    @Test
    @DisplayName("상품 삭제")
    void deleteProduct() throws Exception {
        Long id = 1L;

        doNothing().when(productService).deleteProduct(id);

        mockMvc.perform(
                        delete("/products/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document(
                        "products/delete",
                        pathParameters(
                                parameterWithName("id").description("삭제할 상품의 ID"))
                ));
    }

    @Test
    @DisplayName("상품 정렬 기준 조회")
    void getOrderStandard() throws Exception {
        mockMvc.perform(
                        get("/products/order-standards"))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document(
                        "products/get/order-standards",
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                        .description("상품의 정렬 기준 목록"))
                ));
    }

}
