package com.sylviavitoria.naruto.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
        personagemTaijutsu.setJutsus(Arrays.asList("Dynamic Entry", "Leaf Hurricane"));

        personagemNinjutsu = new NinjaDeNinjutsu();
        personagemNinjutsu.setId(2L);
        personagemNinjutsu.setNome("Naruto Uzumaki");
        personagemNinjutsu.setIdade(17);
        personagemNinjutsu.setAldeia("Aldeia da Folha");
        personagemNinjutsu.setChakra(500);
        personagemNinjutsu.setJutsus(Arrays.asList("Rasengan", "Kage Bunshin no Jutsu"));

        personagemGenjutsu = new NinjaDeGenjutsu();
        personagemGenjutsu.setId(3L);
        personagemGenjutsu.setNome("Itachi Uchiha");
        personagemGenjutsu.setIdade(21);
        personagemGenjutsu.setAldeia("Aldeia da Folha");
        personagemGenjutsu.setChakra(300);
        personagemGenjutsu.setJutsus(Arrays.asList("Tsukuyomi", "Amaterasu"));

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
        dto.setJutsus(Arrays.asList("Dynamic Entry", "Leaf Hurricane"));

        NinjaDeTaijutsu novoPersonagem = new NinjaDeTaijutsu();
        novoPersonagem.setId(1L);
        novoPersonagem.setNome(dto.getNome());
        novoPersonagem.setIdade(dto.getIdade());
        novoPersonagem.setAldeia(dto.getAldeia());
        novoPersonagem.setChakra(dto.getChakra());
        novoPersonagem.setJutsus(dto.getJutsus());

        when(repository.save(any(NinjaDeTaijutsu.class))).thenReturn(novoPersonagem);

        Personagem resultado = service.criarPersonagem(dto);

        assertNotNull(resultado);
        assertTrue(resultado instanceof NinjaDeTaijutsu);
        assertEquals(dto.getNome(), resultado.getNome());
        assertEquals(dto.getIdade(), resultado.getIdade());
        assertEquals(dto.getAldeia(), resultado.getAldeia());
        assertEquals(dto.getChakra(), resultado.getChakra());
        assertEquals(dto.getJutsus(), resultado.getJutsus());
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
        dto.setJutsus(Arrays.asList("Rasengan", "Kage Bunshin no Jutsu"));

        NinjaDeNinjutsu novoPersonagem = new NinjaDeNinjutsu();
        novoPersonagem.setId(2L);
        novoPersonagem.setNome(dto.getNome());
        novoPersonagem.setIdade(dto.getIdade());
        novoPersonagem.setAldeia(dto.getAldeia());
        novoPersonagem.setChakra(dto.getChakra());
        novoPersonagem.setJutsus(dto.getJutsus());

        when(repository.save(any(NinjaDeNinjutsu.class))).thenReturn(novoPersonagem);

        Personagem resultado = service.criarPersonagem(dto);

        assertNotNull(resultado);
        assertTrue(resultado instanceof NinjaDeNinjutsu);
        assertEquals(dto.getNome(), resultado.getNome());
        assertEquals(dto.getIdade(), resultado.getIdade());
        assertEquals(dto.getAldeia(), resultado.getAldeia());
        assertEquals(dto.getChakra(), resultado.getChakra());
        assertEquals(dto.getJutsus(), resultado.getJutsus());
        verify(repository).save(any(NinjaDeNinjutsu.class));
    }
    
    @Test
    @DisplayName("Deve criar personagem do tipo Genjutsu")
    void deveCriarPersonagemDoTipoGenjutsu() {

        PersonagemDTO dto = new PersonagemDTO();
        dto.setTipoNinja("GENJUTSU");
        dto.setNome("Itachi Uchiha");
        dto.setIdade(21);
        dto.setAldeia("Aldeia da Folha");
        dto.setChakra(300);
        dto.setJutsus(Arrays.asList("Tsukuyomi", "Amaterasu"));

        NinjaDeGenjutsu novoPersonagem = new NinjaDeGenjutsu();
        novoPersonagem.setId(3L);
        novoPersonagem.setNome(dto.getNome());
        novoPersonagem.setIdade(dto.getIdade());
        novoPersonagem.setAldeia(dto.getAldeia());
        novoPersonagem.setChakra(dto.getChakra());
        novoPersonagem.setJutsus(dto.getJutsus());

        when(repository.save(any(NinjaDeGenjutsu.class))).thenReturn(novoPersonagem);

        Personagem resultado = service.criarPersonagem(dto);

        assertNotNull(resultado);
        assertTrue(resultado instanceof NinjaDeGenjutsu);
        assertEquals(dto.getNome(), resultado.getNome());
        assertEquals(dto.getIdade(), resultado.getIdade());
        assertEquals(dto.getAldeia(), resultado.getAldeia());
        assertEquals(dto.getChakra(), resultado.getChakra());
        assertEquals(dto.getJutsus(), resultado.getJutsus());
        verify(repository).save(any(NinjaDeGenjutsu.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar personagem com tipo ninja inválido")
    void deveLancarExcecaoAoTentarCriarPersonagemComTipoNinjaInvalido() {
        PersonagemDTO dto = new PersonagemDTO();
        dto.setTipoNinja("INVALID");
        dto.setNome("Invalid Ninja");
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
    @DisplayName("Deve usar jutsu de Taijutsu com sucesso")
    void deveUsarJutsuDeTaijutsuComSucesso() {
        Long idDePersonagem = 1L;
        when(repository.findById(idDePersonagem)).thenReturn(Optional.of(personagemTaijutsu));

        Map<String, Object> resultado = service.usarJutsu(idDePersonagem);

        assertNotNull(resultado);
        assertEquals(personagemTaijutsu.getNome(), resultado.get("nome"));
        assertEquals("Taijutsu", resultado.get("tipoNinja"));
        assertEquals(personagemTaijutsu.getNome() + " está usando um golpe de Taijutsu!", resultado.get("mensagem"));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve usar jutsu de Ninjutsu com sucesso")
    void deveUsarJutsuDeNinjutsuComSucesso() {
        Long idDePersonagem = 2L;
        when(repository.findById(idDePersonagem)).thenReturn(Optional.of(personagemNinjutsu));

        Map<String, Object> resultado = service.usarJutsu(idDePersonagem);

        assertNotNull(resultado);
        assertEquals(personagemNinjutsu.getNome(), resultado.get("nome"));
        assertEquals("Ninjutsu", resultado.get("tipoNinja"));
        assertEquals(personagemNinjutsu.getNome() + " está usando um jutsu de Ninjutsu!", resultado.get("mensagem"));
        verify(repository).findById(idDePersonagem);
    }

    @Test
    @DisplayName("Deve usar jutsu de Genjutsu com sucesso")
    void deveUsarJutsuDeGenjutsuComSucesso() {
        Long idDePersonagem = 3L;
        when(repository.findById(idDePersonagem)).thenReturn(Optional.of(personagemGenjutsu));

        Map<String, Object> resultado = service.usarJutsu(idDePersonagem);

        assertNotNull(resultado);
        assertEquals(personagemGenjutsu.getNome(), resultado.get("nome"));
        assertEquals("Genjutsu", resultado.get("tipoNinja"));
        assertEquals(personagemGenjutsu.getNome() + " está usando um jutsu de Genjutsu!", resultado.get("mensagem"));
        verify(repository).findById(idDePersonagem);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao tentar usar jutsu com personagem não ninja")
    void deveLancarExcecaoAoTentarUsarJutsuComPersonagemNaoNinja() {
        Long idPersonagem = 99L;
        Personagem personagemGenerico = mock(Personagem.class);
        when(personagemGenerico.getNome()).thenReturn("Personagem Genérico");
        
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemGenerico));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.usarJutsu(idPersonagem);
        });
        
        assertEquals("Personagem não é um ninja.", exception.getMessage());
        verify(repository).findById(idPersonagem);
    }

    @Test
    @DisplayName("Deve desviar com Taijutsu com sucesso")
    void deveDesviarComTaijutsuComSucesso() {
        Long idPersonagem = 1L;
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemTaijutsu));

        Map<String, Object> resultado = service.desviar(idPersonagem);

        assertNotNull(resultado);
        assertEquals(personagemTaijutsu.getNome(), resultado.get("nome"));
        assertEquals("Taijutsu", resultado.get("tipoNinja"));
        assertEquals(personagemTaijutsu.getNome() + " está desviando usando suas habilidades de Taijutsu!", resultado.get("mensagem"));
        verify(repository).findById(idPersonagem);
    }
    
    @Test
    @DisplayName("Deve desviar com Ninjutsu com sucesso")
    void deveDesviarComNinjutsuComSucesso() {
        Long idPersonagem = 2L;
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemNinjutsu));

        Map<String, Object> resultado = service.desviar(idPersonagem);

        assertNotNull(resultado);
        assertEquals(personagemNinjutsu.getNome(), resultado.get("nome"));
        assertEquals("Ninjutsu", resultado.get("tipoNinja"));
        assertEquals(personagemNinjutsu.getNome() + " está desviando usando suas habilidades de Ninjutsu!", resultado.get("mensagem"));
        verify(repository).findById(idPersonagem);
    }
    
    @Test
    @DisplayName("Deve desviar com Genjutsu com sucesso")
    void deveDesviarComGenjutsuComSucesso() {
        Long idPersonagem = 3L;
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemGenjutsu));

        Map<String, Object> resultado = service.desviar(idPersonagem);

        assertNotNull(resultado);
        assertEquals(personagemGenjutsu.getNome(), resultado.get("nome"));
        assertEquals("Genjutsu", resultado.get("tipoNinja"));
        assertEquals(personagemGenjutsu.getNome() + " está desviando usando suas habilidades de Genjutsu!", resultado.get("mensagem"));
        verify(repository).findById(idPersonagem);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao tentar desviar com personagem não ninja")
    void deveLancarExcecaoAoTentarDesviarComPersonagemNaoNinja() {
        Long idPersonagem = 99L;
        Personagem personagemGenerico = mock(Personagem.class);
        when(personagemGenerico.getNome()).thenReturn("Personagem Genérico");
        
        when(repository.findById(idPersonagem)).thenReturn(Optional.of(personagemGenerico));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.desviar(idPersonagem);
        });
        
        assertEquals("Personagem não é um ninja.", exception.getMessage());
        verify(repository).findById(idPersonagem);
    }
}
