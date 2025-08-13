package com.ryan.ieltsplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryan.ieltsplatform.constant.ApiEndpoints;
import com.ryan.ieltsplatform.dto.ApiResponse;
import com.ryan.ieltsplatform.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloController.class)
@Import(MessageService.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void hello_ShouldReturnSuccessResponse() throws Exception {
        mockMvc.perform(get(ApiEndpoints.Hello.GREET)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.message").value("Hello from IELTS Platform Backend!"))
                .andExpect(jsonPath("$.data.correlationId").exists())
                .andExpect(jsonPath("$.message").value("Operation completed successfully"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void helloWithName_ShouldReturnPersonalizedMessage() throws Exception {
        String name = "Ryan";
        
        mockMvc.perform(get(ApiEndpoints.buildEndpoint(ApiEndpoints.Hello.GREET_WITH_NAME, name))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.message").value("Hello, Ryan! Welcome to IELTS Platform Backend!"))
                .andExpect(jsonPath("$.data.correlationId").exists())
                .andExpect(jsonPath("$.data.greetedName").value(name));
    }

    @Test
    void helloWithEmptyName_ShouldReturnValidationError() throws Exception {
        mockMvc.perform(get(ApiEndpoints.buildEndpoint(ApiEndpoints.Hello.GREET_WITH_NAME, " "))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("EMPTY_NAME"))
                .andExpect(jsonPath("$.message").value("Name cannot be empty"));
    }

    @Test
    void helloWithLongName_ShouldReturnValidationError() throws Exception {
        String longName = "A".repeat(51);
        
        mockMvc.perform(get(ApiEndpoints.buildEndpoint(ApiEndpoints.Hello.GREET_WITH_NAME, longName))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("NAME_TOO_LONG"))
                .andExpect(jsonPath("$.message").value("Name cannot exceed 50 characters"));
    }

    @Test
    void demonstrateBusinessError_ShouldReturnBusinessError() throws Exception {
        mockMvc.perform(get(ApiEndpoints.Hello.BUSINESS_ERROR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("DEMO_ERROR"))
                .andExpect(jsonPath("$.message").value("This is a demonstration of business exception handling"));
    }

    @Test
    void demonstrateValidationError_ShouldReturnValidationError() throws Exception {
        mockMvc.perform(get(ApiEndpoints.Hello.VALIDATION_ERROR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("DEMO_VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("This is a demonstration of validation exception handling"));
    }

    @Test
    void demonstrateRuntimeError_ShouldReturnInternalServerError() throws Exception {
        mockMvc.perform(get(ApiEndpoints.Hello.RUNTIME_ERROR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}
