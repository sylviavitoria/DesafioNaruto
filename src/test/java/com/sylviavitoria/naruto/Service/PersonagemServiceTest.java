package com.sylviavitoria.naruto.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.sylviavitoria.naruto.dto.JutsuDTO;
import com.sylviavitoria.naruto.dto.PersonagemAtualizarDTO;
import com.sylviavitoria.naruto.dto.PersonagemDTO;
import com.sylviavitoria.naruto.mapper.JutsuMapper;
import com.sylviavitoria.naruto.mapper.PersonagemMapper;
import com.sylviavitoria.naruto.model.Jutsu;
import com.sylviavitoria.naruto.model.NinjaDeGenjutsu;
import com.sylviavitoria.naruto.model.NinjaDeNinjutsu;
import com.sylviavitoria.naruto.model.NinjaDeTaijutsu;
import com.sylviavitoria.naruto.model.Personagem;
import com.sylviavitoria.naruto.repository.PersonagemRepository;
import com.sylviavitoria.naruto.service.PersonagemService;

@ExtendWith(MockitoExtension.class)
public class PersonagemServiceTest {

    @Mock
    private PersonagemRepository repository;

    @InjectMocks
    private PersonagemService service;

    @Mock
    private PersonagemMapper personagemMapper;
    
    @Mock
    private JutsuMapper jutsuMapper;

    private NinjaDeTaijutsu personagemTaijutsu;
    private NinjaDeNinjutsu personagemNinjutsu;
    private NinjaDeGenjutsu personagemGenjutsu;
    private PageImpl<Personagem> pagePersonagens;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        personagemTaijutsu = new NinjaDeTaijutsu();
        personagemTaijutsu.setId(1L);
        personagemTaijutsu.setNome("Rock Lee");
        personagemTaijutsu.setIdade(16);
        personagemTaijutsu.setAldeia("Aldeia da Folha");
        personagemTaijutsu.setChakra(100);
        personagemTaijutsu.adicionarJutsu("Dynamic Entry", 50, 20);
        personagemTaijutsu.adicionarJutsu("Leaf Hurricane", 60, 25);

        personagemNinjutsu = new NinjaDeNinjutsu();
        personagemNinjutsu.setId(2L);
        personagemNinjutsu.setNome("Naruto Uzumaki");
        personagemNinjutsu.setIdade(17);
        personagemNinjutsu.setAldeia("Aldeia da Folha");
        personagemNinjutsu.setChakra(500);
        personagemNinjutsu.adicionarJutsu("Rasengan", 70, 30);
        personagemNinjutsu.adicionarJutsu("Kage Bunshin no Jutsu", 40, 20);

        personagemGenjutsu = new NinjaDeGenjutsu();
        personagemGenjutsu.setId(3L);
        personagemGenjutsu.setNome("Itachi Uchiha");
        personagemGenjutsu.setIdade(21);
        personagemGenjutsu.setAldeia("Aldeia da Folha");
        personagemGenjutsu.setChakra(300);
        personagemGenjutsu.adicionarJutsu("Tsukuyomi", 80, 40);
        personagemGenjutsu.adicionarJutsu("Amaterasu", 90, 45);

        List<Personagem> personagens = Arrays.asList(personagemTaijutsu, personagemNinjutsu, personagemGenjutsu);
        pagePersonagens = new PageImpl<>(personagens);
        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Deve listar todos os personagens com paginação")
    void deveListarTodosComPaginacao() {
        when(repository.findAll(pageable)).thenReturn(pagePersonagens);

        Page<Personagem> resultado = service.listarTodos(pageable);

        assertNotNull(resultado);
        assertEquals(3, resultado.getTotalElements());
        assertEquals(pagePersonagens, resultado);
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve buscar personagem por ID com sucesso")
    void deveBuscarPorIdComSucesso() {
        Long idDePersonagem = 1L;
        when(repository.findById(idDePersonagem)).thenReturn(Optional.of(personagemTaijutsu));

        Personagem resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(personagemTaijutsu.getId(), resultado.getId());
        assertEquals(personagemTaijutsu.getNome(), resultado.getNome());
        verify(repository).findById(idDePersonagem);
    }

    @Test
    @DisplayName("Deve lançar exceção quando buscar personagem inexistente")
    void deveLancarExcecaoQuandoBuscarPersonagemInexistente() {
        Long idDePersonagemInexistente = 99L;
        when(repository.findById(idDePersonagemInexistente)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.buscarPorId(idDePersonagemInexistente);
        });

        assertEquals("Personagem não encontrado com ID: 99", exception.getMessage());
        verify(repository).findById(idDePersonagemInexistente);
    }

