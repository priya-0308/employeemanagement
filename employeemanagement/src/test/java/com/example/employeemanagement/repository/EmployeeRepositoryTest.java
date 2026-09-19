package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void existsByEmail_returnsTrueOnlyForStoredEmail() {
        employeeRepository.save(employee("stored@example.com"));

        assertTrue(employeeRepository.existsByEmail("stored@example.com"));
        assertFalse(employeeRepository.existsByEmail("missing@example.com"));
    }

    @Test
    void existsByEmailAndIdNot_excludesTheProvidedEmployeeId() {
        Employee first = employeeRepository.save(employee("first@example.com"));
        Employee second = employeeRepository.save(employee("second@example.com"));

        assertFalse(employeeRepository.existsByEmailAndIdNot("first@example.com", first.getId()));
        assertTrue(employeeRepository.existsByEmailAndIdNot("first@example.com", second.getId()));
    }

    private Employee employee(String email) {
        return Employee.builder()
                .firstName("Test")
                .lastName("Employee")
                .email(email)
                .phoneNumber("+12345678901")
                .department("IT")
                .jobTitle("Developer")
                .salary(new BigDecimal("5000"))
                .hireDate(LocalDate.of(2024, 1, 1))
                .build();
    }
}
