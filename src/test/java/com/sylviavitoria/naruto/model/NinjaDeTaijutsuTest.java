package com.sylviavitoria.naruto.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NinjaDeTaijutsuTest {

    private NinjaDeTaijutsu ninja;
    private Personagem oponente;

    @BeforeEach
    void setUp() {
        ninja = new NinjaDeTaijutsu("Rock Lee", 16, "Aldeia da Folha", 100);
        oponente = new NinjaDeTaijutsu("Gaara", 16, "Aldeia da Areia", 100);
    }

    @Test
    @DisplayName("Deve usar jutsu corretamente")
    void deveUsarJutsuCorretamente() {
        ninja.adicionarJutsu("Konoha Senpu", 50, 20);
        String resultado = ninja.usarJutsu("Konoha Senpu", oponente);
        
        assertTrue(resultado.contains("Rock Lee usa Konoha Senpu!"));
        assertTrue(resultado.contains("Causou 50 de dano!"));
        assertEquals(50, oponente.getVida());
        assertEquals(90, ninja.getChakra()); 
    }

    @Test
    @DisplayName("Não deve usar jutsu quando chakra é insuficiente")
    void naoDeveUsarJutsuQuandoChakraInsuficiente() {
        ninja.adicionarJutsu("Konoha Senpu", 50, 20);
        ninja.diminuirChakra(95); 
        
        String resultado = ninja.usarJutsu("Konoha Senpu", oponente);
        assertTrue(resultado.contains("não tem chakra suficiente"));
        assertEquals(100, oponente.getVida());
    }

    @Test
    @DisplayName("Não deve usar jutsu quando vida é zero")
    void naoDeveUsarJutsuQuandoVidaZero() {
        ninja.adicionarJutsu("Konoha Senpu", 50, 20);
        ninja.receberDano(100);
        
        String resultado = ninja.usarJutsu("Konoha Senpu", oponente);
        assertTrue(resultado.contains("não pode mais lutar"));
        assertEquals(100, oponente.getVida());
    }

    @Test
    @DisplayName("Não deve usar jutsu desconhecido")
    void naoDeveUsarJutsuDesconhecido() {
        String resultado = ninja.usarJutsu("Jutsu Desconhecido", oponente);
        assertTrue(resultado.contains("não conhece o jutsu"));
        assertEquals(100, oponente.getVida());
    }

    @Test
    @DisplayName("Deve desviar com sucesso quando chance é favorável")
    void deveDesviarComSucessoQuandoChanceFavoravel() {

        NinjaDeTaijutsu ninjaTestado = new NinjaDeTaijutsu("Rock Lee", 16, "Aldeia da Folha", 100) {
            @Override
            protected int gerarNumeroAleatorio(int min, int max) {
                return 60;
            }
        };

        String resultado = ninjaTestado.desviar("Sabaku Kyu", 30);
        assertTrue(resultado.contains("Conseguiu desviar"));
        assertEquals(100, ninjaTestado.getVida());
    }

    @Test
    @DisplayName("Deve falhar ao desviar quando chance é desfavorável")
    void deveFalharAoDesviarQuandoChanceDesfavoravel() {
        
        NinjaDeTaijutsu ninjaTestado = new NinjaDeTaijutsu("Rock Lee", 16, "Aldeia da Folha", 100) {
            @Override
            protected int gerarNumeroAleatorio(int min, int max) {
                return 61;
            }
        };

        String resultado = ninjaTestado.desviar("Sabaku Kyu", 30);
        assertTrue(resultado.contains("Não conseguiu desviar"));
        assertEquals(100, ninjaTestado.getVida());
    }

}