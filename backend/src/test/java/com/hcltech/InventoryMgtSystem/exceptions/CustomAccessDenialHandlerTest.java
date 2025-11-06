package com.hcltech.InventoryMgtSystem.exceptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.InventoryMgtSystem.dtos.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.mockito.Mockito.*;
class CustomAccessDenialHandlerTest {
    @Test
    void testHandleAccessDenied() throws Exception {
        // Arrange
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        CustomAccessDenialHandler handler = new CustomAccessDenialHandler(mockMapper);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AccessDeniedException exception = new AccessDeniedException("Access Denied");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Mock the ObjectMapper to return a dummy JSON string
        when(mockMapper.writeValueAsString(any(Response.class)))
                .thenReturn("{\"status\":403,\"message\":\"Access Denied\"}");

        // Act
        handler.handle(request, response, exception);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpStatus.FORBIDDEN.value());

        String json = stringWriter.toString();
        assert json.contains("\"status\":403");
        assert json.contains("\"message\":\"Access Denied\"");
    }
}