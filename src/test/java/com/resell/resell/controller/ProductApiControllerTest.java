package com.resell.resell.controller;

import static com.resell.resell.controller.dto.ProductDto.*;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import com.resell.resell.controller.ProductApiController;
import com.resell.resell.controller.dto.BrandDto;
import com.resell.resell.domain.product.common.Currency;
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
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
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
    public void setup(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .apply(sharedHttpSession())
                .build();
    }

    private BrandDto.BrandInfo createBrandInfo() {
        return BrandDto.BrandInfo.builder()
                .nameKor("테2")
                .nameEng("T342")
                .originImagePath("https://hongjungwan-brands-origin.s3.ap-northeast-2.amazonaws.com/052963df-4b8f-44a1-9569-a4864de89610.png")
                .thumbnailImagePath("https://hongjungwan-brands-thumbnail.s3.ap-northeast-2.amazonaws.com/052963df-4b8f-44a1-9569-a4864de89610.png")
                .build();
    }

    private SaveRequest createSaveRequest() {
        return SaveRequest.builder()
                .nameKor("테스트")
                .nameEng("Test")
                .modelNumber("DD1391-100")
                .color("WHITE/BLACK")
                .releaseDate(LocalDate.of(2022, 10, 17))
                .releasePrice(119000)
                .currency(Currency.KRW)
                .sizeClassification(SizeClassification.MENS)
                .sizeUnit(SizeUnit.MM)
                .minSize(240)
                .maxSize(320)
                .sizeGap(5)
                .brand(createBrandInfo())
                .originImagePath("https://hongjungwan-brands-origin.s3.ap-northeast-2.amazonaws.com/052963df-4b8f-44a1-9569-a4864de89610.png")
                .build();
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
    @DisplayName("상품 생성")
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

}
