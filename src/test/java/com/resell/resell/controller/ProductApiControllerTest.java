package com.resell.resell.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resell.resell.controller.dto.ProductDto.SaveRequest;
import com.resell.resell.domain.product.common.Currency;
import com.resell.resell.domain.product.common.SizeClassification;
import com.resell.resell.domain.product.common.SizeUnit;
import com.resell.resell.service.ProductService;
import com.resell.resell.service.SessionLoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(ProductApiController.class)
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
public class ProductApiControllerTest {
    
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

    private SaveRequest createSaveRequest() {
        return SaveRequest.builder()
                .nameKor("덩크 로우")
                .nameEng("Dunk Low")
                .modelNumber("DD1391-100")
                .color("WHITE/BLACK")
                .releaseDate(LocalDate.of(2022, 01, 15))
                .releasePrice(119000)
                .currency(Currency.KRW)
                .sizeClassification(SizeClassification.MENS)
                .sizeUnit(SizeUnit.MM)
                .minSize(240)
                .maxSize(320)
                .sizeGap(5)
                .build();
    }

}
