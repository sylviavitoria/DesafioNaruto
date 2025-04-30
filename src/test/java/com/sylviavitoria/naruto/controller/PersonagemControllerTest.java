package com.sylviavitoria.naruto.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sylviavitoria.naruto.dto.JutsuDTO;
import com.sylviavitoria.naruto.dto.PersonagemAtualizarDTO;
import com.sylviavitoria.naruto.dto.PersonagemDTO;
import com.sylviavitoria.naruto.dto.PersonagemResponseDTO;
import com.sylviavitoria.naruto.model.NinjaDeNinjutsu;
import com.sylviavitoria.naruto.model.Personagem;
import com.sylviavitoria.naruto.security.JwtAuthenticationFilter;
import com.sylviavitoria.naruto.security.JwtService;
import com.sylviavitoria.naruto.service.PersonagemService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

@WebMvcTest(controllers = PersonagemController.class, excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
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
                JutsuDTO chidori = JutsuDTO.builder()
                                .nome("Chidori")
                                .dano(70)
                                .consumoDeChakra(30)
                                .build();

                jutsusDTO.put("Chidori", chidori);
                personagemDTO.setJutsus(jutsusDTO);

                personagemAtualizarDTO = new PersonagemAtualizarDTO();
                personagemAtualizarDTO.setNome("Naruto Uzumaki (Modo Sábio)");
                personagemAtualizarDTO.setChakra(150);
                personagemAtualizarDTO.setJutsus(Arrays.asList("Rasengan", "Rasenshuriken"));

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
        @DisplayName("Deve retornar lista paginada de personagens")
        @WithMockUser
        void listarTodos_DeveRetornarPersonagensPaginados() throws Exception {
                PersonagemResponseDTO dto = PersonagemResponseDTO.builder()
                                .id(1L)
                                .nome("Naruto Uzumaki")
                                .idade(17)
                                .aldeia("Aldeia da Folha")
                                .chakra(100)
                                .tipoNinja("NINJUTSU")
                                .jutsus(Arrays.asList("Rasengan", "Kage Bunshin no Jutsu"))
                                .build();

                Page<PersonagemResponseDTO> paginaDTO = new PageImpl<>(Arrays.asList(dto));

                when(personagemService.listarTodosDTO(0, 10, null)).thenReturn(paginaDTO);

                mockMvc.perform(get("/api/v1/personagens")
                                .param("page", "0")
                                .param("size", "10")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id").value(1))
                                .andExpect(jsonPath("$.content[0].nome").value("Naruto Uzumaki"))
                                .andExpect(jsonPath("$.totalElements").value(1));

                verify(personagemService).listarTodosDTO(0, 10, null);
        }

        @Test
        @DisplayName("Deve listar personagens paginados com ordenação")
        @WithMockUser
        void listarTodos_ComParametroDeOrdenacao_DeveRetornarPersonagensPaginados() throws Exception {
                PersonagemResponseDTO dto = PersonagemResponseDTO.builder()
                                .id(1L)
                                .nome("Naruto Uzumaki")
                                .idade(17)
                                .aldeia("Aldeia da Folha")
                                .chakra(100)
                                .tipoNinja("NINJUTSU")
                                .jutsus(Arrays.asList("Rasengan", "Kage Bunshin no Jutsu"))
                                .build();

                Page<PersonagemResponseDTO> paginaDTO = new PageImpl<>(Arrays.asList(dto));

                when(personagemService.listarTodosDTO(0, 10, "nome")).thenReturn(paginaDTO);

                mockMvc.perform(get("/api/v1/personagens")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "nome")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.content[0].id").value(1))
                                .andExpect(jsonPath("$.content[0].nome").value("Naruto Uzumaki"))
                                .andExpect(jsonPath("$.content[0].tipoNinja").value("NINJUTSU"));

                verify(personagemService).listarTodosDTO(0, 10, "nome");
        }

        @Test
        @DisplayName("Deve listar jutsus de um personagem")
        @WithMockUser
        void listarJutsus_PersonagemExistente_DeveListarJutsus() throws Exception {
                Long idPersonagem = 1L;
                when(personagemService.listarJutsus(idPersonagem)).thenReturn(jutsusMap);

                mockMvc.perform(get("/api/v1/personagens/1/jutsus")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.personagemId").value(1))
                                .andExpect(jsonPath("$.nome").value("Naruto Uzumaki"))
                                .andExpect(jsonPath("$.jutsus.Rasengan.dano").value(70))
                                .andExpect(jsonPath("$.jutsus.Rasengan.consumoDeChakra").value(30))
                                .andExpect(jsonPath("$.jutsus['Kage Bunshin no Jutsu'].dano").value(40))
                                .andExpect(jsonPath("$.jutsus['Kage Bunshin no Jutsu'].consumoDeChakra").value(20));

                verify(personagemService).listarJutsus(idPersonagem);
        }

        @Test
        @DisplayName("Deve retornar erro ao listar jutsus de personagem inexistente")
        @WithMockUser
        void listarJutsus_PersonagemInexistente_DeveRetornarErro() throws Exception {
                Long idPersonagemInexistente = 999L;
                when(personagemService.listarJutsus(idPersonagemInexistente))
                                .thenThrow(new RuntimeException("Personagem nao encontrado"));

                mockMvc.perform(get("/api/v1/personagens/999/jutsus")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isInternalServerError());

                verify(personagemService).listarJutsus(idPersonagemInexistente);
        }

        @Test
        @DisplayName("Deve buscar personagem por ID")
        @WithMockUser
        void buscarPorId_DeveRetornarPersonagem() throws Exception {
                Long id = 1L;
                PersonagemResponseDTO dto = PersonagemResponseDTO.builder()
                                .id(1L)
                                .nome("Naruto Uzumaki")
                                .idade(17)
                                .aldeia("Aldeia da Folha")
                                .chakra(100)
                                .tipoNinja("NINJUTSU")
                                .jutsus(Arrays.asList("Rasengan", "Kage Bunshin no Jutsu"))
                                .build();

                when(personagemService.buscarPorIdDTO(id)).thenReturn(dto);

                mockMvc.perform(get("/api/v1/personagens/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("Naruto Uzumaki"))
                                .andExpect(jsonPath("$.tipoNinja").value("NINJUTSU"));

                verify(personagemService).buscarPorIdDTO(id);
        }

        @Test
        @DisplayName("Deve criar novo personagem com sucesso")
        @WithMockUser(roles = "ADMIN")
        void criar_DeveRetornarPersonagemCriado() throws Exception {
                PersonagemDTO requestDto = new PersonagemDTO();
                requestDto.setTipoNinja("NINJUTSU");
                requestDto.setNome("Naruto Uzumaki");
                requestDto.setIdade(17);
                requestDto.setAldeia("Aldeia da Folha");
                requestDto.setChakra(100);

                PersonagemResponseDTO responseDto = PersonagemResponseDTO.builder()
                                .id(1L)
                                .nome("Naruto Uzumaki")
                                .idade(17)
                                .aldeia("Aldeia da Folha")
                                .chakra(100)
                                .tipoNinja("NINJUTSU")
                                .jutsus(Arrays.asList("Rasengan", "Kage Bunshin no Jutsu"))
                                .build();

                when(personagemService.criarPersonagemDTO(requestDto)).thenReturn(responseDto);

                mockMvc.perform(post("/api/v1/personagens")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("Naruto Uzumaki"))
                                .andExpect(jsonPath("$.tipoNinja").value("NINJUTSU"));

                verify(personagemService).criarPersonagemDTO(requestDto);
        }

        @Test
        @DisplayName("Deve atualizar personagem com sucesso")
        @WithMockUser
        void atualizar_DeveRetornarPersonagemAtualizado() throws Exception {
                Long id = 1L;
                PersonagemAtualizarDTO requestDto = new PersonagemAtualizarDTO();
                requestDto.setNome("Naruto Uzumaki (Modo Sábio)");
                requestDto.setChakra(150);
                requestDto.setJutsus(Arrays.asList("Rasengan", "Rasenshuriken"));

                PersonagemResponseDTO responseDto = PersonagemResponseDTO.builder()
                                .id(1L)
                                .nome("Naruto Uzumaki (Modo Sábio)") 
                                .idade(17)
                                .aldeia("Aldeia da Folha")
                                .chakra(150)
                                .vida(100)
                                .tipoNinja("NINJUTSU")
                                .jutsus(Arrays.asList("Rasengan", "Rasenshuriken")) 
                                .build();

                when(personagemService.atualizarPersonagemDTO(id, requestDto)).thenReturn(responseDto);

                mockMvc.perform(put("/api/v1/personagens/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("Naruto Uzumaki (Modo Sábio)"))
                                .andExpect(jsonPath("$.chakra").value(150))
                                .andExpect(jsonPath("$.jutsus").isArray())
                                .andExpect(jsonPath("$.jutsus", hasSize(2)))
                                .andExpect(jsonPath("$.jutsus[0]").value("Rasengan"))
                                .andExpect(jsonPath("$.jutsus[1]").value("Rasenshuriken"));

                verify(personagemService).atualizarPersonagemDTO(id, requestDto);
        }

        @Test
        @DisplayName("Deve deletar personagem com sucesso")
        @WithMockUser
        void deletar_DeveRetornarNoContent() throws Exception {
                Long id = 1L;
                doNothing().when(personagemService).deletar(id);

                mockMvc.perform(delete("/api/v1/personagens/{id}", id))
                                .andExpect(status().isNoContent());

                verify(personagemService).deletar(id);
        }

        @Test
        @DisplayName("Deve adicionar jutsu ao personagem com sucesso")
        @WithMockUser
        void adicionarJutsu_DeveRetornarPersonagemAtualizado() throws Exception {
                Long id = 1L;
                JutsuDTO jutsuDTO = JutsuDTO.builder()
                                .nome("Rasenshuriken")
                                .dano(100)
                                .consumoDeChakra(50)
                                .build();

                PersonagemResponseDTO responseDto = PersonagemResponseDTO.builder()
                                .id(1L)
                                .nome("Naruto Uzumaki")
                                .idade(17)
                                .aldeia("Aldeia da Folha")
                                .chakra(100)
                                .tipoNinja("NINJUTSU")
                                .jutsus(Arrays.asList("Rasengan", "Rasenshuriken"))
                                .build();

                when(personagemService.adicionarJutsuDTO(id, jutsuDTO)).thenReturn(responseDto);

                mockMvc.perform(post("/api/v1/personagens/{id}/jutsus", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jutsuDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.jutsus").isArray())
                                .andExpect(jsonPath("$.jutsus", hasSize(2)))
                                .andExpect(jsonPath("$.jutsus[1]").value("Rasenshuriken"));

                verify(personagemService).adicionarJutsuDTO(id, jutsuDTO);
        }
}
