package com.sylviavitoria.naruto.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sylviavitoria.naruto.model.Jutsu;
import com.sylviavitoria.naruto.model.NinjaDeNinjutsu;
import com.sylviavitoria.naruto.model.NinjaDeTaijutsu;
import com.sylviavitoria.naruto.model.Personagem;
import com.sylviavitoria.naruto.service.BatalhaService;
import com.sylviavitoria.naruto.service.PersonagemService;

@ExtendWith(MockitoExtension.class)
public class BatalhaServiceTest {

    @Mock
    private PersonagemService personagemService;

    @InjectMocks
    private BatalhaService batalhaService;

    private NinjaDeTaijutsu atacante;
    private NinjaDeNinjutsu defensor;
    private String nomeJutsu;
    private Jutsu jutsu;

    @BeforeEach
    void setUp() {
        atacante = new NinjaDeTaijutsu();
        atacante.setId(1L);
        atacante.setNome("Rock Lee");
        atacante.setIdade(17);
        atacante.setAldeia("Aldeia da Folha");
        atacante.setChakra(100);
        atacante.setVida(100);

        nomeJutsu = "Dynamic Entry";
        jutsu = new Jutsu(50, 20);
        atacante.adicionarJutsu(nomeJutsu, 50, 20);

        defensor = new NinjaDeNinjutsu();
        defensor.setId(2L);
        defensor.setNome("Naruto Uzumaki");
        defensor.setIdade(17);
        defensor.setAldeia("Aldeia da Folha");
        defensor.setChakra(100);
        defensor.setVida(100);
        defensor.adicionarJutsu("Rasengan", 70, 30);
    }

