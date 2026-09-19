package com.example.employeemanagement.security.dto;

public record AuthResponse(String token, String tokenType, String username, String role) {}
