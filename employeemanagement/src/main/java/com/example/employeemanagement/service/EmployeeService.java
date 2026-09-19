package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;

import java.util.List;

public interface EmployeeService {
    List<EmployeeResponseDto> getAllEmployees();
    EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto);
    EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto requestDto);
}
