package com.javaassignment.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaassignment.api.model.User;
import com.javaassignment.api.service.RandomUserService;
import com.javaassignment.api.validator.EnglishAlphabetsValidator;
import com.javaassignment.api.validator.NumericValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RandomUserControllerTest {

    @Mock
    private RandomUserService randomUserService;

    @Mock
    private EnglishAlphabetsValidator alphabetsValidator;

    @Mock
    private NumericValidator numericValidator;

    @InjectMocks
    private RandomUserController randomUserController;

    private final MockMvc mockMvc;


    public RandomUserControllerTest() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(randomUserController).build();
    }

    @Test
    void testGetRecentUsers() throws Exception {
        when(alphabetsValidator.validate(any())).thenReturn(true);
        when(numericValidator.validate(any())).thenReturn(true);
        when(randomUserService.getRecentUsers(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn("Mocked response"); // Replace with your expected response

        mockMvc.perform(MockMvcRequestBuilders.get("/api/recentusers")
                        .param("limit", "3")
                        .param("offset", "0")
                        .param("sortType", "Name")
                        .param("sortOrder", "Odd"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value("Mocked response")); // Replace with your expected response
    }

    @Test
    void testCreateUser() throws Exception {
        when(alphabetsValidator.validate(any())).thenReturn(true);
        when(numericValidator.validate(any())).thenReturn(true);
        when(randomUserService.createUser(anyInt()))
                .thenReturn("[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"age\":25,\"gender\":\"Male\",\"nationality\":\"US\",\"verificationResult\":\"VERIFIED\"}]"); // Replace with your expected response

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1)) // Replace with your expected values
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].age").value(25))
                .andExpect(jsonPath("$[0].gender").value("Male"))
                .andExpect(jsonPath("$[0].nationality").value("US"))
                .andExpect(jsonPath("$[0].verificationResult").value("VERIFIED"));
    }
}
