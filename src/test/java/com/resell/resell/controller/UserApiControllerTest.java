package com.resell.resell.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resell.resell.controller.dto.AddressDto;
import com.resell.resell.controller.dto.UserDto.*;
import com.resell.resell.domain.addressBook.Address;
import com.resell.resell.domain.users.common.Account;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.resell.resell.common.constants.UserConstants.USER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
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
    @DisplayName("회원가입 -> 모든 유효성 검사 통과 -> 회원가입 성공")
    void createUser_successful() throws Exception {
        SaveRequest saveRequest = SaveRequest.builder()
                .email("hong43ok@gmail.com")
                .password("test1234")
                .nickname("hong43ok")
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
        String email = "hong43ok@gmail.com";
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
        String email = "hong43ok@gmail.com";
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
        String nickname = "hong43ok";
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
        String nickname = "hong43ok";
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
        String email = "hong43ok@gmail.com";

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

        String email = "hong43ok@gmail.com";
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
                .email("hong43ok@gmail.com")
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
                .email("hong43ok@gmail.com")
                .nickname("hong43ok")
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
                .email("hong43ok@gmail.com")
                .phone("01012345678")
                .build();

        String email = "hong43ok@gmail.com";

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
                .email("hong43ok@gmail.com")
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

    @Test
    @DisplayName("비밀번호 찾기 -> 인증번호 일치 -> 이메일 인증 성공")
    void emailCertification_successful() throws Exception {
        EmailCertificationRequest requestDto = EmailCertificationRequest.builder()
                .email("hong43ok@gmail.com")
                .certificationNumber("123456")
                .build();

        doNothing().when(emailCertificationService).verifyEmail(requestDto);

        mockMvc.perform(post("/users/email-certification/confirms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/certification/email/successful", requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("비밀번호 찾기를 원하는 이메일"),
                        fieldWithPath("certificationNumber").type(JsonFieldType.STRING)
                                .description("사용자가 입력한 인증번호")
                )));
    }

    @Test
    @DisplayName("비밀번호 찾기 -> 인증이 완료 -> 비밀번호 변경")
    void changePasswordByForget() throws Exception {
        ChangePasswordRequest requestDto = ChangePasswordRequest.builder()
                .email("hong43ok@gmail.com")
                .passwordAfter("test12345")
                .passwordBefore(null)
                .build();

        doNothing().when(userService).updatePasswordByForget(requestDto);

        mockMvc.perform(patch("/users/forget/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/forgetPassword/updatePassword", requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("비밀번호를 변경할 회원 ID(email)"),
                        fieldWithPath("passwordAfter").type(JsonFieldType.STRING).description("변경할 비밀번호"),
                        fieldWithPath("passwordBefore").ignored()
                )))
        ;
    }

    @Test
    @DisplayName("비밀번호 변경 -> 이전 비밀번호 일치 -> 비밀번호 변경 성공")
    void changePassword_successful() throws Exception {
        ChangePasswordRequest requestDto = ChangePasswordRequest.builder()
                .email("hong43ok@gmail.com")
                .passwordAfter("test12345")
                .passwordBefore("newPassword1234")
                .build();

        String currentUer = "hong43ok@gmail.com";

        doNothing().when(userService).updatePassword(currentUer, requestDto);

        mockMvc.perform(patch("/users/password")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/changeUserInfo/password/successful", requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("비밀번호 변경을 원하는 회원 ID(email"),
                        fieldWithPath("passwordAfter").type(JsonFieldType.STRING).description("변경할 비밀번호"),
                        fieldWithPath("passwordBefore").type(JsonFieldType.STRING).description("이전 비밀번호")
                )));
    }

    @Test
    @DisplayName("환급 계좌 -> 환급 계좌 설정/변경")
    void changeAccount() throws Exception {
        Account account = new Account("국민", "1234", "홍정완");
        String currentUser = "hong43ok@gmail.com";

        doNothing().when(userService).updateAccount(currentUser, account);

        mockMvc.perform(patch("/users/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/changeUserInfo/account/change", requestFields(
                        fieldWithPath("bankName").type(JsonFieldType.STRING).description("은행명"),
                        fieldWithPath("accountNumber").type(JsonFieldType.STRING).description("계좌 번호"),
                        fieldWithPath("depositor").type(JsonFieldType.STRING).description("예금주")
                )));
    }

    @Test
    @DisplayName("환급 계좌 -> [USER] 환급 계좌 정보 리턴")
    void getAccountResource() throws Exception {
        Account account = new Account("국민", "1234", "홍정완");

        given(userService.getAccount(any())).willReturn(account);

        mockMvc.perform(get("/users/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/changeUserInfo/account/Resource",
                        responseFields(
                                fieldWithPath("bankName").type(JsonFieldType.STRING).description("은행명"),
                                fieldWithPath("accountNumber").type(JsonFieldType.STRING).description("계좌 번호"),
                                fieldWithPath("depositor").type(JsonFieldType.STRING).description("예금주")
                        )));
    }


    @Test
    @DisplayName("주소록 -> 주소를 추가")
    void addAddressBook() throws Exception {

        String currentUser = "hong43ok@gmail.com";
        AddressDto.SaveRequest requestDto = AddressDto.SaveRequest.builder()
                .id(1L)
                .addressName("회사")
                .roadNameAddress("회사로 123")
                .detailedAddress("456동 123호")
                .postalCode("23456")
                .build();

        doNothing().when(userService).addAddress(currentUser, requestDto);

        mockMvc.perform(post("/users/addressBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/changeUserInfo/addressBook/add/successful", requestFields(
                        fieldWithPath("id").ignored(),
                        fieldWithPath("addressName").type(JsonFieldType.STRING).description("주소록 이름"),
                        fieldWithPath("roadNameAddress").type(JsonFieldType.STRING).description("도로명 주소"),
                        fieldWithPath("detailedAddress").type(JsonFieldType.STRING).description("상세 주소"),
                        fieldWithPath("postalCode").type(JsonFieldType.STRING).description("우편번호")
                )));
    }

    @Test
    @DisplayName("주소록 -> 회원 주소록 정보 리턴")
    void getAddressBook() throws Exception {
        List<Address> addressList = new ArrayList<>();
        Address address = new Address(10L, "학교", "경기로", "111동 111호", "12345");
        addressList.add(address);
        given(userService.getAddressBook(any())).willReturn(addressList);

        mockMvc.perform(get("/users/addressBook")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/changeUserInfo/addressBook/Resource", responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("주소 ID[PK]"),
                        fieldWithPath("[].addressName").type(JsonFieldType.STRING).description("주소 이름"),
                        fieldWithPath("[].roadNameAddress").type(JsonFieldType.STRING)
                                .description("도로명 주소"),
                        fieldWithPath("[].detailedAddress").type(JsonFieldType.STRING).description("상세 주소"),
                        fieldWithPath("[].postalCode").type(JsonFieldType.STRING).description("우편 번호")
                )));
    }

    @Test
    @DisplayName("주소록 -> 주소 삭제")
    void deleteAddressBook() throws Exception {
        String currentUser = "hong43ok@gmail.com";
        AddressDto.IdRequest idRequest = AddressDto.IdRequest.builder()
                .id(2L)
                .build();

        doNothing().when(userService).deleteAddress(currentUser, idRequest);

        mockMvc.perform(delete("/users/addressBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(idRequest)))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/changeUserInfo/addressBook/delete", requestFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("삭제할 주소의 ID[PK]")
                )));
    }

    @Test
    @DisplayName("주소록 -> 주소 하나 수정")
    void updateAddressBook() throws Exception {
        AddressDto.SaveRequest requestDto = AddressDto.SaveRequest.builder()
                .id(1L)
                .addressName("학교")
                .roadNameAddress("경기로")
                .detailedAddress("111동 112호")
                .postalCode("12346")
                .build();

        doNothing().when(userService).updateAddress(requestDto);

        mockMvc.perform(patch("/users/addressBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("users/changeUserInfo/addressBook/update", requestFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
                        fieldWithPath("addressName").type(JsonFieldType.STRING).description("주소록 이름"),
                        fieldWithPath("roadNameAddress").type(JsonFieldType.STRING).description("도로명 주소"),
                        fieldWithPath("detailedAddress").type(JsonFieldType.STRING).description("상세 주소"),
                        fieldWithPath("postalCode").type(JsonFieldType.STRING).description("우편번호")
                )));
    }

}