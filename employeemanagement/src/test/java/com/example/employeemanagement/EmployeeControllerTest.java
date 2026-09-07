package com.example.employeemanagement;

import com.example.employeemanagement.controller.EmployeeController;
import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.example.employeemanagement.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void createEmployee_ValidRequest_ReturnsCreated() throws Exception {
        EmployeeRequestDto request = new EmployeeRequestDto(
                "priyanka", "k", "priyanka@gmail.com", "+12345678901",
                "IT", "Developer", new BigDecimal("5000"), LocalDate.now()
        );
        EmployeeResponseDto response = new EmployeeResponseDto(
                1L, "priyanka", "k", "priyanka@gmail.com", "+12345678901",
                "IT", "Developer", new BigDecimal("5000"), LocalDate.now()
        );

        when(employeeService.createEmployee(any())).thenReturn(response);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("priyanka@gmail.com"));
    }
    @Test
    void createEmployee_MissingRequiredFields() throws Exception {
        EmployeeRequestDto invalidDto = new EmployeeRequestDto(
                "", "", "", null, "", "", null, null
        );

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(employeeService);
    }

    @Test
    void updateEmployee_test() throws Exception {
        EmployeeRequestDto request = new EmployeeRequestDto(
                "priyanka", "k", "priyanka@gmail.com", "+12345678901",
                "IT", "Developer", new BigDecimal("6000"), LocalDate.now()
        );
        EmployeeResponseDto response = new EmployeeResponseDto(
                1L, "priyanka", "k", "priyanka@gmail.com", "+12345678901",
                "IT", "Developer", new BigDecimal("6000"), LocalDate.now()
        );

        when(employeeService.updateEmployee(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salary").value(6000));
    }
    @Test
    void updateEmployee_InvalidPathId() throws Exception {
        EmployeeRequestDto requestDto = new EmployeeRequestDto(
                "priyanka",
                "k",
                "priya@gmail.com",
                "+1245677889",
                "IT",
                "Developer",
                new BigDecimal("6000"),
                LocalDate.now()
        );

        mockMvc.perform(put("/api/employees/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(employeeService);
    }
    
}