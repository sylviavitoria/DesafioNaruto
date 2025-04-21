package com.sylviavitoria.naruto.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {
    
    private GlobalExceptionHandler exceptionHandler;
    
    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }
    
    @Test
    void handleIllegalArgumentException_deveRetornarStatusBadRequest() {
        String mensagemErro = "Parâmetro inválido";
        IllegalArgumentException exception = new IllegalArgumentException(mensagemErro);
        
        ResponseEntity<Object> response = exceptionHandler.handleIllegalArgumentException(exception);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.get("status"));
        assertEquals("Requisição inválida", body.get("error"));
        assertEquals(mensagemErro, body.get("message"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }
    
    @Test
    void handleRuntimeException_deveRetornarStatusInternalServerError() {
        String mensagemErro = "Erro interno do servidor";
        RuntimeException exception = new RuntimeException(mensagemErro);
        
        ResponseEntity<Object> response = exceptionHandler.handleRuntimeException(exception);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.get("status"));
        assertEquals("Erro interno", body.get("error"));
        assertEquals(mensagemErro, body.get("message"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
    }
}