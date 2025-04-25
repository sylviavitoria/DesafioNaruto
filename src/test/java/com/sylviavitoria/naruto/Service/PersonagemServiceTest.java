package com.sylviavitoria.naruto.Service;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

        assertEquals("Personagem nao encontrado", exception.getMessage());
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
        int chakraNegativo = -10;
        personagemTaijutsu.setChakra(chakraNegativo);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.salvar(personagemTaijutsu);
        });

        assertEquals("Chakra nao pode ser negativo", exception.getMessage());
        verify(repository, never()).save(any(Personagem.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar personagem sem nome")
    void deveLancarExcecaoAoTentarSalvarPersonagemSemNome() {
        personagemTaijutsu.setNome("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.salvar(personagemTaijutsu);
        });

        assertEquals("Nome é obrigatorio", exception.getMessage());
        verify(repository, never()).save(any(Personagem.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar personagem com nome nulo")
    void deveLancarExcecaoAoTentarSalvarPersonagemComNomeNulo() {
        personagemTaijutsu.setNome(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.salvar(personagemTaijutsu);
        });

        assertEquals("Nome é obrigatorio", exception.getMessage());
        verify(repository, never()).save(any(Personagem.class));
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
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve atualizar personagem existente com sucesso")
    void deveAtualizarPersonagemExistenteComSucesso() {
        Long idPersonagem = 1L;
        int idadePersonagem = 17;
        personagemTaijutsu.setNome("Rock Lee");
        personagemTaijutsu.setIdade(idadePersonagem);

        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));
        when(repository.save(any(Personagem.class))).thenReturn(personagemTaijutsu);

        Personagem atualizado = service.atualizar(idPersonagem, personagemTaijutsu);

        assertNotNull(atualizado);
        assertEquals("Rock Lee", atualizado.getNome());
        assertEquals(idadePersonagem, atualizado.getIdade());
        verify(repository).findById(idPersonagem);
        verify(repository).save(personagemTaijutsu);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar personagem com chakra negativo")
    void deveLancarExcecaoAoTentarAtualizarPersonagemComChakraNegativo() {
        Long idPersonagem = 1L;
        int chakraNegativo = -50;
        personagemTaijutsu.setChakra(chakraNegativo);

        when(repository.findById(idPersonagem)).thenReturn(Optional.of(new NinjaDeTaijutsu()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.atualizar(idPersonagem, personagemTaijutsu);
        });

        assertEquals("Chakra nao pode ser negativo", exception.getMessage());
        verify(repository).findById(idPersonagem);
        verify(repository, never()).save(any(Personagem.class));
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

        Map<String, JutsuDTO> jutsusMap = new HashMap<>();

        JutsuDTO dynamicEntry = new JutsuDTO();
        dynamicEntry.setNome("Dynamic Entry");
        dynamicEntry.setDano(50);
        dynamicEntry.setConsumoDeChakra(20);
        jutsusMap.put("Dynamic Entry", dynamicEntry);

        JutsuDTO leafHurricane = new JutsuDTO();
        leafHurricane.setNome("Leaf Hurricane");
        leafHurricane.setDano(60);
        leafHurricane.setConsumoDeChakra(25);
        jutsusMap.put("Leaf Hurricane", leafHurricane);

        dto.setJutsus(jutsusMap);

        NinjaDeTaijutsu novoPersonagem = new NinjaDeTaijutsu();
        novoPersonagem.setId(1L);
        novoPersonagem.setNome(dto.getNome());
        novoPersonagem.setIdade(dto.getIdade());
        novoPersonagem.setAldeia(dto.getAldeia());
        novoPersonagem.setChakra(dto.getChakra());

        dto.getJutsus().forEach((nome, jutsuDTO) -> novoPersonagem.adicionarJutsu(nome, jutsuDTO.getDano(),
                jutsuDTO.getConsumoDeChakra()));

        when(repository.save(any(NinjaDeTaijutsu.class))).thenReturn(novoPersonagem);

        Personagem resultado = service.criarPersonagem(dto);

        assertNotNull(resultado);
        assertTrue(resultado instanceof NinjaDeTaijutsu);
        assertEquals(dto.getNome(), resultado.getNome());
        assertEquals(dto.getIdade(), resultado.getIdade());
        assertEquals(dto.getAldeia(), resultado.getAldeia());
        assertEquals(dto.getChakra(), resultado.getChakra());
        assertEquals(dto.getJutsus().keySet(), resultado.getJutsusMap().keySet());
        verify(repository).save(any(NinjaDeTaijutsu.class));
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

        JutsuDTO rasengan = new JutsuDTO();
        rasengan.setNome("Rasengan");
        rasengan.setDano(70);
        rasengan.setConsumoDeChakra(30);
        jutsusMap.put("Rasengan", rasengan);

        JutsuDTO kageBunshin = new JutsuDTO();
        kageBunshin.setNome("Kage Bunshin no Jutsu");
        kageBunshin.setDano(40);
        kageBunshin.setConsumoDeChakra(20);
        jutsusMap.put("Kage Bunshin no Jutsu", kageBunshin);

        dto.setJutsus(jutsusMap);

        NinjaDeNinjutsu novoPersonagem = new NinjaDeNinjutsu();
        novoPersonagem.setId(2L);
        novoPersonagem.setNome(dto.getNome());
        novoPersonagem.setIdade(dto.getIdade());
        novoPersonagem.setAldeia(dto.getAldeia());
        novoPersonagem.setChakra(dto.getChakra());

        dto.getJutsus().forEach((nome, jutsuDTO) -> novoPersonagem.adicionarJutsu(nome, jutsuDTO.getDano(),
                jutsuDTO.getConsumoDeChakra()));

        when(repository.save(any(NinjaDeNinjutsu.class))).thenReturn(novoPersonagem);

        Personagem resultado = service.criarPersonagem(dto);

        assertNotNull(resultado);
        assertTrue(resultado instanceof NinjaDeNinjutsu);
        assertEquals(dto.getNome(), resultado.getNome());
        assertEquals(dto.getIdade(), resultado.getIdade());
        assertEquals(dto.getAldeia(), resultado.getAldeia());
        assertEquals(dto.getChakra(), resultado.getChakra());
        assertEquals(dto.getJutsus().keySet(), resultado.getJutsusMap().keySet());
        verify(repository).save(any(NinjaDeNinjutsu.class));
    }

    @Test
    @DisplayName("Deve adicionar jutsu ao personagem com sucesso")
    void deveAdicionarJutsuComSucesso() {
        Long idPersonagem = 1L;
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));
        when(repository.save(any(Personagem.class))).thenReturn(personagemTaijutsu);

        JutsuDTO jutsuDTO = new JutsuDTO();
        jutsuDTO.setNome("Novo Jutsu");
        jutsuDTO.setDano(65);
        jutsuDTO.setConsumoDeChakra(25);

        Personagem resultado = service.adicionarJutsu(idPersonagem, jutsuDTO);

        assertNotNull(resultado);
        assertTrue(resultado.getJutsusMap().containsKey(jutsuDTO.getNome()));
        assertEquals(jutsuDTO.getDano(), resultado.getJutsu(jutsuDTO.getNome()).getDano());
        assertEquals(jutsuDTO.getConsumoDeChakra(), resultado.getJutsu(jutsuDTO.getNome()).getConsumoDeChakra());

        verify(repository).findById(idPersonagem);
        verify(repository).save(personagemTaijutsu);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar adicionar jutsu com dano inválido")
    void deveLancarExcecaoAoTentarAdicionarJutsuComDanoInvalido() {
        Long idPersonagem = 1L;
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));

        JutsuDTO jutsuDTO = new JutsuDTO();
        jutsuDTO.setNome("Jutsu Inválido");
        jutsuDTO.setDano(0); 
        jutsuDTO.setConsumoDeChakra(25);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.adicionarJutsu(idPersonagem, jutsuDTO);
        });

        assertEquals("Dano do jutsu deve ser maior que zero", exception.getMessage());
        verify(repository).findById(idPersonagem);
        verify(repository, never()).save(any(Personagem.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar adicionar jutsu com consumo de chakra inválido")
    void deveLancarExcecaoAoTentarAdicionarJutsuComConsumoDeChakraInvalido() {
        Long idPersonagem = 1L;
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));

        JutsuDTO jutsuDTO = new JutsuDTO();
        jutsuDTO.setNome("Jutsu Inválido");
        jutsuDTO.setDano(50);
        jutsuDTO.setConsumoDeChakra(0); 

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.adicionarJutsu(idPersonagem, jutsuDTO);
        });

        assertEquals("Consumo de chakra deve ser maior que zero", exception.getMessage());
        verify(repository).findById(idPersonagem);
        verify(repository, never()).save(any(Personagem.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar adicionar jutsu em personagem inexistente")
    void deveLancarExcecaoAoTentarAdicionarJutsuEmPersonagemInexistente() {
        Long idPersonagemInexistente = 99L;
        when(repository.findById(idPersonagemInexistente)).thenReturn(Optional.empty());

        JutsuDTO jutsuDTO = new JutsuDTO();
        jutsuDTO.setNome("Jutsu Teste");
        jutsuDTO.setDano(50);
        jutsuDTO.setConsumoDeChakra(25);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.adicionarJutsu(idPersonagemInexistente, jutsuDTO);
        });

        assertEquals("Personagem nao encontrado", exception.getMessage());
        verify(repository).findById(idPersonagemInexistente);
        verify(repository, never()).save(any(Personagem.class));
    }

    @Test
    @DisplayName("Deve listar jutsus do personagem com sucesso")
    void deveListarJutsusDoPersonagemComSucesso() {
        Long idPersonagem = 1L;
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));

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
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar listar jutsus de personagem inexistente")
    void deveLancarExcecaoAoTentarListarJutsusDePersonagemInexistente() {
        Long idPersonagemInexistente = 99L;
        when(repository.findById(idPersonagemInexistente)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.listarJutsus(idPersonagemInexistente);
        });

        assertEquals("Personagem nao encontrado", exception.getMessage());
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

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.criarPersonagem(dto);
        });

        assertEquals("Tipo de ninja inválido", exception.getMessage());
        verify(repository, never()).save(any(Personagem.class));
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

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.criarPersonagem(dto);
        });

        assertEquals("Tipo de ninja inválido", exception.getMessage());
        verify(repository, never()).save(any(Personagem.class));
    }

    @Test
    @DisplayName("Deve atualizar personagem com DTO com sucesso")
    void deveAtualizarPersonagemComDTOComSucesso() {
        
        Long idPersonagem = 1L;
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));
        when(repository.save(any(Personagem.class))).thenReturn(personagemTaijutsu);

        String novoNome = "Rock Lee Atualizado";
        int novaIdade = 18;
        String novaAldeia = "Aldeia da Areia";
        int novoChakra = 120;
        List<String> novosJutsus = Arrays.asList("Lotus Primária", "Tornado da Folha");

        PersonagemAtualizarDTO dto = new PersonagemAtualizarDTO();
        dto.setNome(novoNome);
        dto.setIdade(novaIdade);
        dto.setAldeia(novaAldeia);
        dto.setChakra(novoChakra);
        dto.setJutsus(novosJutsus);

        Personagem resultado = service.atualizarPersonagem(idPersonagem, dto);

        assertNotNull(resultado);
        assertEquals(novoNome, resultado.getNome());
        assertEquals(novaIdade, resultado.getIdade());
        assertEquals(novaAldeia, resultado.getAldeia());
        assertEquals(novoChakra, resultado.getChakra());

        Set<String> jutsuNames = resultado.getJutsusMap().keySet();
        assertEquals(novosJutsus.size(), jutsuNames.size());
        for (String jutsu : novosJutsus) {
            assertTrue(jutsuNames.contains(jutsu));
            assertNotNull(resultado.getJutsu(jutsu));
            assertEquals(50, resultado.getJutsu(jutsu).getDano());
            assertEquals(20, resultado.getJutsu(jutsu).getConsumoDeChakra());
        }

        verify(repository).findById(idPersonagem);
        verify(repository).save(personagemTaijutsu);
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

        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));
        when(repository.save(any(Personagem.class))).thenReturn(personagemTaijutsu);

        String novoNome = "Rock Lee Atualizado";
        PersonagemAtualizarDTO dto = new PersonagemAtualizarDTO();
        dto.setNome(novoNome);

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
        verify(repository).save(personagemTaijutsu);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar personagem inexistente com DTO")
    void deveLancarExcecaoAoTentarAtualizarPersonagemInexistenteComDTO() {
        Long idPersonagemInexistente = 99L;
        when(repository.findById(idPersonagemInexistente)).thenReturn(Optional.empty());

        PersonagemAtualizarDTO dto = new PersonagemAtualizarDTO();
        dto.setNome("Personagem Inexistente");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.atualizarPersonagem(idPersonagemInexistente, dto);
        });

        assertEquals("Personagem nao encontrado", exception.getMessage());
        verify(repository).findById(idPersonagemInexistente);
        verify(repository, never()).save(any(Personagem.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar personagem com chakra negativo via DTO")
    void deveLancarExcecaoAoTentarAtualizarPersonagemComChakraNegativoViaDTO() {

        Long idPersonagem = 1L;
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));

        int chakraNegativo = -10;
        PersonagemAtualizarDTO dto = new PersonagemAtualizarDTO();
        dto.setChakra(chakraNegativo);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.atualizarPersonagem(idPersonagem, dto);
        });

        assertEquals("Chakra nao pode ser negativo", exception.getMessage());
        verify(repository).findById(idPersonagem);
        verify(repository, never()).save(any(Personagem.class));
    }
}
