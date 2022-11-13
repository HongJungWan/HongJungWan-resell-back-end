package com.resell.resell.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resell.resell.service.BrandService;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.resell.resell.controller.dto.BrandDto.*;
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

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(BrandApiController.class)
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
class BrandApiControllerTest {

    @MockBean
    private BrandService brandService;

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

    private SaveRequest createSaveRequest() {
        return SaveRequest.builder()
                .nameKor("저장")
                .nameEng("Save Test")
                .build();
    }

    private SaveRequest createUpdateRequest() {
        return SaveRequest.builder()
                .nameKor("브랜드 업데이트")
                .nameEng("Brand Update Test")
                .originImagePath("https://hongjungwan-brands-origin-temp.s3.ap-northeast-2.amazonaws.com/9a9cec61-4647-4cf5-968b-cc0e9a946730.jpg")
                .thumbnailImagePath("https://hongjungwan-brands-thumbnail-temp.s3.ap-northeast-2.amazonaws.com/9a9cec61-4647-4cf5-968b-cc0e9a946730.jpg")
                .build();
    }

    private BrandInfo createBrandInfo() {
        return BrandInfo.builder()
                .id(1L)
                .nameKor("브랜드 생성")
                .nameEng("Brand Create Test")
                .originImagePath("https://hongjungwan-brands-origin-temp.s3.ap-northeast-2.amazonaws.com/9a9cec61-4647-4cf5-968b-cc0e9a946730.jpg")
                .thumbnailImagePath("https://hongjungwan-brands-thumbnail-temp.s3.ap-northeast-2.amazonaws.com/9a9cec61-4647-4cf5-968b-cc0e9a946730.jpg")
                .build();
    }

    private BrandInfo createAnotherBrandInfo() {
        return BrandInfo.builder()
                .id(2L)
                .nameKor("브랜드 생성2")
                .nameEng("Brand Create Test2")
                .originImagePath("https://hongjungwan-brands-origin-temp.s3.ap-northeast-2.amazonaws.com/9a9cec61-4647-4cf5-968b-cc0e9a946730.jpg")
                .thumbnailImagePath("https://hongjungwan-brands-thumbnail-temp.s3.ap-northeast-2.amazonaws.com/9a9cec61-4647-4cf5-968b-cc0e9a946730.jpg")
                .build();
    }

    private List<BrandInfo> createBrandInfos() {
        List<BrandInfo> brandInfos = new ArrayList<>();
        BrandInfo brandInfo = createBrandInfo();
        BrandInfo anotherBrandInfo = createAnotherBrandInfo();

        brandInfos.add(brandInfo);
        brandInfos.add(anotherBrandInfo);

        return brandInfos;
    }

    private MockMultipartFile createImageFile() {
        return new MockMultipartFile("brandImage", "brandImage", MediaType.IMAGE_PNG_VALUE, "sample".getBytes());
    }

