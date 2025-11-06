package com.hcltech.InventoryMgtSystem.controllers;
import com.hcltech.InventoryMgtSystem.dtos.LoginRequest;
import com.hcltech.InventoryMgtSystem.dtos.RegisterRequest;
import com.hcltech.InventoryMgtSystem.dtos.Response;
import com.hcltech.InventoryMgtSystem.enums.UserRole;
import com.hcltech.InventoryMgtSystem.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class AuthControllerTest {
    @InjectMocks
    private AuthController authController; // ✅ This is missing in your current test
    @Mock
    private UserService userService;
    @Mock
    private RegisterRequest registerRequest;
    @Mock
    private LoginRequest loginRequest;
    @Mock
    private Response mockResponse;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerRequest = RegisterRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("password123")
                .phoneNumber("1234567890")
                .build(); // ✅ Required to complete the builder

        loginRequest = LoginRequest.builder()
                .email("john@example.com")
                .password("password123")
                .build(); // ✅ Assign to loginRequest

        mockResponse = Response.builder()
                .status(200)
                .message("Success")
                .build();
    }

    @Test
    void testRegisterUser() {
        when(userService.registerUser(registerRequest)).thenReturn(mockResponse);
        ResponseEntity<Response> responseEntity = authController.registerUser(registerRequest);
        assertEquals(200, responseEntity.getBody().getStatus());
        assertEquals("Success", responseEntity.getBody().getMessage());
        verify(userService).registerUser(registerRequest);
    }

    @Test
    void testLoginUser() {
        when(userService.loginUser(loginRequest)).thenReturn(mockResponse);

        ResponseEntity<Response> responseEntity = authController.loginUser(loginRequest);

        assertEquals(200, responseEntity.getBody().getStatus());
        assertEquals("Success", responseEntity.getBody().getMessage());
        verify(userService).loginUser(loginRequest);
    }
}