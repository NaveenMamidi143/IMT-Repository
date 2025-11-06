package com.hcltech.InventoryMgtSystem.exceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NameValueRequiredExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "Name value is required";
        NameValueRequiredException exception = new NameValueRequiredException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        NameValueRequiredException exception = new NameValueRequiredException("Error");
        assertTrue(exception instanceof RuntimeException);
    }
}