    @Test
    @DisplayName("Deve salvar personagem com sucesso")
    void deveSalvarPersonagemComSucesso() {
        when(repository.save(personagemTaijutsu)).thenReturn(personagemTaijutsu);

        Personagem resultado = service.salvar(personagemTaijutsu);

        assertNotNull(resultado);
        assertEquals(personagemTaijutsu.getId(), resultado.getId());
        assertEquals(personagemTaijutsu.getNome(), resultado.getNome());
        verify(repository).save(personagemTaijutsu);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar personagem com chakra negativo")
    void deveLancarExcecaoAoTentarSalvarPersonagemComChakraNegativo() {

        NinjaDeTaijutsu personagem = new NinjaDeTaijutsu();
        personagem.setNome("Rock Lee");
        personagem.setIdade(16);
        personagem.setAldeia("Aldeia da Folha");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personagem.setChakra(-10);
        });

        assertEquals("Chakra não pode ser negativo", exception.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar personagem sem nome")
    void deveLancarExcecaoAoTentarSalvarPersonagemSemNome() {
        NinjaDeTaijutsu personagem = new NinjaDeTaijutsu();
        personagem.setIdade(16);
        personagem.setAldeia("Aldeia da Folha");
        personagem.setChakra(100);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personagem.setNome("");
        });

        assertEquals("Nome não pode ser vazio ou nulo", exception.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar personagem com nome nulo")
    void deveLancarExcecaoAoTentarSalvarPersonagemComNomeNulo() {

        NinjaDeTaijutsu personagem = new NinjaDeTaijutsu();
        personagem.setIdade(16);
        personagem.setAldeia("Aldeia da Folha");
        personagem.setChakra(100);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            personagem.setNome(null);
        });

