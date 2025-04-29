package com.sylviavitoria.naruto.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NinjaDeGenjutsuTest {

    private NinjaDeGenjutsu ninja;
    private Personagem oponente;

    @BeforeEach
    void setUp() {
        ninja = new NinjaDeGenjutsu("Itachi", 21, "Aldeia da Folha", 100);
        oponente = new NinjaDeGenjutsu("Kurenai", 27, "Aldeia da Folha", 100);
    }

    @Test
    @DisplayName("Deve criar ninja de genjutsu com valores corretos")
    void deveCriarNinjaDeGenjutsuComValoresCorretos() {
        assertEquals("Itachi", ninja.getNome());
        assertEquals(21, ninja.getIdade());
        assertEquals("Aldeia da Folha", ninja.getAldeia());
        assertEquals(100, ninja.getChakra());
        assertEquals(100, ninja.getVida());
    }

    @Test
    @DisplayName("Deve usar jutsu corretamente")
    void deveUsarJutsuCorretamente() {
        ninja.adicionarJutsu("Tsukuyomi", 50, 20);
        String resultado = ninja.usarJutsu("Tsukuyomi", oponente);
        
        assertTrue(resultado.contains("Itachi usa Tsukuyomi!"));
        assertTrue(resultado.contains("Causou 50 de dano!"));
        assertEquals(50, oponente.getVida());
        assertEquals(90, ninja.getChakra());
    }

    @Test
    @DisplayName("Não deve usar jutsu quando chakra é insuficiente")
    void naoDeveUsarJutsuQuandoChakraInsuficiente() {
        ninja.adicionarJutsu("Tsukuyomi", 50, 20);
        ninja.diminuirChakra(95);
        
        String resultado = ninja.usarJutsu("Tsukuyomi", oponente);
        assertTrue(resultado.contains("não tem chakra suficiente"));
        assertEquals(100, oponente.getVida());
    }

    @Test
    @DisplayName("Não deve usar jutsu quando vida é zero")
    void naoDeveUsarJutsuQuandoVidaZero() {
        ninja.adicionarJutsu("Tsukuyomi", 50, 20);
        ninja.receberDano(100);
        
        String resultado = ninja.usarJutsu("Tsukuyomi", oponente);
        assertTrue(resultado.contains("não pode mais lutar"));
        assertEquals(100, oponente.getVida());
    }

    @Test
    @DisplayName("Deve desviar com sucesso quando chance é favorável")
    void deveDesviarComSucessoQuandoChanceFavoravel() {
        int idade = 21;
        int vidaInicial = 100;
        int dano = 30;
        int chanceDesvio = 40;
        NinjaDeGenjutsu ninjaTestado = new NinjaDeGenjutsu("Itachi", idade, "Aldeia da Folha", vidaInicial) {
            @Override
            protected int gerarNumeroAleatorio(int min, int max) {
                return chanceDesvio;
            }
        };

        String resultado = ninjaTestado.desviar("Rasengan", dano);
        assertTrue(resultado.contains("Conseguiu desviar"));
        assertEquals(vidaInicial, ninjaTestado.getVida());
    }

    @Test
    @DisplayName("Deve falhar ao desviar quando chance é desfavorável")
    void deveFalharAoDesviarQuandoChanceDesfavoravel() {
        int idade = 21;
        int vidaInicial = 100;
        int dano = 30;
        int chanceDesvio = 51; 
        NinjaDeGenjutsu ninjaTestado = new NinjaDeGenjutsu("Itachi", idade, "Aldeia da Folha", vidaInicial) {
            @Override
            protected int gerarNumeroAleatorio(int min, int max) {
                return chanceDesvio;
            }
        };

        String resultado = ninjaTestado.desviar("Rasengan", dano);
        assertTrue(resultado.contains("Não conseguiu desviar"));
        assertEquals(vidaInicial, ninjaTestado.getVida());
    }
}