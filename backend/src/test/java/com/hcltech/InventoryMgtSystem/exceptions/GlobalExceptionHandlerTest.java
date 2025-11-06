package com.hcltech.InventoryMgtSystem.exceptions;
import com.hcltech.InventoryMgtSystem.dtos.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleAllExceptions() {
        Exception ex = new Exception("Generic error");
        ResponseEntity<Response> responseEntity = handler.handleAllExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(500, responseEntity.getBody().getStatus());
        assertEquals("Generic error", responseEntity.getBody().getMessage());
    }

    @Test
    void testHandleNotFoundException() {
        NotFoundException ex = new NotFoundException("Resource not found");
        ResponseEntity<Response> responseEntity = handler.handleNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(404, responseEntity.getBody().getStatus());
        assertEquals("Resource not found", responseEntity.getBody().getMessage());
    }

    @Test
    void testHandleNameValueRequiredException() {
        NameValueRequiredException ex = new NameValueRequiredException("Name is required");
        ResponseEntity<Response> responseEntity = handler.handleNameValueRequiredException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(400, responseEntity.getBody().getStatus());
        assertEquals("Name is required", responseEntity.getBody().getMessage());
    }

    @Test
    void testHandleInvalidCredentialsException() {
        InvalidCredentialsException ex = new InvalidCredentialsException("Invalid credentials");
        ResponseEntity<Response> responseEntity = handler.handleInvalidCredentialsException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(400, responseEntity.getBody().getStatus());
        assertEquals("Invalid credentials", responseEntity.getBody().getMessage());
    }
}
