package com.hcltech.InventoryMgtSystem.exceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidCredentialsExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "Invalid username or password";
        InvalidCredentialsException exception = new InvalidCredentialsException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        InvalidCredentialsException exception = new InvalidCredentialsException("Error");
        assertTrue(exception instanceof RuntimeException);
    }
}