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
    private Long id;                 

    @Column(nullable = false)
    private String firstName;        

    @Column(nullable = false)
    private String lastName;         

    @Column(nullable = false, unique = true)
    private String email;            

    private String phoneNumber;       

    @Column(nullable = false)
    private String department;        

    private String jobTitle;         

    @Column(nullable = false)
    private BigDecimal salary;       

    private LocalDate hireDate;       
}
