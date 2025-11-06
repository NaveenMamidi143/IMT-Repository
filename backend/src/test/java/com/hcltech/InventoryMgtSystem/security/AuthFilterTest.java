package com.hcltech.InventoryMgtSystem.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.IOException;
import static org.mockito.Mockito.*;
class AuthFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthFilter authFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testValidToken() throws ServletException, IOException {
        String token = "Bearer valid.jwt.token";
        String email = "user@example.com";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtils.getUsernameFromToken("valid.jwt.token")).thenReturn(email);
        when(customUserDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtils.isTokeValid("valid.jwt.token", userDetails)).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(null);

        authFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() != null;
    }

    @Test
    void testInvalidToken() throws ServletException, IOException {
        String token = "Bearer invalid.jwt.token";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtils.getUsernameFromToken("invalid.jwt.token")).thenReturn(null);

        authFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    @Test
    void testMissingToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        authFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    @Test
    void testExceptionDuringFilterChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);
        doThrow(new IOException("Filter chain error")).when(filterChain).doFilter(request, response);

        authFilter.doFilterInternal(request, response, filterChain);

        // Exception should be logged, but not thrown
        verify(filterChain).doFilter(request, response);
    }
}