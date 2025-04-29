package com.sylviavitoria.naruto.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NinjaDeNinjutsuTest {

    private NinjaDeNinjutsu ninja;
    private Personagem oponente;

    @BeforeEach
    void setUp() {
        ninja = new NinjaDeNinjutsu("Naruto", 17, "Aldeia da Folha", 100);
        oponente = new NinjaDeNinjutsu("Sasuke", 17, "Aldeia da Folha", 100);
    }

    @Test
    @DisplayName("Deve criar ninja de ninjutsu com valores corretos")
    void deveCriarNinjaDeNinjutsuComValoresCorretos() {
        assertEquals("Naruto", ninja.getNome());
        assertEquals(17, ninja.getIdade());
        assertEquals("Aldeia da Folha", ninja.getAldeia());
        assertEquals(100, ninja.getChakra());
        assertEquals(100, ninja.getVida());
    }

    @Test
    @DisplayName("Deve usar jutsu corretamente")
    void deveUsarJutsuCorretamente() {
        ninja.adicionarJutsu("Rasengan", 50, 20);
        String resultado = ninja.usarJutsu("Rasengan", oponente);
        
        assertTrue(resultado.contains("Naruto usa Rasengan!"));
        assertTrue(resultado.contains("Causou 50 de dano!"));
        assertEquals(50, oponente.getVida());
        assertEquals(90, ninja.getChakra());
    }

    @Test
    @DisplayName("Não deve usar jutsu quando chakra é insuficiente")
    void naoDeveUsarJutsuQuandoChakraInsuficiente() {
        ninja.adicionarJutsu("Rasengan", 50, 20);
        ninja.diminuirChakra(95);
        
        String resultado = ninja.usarJutsu("Rasengan", oponente);
        assertTrue(resultado.contains("não tem chakra suficiente"));
        assertEquals(100, oponente.getVida());
    }

    @Test
    @DisplayName("Não deve usar jutsu quando vida é zero")
    void naoDeveUsarJutsuQuandoVidaZero() {
        ninja.adicionarJutsu("Rasengan", 50, 20);
        ninja.receberDano(100);
        
        String resultado = ninja.usarJutsu("Rasengan", oponente);
        assertTrue(resultado.contains("não pode mais lutar"));
        assertEquals(100, oponente.getVida());
    }

    @Test
    @DisplayName("Deve desviar com sucesso quando chance é favorável")
    void deveDesviarComSucessoQuandoChanceFavoravel() {
        NinjaDeNinjutsu ninjaTestado = new NinjaDeNinjutsu("Naruto", 17, "Aldeia da Folha", 100) {
            @Override
            protected int gerarNumeroAleatorio(int min, int max) {
                return 40;
            }
        };

        String resultado = ninjaTestado.desviar("Chidori", 30);
        assertTrue(resultado.contains("Conseguiu desviar"));
        assertEquals(100, ninjaTestado.getVida());
    }

    @Test
    @DisplayName("Deve falhar ao desviar quando chance é desfavorável")
    void deveFalharAoDesviarQuandoChanceDesfavoravel() {
        NinjaDeNinjutsu ninjaTestado = new NinjaDeNinjutsu("Naruto", 17, "Aldeia da Folha", 100) {
            @Override
            protected int gerarNumeroAleatorio(int min, int max) {
                return 61;
            }
        };

        String resultado = ninjaTestado.desviar("Chidori", 30);
        assertTrue(resultado.contains("Não conseguiu desviar"));
        assertEquals(100, ninjaTestado.getVida());
    }
}