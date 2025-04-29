package com.sylviavitoria.naruto.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
    
    public static NotFoundException personagemNaoEncontrado(Long id) {
        return new NotFoundException("Personagem não encontrado com ID: " + id);
    }
}