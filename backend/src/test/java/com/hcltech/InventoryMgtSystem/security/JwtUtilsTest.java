package com.hcltech.InventoryMgtSystem.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private final String secret = "mySecretKeyForJwtSigning1234567890"; // should be long enough for HMAC-SHA256

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secreteJwtString", secret);
        ReflectionTestUtils.invokeMethod(jwtUtils, "init");
    }

    @Test
    void testGenerateTokenAndExtractUsername() {
        String email = "test@example.com";
        String token = jwtUtils.generateToken(email);

        assertNotNull(token);
        String extractedUsername = jwtUtils.getUsernameFromToken(token);
        assertEquals(email, extractedUsername);
    }

    @Test
    void testIsTokenValid_ValidToken() {
        String email = "valid@example.com";
        String token = jwtUtils.generateToken(email);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(email);

        assertTrue(jwtUtils.isTokeValid(token, userDetails));
    }

    @Test
    void testIsTokenValid_InvalidUsername() {
        String token = jwtUtils.generateToken("user1@example.com");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user2@example.com");

        assertFalse(jwtUtils.isTokeValid(token, userDetails));
    }
    @Test
    void testIsTokenExpired() throws InterruptedException {
        String shortLivedSecret = "shortLivedSecretKeyThatIsLongEnough1234567890!";
        JwtUtils shortLivedJwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(shortLivedJwtUtils, "secreteJwtString", shortLivedSecret);
        ReflectionTestUtils.invokeMethod(shortLivedJwtUtils, "init");

        SecretKey key = Keys.hmacShaKeyFor(shortLivedSecret.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
                .subject("expired@example.com")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000)) // 1 second
                .signWith(key)
                .compact();

        Thread.sleep(1500); // Wait for token to expire

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("expired@example.com");

        // Expect the token to be expired and throw an exception
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            shortLivedJwtUtils.isTokeValid(token, userDetails);
        });
    }
}
