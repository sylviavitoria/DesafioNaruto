package com.sylviavitoria.naruto.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secretKey",
                "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);

        userDetails = new User("ninja_test", "password123", new ArrayList<>());
    }

    @Test
    @DisplayName("Deve extrair username do token")
    void deveExtrairUsernameDoToken() {
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertEquals("ninja_test", username);
    }

    @Test
    @DisplayName("Deve extrair data de expiração do token")
    void deveExtrairDataDeExpiracaoDoToken() {
        String token = jwtService.generateToken(userDetails);

        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    @DisplayName("Deve gerar token com claims extras")
    void deveGerarTokenComClaimsExtras() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("aldeia", "Folha");
        extraClaims.put("rank", "Jounin");

        String token = jwtService.generateToken(extraClaims, userDetails);

        assertEquals("Folha", jwtService.extractClaim(token, claims -> claims.get("aldeia")));
        assertEquals("Jounin", jwtService.extractClaim(token, claims -> claims.get("rank")));
    }

    @Test
    @DisplayName("Deve validar token válido")
    void deveValidarTokenValido() {
        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    @DisplayName("Deve rejeitar token para usuário diferente")
    void deveRejeitarTokenParaUsuarioDiferente() {
        String token = jwtService.generateToken(userDetails);
        UserDetails outroUsuario = new User("sasuke", "password123", new ArrayList<>());

        assertFalse(jwtService.isTokenValid(token, outroUsuario));
    }

    @Test
    @DisplayName("Deve gerar tokens diferentes para o mesmo usuário")
    void deveGerarTokensDiferentesParaMesmoUsuario() throws Exception {
        String token1 = jwtService.generateToken(userDetails);

        Thread.sleep(1100); 

        String token2 = jwtService.generateToken(userDetails);

        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Deve extrair todas as claims do token")
    void deveExtrairTodasClaimsDoToken() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("ninjutsu", "Rasengan");
        String token = jwtService.generateToken(extraClaims, userDetails);

        Object ninjutsu = jwtService.extractClaim(token, claims -> claims.get("ninjutsu"));

        assertEquals("Rasengan", ninjutsu);
    }
}