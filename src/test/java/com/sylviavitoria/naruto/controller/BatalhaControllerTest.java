package com.sylviavitoria.naruto.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sylviavitoria.naruto.security.JwtAuthenticationFilter;
import com.sylviavitoria.naruto.security.JwtService;
import com.sylviavitoria.naruto.service.BatalhaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

@WebMvcTest(controllers = BatalhaController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
public class BatalhaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BatalhaService batalhaService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> resultadoBatalha;

    @BeforeEach
    void setUp() {
        resultadoBatalha = new HashMap<>();
        resultadoBatalha.put("vencedor", "Naruto Uzumaki");
        
        List<String> log = new ArrayList<>();
        log.add("Naruto Uzumaki usou Rasengan!");
        log.add("Sasuke Uchiha recebeu 70 de dano!");
        resultadoBatalha.put("log", log);
        
        Map<String, Object> statusAtacante = new HashMap<>();
        statusAtacante.put("vida", 100);
        statusAtacante.put("chakra", 70);
        resultadoBatalha.put("statusAtacante", statusAtacante);
        
        Map<String, Object> statusDefensor = new HashMap<>();
        statusDefensor.put("vida", 30);
        statusDefensor.put("chakra", 100);
        resultadoBatalha.put("statusDefensor", statusDefensor);
    }

    @Test
    @DisplayName("Deve realizar ataque com sucesso")
    @WithMockUser
    void realizarAtaque_DeveRetornarResultadoDaBatalha() throws Exception {
        Long atacanteId = 1L;
        Long defensorId = 2L;
        String nomeJutsu = "Rasengan";

        when(batalhaService.realizarAtaque(atacanteId, defensorId, nomeJutsu))
            .thenReturn(resultadoBatalha);

        mockMvc.perform(post("/api/v1/batalhas/atacar/{atacanteId}/{defensorId}", atacanteId, defensorId)
                .param("nomeJutsu", nomeJutsu)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vencedor").value("Naruto Uzumaki"))
                .andExpect(jsonPath("$.log").isArray())
                .andExpect(jsonPath("$.statusAtacante.vida").value(100))
                .andExpect(jsonPath("$.statusAtacante.chakra").value(70))
                .andExpect(jsonPath("$.statusDefensor.vida").value(30))
                .andExpect(jsonPath("$.statusDefensor.chakra").value(100));

        verify(batalhaService).realizarAtaque(atacanteId, defensorId, nomeJutsu);
        verifyNoMoreInteractions(batalhaService);
    }

    @Test
    @DisplayName("Deve retornar erro quando atacante não existe")
    @WithMockUser
    void realizarAtaque_AtacanteInexistente_DeveRetornarErro() throws Exception {
        Long atacanteId = 999L;
        Long defensorId = 2L;
        String nomeJutsu = "Rasengan";

        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", "Atacante não encontrado");

        when(batalhaService.realizarAtaque(atacanteId, defensorId, nomeJutsu))
            .thenReturn(erro);

        mockMvc.perform(post("/api/v1/batalhas/atacar/{atacanteId}/{defensorId}", atacanteId, defensorId)
                .param("nomeJutsu", nomeJutsu)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.erro").value("Atacante não encontrado"));

        verify(batalhaService).realizarAtaque(atacanteId, defensorId, nomeJutsu);
        verifyNoMoreInteractions(batalhaService);
    }

    @Test
    @DisplayName("Deve retornar erro quando jutsu não existe")
    @WithMockUser
    void realizarAtaque_JutsuInexistente_DeveRetornarErro() throws Exception {
        Long atacanteId = 1L;
        Long defensorId = 2L;
        String nomeJutsu = "JutsuInexistente";

        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", "Jutsu não encontrado");
        List<String> log = new ArrayList<>();
        log.add("Naruto Uzumaki não conhece o jutsu JutsuInexistente");
        erro.put("log", log);

        when(batalhaService.realizarAtaque(atacanteId, defensorId, nomeJutsu))
            .thenReturn(erro);

        mockMvc.perform(post("/api/v1/batalhas/atacar/{atacanteId}/{defensorId}", atacanteId, defensorId)
                .param("nomeJutsu", nomeJutsu)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.erro").value("Jutsu não encontrado"))
                .andExpect(jsonPath("$.log[0]").value("Naruto Uzumaki não conhece o jutsu JutsuInexistente"));

        verify(batalhaService).realizarAtaque(atacanteId, defensorId, nomeJutsu);
        verifyNoMoreInteractions(batalhaService);
    }
}