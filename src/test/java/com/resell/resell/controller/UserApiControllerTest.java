package com.resell.resell.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.resell.resell.service.UserService;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.resell.resell.controller.dto.UserDto.SaveRequest;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;


@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(UserApiController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserApiControllerTest {

    @MockBean
    private UserService userService;

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

    @Test
    @DisplayName("회원 가입 - 성공")
    void createUser_successful() throws Exception {
        SaveRequest saveRequest = SaveRequest.builder()
                .email("HongJungWan@test.com")
                .password("test1234")
                .phone("01077777777")
                .nickname("GodHong")
                .build();

        doNothing().when(userService).save(saveRequest);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveRequest)))
                .andDo(print())
                .andExpect(status().isCreated())

                .andDo(document("users/create/successful", requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("user's email address"),
                        fieldWithPath("password").type(JsonFieldType.STRING)
                                .description("user's password"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING)
                                .description("user's nickname"),
                        fieldWithPath("phone").type(JsonFieldType.STRING).description("user's phone")
                )));
    }

    @Test
    @DisplayName("회원가입 -> 중복 닉네임 or 중복 이메일 -> 이메일 회원가입 실패")
    void createUser_failure() throws Exception {
    }

    @Test
    @DisplayName("이메일 중복 체크 -> 이메일 중복")
    void checkEmailDuplicate_failure() throws Exception {
        String email = "HongJungWan@test.com";
        given(userService.checkEmailDuplicate(email)).willReturn(true);

        mockMvc.perform(get("/users/user-emails/{email}/exists", email))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(document("users/duplicateEmail/failure",
                        pathParameters(
                                parameterWithName("email").description("이메일"))));
    }

    @Test
    @DisplayName("이메일 중복 체크 -> 사용 가능 이메일")
    void checkEmailDuplicate_successful() throws Exception {
        String email = "HongJungWan@test.com";
        given(userService.checkEmailDuplicate(email)).willReturn(false);

        mockMvc.perform(get("/users/user-emails/{email}/exists", email))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andDo(document("users/duplicateEmail/successful",
                        pathParameters(
                                parameterWithName("email").description("이메일"))));
    }

    @Test
    @DisplayName("닉네임 중복 체크 -> 닉네임 중복")
    void checkNicknameDuplicate_failure() {
    }

    @Test
    @DisplayName("닉네임 중복 체크 -> 사용 가능 닉네임")
    void checkNicknameDuplicate_successful() {
    }

}