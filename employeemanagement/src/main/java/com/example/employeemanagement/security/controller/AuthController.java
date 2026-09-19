package com.example.employeemanagement.security.controller;

import com.example.employeemanagement.security.dto.*;
import com.example.employeemanagement.security.entity.AppUser;
import com.example.employeemanagement.security.repository.AppUserRepository;
import com.example.employeemanagement.security.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        AppUser user = userRepository.save(AppUser.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(AppUser.Role.USER)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(user));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(), request.password()));
        AppUser user = userRepository.findByUsername(request.username()).orElseThrow();
        return toResponse(user);
    }

    private AuthResponse toResponse(AppUser user) {
        var principal = User.withUsername(user.getUsername()).password(user.getPassword())
                .roles(user.getRole().name()).build();
        return new AuthResponse(jwtService.generateToken(principal), "Bearer",
                user.getUsername(), user.getRole().name());
    }
}
