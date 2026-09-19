package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.DuplicateResourceException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private final EmployeeRequestDto request = new EmployeeRequestDto(
            "Priyanka", "K", "priyanka@example.com", "+12345678901",
            "IT", "Developer", new BigDecimal("5000"), LocalDate.of(2024, 1, 10));

    @Test
    void createEmployee_whenEmailIsUnique_savesAndReturnsMappedEmployee() {
        Employee saved = employee(1L, request);
        when(employeeRepository.existsByEmail(request.email())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        EmployeeResponseDto result = employeeService.createEmployee(request);

        assertEquals(1L, result.id());
        assertEquals(request.firstName(), result.firstName());
        assertEquals(request.email(), result.email());
        assertEquals(request.salary(), result.salary());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void createEmployee_whenEmailAlreadyExists_throwsDuplicateException() {
        when(employeeRepository.existsByEmail(request.email())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> employeeService.createEmployee(request));

        assertEquals("Employee with email priyanka@example.com already exists.", exception.getMessage());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void updateEmployee_whenEmployeeExists_updatesAllFieldsAndReturnsResponse() {
        Employee existing = employee(1L, request);
        EmployeeRequestDto updated = new EmployeeRequestDto(
                "Updated", "Name", "updated@example.com", "+10987654321",
                "Finance", "Manager", new BigDecimal("7000"), LocalDate.of(2024, 2, 20));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(employeeRepository.existsByEmailAndIdNot(updated.email(), 1L)).thenReturn(false);
        when(employeeRepository.save(existing)).thenReturn(existing);

        EmployeeResponseDto result = employeeService.updateEmployee(1L, updated);

        assertEquals("Updated", result.firstName());
        assertEquals("Name", result.lastName());
        assertEquals("updated@example.com", result.email());
        assertEquals("Finance", result.department());
        assertEquals("Manager", result.jobTitle());
        assertEquals(new BigDecimal("7000"), result.salary());
        assertEquals(LocalDate.of(2024, 2, 20), result.hireDate());
        verify(employeeRepository).save(existing);
    }

    @Test
    void updateEmployee_whenEmployeeDoesNotExist_throwsNotFoundException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.updateEmployee(99L, request));

        assertEquals("Employee not found with id: 99", exception.getMessage());
        verify(employeeRepository, never()).existsByEmailAndIdNot(any(), any());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void updateEmployee_whenEmailBelongsToAnotherEmployee_throwsDuplicateException() {
        Employee existing = employee(1L, request);
        EmployeeRequestDto updated = new EmployeeRequestDto(
                "Updated", "Name", "other@example.com", "+10987654321",
                "Finance", "Manager", new BigDecimal("7000"), request.hireDate());
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(employeeRepository.existsByEmailAndIdNot(updated.email(), 1L)).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> employeeService.updateEmployee(1L, updated));

        assertEquals("Email other@example.com is already in use by another employee.", exception.getMessage());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void updateEmployee_whenEmailIsUnchanged_allowsUpdate() {
        Employee existing = employee(1L, request);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(employeeRepository.existsByEmailAndIdNot(request.email(), 1L)).thenReturn(false);
        when(employeeRepository.save(existing)).thenReturn(existing);

        EmployeeResponseDto result = employeeService.updateEmployee(1L, request);

        assertEquals(1L, result.id());
        assertEquals(request.email(), result.email());
        verify(employeeRepository).existsByEmailAndIdNot(request.email(), 1L);
        verify(employeeRepository).save(existing);
    }

    private Employee employee(Long id, EmployeeRequestDto dto) {
        return Employee.builder()
                .id(id)
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .phoneNumber(dto.phoneNumber())
                .department(dto.department())
                .jobTitle(dto.jobTitle())
                .salary(dto.salary())
                .hireDate(dto.hireDate())
                .build();
    }
}
