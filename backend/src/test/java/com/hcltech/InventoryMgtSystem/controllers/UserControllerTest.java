package com.hcltech.InventoryMgtSystem.controllers;
import com.hcltech.InventoryMgtSystem.dtos.Response;
import com.hcltech.InventoryMgtSystem.dtos.UserDTO;
import com.hcltech.InventoryMgtSystem.models.User;
import com.hcltech.InventoryMgtSystem.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void testGetAllUsers() {
        Response mockResponse = Response.builder().status(200).message("All users").build();
        when(userService.getAllUsers()).thenReturn(mockResponse);

        ResponseEntity<Response> response = userController.getAllUsers();

        assertEquals(200, response.getBody().getStatus());
        assertEquals("All users", response.getBody().getMessage());
    }

    @Test
    void testGetUserById() {
        Response mockResponse = Response.builder().status(200).message("User found").build();
        when(userService.getUserById(1L)).thenReturn(mockResponse);

        ResponseEntity<Response> response = userController.getUserById(1L);

        assertEquals(200, response.getBody().getStatus());
        assertEquals("User found", response.getBody().getMessage());
    }

    @Test
    void testUpdateUser() {
        UserDTO userDTO = new UserDTO();
        Response mockResponse = Response.builder().status(200).message("User updated").build();
        when(userService.updateUser(1L, userDTO)).thenReturn(mockResponse);

        ResponseEntity<Response> response = userController.updateUser(1L, userDTO);

        assertEquals(200, response.getBody().getStatus());
        assertEquals("User updated", response.getBody().getMessage());
    }

    @Test
    void testDeleteUser() {
        Response mockResponse = Response.builder().status(200).message("User deleted").build();
        when(userService.deleteUser(1L)).thenReturn(mockResponse);

        ResponseEntity<Response> response = userController.deleteUser(1L);

        assertEquals(200, response.getBody().getStatus());
        assertEquals("User deleted", response.getBody().getMessage());
    }

    @Test
    void testGetUserAndTransactions() {
        Response mockResponse = Response.builder().status(200).message("User transactions").build();
        when(userService.getUserTransactions(1L)).thenReturn(mockResponse);

        ResponseEntity<Response> response = userController.getUserAndTransactions(1L);

        assertEquals(200, response.getBody().getStatus());
        assertEquals("User transactions", response.getBody().getMessage());
    }

    @Test
    void testGetCurrentUser() {
        User mockUser = new User();
        when(userService.getCurrentLoggedInUser()).thenReturn(mockUser);

        ResponseEntity<User> response = userController.getCurrentUser();

        assertEquals(mockUser, response.getBody());
    }
}