    private MockMultipartFile convertMultipartFile(Object dto) throws JsonProcessingException {
        return new MockMultipartFile("requestDto", "requestDto", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(dto).getBytes(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("브랜드 등록")
    void createBrand() throws Exception {
        SaveRequest saveRequest = createSaveRequest();
        MockMultipartFile requestDto = convertMultipartFile(saveRequest);
        MockMultipartFile brandImage = createImageFile();

        doNothing().when(brandService).saveBrand(saveRequest, brandImage);

        mockMvc.perform(
                        multipart("/brands")
                                .file(requestDto)
                                .file(brandImage)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())

                .andDo(document("brands/create",
                        requestPartFields("requestDto",
                                fieldWithPath("nameKor").type(JsonFieldType.STRING).description("저장할 브랜드의 한글명"),
                                fieldWithPath("nameEng").type(JsonFieldType.STRING).description("저장할 브랜드의 영문명"),
                                fieldWithPath("originImagePath").ignored(),
                                fieldWithPath("thumbnailImagePath").ignored()),
                        requestParts(
                                partWithName("requestDto").ignored(),
                                partWithName("brandImage").description("저장할 브랜드의 이미지 파일").optional())
                ));

    }

    @Test
    @DisplayName("브랜드 조회 - ID")
    void getBrandInfos() throws Exception {
        BrandInfo brandInfo = createBrandInfo();
        Long id = brandInfo.getId();
        given(brandService.getBrandInfo(id)).willReturn(brandInfo);

        mockMvc.perform(
                        get("/brands/{id}", id)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("brands/get/details",
                        pathParameters(
                                parameterWithName("id").description("조회할 브랜드의 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("브랜드 ID"),
                                fieldWithPath("nameKor").type(JsonFieldType.STRING).description("브랜드 한글명"),
                                fieldWithPath("nameEng").type(JsonFieldType.STRING).description("브랜드 영문명"),
                                fieldWithPath("originImagePath").type(JsonFieldType.STRING).description("브랜드 원본 이미지 경로"),
                                fieldWithPath("thumbnailImagePath").type(JsonFieldType.STRING).description("브랜드 썸네일 이미지 경로")
                        )));

    }

    @Test
    @DisplayName("브랜드 전체 조회")
    void getBrandInfo() throws Exception {
        List<BrandInfo> brandInfos = createBrandInfos();
        FieldDescriptor[] brandInfo = new FieldDescriptor[]{
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("브랜드 ID"),
                fieldWithPath("nameKor").type(JsonFieldType.STRING).description("브랜드 한글명"),
                fieldWithPath("nameEng").type(JsonFieldType.STRING).description("브랜드 영문명"),
                fieldWithPath("originImagePath").type(JsonFieldType.STRING).description("브랜드 원본 이미지 경로"),
                fieldWithPath("thumbnailImagePath").type(JsonFieldType.STRING).description("브랜드 썸네일 이미지 경로")
        };
        given(brandService.getBrandInfos()).willReturn(brandInfos);

        mockMvc.perform(
                        get("/brands")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("brands/get",
                        responseFields(
                                fieldWithPath("[]").description("브랜드 정보 배열"))
                                .andWithPrefix("[].", brandInfo)
                ));
    }

    @Test
    @DisplayName("브랜드 수정")
    void updateBrand() throws Exception {
        Long id = 1L;
        SaveRequest updateRequest = createUpdateRequest();
        MockMultipartFile requestDto = convertMultipartFile(updateRequest);
        MockMultipartFile brandImage = createImageFile();

        MockMultipartHttpServletRequestBuilder builder =
                RestDocumentationRequestBuilders.fileUpload("/brands/{id}", id);
        builder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        mockMvc.perform(
                        builder
                                .file(requestDto)
                                .file(brandImage)
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("brands/update",
                        pathParameters(
                                parameterWithName("id").description("수정할 브랜드의 ID")
                        ),
                        requestPartFields("requestDto",
                                fieldWithPath("nameKor").type(JsonFieldType.STRING).description("수정할 브랜드의 한글명"),
                                fieldWithPath("nameEng").type(JsonFieldType.STRING).description("수정할 브랜드의 영문명"),
                                fieldWithPath("originImagePath").type(JsonFieldType.STRING).description("기존 브랜드 원본 이미지 경로(null이라면 기존 이미지 삭제)"),
                                fieldWithPath("thumbnailImagePath").ignored()),
                        requestParts(
                                partWithName("requestDto").ignored(),
                                partWithName("brandImage").description("수정할 브랜드의 이미지 파일").optional()
                        )));

    }


    @Test
    @DisplayName("브랜드 삭제")
    void deleteBrand() throws Exception {
        Long id = 1L;

        doNothing().when(brandService).deleteBrand(id);

        mockMvc.perform(
                        delete("/brands/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document(
                        "brands/delete",
                        pathParameters(
                                parameterWithName("id").description("삭제할 브랜드의 ID")
                        )
                ));
    }

}