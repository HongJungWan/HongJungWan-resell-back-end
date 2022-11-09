package com.resell.resell.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resell.resell.controller.dto.UserDto;
import com.resell.resell.controller.dto.UserDto.UserBanRequest;
import com.resell.resell.controller.dto.UserDto.UserListResponse;
import com.resell.resell.domain.users.common.UserLevel;
import com.resell.resell.domain.users.common.UserStatus;
import com.resell.resell.service.AdminService;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(AdminApiController.class)
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
public class AdminApiControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private SessionLoginService sessionLoginService;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .apply(sharedHttpSession())
                .build();
    }

    private List<UserListResponse> setUsers() {
        List<UserListResponse> list = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            UserListResponse userListResponse = UserListResponse.builder()
                    .id((long) i)
                    .email("hong43ok@gmail.com" + i)
                    .userLevel(UserLevel.AUTH)
                    .build();
            list.add(userListResponse);
        }
        return list;
    }

    @DisplayName("관리자 : 회원 전체 조회")
    @Test
    public void getAllUsers() throws Exception {
        List<UserListResponse> list = setUsers();

        long total = list.size();
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserListResponse> result = new PageImpl<>(list, pageable, total);

        given(adminService.findUsers(any(), any())).willReturn(result);

        mockMvc.perform(get("/admin/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].id").value(list.get(0).getId()))
                .andExpect(jsonPath("$.content.[0].email").value(list.get(0).getEmail()))
                .andExpect(jsonPath("$.content.[0].userLevel").value("AUTH"))
                .andExpect(jsonPath("$.content.[1].id").value(list.get(1).getId()))
                .andExpect(jsonPath("$.content.[1].email").value(list.get(1).getEmail()))
                .andExpect(jsonPath("$.content.[1].us"
                        + "erLevel").value("AUTH"))
                .andDo(print())

                .andDo(document("admin/get/findAll",
                        responseFields(
                                fieldWithPath("content.[].id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("content.[].email").type(JsonFieldType.STRING).description("email"),
                                fieldWithPath("content.[].userLevel").type(JsonFieldType.STRING).description("userLevel"),
                                fieldWithPath("pageable.offset").ignored(),
                                fieldWithPath("pageable.pageSize").ignored(),
                                fieldWithPath("pageable.pageNumber").ignored(),
                                fieldWithPath("pageable.paged").ignored(),
                                fieldWithPath("pageable.unpaged").ignored(),
                                fieldWithPath("pageable.sort.sorted").ignored(),
                                fieldWithPath("pageable.sort.unsorted").ignored(),
                                fieldWithPath("pageable.sort.empty").ignored(),
                                fieldWithPath("sort.empty").ignored(),
                                fieldWithPath("sort.sorted").ignored(),
                                fieldWithPath("sort.unsorted").ignored(),
                                fieldWithPath("totalPages").ignored(),
                                fieldWithPath("size").ignored(),
                                fieldWithPath("number").ignored(),
                                fieldWithPath("first").ignored(),
                                fieldWithPath("last").ignored(),
                                fieldWithPath("numberOfElements").ignored(),
                                fieldWithPath("empty").ignored(),
                                fieldWithPath("totalElements").ignored()
                        )
                ));
    }

    @DisplayName("관리자 : ID[PK] 기준 회원 검색")
    @Test
    public void findById() throws Exception {

        List<UserListResponse> list = setUsers();
        long total = list.size();
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserListResponse> result = new PageImpl<>(list, pageable, total);

        given(adminService.findUsers(any(), any())).willReturn(result);

        mockMvc.perform(get("/admin/users?id=1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].id").value(list.get(0).getId()))
                .andExpect(jsonPath("$.content.[0].email").value(list.get(0).getEmail()))
                .andExpect(jsonPath("$.content.[0].userLevel").value("AUTH"))

                .andDo(document("admin/get/findById",
                        requestParameters(parameterWithName("id").description("검색할 회원의 ID[PK]")),
                        responseFields(
                                fieldWithPath("content.[].id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("content.[].email").type(JsonFieldType.STRING).description("email"),
                                fieldWithPath("content.[].userLevel").type(JsonFieldType.STRING).description("userLevel"),
                                fieldWithPath("content.[].userLevel").ignored(),
                                fieldWithPath("pageable.offset").ignored(),
                                fieldWithPath("pageable.pageSize").ignored(),
                                fieldWithPath("pageable.pageNumber").ignored(),
                                fieldWithPath("pageable.paged").ignored(),
                                fieldWithPath("pageable.unpaged").ignored(),
                                fieldWithPath("pageable.sort.sorted").ignored(),
                                fieldWithPath("pageable.sort.unsorted").ignored(),
                                fieldWithPath("pageable.sort.empty").ignored(),
                                fieldWithPath("sort.empty").ignored(),
                                fieldWithPath("sort.sorted").ignored(),
                                fieldWithPath("sort.unsorted").ignored(),
                                fieldWithPath("totalPages").ignored(),
                                fieldWithPath("size").ignored(),
                                fieldWithPath("number").ignored(),
                                fieldWithPath("first").ignored(),
                                fieldWithPath("last").ignored(),
                                fieldWithPath("numberOfElements").ignored(),
                                fieldWithPath("empty").ignored(),
                                fieldWithPath("totalElements").ignored()
                        )
                ));
    }

    @DisplayName("관리자 : 회원 BAN 처리")
    @Test
    public void restrictUsers() throws Exception {
        UserBanRequest userBanRequest = UserBanRequest.builder()
                .id(1L)
                .userStatus(UserStatus.BAN)
                .build();

        mockMvc.perform(post("/admin/users/ban")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBanRequest)))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document("admin/ban", requestFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                .description("BAN 처리 할 회원 IP[PK]"),
                        fieldWithPath("userStatus").type(JsonFieldType.STRING).description("BAN")
                )));
    }

}