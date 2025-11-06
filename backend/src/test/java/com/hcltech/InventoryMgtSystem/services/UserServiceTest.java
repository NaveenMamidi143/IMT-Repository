package com.hcltech.InventoryMgtSystem.services;
import com.hcltech.InventoryMgtSystem.dtos.*;
import com.hcltech.InventoryMgtSystem.enums.UserRole;
import com.hcltech.InventoryMgtSystem.exceptions.InvalidCredentialsException;
import com.hcltech.InventoryMgtSystem.exceptions.NotFoundException;
import com.hcltech.InventoryMgtSystem.models.User;
import com.hcltech.InventoryMgtSystem.repositories.UserRepository;
import com.hcltech.InventoryMgtSystem.security.JwtUtils;
import com.hcltech.InventoryMgtSystem.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ModelMapper modelMapper;
    @Mock private JwtUtils jwtUtils;
    @Mock private Authentication authentication;
    @InjectMocks private UserServiceImpl userService;
    private User user;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .phoneNumber("1234567890")
                .role(UserRole.MANAGER)
                .build();
        registerRequest = RegisterRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("password")
                .phoneNumber("1234567890")
                .role(UserRole.MANAGER)
                .build();
        loginRequest = LoginRequest.builder()
                .email("john@example.com")
                .password("password")
                .build();

        userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setPhoneNumber("1234567890");
        userDTO.setRole(UserRole.MANAGER);
    }
    @Test
    void testRegisterUser() {
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        Response response = userService.registerUser(registerRequest);

        assertEquals(200, response.getStatus());
        assertEquals("User was successfully registered", response.getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testLoginUser_Success() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtils.generateToken(user.getEmail())).thenReturn("jwt-token");

        Response response = userService.loginUser(loginRequest);

        assertEquals(200, response.getStatus());
        assertEquals("User Logged in Successfully", response.getMessage());
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void testLoginUser_EmailNotFound() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.loginUser(loginRequest));
    }

    @Test
    void testLoginUser_InvalidPassword() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userService.loginUser(loginRequest));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = List.of(user);
        List<UserDTO> userDTOList = List.of(userDTO);

        when(userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(users);
        Type listType = new TypeToken<List<UserDTO>>() {}.getType();
        when(modelMapper.map(users, listType)).thenReturn(userDTOList);

        Response response = userService.getAllUsers();

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertEquals(1, response.getUsers().size());
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        Response response = userService.getUserById(1L);

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertEquals("John Doe", response.getUser().getName());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
    }
    @Test
    void testUpdateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userDTO.setEmail("newemail@example.com");
        userDTO.setPhoneNumber("9876543210");
        userDTO.setName("Updated Name");
        userDTO.setRole(UserRole.ADMIN);
        userDTO.setPassword("newPassword");

        Response response = userService.updateUser(1L, userDTO);

        assertEquals(200, response.getStatus());
        assertEquals("User successfully updated", response.getMessage());

        verify(userRepository).save(any(User.class));
    }
    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(1L, userDTO));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);

        Response response = userService.deleteUser(1L);

        assertEquals(200, response.getStatus());
        assertEquals("User successfully Deleted", response.getMessage());
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void testGetUserTransactions_Success() {
        // Mock user entity
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Mock DTO with non-null transactions list
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setUser(userDTO);
        transactionDTO.setSupplier(null); // or mock supplier if needed

        userDTO.setTransactions(List.of(transactionDTO));

        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        Response response = userService.getUserTransactions(1L);

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertEquals("John Doe", response.getUser().getName());

        verify(userRepository).findById(1L);
        verify(modelMapper).map(user, UserDTO.class);
    }


    @Test
    void testGetCurrentLoggedInUser() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("john@example.com");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        User result = userService.getCurrentLoggedInUser();

        assertEquals("john@example.com", result.getEmail());
    }
}