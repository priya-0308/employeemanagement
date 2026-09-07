package com.example.employeemanagement.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record EmployeeResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String department,
        String jobTitle,
        BigDecimal salary,
        LocalDate hireDate
) {}