        assertEquals("Nome não pode ser vazio ou nulo", exception.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve deletar personagem com sucesso")
    void deveDeletarPersonagemComSucesso() {
        Long idDeletar = 1L;
        when(repository.existsById(idDeletar)).thenReturn(true);
        doNothing().when(repository).deleteById(idDeletar);

        service.deletar(idDeletar);

        verify(repository).existsById(idDeletar);
        verify(repository).deleteById(idDeletar);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar personagem inexistente")
    void deveLancarExcecaoAoTentarDeletarPersonagemInexistente() {
        Long idDeletarInexistente = 99L;
        when(repository.existsById(idDeletarInexistente)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.deletar(idDeletarInexistente);
        });

        assertEquals("Personagem com ID 99 nao existe", exception.getMessage());
        verify(repository).existsById(idDeletarInexistente);
        verifyNoMoreInteractions(repository);
    }


    @Test
    @DisplayName("Deve criar personagem do tipo Taijutsu")
    void deveCriarPersonagemDoTipoTaijutsu() {

        PersonagemDTO dto = new PersonagemDTO();
        dto.setTipoNinja("TAIJUTSU");
        dto.setNome("Rock Lee");
        dto.setIdade(16);
        dto.setAldeia("Aldeia da Folha");
        dto.setChakra(100);

        NinjaDeTaijutsu personagemEsperado = new NinjaDeTaijutsu();
        personagemEsperado.setNome(dto.getNome());
        personagemEsperado.setIdade(dto.getIdade());
        personagemEsperado.setAldeia(dto.getAldeia());
        personagemEsperado.setChakra(dto.getChakra());

        when(repository.save(personagemEsperado)).thenReturn(personagemEsperado);

        Personagem resultado = service.criarPersonagem(dto);

        assertNotNull(resultado);
        assertEquals(dto.getNome(), resultado.getNome());
        assertEquals(dto.getIdade(), resultado.getIdade());
        assertEquals(dto.getAldeia(), resultado.getAldeia());
        assertEquals(dto.getChakra(), resultado.getChakra());

        verify(repository).save(personagemEsperado);
    }

    @Test
    @DisplayName("Deve criar personagem do tipo Ninjutsu")
    void deveCriarPersonagemDoTipoNinjutsu() {
        PersonagemDTO dto = new PersonagemDTO();
        dto.setTipoNinja("NINJUTSU");
        dto.setNome("Naruto Uzumaki");
        dto.setIdade(17);
        dto.setAldeia("Aldeia da Folha");
        dto.setChakra(500);

        Map<String, JutsuDTO> jutsusMap = new HashMap<>();
        JutsuDTO rasengan = JutsuDTO.builder()
            .nome("Rasengan")
            .dano(70)
            .consumoDeChakra(30)
            .build();
        jutsusMap.put("Rasengan", rasengan);

        dto.setJutsus(jutsusMap);

        Jutsu jutsuObj = new Jutsu(70, 30);
        when(jutsuMapper.converterParaJutsu(rasengan)).thenReturn(jutsuObj);

        NinjaDeNinjutsu personagemEsperado = new NinjaDeNinjutsu();
        personagemEsperado.setNome(dto.getNome());
        personagemEsperado.setIdade(dto.getIdade());
        personagemEsperado.setAldeia(dto.getAldeia());
        personagemEsperado.setChakra(dto.getChakra());
        personagemEsperado.adicionarJutsu("Rasengan", 70, 30);

        when(repository.save(eq(personagemEsperado))).thenReturn(personagemEsperado);

        Personagem resultado = service.criarPersonagem(dto);

        assertNotNull(resultado);
        assertTrue(resultado instanceof NinjaDeNinjutsu);
        assertEquals(dto.getNome(), resultado.getNome());
        assertEquals(dto.getIdade(), resultado.getIdade());
        assertEquals(dto.getAldeia(), resultado.getAldeia());
        assertEquals(dto.getChakra(), resultado.getChakra());
        assertEquals(dto.getJutsus().keySet(), resultado.getJutsusMap().keySet());
        
        verify(repository).save(eq(personagemEsperado));
        verify(jutsuMapper).converterParaJutsu(rasengan);
    }

    @Test
    @DisplayName("Deve adicionar jutsu ao personagem com sucesso")
    void deveAdicionarJutsuComSucesso() {
        Long idPersonagem = 1L;
        String nomeJutsu = "Novo Jutsu";
        int dano = 65;
        int consumo = 25;

        personagemTaijutsu.adicionarJutsu(nomeJutsu, dano, consumo);

        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));
        when(repository.save(personagemTaijutsu)).thenReturn(personagemTaijutsu);

        JutsuDTO jutsuDTO = JutsuDTO.builder()
            .nome(nomeJutsu)
            .dano(dano)
            .consumoDeChakra(consumo)
            .build();

        Personagem resultado = service.adicionarJutsu(idPersonagem, jutsuDTO);

        assertNotNull(resultado);
        assertTrue(resultado.getJutsusMap().containsKey(nomeJutsu));
        assertEquals(dano, resultado.getJutsu(nomeJutsu).getDano());
        assertEquals(consumo, resultado.getJutsu(nomeJutsu).getConsumoDeChakra());

