package com.sylviavitoria.naruto.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonagemTest {

    private static class PersonagemImpl extends Personagem {
        public PersonagemImpl(String nome, int idade, String aldeia, int chakra) {
            super(nome, idade, aldeia, chakra);
        }
    }

    private Personagem personagem;

    @BeforeEach
    void setUp() {
        personagem = new PersonagemImpl("Naruto", 17, "Aldeia da Folha", 100);
    }

    @Test
    @DisplayName("Deve criar personagem com valores corretos")
    void deveCriarPersonagemComValoresCorretos() {
        assertEquals("Naruto", personagem.getNome());
        assertEquals(17, personagem.getIdade());
        assertEquals("Aldeia da Folha", personagem.getAldeia());
        assertEquals(100, personagem.getChakra());
        assertEquals(100, personagem.getVida());
        assertTrue(personagem.getJutsusMap().isEmpty());
    }

    @Test
    @DisplayName("Deve adicionar jutsu corretamente")
    void deveAdicionarJutsuCorretamente() {
        personagem.adicionarJutsu("Rasengan", 70, 30);

        assertTrue(personagem.getJutsusMap().containsKey("Rasengan"));
        assertEquals(70, personagem.getJutsu("Rasengan").getDano());
        assertEquals(30, personagem.getJutsu("Rasengan").getConsumoDeChakra());
    }

    @Test
    @DisplayName("Deve retornar lista de jutsus corretamente")
    void deveRetornarListaDeJutsusCorretamente() {
        personagem.adicionarJutsu("Rasengan", 70, 30);
        personagem.adicionarJutsu("Kage Bunshin", 40, 20);

        assertEquals(2, personagem.getJutsus().size());
        assertTrue(personagem.getJutsus().contains("Rasengan"));
        assertTrue(personagem.getJutsus().contains("Kage Bunshin"));
    }

    @Test
    @DisplayName("Deve aumentar chakra corretamente")
    void deveAumentarChakraCorretamente() {
        personagem.aumentarChakra(50);
        assertEquals(150, personagem.getChakra());
    }

    @Test
    @DisplayName("Deve diminuir chakra corretamente")
    void deveDiminuirChakraCorretamente() {
        personagem.diminuirChakra(30);
        assertEquals(70, personagem.getChakra());
    }

    @Test
    @DisplayName("Não deve permitir chakra negativo ao diminuir")
    void naoDevePermitirChakraNegativoAoDiminuir() {
        personagem.diminuirChakra(150);
        assertEquals(0, personagem.getChakra());
    }

    @Test
    @DisplayName("Deve receber dano corretamente")
    void deveReceberDanoCorretamente() {
        personagem.receberDano(30);
        assertEquals(70, personagem.getVida());
    }

    @Test
    @DisplayName("Não deve permitir vida negativa ao receber dano")
    void naoDevePermitirVidaNegativaAoReceberDano() {
        personagem.receberDano(150);
        assertEquals(0, personagem.getVida());
    }

    @Test
    @DisplayName("Deve gerar número aleatório dentro do intervalo")
    void deveGerarNumeroAleatorioNoIntervalo() {
        int min = 1;
        int max = 10;
        for (int i = 0; i < 100; i++) {
            int numero = personagem.gerarNumeroAleatorio(min, max);
            assertTrue(numero >= min && numero <= max);
        }
    }

    @Test
    @DisplayName("Deve retornar true para podeLutar quando vida e chakra são maiores que zero")
    void deveRetornarTrueParaPodeLutarQuandoVidaEChakraMaioresQueZero() {
        assertTrue(personagem.podeLutar());
    }

    @Test
    @DisplayName("Deve retornar false para podeLutar quando vida é zero")
    void deveRetornarFalseParaPodeLutarQuandoVidaZero() {
        personagem.receberDano(100);
        assertFalse(personagem.podeLutar());
    }

    @Test
    @DisplayName("Deve retornar false para podeLutar quando chakra é zero")
    void deveRetornarFalseParaPodeLutarQuandoChakraZero() {
        personagem.diminuirChakra(100);
        assertFalse(personagem.podeLutar());
    }

    @Test
    @DisplayName("Deve exibir informações corretamente")
    void deveExibirInformacoesCorretamente() {
        personagem.adicionarJutsu("Rasengan", 70, 30);
        
        String infoEsperada = String.format("Nome: Naruto\n" +
                "Idade: 17\n" +
                "Aldeia: Aldeia da Folha\n" +
                "Vida: 100\n" +
                "Chakra: 100\n" +
                "Jutsus:\n" +
                "- Rasengan (Dano: 70, Consumo: 30)\n");
        
        assertEquals(infoEsperada, personagem.exibirInformacoes());
    }
}