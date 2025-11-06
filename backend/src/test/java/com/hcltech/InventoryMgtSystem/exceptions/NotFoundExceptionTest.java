package com.hcltech.InventoryMgtSystem.exceptions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "Item not found";
        NotFoundException exception = new NotFoundException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        NotFoundException exception = new NotFoundException("Error");
        assertTrue(exception instanceof RuntimeException);
    }
}