        verify(repository).findById(idPersonagem);
        verify(repository).save(personagemTaijutsu);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar adicionar jutsu com dano inválido")
    void deveLancarExcecaoAoTentarAdicionarJutsuComDanoInvalido() {
        Long idPersonagem = 1L;
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));

        JutsuDTO jutsuDTO = JutsuDTO.builder()
            .nome("Jutsu Inválido")
            .dano(0)
            .consumoDeChakra(20)
            .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.adicionarJutsu(idPersonagem, jutsuDTO);
        });

        assertEquals("Dano do jutsu deve ser maior que zero", exception.getMessage());
        verify(repository).findById(idPersonagem);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar adicionar jutsu com consumo de chakra inválido")
    void deveLancarExcecaoAoTentarAdicionarJutsuComConsumoDeChakraInvalido() {
        Long idPersonagem = 1L;
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));
    
        JutsuDTO jutsuDTO = JutsuDTO.builder()
        .nome("Jutsu Inválido")
        .dano(50)
        .consumoDeChakra(0)
        .build();
    
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.adicionarJutsu(idPersonagem, jutsuDTO);
        });
    
        assertEquals("Consumo de chakra deve ser maior que zero", exception.getMessage());
        verify(repository).findById(idPersonagem);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve listar jutsus do personagem com sucesso")
    void deveListarJutsusDoPersonagemComSucesso() {
        Long idPersonagem = 1L;
        
        Map<String, Map<String, Object>> jutsuDetalhesEsperado = new HashMap<>();
        personagemTaijutsu.getJutsusMap().forEach((nome, jutsu) -> {
            Map<String, Object> detalhes = new HashMap<>();
            detalhes.put("dano", jutsu.getDano());
            detalhes.put("consumoDeChakra", jutsu.getConsumoDeChakra());
            jutsuDetalhesEsperado.put(nome, detalhes);
        });
        
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));
        
        when(personagemMapper.converterJutsusParaDTO(personagemTaijutsu.getJutsusMap()))
            .thenReturn(jutsuDetalhesEsperado);
    
        Map<String, Object> resultado = service.listarJutsus(idPersonagem);
    
        assertNotNull(resultado);
        assertEquals(idPersonagem, resultado.get("personagemId"));
        assertEquals(personagemTaijutsu.getNome(), resultado.get("nome"));
    
        Map<String, Map<String, Object>> jutsuDetalhes = (Map<String, Map<String, Object>>) resultado.get("jutsus");
        assertNotNull(jutsuDetalhes);
        assertFalse(jutsuDetalhes.isEmpty());
    
        personagemTaijutsu.getJutsusMap().forEach((nome, jutsu) -> {
            assertTrue(jutsuDetalhes.containsKey(nome));
            assertEquals(jutsu.getDano(), jutsuDetalhes.get(nome).get("dano"));
            assertEquals(jutsu.getConsumoDeChakra(), jutsuDetalhes.get(nome).get("consumoDeChakra"));
        });
    
        verify(repository).findById(idPersonagem);
        verify(personagemMapper).converterJutsusParaDTO(personagemTaijutsu.getJutsusMap());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar listar jutsus de personagem inexistente")
    void deveLancarExcecaoAoTentarListarJutsusDePersonagemInexistente() {
        Long idPersonagemInexistente = 99L;
        when(repository.findById(idPersonagemInexistente)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.listarJutsus(idPersonagemInexistente);
        });

        assertEquals("Personagem não encontrado com ID: 99", exception.getMessage());
        verify(repository).findById(idPersonagemInexistente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar personagem com tipo ninja nulo")
    void deveLancarExcecaoAoTentarCriarPersonagemComTipoNinjaNulo() {
        PersonagemDTO dto = new PersonagemDTO();
        dto.setTipoNinja(null);
        dto.setNome("Ninja Inválido");
        dto.setIdade(20);
        dto.setAldeia("Aldeia da Folha");
        dto.setChakra(100);

        NinjaDeTaijutsu personagemEsperado = new NinjaDeTaijutsu();
        personagemEsperado.setNome(dto.getNome());
        personagemEsperado.setIdade(dto.getIdade());
        personagemEsperado.setAldeia(dto.getAldeia());
        personagemEsperado.setChakra(dto.getChakra());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.criarPersonagem(dto);
        });

        assertEquals("Tipo de ninja inválido", exception.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar personagem com tipo ninja vazio")
    void deveLancarExcecaoAoTentarCriarPersonagemComTipoNinjaVazio() {
        PersonagemDTO dto = new PersonagemDTO();
        dto.setTipoNinja("");
        dto.setNome("Ninja Inválido");
        dto.setIdade(20);
        dto.setAldeia("Aldeia da Folha");
        dto.setChakra(100);

        NinjaDeTaijutsu personagemEsperado = new NinjaDeTaijutsu();
        personagemEsperado.setNome(dto.getNome());
        personagemEsperado.setIdade(dto.getIdade());
        personagemEsperado.setAldeia(dto.getAldeia());
        personagemEsperado.setChakra(dto.getChakra());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.criarPersonagem(dto);
        });
        assertEquals("Tipo de ninja inválido", exception.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve atualizar personagem com DTO com sucesso")
    void deveAtualizarPersonagemComDTOComSucesso() {
        Long idPersonagem = 1L;
        String novoNome = "Rock Lee Atualizado";
        int novaIdade = 18;
        String novaAldeia = "Aldeia da Areia";
        int novoChakra = 120;
        List<String> novosJutsus = Arrays.asList("Lotus Primária");

        PersonagemAtualizarDTO dto = new PersonagemAtualizarDTO();
        dto.setNome(novoNome);
        dto.setIdade(novaIdade);
        dto.setAldeia(novaAldeia);
        dto.setChakra(novoChakra);
        dto.setJutsus(novosJutsus);

        NinjaDeTaijutsu personagemAtualizado = new NinjaDeTaijutsu();
        personagemAtualizado.setId(idPersonagem);
        personagemAtualizado.setNome(novoNome);
        personagemAtualizado.setIdade(novaIdade);
        personagemAtualizado.setAldeia(novaAldeia);
        personagemAtualizado.setChakra(novoChakra);
        personagemAtualizado.adicionarJutsu("Lotus Primária", 50, 10);

        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));
        when(repository.save(personagemAtualizado)).thenReturn(personagemAtualizado);

        Personagem resultado = service.atualizarPersonagem(idPersonagem, dto);

        assertNotNull(resultado);
        assertEquals(novoNome, resultado.getNome());
        assertEquals(novaIdade, resultado.getIdade());
        assertEquals(novaAldeia, resultado.getAldeia());
        assertEquals(novoChakra, resultado.getChakra());
        assertTrue(resultado.getJutsusMap().containsKey("Lotus Primária"));

        verify(repository).findById(idPersonagem);
        verify(repository).save(personagemAtualizado);
    }

    @Test
    @DisplayName("Deve atualizar apenas os campos não nulos do personagem")
    void deveAtualizarApenasOsCamposNaoNulosDoPersonagem() {
        Long idPersonagem = 1L;
        String nomeOriginal = "Rock Lee";
        int idadeOriginal = 16;
        String aldeiaOriginal = "Aldeia da Folha";
        int chakraOriginal = 100;

        personagemTaijutsu.setNome(nomeOriginal);
        personagemTaijutsu.setIdade(idadeOriginal);
        personagemTaijutsu.setAldeia(aldeiaOriginal);
        personagemTaijutsu.setChakra(chakraOriginal);

        String novoNome = "Rock Lee Atualizado";
        PersonagemAtualizarDTO dto = new PersonagemAtualizarDTO();
        dto.setNome(novoNome);

        NinjaDeTaijutsu personagemAtualizado = new NinjaDeTaijutsu();
        personagemAtualizado.setId(idPersonagem);
        personagemAtualizado.setNome(novoNome);
        personagemAtualizado.setIdade(idadeOriginal);
        personagemAtualizado.setAldeia(aldeiaOriginal);
        personagemAtualizado.setChakra(chakraOriginal);
        personagemAtualizado.adicionarJutsu("Dynamic Entry", 50, 20);
        personagemAtualizado.adicionarJutsu("Leaf Hurricane", 60, 25);

        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));
        when(repository.save(personagemAtualizado)).thenReturn(personagemAtualizado);

        Personagem resultado = service.atualizarPersonagem(idPersonagem, dto);

        assertNotNull(resultado);
        assertEquals(novoNome, resultado.getNome());
        assertEquals(idadeOriginal, resultado.getIdade());
        assertEquals(aldeiaOriginal, resultado.getAldeia());
        assertEquals(chakraOriginal, resultado.getChakra());
        assertEquals(2, resultado.getJutsusMap().size());
        assertTrue(resultado.getJutsusMap().containsKey("Dynamic Entry"));
        assertTrue(resultado.getJutsusMap().containsKey("Leaf Hurricane"));

        verify(repository).findById(idPersonagem);
        verify(repository).save(personagemAtualizado);
    }

}