    @Test
    @DisplayName("Deve retornar erro quando o atacante não é um ninja")
    void deveRetornarErroQuandoAtacanteNaoENinja() {
        Personagem personagemNaoNinja = mock(Personagem.class);
        when(personagemService.buscarPorId(1L)).thenReturn(personagemNaoNinja);
        when(personagemService.buscarPorId(2L)).thenReturn(defensor);

        Map<String, Object> resultado = batalhaService.realizarAtaque(1L, 2L, nomeJutsu);

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("erro"));
        assertEquals("Atacante não é um ninja", resultado.get("erro"));
        verify(personagemService).buscarPorId(1L);
        verify(personagemService).buscarPorId(2L);
        verify(personagemService, never()).salvar(personagemNaoNinja);
        verify(personagemService, never()).salvar(defensor);
    }

    @Test
    @DisplayName("Deve retornar erro quando o defensor não é um ninja")
    void deveRetornarErroQuandoDefensorNaoENinja() {
        Personagem personagemNaoNinja = mock(Personagem.class);
        when(personagemService.buscarPorId(1L)).thenReturn(atacante);
        when(personagemService.buscarPorId(2L)).thenReturn(personagemNaoNinja);

        Map<String, Object> resultado = batalhaService.realizarAtaque(1L, 2L, nomeJutsu);

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("erro"));
        assertEquals("Defensor não é um ninja", resultado.get("erro"));
        verify(personagemService).buscarPorId(1L);
        verify(personagemService).buscarPorId(2L);
        verify(personagemService, never()).salvar(atacante);
        verify(personagemService, never()).salvar(personagemNaoNinja);
    }

    @Test
    @DisplayName("Deve retornar erro quando o jutsu não é encontrado")
    void deveRetornarErroQuandoJutsuNaoEEncontrado() {
        String jutsuInexistente = "Jutsu Inexistente";
        when(personagemService.buscarPorId(1L)).thenReturn(atacante);
        when(personagemService.buscarPorId(2L)).thenReturn(defensor);

        Map<String, Object> resultado = batalhaService.realizarAtaque(1L, 2L, jutsuInexistente);

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("erro"));
        assertEquals("Jutsu não encontrado", resultado.get("erro"));
        List<String> log = (List<String>) resultado.get("log");
        assertFalse(log.isEmpty());
        assertTrue(log.get(0).contains("não conhece o jutsu"));
        verify(personagemService).buscarPorId(1L);
        verify(personagemService).buscarPorId(2L);
        verify(personagemService, never()).salvar(atacante);
        verify(personagemService, never()).salvar(defensor);
    }

    @Test
    @DisplayName("Deve retornar vencedor quando o atacante não pode mais lutar")
    void deveRetornarVencedorQuandoAtacanteNaoPodeMaisLutar() {
        atacante.setVida(0);
        when(personagemService.buscarPorId(1L)).thenReturn(atacante);
        when(personagemService.buscarPorId(2L)).thenReturn(defensor);

        Map<String, Object> resultado = batalhaService.realizarAtaque(1L, 2L, nomeJutsu);

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("vencedor"));
        assertEquals(defensor.getNome(), resultado.get("vencedor"));
        List<String> log = (List<String>) resultado.get("log");
        assertFalse(log.isEmpty());
        assertTrue(log.get(0).contains("não pode mais lutar"));
        verify(personagemService).buscarPorId(1L);
        verify(personagemService).buscarPorId(2L);
        verify(personagemService, never()).salvar(atacante);
        verify(personagemService, never()).salvar(defensor);
    }

    @Test
    @DisplayName("Deve retornar vencedor quando o defensor não pode mais lutar")
    void deveRetornarVencedorQuandoDefensorNaoPodeMaisLutar() {
        defensor.setVida(0);
        when(personagemService.buscarPorId(1L)).thenReturn(atacante);
        when(personagemService.buscarPorId(2L)).thenReturn(defensor);

        Map<String, Object> resultado = batalhaService.realizarAtaque(1L, 2L, nomeJutsu);

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("vencedor"));
        assertEquals(atacante.getNome(), resultado.get("vencedor"));
        List<String> log = (List<String>) resultado.get("log");
        assertFalse(log.isEmpty());
        assertTrue(log.get(0).contains("não pode mais lutar"));
        verify(personagemService).buscarPorId(1L);
        verify(personagemService).buscarPorId(2L);
        verify(personagemService, never()).salvar(atacante);
        verify(personagemService, never()).salvar(defensor);
    }

    @Test
    @DisplayName("Deve retornar vencedor quando o defensor fica sem vida após o ataque")
    void deveRetornarVencedorQuandoDefensorFicaSemVidaAposAtaque() {
        defensor.setVida(30);
        NinjaDeTaijutsu atacanteSpy = spy(atacante);
        
        doAnswer(invocation -> {
            Personagem defensor = invocation.getArgument(1);
            defensor.receberDano(50);
            return "Rock Lee usou Dynamic Entry e causou dano fatal!";
        }).when(atacanteSpy).usarJutsu(nomeJutsu, defensor);

        when(personagemService.buscarPorId(1L)).thenReturn(atacanteSpy);
        when(personagemService.buscarPorId(2L)).thenReturn(defensor);
        when(personagemService.salvar(atacanteSpy)).thenReturn(atacanteSpy);
        when(personagemService.salvar(defensor)).thenReturn(defensor);

        Map<String, Object> resultado = batalhaService.realizarAtaque(1L, 2L, nomeJutsu);

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("vencedor"));
        assertEquals(atacanteSpy.getNome(), resultado.get("vencedor"));
        verify(personagemService).buscarPorId(1L);
        verify(personagemService).buscarPorId(2L);
        verify(personagemService).salvar(atacanteSpy);
        verify(personagemService).salvar(defensor);
        verify(atacanteSpy).usarJutsu(nomeJutsu, defensor);
    }

    @Test
    @DisplayName("Deve capturar e retornar exceções lançadas durante o ataque")
    void deveCapturarERetornarExcecoesLancadasDuranteAtaque() {
        String mensagemErro = "Erro ao buscar personagem";
        when(personagemService.buscarPorId(1L)).thenThrow(new RuntimeException(mensagemErro));

        Map<String, Object> resultado = batalhaService.realizarAtaque(1L, 2L, nomeJutsu);

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("erro"));
        assertEquals(mensagemErro, resultado.get("erro"));
        verify(personagemService).buscarPorId(1L);
        verify(personagemService, never()).buscarPorId(2L);
        verify(personagemService, never()).salvar(atacante);
        verify(personagemService, never()).salvar(defensor);
    }
}