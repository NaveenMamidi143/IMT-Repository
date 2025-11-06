package com.hcltech.InventoryMgtSystem.models;

import com.hcltech.InventoryMgtSystem.enums.UserRole;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserFields() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("securePass123");
        user.setPhoneNumber("1234567890");
        user.setRole(UserRole.ADMIN);
        user.setTransactions(Collections.emptyList());

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("securePass123", user.getPassword());
        assertEquals("1234567890", user.getPhoneNumber());
        assertEquals(UserRole.ADMIN, user.getRole());
        assertNotNull(user.getTransactions());
        assertTrue(user.getTransactions().isEmpty());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void testUserBuilder() {
        User user = User.builder()
                .id(2L)
                .name("Jane Smith")
                .email("jane@example.com")
                .password("pass456")
                .phoneNumber("9876543210")
                .role(UserRole.ADMIN)
                .transactions(Collections.emptyList())
                .build();

        assertEquals(2L, user.getId());
        assertEquals("Jane Smith", user.getName());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("pass456", user.getPassword());
        assertEquals("9876543210", user.getPhoneNumber());
        assertEquals(UserRole.ADMIN, user.getRole());
        assertNotNull(user.getTransactions());
        assertTrue(user.getTransactions().isEmpty());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void testToString() {
        User user = new User();
        user.setId(3L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("alicePass");
        user.setPhoneNumber("5551234567");
        user.setRole(UserRole.ADMIN); // Use a valid enum value

        String output = user.toString();
        assertTrue(output.contains("id=3"));
        assertTrue(output.contains("name='Alice'"));
        assertTrue(output.contains("email='alice@example.com'"));
        assertTrue(output.contains("password='alicePass'"));
        assertTrue(output.contains("phoneNumber='5551234567'"));
        assertTrue(output.contains("role=ADMIN")); // Match actual enum value
        assertTrue(output.contains("createdAt="));
    }
}
