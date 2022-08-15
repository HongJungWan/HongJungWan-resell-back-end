package com.resell.resell.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resell.resell.exception.user.DuplicateEmailException;
import com.resell.resell.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.resell.resell.controller.dto.UserDto.SaveRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@WebMvcTest(UserApiController.class)
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
class UserApiControllerTest {

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
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
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원 가입 - 실패 (이메일 중복 or 닉네임 중복)")
    void createUser_failure() throws Exception {
        SaveRequest saveRequest = SaveRequest.builder()
                .email("HongJungWan@test.com")
                .password("test1234")
                .phone("01077777777")
                .nickname("GodHong")
                .build();

        doThrow(new DuplicateEmailException()).when(userService).save(any());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveRequest)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("이메일 중복 체크 -> 이메일 중복")
    void checkEmailDuplicate_failure() {
    }

    @Test
    @DisplayName("이메일 중복 체크 -> 사용 가능 이메일")
    void checkEmailDuplicate_successful() {
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