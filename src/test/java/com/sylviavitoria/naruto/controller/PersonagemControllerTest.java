package com.sylviavitoria.naruto.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sylviavitoria.naruto.dto.PersonagemAtualizarDTO;
import com.sylviavitoria.naruto.dto.PersonagemDTO;
import com.sylviavitoria.naruto.model.NinjaDeNinjutsu;
import com.sylviavitoria.naruto.model.Personagem;
import com.sylviavitoria.naruto.security.JwtAuthenticationFilter;
import com.sylviavitoria.naruto.security.JwtService;
import com.sylviavitoria.naruto.service.PersonagemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        @BeforeEach
        void setUp() {
                personagem = new NinjaDeNinjutsu();
                personagem.setId(1L);
                personagem.setNome("Naruto Uzumaki");
                personagem.setIdade(17);
                personagem.setAldeia("Aldeia da Folha");
                personagem.setChakra(100);
                personagem.setJutsus(Arrays.asList("Rasengan", "Kage Bunshin no Jutsu"));

                personagemDTO = new PersonagemDTO();
                personagemDTO.setTipoNinja("NINJUTSU");
                personagemDTO.setNome("Sasuke Uchiha");
                personagemDTO.setIdade(17);
                personagemDTO.setAldeia("Aldeia da Folha");
                personagemDTO.setChakra(90);
                personagemDTO.setJutsus(Arrays.asList("Chidori", "Sharingan"));

                personagemAtualizarDTO = new PersonagemAtualizarDTO();
                personagemAtualizarDTO.setNome("Naruto Uzumaki (Modo Sábio)");
                personagemAtualizarDTO.setChakra(150);
        }

        @Test
        @WithMockUser
        void buscarPorId_QuandoPersonagemExiste_DeveRetornarPersonagem() throws Exception {
                when(personagemService.buscarPorId(1L)).thenReturn(personagem);

                mockMvc.perform(get("/api/v1/personagens/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("Naruto Uzumaki"))
                                .andExpect(jsonPath("$.idade").value(17))
                                .andExpect(jsonPath("$.aldeia").value("Aldeia da Folha"))
                                .andExpect(jsonPath("$.chakra").value(100))
                                .andExpect(jsonPath("$.jutsus[0]").value("Rasengan"))
                                .andExpect(jsonPath("$.jutsus[1]").value("Kage Bunshin no Jutsu"));
        }

        @Test
        @WithMockUser
        void buscarPorId_QuandoPersonagemNaoExiste_DeveRetornarStatus404() throws Exception {
                when(personagemService.buscarPorId(anyLong()))
                                .thenThrow(new RuntimeException("Personagem nao encontrado"));

                mockMvc.perform(get("/api/v1/personagens/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isInternalServerError());
        }

        @Test
        @WithMockUser
        void buscarPorId_QuandoIdInvalido_DeveRetornarStatus500() throws Exception {
                mockMvc.perform(get("/api/v1/personagens/abc")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isInternalServerError());
        }

        @Test
        @WithMockUser
        void listarTodos_DeveRetornarPersonagensPaginados() throws Exception {
                List<Personagem> personagens = Arrays.asList(personagem);
                Page<Personagem> paginaPersonagens = new PageImpl<>(personagens);
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
        }

        @Test
        @WithMockUser
        void listarTodos_ComParametroDeOrdenacao_DeveRetornarPersonagensPaginados() throws Exception {
                List<Personagem> personagens = Arrays.asList(personagem);
                Page<Personagem> paginaPersonagens = new PageImpl<>(personagens);
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
        }

        @Test
        @WithMockUser
        void usarJutsu_QuandoPersonagemExiste_DeveRetornarMensagemJutsu() throws Exception {
                Map<String, Object> resultado = new HashMap<>();
                resultado.put("nome", "Naruto Uzumaki");
                resultado.put("tipoNinja", "Ninjutsu");
                resultado.put("mensagem", "Naruto Uzumaki está usando um jutsu de Ninjutsu!");

                when(personagemService.usarJutsu(anyLong())).thenReturn(resultado);

                mockMvc.perform(get("/api/v1/personagens/1/usar-jutsu")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.nome").value("Naruto Uzumaki"))
                                .andExpect(jsonPath("$.tipoNinja").value("Ninjutsu"))
                                .andExpect(jsonPath("$.mensagem")
                                                .value("Naruto Uzumaki está usando um jutsu de Ninjutsu!"));

                verify(personagemService, times(1)).usarJutsu(1L);
        }

        @Test
        @WithMockUser
        void usarJutsu_QuandoPersonagemNaoExiste_DeveRetornarStatus404() throws Exception {
                when(personagemService.usarJutsu(anyLong()))
                                .thenThrow(new RuntimeException("Personagem nao encontrado"));

                mockMvc.perform(get("/api/v1/personagens/999/usar-jutsu")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isInternalServerError());

                verify(personagemService, times(1)).usarJutsu(999L);
        }

        @Test
        @WithMockUser
        void usarJutsu_QuandoPersonagemNaoENinja_DeveRetornarStatus400() throws Exception {
                when(personagemService.usarJutsu(anyLong()))
                                .thenThrow(new IllegalArgumentException("Personagem não é um ninja."));

                mockMvc.perform(get("/api/v1/personagens/1/usar-jutsu")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest()); 

                verify(personagemService, times(1)).usarJutsu(1L);
        }

        @Test
        @WithMockUser
        void desviar_QuandoPersonagemNaoENinja_DeveRetornarStatus400() throws Exception {
                when(personagemService.desviar(anyLong()))
                                .thenThrow(new IllegalArgumentException("Personagem não é um ninja."));

                mockMvc.perform(get("/api/v1/personagens/1/desviar")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest()); 

                verify(personagemService, times(1)).desviar(1L);
        }

        @Test
        @WithMockUser
        void desviar_QuandoPersonagemExiste_DeveRetornarMensagemDesvio() throws Exception {
                Map<String, Object> resultado = new HashMap<>();
                resultado.put("nome", "Naruto Uzumaki");
                resultado.put("tipoNinja", "Ninjutsu");
                resultado.put("mensagem", "Naruto Uzumaki está desviando usando suas habilidades de Ninjutsu!");

                when(personagemService.desviar(anyLong())).thenReturn(resultado);

                mockMvc.perform(get("/api/v1/personagens/1/desviar")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.nome").value("Naruto Uzumaki"))
                                .andExpect(jsonPath("$.tipoNinja").value("Ninjutsu"))
                                .andExpect(jsonPath("$.mensagem")
                                                .value("Naruto Uzumaki está desviando usando suas habilidades de Ninjutsu!"));

                verify(personagemService, times(1)).desviar(1L);
        }

        @Test
        @WithMockUser
        void desviar_QuandoPersonagemNaoExiste_DeveRetornarStatus404() throws Exception {
                when(personagemService.desviar(anyLong()))
                                .thenThrow(new RuntimeException("Personagem nao encontrado"));

                mockMvc.perform(get("/api/v1/personagens/999/desviar")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isInternalServerError());

                verify(personagemService, times(1)).desviar(999L);
        }

}