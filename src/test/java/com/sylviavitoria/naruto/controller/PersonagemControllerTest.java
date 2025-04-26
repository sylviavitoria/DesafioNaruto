package com.sylviavitoria.naruto.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sylviavitoria.naruto.dto.JutsuDTO;
import com.sylviavitoria.naruto.dto.PersonagemAtualizarDTO;
import com.sylviavitoria.naruto.dto.PersonagemDTO;
import com.sylviavitoria.naruto.model.NinjaDeNinjutsu;
import com.sylviavitoria.naruto.model.Personagem;
import com.sylviavitoria.naruto.security.JwtAuthenticationFilter;
import com.sylviavitoria.naruto.security.JwtService;
import com.sylviavitoria.naruto.service.PersonagemService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

@WebMvcTest(controllers = PersonagemController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
public class PersonagemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonagemService personagemService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private Personagem personagem;
    private PersonagemDTO personagemDTO;
    private PersonagemAtualizarDTO personagemAtualizarDTO;
    private JutsuDTO jutsuDTO;
    private Map<String, Object> jutsusMap;

    @BeforeEach
    void setUp() {
        personagem = new NinjaDeNinjutsu();
        personagem.setId(1L);
        personagem.setNome("Naruto Uzumaki");
        personagem.setIdade(17);
        personagem.setAldeia("Aldeia da Folha");
        personagem.setChakra(100);

        personagem.adicionarJutsu("Rasengan", 70, 30);
        personagem.adicionarJutsu("Kage Bunshin no Jutsu", 40, 20);

        personagemDTO = new PersonagemDTO();
        personagemDTO.setTipoNinja("NINJUTSU");
        personagemDTO.setNome("Sasuke Uchiha");
        personagemDTO.setIdade(17);
        personagemDTO.setAldeia("Aldeia da Folha");
        personagemDTO.setChakra(90);

        Map<String, JutsuDTO> jutsusDTO = new HashMap<>();
        JutsuDTO chidori = new JutsuDTO();
        chidori.setNome("Chidori");
        chidori.setDano(70);
        chidori.setConsumoDeChakra(30);
        jutsusDTO.put("Chidori", chidori);

        JutsuDTO sharingan = new JutsuDTO();
        sharingan.setNome("Sharingan");
        sharingan.setDano(40);
        sharingan.setConsumoDeChakra(20);
        jutsusDTO.put("Sharingan", sharingan);

        personagemDTO.setJutsus(jutsusDTO);

        personagemAtualizarDTO = new PersonagemAtualizarDTO();
        personagemAtualizarDTO.setNome("Naruto Uzumaki (Modo Sábio)");
        personagemAtualizarDTO.setChakra(150);
        personagemAtualizarDTO.setJutsus(Arrays.asList("Rasengan", "Rasenshuriken"));

        jutsuDTO = new JutsuDTO();
        jutsuDTO.setNome("Rasenshuriken");
        jutsuDTO.setDano(100);
        jutsuDTO.setConsumoDeChakra(50);

        jutsusMap = new HashMap<>();
        jutsusMap.put("personagemId", 1L);
        jutsusMap.put("nome", "Naruto Uzumaki");

        Map<String, Map<String, Object>> jutsuDetalhes = new HashMap<>();
        Map<String, Object> rasengan = new HashMap<>();
        rasengan.put("dano", 70);
        rasengan.put("consumoDeChakra", 30);
        jutsuDetalhes.put("Rasengan", rasengan);

        Map<String, Object> kageBunshin = new HashMap<>();
        kageBunshin.put("dano", 40);
        kageBunshin.put("consumoDeChakra", 20);
        jutsuDetalhes.put("Kage Bunshin no Jutsu", kageBunshin);

        jutsusMap.put("jutsus", jutsuDetalhes);
    }


    @Test
    @DisplayName("Deve retornar status 500 quando personagem não existir")
    @WithMockUser
    void buscarPorId_QuandoPersonagemNaoExiste_DeveRetornarStatus500() throws Exception {
        when(personagemService.buscarPorId(anyLong()))
                .thenThrow(new RuntimeException("Personagem nao encontrado"));

        mockMvc.perform(get("/api/v1/personagens/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(personagemService).buscarPorId(999L);
    }

    @Test
    @DisplayName("Deve retornar status 500 quando ID for inválido")
    @WithMockUser
    void buscarPorId_QuandoIdInvalido_DeveRetornarStatus500() throws Exception {
        mockMvc.perform(get("/api/v1/personagens/abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Deve retornar lista paginada de personagens")
    @WithMockUser
    void listarTodos_DeveRetornarPersonagensPaginados() throws Exception {
        List<Personagem> personagens = Arrays.asList(personagem);
        PageImpl<Personagem> paginaPersonagens = new PageImpl<>(personagens);
        when(personagemService.listarTodos(any(Pageable.class))).thenReturn(paginaPersonagens);

        mockMvc.perform(get("/api/v1/personagens")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Naruto Uzumaki"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(personagemService).listarTodos(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve listar personagens paginados com ordenação")
    @WithMockUser
    void listarTodos_ComParametroDeOrdenacao_DeveRetornarPersonagensPaginados() throws Exception {
        List<Personagem> personagens = Arrays.asList(personagem);
        PageImpl<Personagem> paginaPersonagens = new PageImpl<>(personagens);
        when(personagemService.listarTodos(any(Pageable.class))).thenReturn(paginaPersonagens);

        mockMvc.perform(get("/api/v1/personagens")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "nome")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Naruto Uzumaki"));

        verify(personagemService).listarTodos(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar status 403 ao tentar criar personagem sem permissão")
    @WithMockUser 
    void criar_SemPermissao_DeveRetornarForbidden() throws Exception {
        mockMvc.perform(post("/api/v1/personagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personagemDTO)))
                .andExpect(status().isForbidden());

        verify(personagemService, never()).criarPersonagem(any(PersonagemDTO.class));
    }

    @Test
    @DisplayName("Deve listar jutsus de um personagem")
    @WithMockUser
    void listarJutsus_PersonagemExistente_DeveListarJutsus() throws Exception {
        when(personagemService.listarJutsus(1L)).thenReturn(jutsusMap);

        mockMvc.perform(get("/api/v1/personagens/1/jutsus")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personagemId").value(1))
                .andExpect(jsonPath("$.nome").value("Naruto Uzumaki"))
                .andExpect(jsonPath("$.jutsus.Rasengan.dano").value(70))
                .andExpect(jsonPath("$.jutsus.Rasengan.consumoDeChakra").value(30))
                .andExpect(jsonPath("$.jutsus['Kage Bunshin no Jutsu'].dano").value(40))
                .andExpect(jsonPath("$.jutsus['Kage Bunshin no Jutsu'].consumoDeChakra").value(20));

        verify(personagemService).listarJutsus(1L);
    }

    @Test
    @DisplayName("Deve retornar erro ao listar jutsus de personagem inexistente")
    @WithMockUser
    void listarJutsus_PersonagemInexistente_DeveRetornarErro() throws Exception {
        when(personagemService.listarJutsus(999L))
                .thenThrow(new RuntimeException("Personagem nao encontrado"));

        mockMvc.perform(get("/api/v1/personagens/999/jutsus")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(personagemService).listarJutsus(999L);
    }
}