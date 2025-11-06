package com.hcltech.InventoryMgtSystem.exceptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.InventoryMgtSystem.dtos.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

class CustomAuthenticationEntryPointTest {

    @Test
    void testCommenceUnauthorizedAccess() throws Exception {
        // Arrange
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint(mockMapper);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException exception = new AuthenticationException("Unauthorized") {};

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Mock ObjectMapper to avoid LocalDateTime serialization error
        when(mockMapper.writeValueAsString(any(Response.class)))
                .thenReturn("{\"status\":401,\"message\":\"Unauthorized\"}");

        // Act
        entryPoint.commence(request, response, exception);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());

        String json = stringWriter.toString();
        assert json.contains("\"status\":401");
        assert json.contains("\"message\":\"Unauthorized\"");
    }
}