package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.example.employeemanagement.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get all employees")
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new employee")
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto requestDto) {
        EmployeeResponseDto created = employeeService.createEmployee(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing employee by ID")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDto requestDto) {
        EmployeeResponseDto updated = employeeService.updateEmployee(id, requestDto);
        return ResponseEntity.ok(updated);
    }
}
