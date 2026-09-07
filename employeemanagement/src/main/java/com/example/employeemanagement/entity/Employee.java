package com.example.employeemanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                  // 1

    @Column(nullable = false)
    private String firstName;         // 2

    @Column(nullable = false)
    private String lastName;          // 3

    @Column(nullable = false, unique = true)
    private String email;             // 4

    private String phoneNumber;       // 5

    @Column(nullable = false)
    private String department;        // 6

    private String jobTitle;          // 7

    @Column(nullable = false)
    private BigDecimal salary;        // 8

    private LocalDate hireDate;       // 9
}