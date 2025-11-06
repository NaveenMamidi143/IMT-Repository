package com.hcltech.InventoryMgtSystem.services;

import com.hcltech.InventoryMgtSystem.dtos.LoginRequest;
import com.hcltech.InventoryMgtSystem.dtos.RegisterRequest;
import com.hcltech.InventoryMgtSystem.dtos.Response;
import com.hcltech.InventoryMgtSystem.dtos.UserDTO;
import com.hcltech.InventoryMgtSystem.models.User;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response getUserById(Long id);

    Response updateUser(Long id, UserDTO userDTO);

    Response deleteUser(Long id);

    Response getUserTransactions(Long id);
}
