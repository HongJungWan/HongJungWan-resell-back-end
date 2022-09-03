package com.resell.resell.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resell.resell.controller.dto.UserDto.*;
import com.resell.resell.domain.users.common.UserLevel;
import com.resell.resell.service.EmailCertificationService.EmailCertificationService;
import com.resell.resell.service.SessionLoginService;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static com.resell.resell.common.utils.constants.UserConstants.USER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;


@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(UserApiController.class)
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
class UserApiControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private SessionLoginService sessionLoginService;

    @MockBean
    private EmailCertificationService emailCertificationService;

    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .apply(sharedHttpSession())
                .build();
    }

    @Test
    @DisplayName("회원가입 -> 모든 유효성 검사 통과 > 회원가입 성공")
    void createUser_successful() throws Exception {
        SaveRequest saveRequest = SaveRequest.builder()
                .email("hjw43ok@hs.ac.kr")
                .password("test1234")
                .nickname("hjw43ok")
                .phone("01012345678")
                .build();

        doNothing().when(userService).save(saveRequest);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveRequest)))
                .andDo(print())
                .andExpect(status().isCreated())

                .andDo(document("users/create/successful", requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("The user's email address"),
                        fieldWithPath("password").type(JsonFieldType.STRING)
                                .description("The user's password"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING)
                                .description("The user's nickname"),
                        fieldWithPath("phone").type(JsonFieldType.STRING).description("The user's phone")
                )));
    }

    @Test
    @DisplayName("이메일 중복 검사 -> 중복 이메일")
    void DuplicateEmailCheck_failure() throws Exception {
        String email = "hjw43ok@hs.ac.kr";
        given(userService.checkEmailDuplicate(email)).willReturn(true);

        mockMvc.perform(get("/users/user-emails/{email}/exists", email))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))

                .andDo(document("users/duplicateEmail/failure",
                        pathParameters(
                                parameterWithName("email").description("이메일"))));
    }

    @Test
    @DisplayName("이메일 중복 검사 -> 사용 가능 이메일")
    void DuplicateEmailCheck_successful() throws Exception {
        String email = "hjw43ok@hs.ac.kr";
        given(userService.checkEmailDuplicate(email)).willReturn(false);

        ResultActions resultActions = mockMvc.perform(get("/users/user-emails/{email}/exists", email))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))

                .andDo(document("users/duplicateEmail/successful",
                        pathParameters(
                                parameterWithName("email").description("이메일"))));
    }

    @Test
    @DisplayName("닉네임 중복 검사 -> 중복 닉네임")
    void DuplicateNicknameCheck_failure() throws Exception {
        String nickname = "hjw43ok";
        given(userService.checkNicknameDuplicate(nickname)).willReturn(true);

        mockMvc.perform(get("/users/user-nicknames/{nickname}/exists", nickname))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))

                .andDo(document("users/duplicateNickname/failure",
                        pathParameters(
                                parameterWithName("nickname").description("닉네임"))));
    }

    @Test
    @DisplayName("닉네임 중복 검사 -> 사용 가능 닉네임")
    void DuplicateNicknameCheck_successful() throws Exception {
        String nickname = "hjw43ok";
        given(userService.checkNicknameDuplicate(nickname)).willReturn(false);

        mockMvc.perform(get("/users/user-nicknames/{nickname}/exists", nickname))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))

                .andDo(document("users/duplicateNickname/successful",
                        pathParameters(
                                parameterWithName("nickname").description("닉네임"))));
    }

    @Test
    @DisplayName("이메일 인증 -> 회원가입 시 이메일 발송 -> 토큰 링크 클릭 -> 인증 성공")
    void emailTokenCertification_successful() throws Exception {
        String token = UUID.randomUUID().toString();
        String email = "hjw43ok@hs.ac.kr";

        doNothing().when(userService).updateEmailVerified(token, email);

        mockMvc.perform(get("/users/email-check-token")
                        .param("token", token)
                        .param("email", email))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/emailAuth/successful", requestParameters(
                        parameterWithName("token").description("회원가입시 발송되는 랜덤 토큰"),
                        parameterWithName("email").description("회원가입시 입력한 이메일")
                )));
    }

    @Test
    @DisplayName("이메일 인증 토큰 -> 재전송")
    void resendEmailToken() throws Exception {

        String email = "hjw43ok@hs.ac.kr";
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(USER_ID, email);

        doNothing().when(emailCertificationService).sendEmailForEmailCheck(email);

        mockMvc.perform(post("/users/resend-email-token")
                        .session(session))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/emailAuth/resend"));
    }

    @Test
    @DisplayName("로그인 - 등록된 ID, PW 입력 -> 로그인 성공")
    void login_successful() throws Exception {
        LoginRequest requestDto = LoginRequest.builder()
                .email("hjw43ok@hs.ac.kr")
                .password("test1234")
                .build();

        doNothing().when(sessionLoginService).login(requestDto);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/login/successful", requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("login ID (email)"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("password")
                )));
    }

    @Test
    @DisplayName("로그아웃 - 로그아웃 성공")
    void logout() throws Exception {

        doNothing().when(sessionLoginService).logout();

        mockMvc.perform(delete("/users/logout"))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/logout"));
    }

    @Test
    @DisplayName("회원 정보 - 마이페이지(회원 정보) 리턴")
    void myPage() throws Exception {

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .email("hjw43ok@hs.ac.kr")
                .nickname("hjw43ok")
                .phone("01012345678")
                .userLevel(UserLevel.UNAUTH)
                .build();

        given(sessionLoginService.getCurrentUser(any())).willReturn(userInfoDto);

        mockMvc.perform(get("/users/myInfo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/myInfo",
                        responseFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("회원 휴대폰 번호"),
                                fieldWithPath("userLevel").type(JsonFieldType.STRING)
                                        .description("이메일 인증 여부")
                        )));
    }

    @Test
    @DisplayName("비밀번호 찾기 - 존재하는 email 입력 -> 리소스(email,phone) 리턴")
    void getUerResource_successful() throws Exception {
        FindUserResponse responseDto = FindUserResponse.builder()
                .email("hjw43ok@hs.ac.kr")
                .phone("01012345678")
                .build();

        String email = "hjw43ok@hs.ac.kr";

        given(userService.getUserResource(any())).willReturn(responseDto);

        mockMvc.perform(get("/users/find/{email}", email))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/forgetPassword/resource/successful", responseFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("회원 휴대폰 번호")
                        ),
                        pathParameters(
                                parameterWithName("email").description("이메일")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 찾기 -> email 인증 선택 -> [email] 인증번호 발송")
    void sendEmailCertification() throws Exception {
        EmailCertificationRequest requestDto = EmailCertificationRequest.builder()
                .email("hjw43ok@hs.ac.kr")
                .certificationNumber(null)
                .build();

        String email = requestDto.getEmail();

        doNothing().when(emailCertificationService).sendEmailForCertification(email);

        mockMvc.perform(post("/users/email-certification/sends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated())

                .andDo(document("users/certification/email/send", requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("인증번호를 받을 이메일"),
                        fieldWithPath("certificationNumber").ignored()
                )));
    }

}