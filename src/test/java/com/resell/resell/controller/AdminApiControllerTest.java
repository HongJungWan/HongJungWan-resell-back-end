package com.resell.resell.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resell.resell.service.AdminService;
import com.resell.resell.service.SessionLoginService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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

    
}