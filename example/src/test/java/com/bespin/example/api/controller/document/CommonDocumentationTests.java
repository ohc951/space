package com.bespin.example.api.controller.document;

import com.bespin.example.api.controller.document.utils.CustomResponseFieldsSnippet;
import com.bespin.example.response.ApiResponseCode;
import com.bespin.example.utils.EnumType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Project : group.example
 * Class : com.bespin.example.api.controller.document.CommonDocumentationTests
 * Version : 0.0.1
 * Created by josihyeon on 2019-05-23.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(EnumViewController.class)
@AutoConfigureRestDocs
public class CommonDocumentationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void commons() throws Exception {

        //when
        ResultActions result = this.mockMvc.perform(
                get("/docs")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("common",
                        customResponseFields("custom-response", null,
                                attributes(key("title").value("공통응답")),
                                subsectionWithPath("data").description("데이터"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지")
                        ),
                        customResponseFields("custom-response", beneathPath("data.apiResponseCodes").withSubsectionId("apiResponseCodes"),
                                attributes(key("title").value("응답코드")),
                                enumConvertFieldDescriptor(ApiResponseCode.values())
                        )
                ));
    }

    private FieldDescriptor[] enumConvertFieldDescriptor(EnumType[] enumTypes) {

        return Arrays.stream(enumTypes)
                .map(enumType -> fieldWithPath(enumType.getId()).description(enumType.getText()))
                .toArray(FieldDescriptor[]::new);
    }

    public static CustomResponseFieldsSnippet customResponseFields(String type, PayloadSubsectionExtractor<?> subsectionExtractor, Map<String, Object> attributes, FieldDescriptor... descriptors) {
        return new CustomResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes, true);
    }
}

