package com.sylviavitoria.naruto.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.io.IOException;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private UserDetails userDetails;
    private static final String TOKEN_VALIDO = "TokenValido";
    private static final String TOKEN_INVALIDO = "TokenInvalido";
    private static final String USERNAME = "ninja_test";

    @BeforeEach
    void setUp() {
    
        SecurityContextHolder.setContext(securityContext);

        userDetails = new User(USERNAME, "password", new ArrayList<>());
    }

    @Test
    @DisplayName("Não deve autenticar quando o header Authorization estiver ausente")
    void devePassarADianteQuandoHeaderAuthorizationEstiverAusente() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    @DisplayName("Não deve autenticar quando o Authorization não começar com Bearer")
    void devePassarADianteQuandoAuthorizationNaoComecaComBearer() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Token " + TOKEN_VALIDO);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    @DisplayName("Deve extrair username do token e carregar UserDetails quando token for válido")
    void deveExtrairUsernameECarregarUserDetailsQuandoTokenForValido() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN_VALIDO);
        when(jwtService.extractUsername(TOKEN_VALIDO)).thenReturn(USERNAME);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);
        when(jwtService.isTokenValid(TOKEN_VALIDO, userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(1)).extractUsername(TOKEN_VALIDO);
        verify(userDetailsService, times(1)).loadUserByUsername(USERNAME);
        verify(jwtService, times(1)).isTokenValid(TOKEN_VALIDO, userDetails);
        verify(securityContext, times(1)).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Não deve configurar autenticação quando token for inválido")
    void naoDeveConfigurarAutenticacaoQuandoTokenForInvalido() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN_INVALIDO);
        when(jwtService.extractUsername(TOKEN_INVALIDO)).thenReturn(USERNAME);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);
        when(jwtService.isTokenValid(TOKEN_INVALIDO, userDetails)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(1)).extractUsername(TOKEN_INVALIDO);
        verify(userDetailsService, times(1)).loadUserByUsername(USERNAME);
        verify(jwtService, times(1)).isTokenValid(TOKEN_INVALIDO, userDetails);
        verify(securityContext, never()).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Não deve carregar UserDetails quando username do token for nulo")
    void naoDeveCarregarUserDetailsQuandoUsernameForNulo() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN_INVALIDO);
        when(jwtService.extractUsername(TOKEN_INVALIDO)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(1)).extractUsername(TOKEN_INVALIDO);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Não deve configurar autenticação quando o contexto já possui autenticação")
    void naoDeveConfigurarAutenticacaoQuandoContextoJaPossuiAutenticacao() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN_VALIDO);
        when(jwtService.extractUsername(TOKEN_VALIDO)).thenReturn(USERNAME);
        when(securityContext.getAuthentication()).thenReturn(mock(UsernamePasswordAuthenticationToken.class));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(1)).extractUsername(TOKEN_VALIDO);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

}