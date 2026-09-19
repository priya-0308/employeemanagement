package com.example.employeemanagement.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceExceptionTest {

    @Test
    void duplicateResourceException_preservesMessage() {
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> { throw new DuplicateResourceException("duplicate"); });

        assertEquals("duplicate", exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void resourceNotFoundException_preservesMessage() {
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> { throw new ResourceNotFoundException("not found"); });

        assertEquals("not found", exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }
}
