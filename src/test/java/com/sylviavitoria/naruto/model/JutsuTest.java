package com.sylviavitoria.naruto.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JutsuTest {
    @Test
    @DisplayName("Deve criar jutsu com valores corretos")
    void deveCriarJutsuComValoresCorretos() {
        Jutsu jutsu = new Jutsu(50, 20);
        
        assertEquals(50, jutsu.getDano());
        assertEquals(20, jutsu.getConsumoDeChakra());
    }

    @Test
    @DisplayName("Deve calcular dano real corretamente")
    void deveCalcularDanoRealCorretamente() {
        Jutsu jutsu = new Jutsu(50, 20);
        double modificador = 1.5;
        
        int danoCalculado = jutsu.calcularDanoReal(modificador);
        
        assertEquals(75, danoCalculado);
    }

    @Test
    @DisplayName("Deve criar jutsu usando setter")
    void deveCriarJutsuUsandoSetter() {
        Jutsu jutsu = new Jutsu();
        jutsu.setDano(60);
        jutsu.setConsumoDeChakra(30);
        
        assertEquals(60, jutsu.getDano());
        assertEquals(30, jutsu.getConsumoDeChakra());
    }
}
