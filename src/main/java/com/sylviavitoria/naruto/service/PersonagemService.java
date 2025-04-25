package com.sylviavitoria.naruto.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sylviavitoria.naruto.dto.JutsuDTO;
import com.sylviavitoria.naruto.dto.PersonagemAtualizarDTO;
import com.sylviavitoria.naruto.dto.PersonagemDTO;
import com.sylviavitoria.naruto.model.NinjaDeGenjutsu;
import com.sylviavitoria.naruto.model.NinjaDeNinjutsu;
import com.sylviavitoria.naruto.model.NinjaDeTaijutsu;
import com.sylviavitoria.naruto.model.Personagem;
import com.sylviavitoria.naruto.repository.PersonagemRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonagemService {
    
    private final PersonagemRepository repository;
    
    public Page<Personagem> listarTodos(Pageable pageable) {
        log.info("Listando todos os personagens com paginacao: page={}, size={}", 
                pageable.getPageNumber(), pageable.getPageSize());
        return repository.findAll(pageable);
    }
    
    public Personagem buscarPorId(Long id) {
        log.info("Buscando personagem com id: {}", id);
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Personagem nao encontrado"));
    }
    
    public Personagem salvar(Personagem personagem) {
        log.info("Salvando personagem: {}", personagem.getNome());
        if (personagem.getChakra() < 0) {
            log.warn("Tentativa de salvar personagem com chakra negativo: {}", personagem.getChakra());
            throw new IllegalArgumentException("Chakra nao pode ser negativo");
        }
        if (personagem.getNome() == null || personagem.getNome().isBlank()) {
            log.warn("Tentativa de salvar personagem sem nome");
            throw new IllegalArgumentException("Nome é obrigatorio");
        }
        Personagem salvo = repository.save(personagem);
        log.info("Personagem salvo com sucesso: id={}", salvo.getId());
        return salvo;
    }
    
    public void deletar(Long id) {
        log.info("Removendo personagem com id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Tentativa de remover personagem inexistente: id={}", id);
            throw new IllegalArgumentException("Personagem com ID " + id + " nao existe");
        }
        repository.deleteById(id);
        log.info("Personagem removido com sucesso: id={}", id);
    }
    
    public Personagem atualizar(Long id, Personagem personagem) {
        log.info("Atualizando personagem com id: {}", id);
        Personagem existente = buscarPorId(id);
        if (personagem.getChakra() < 0) {
            log.warn("Tentativa de atualizar personagem com chakra negativo: {}", personagem.getChakra());
            throw new IllegalArgumentException("Chakra nao pode ser negativo");
        }
        personagem.setId(existente.getId());
        Personagem atualizado = repository.save(personagem);
        log.info("Personagem atualizado com sucesso: id={}", id);
        return atualizado;
    }
    
    public Personagem criarPersonagem(PersonagemDTO dto) {
        log.info("Criando personagem do tipo: {}", dto.getTipoNinja());

        if (dto.getTipoNinja() == null || dto.getTipoNinja().isBlank()) {
            log.warn("Tentativa de criar personagem com tipo ninja nulo ou vazio");
            throw new IllegalArgumentException("Tipo de ninja inválido");
        }
        Personagem personagem;
        
        switch (dto.getTipoNinja().toUpperCase()) {
            case "TAIJUTSU":
                personagem = new NinjaDeTaijutsu();
                break;
            case "NINJUTSU":
                personagem = new NinjaDeNinjutsu();
                break;
            case "GENJUTSU":
                personagem = new NinjaDeGenjutsu();
                break;
            default:
                log.warn("Tentativa de criar personagem com tipo ninja inválido: {}", dto.getTipoNinja());
                throw new IllegalArgumentException("Tipo de ninja inválido");
        }
        
        personagem.setNome(dto.getNome());
        personagem.setIdade(dto.getIdade());
        personagem.setAldeia(dto.getAldeia());
        personagem.setChakra(dto.getChakra());
        
        if (dto.getJutsus() != null) {
            dto.getJutsus().forEach((nomeJutsu, jutsuDTO) -> {
                personagem.adicionarJutsu(
                    nomeJutsu,
                    jutsuDTO.getDano(),
                    jutsuDTO.getConsumoDeChakra()
                );
            });
        }
        
        return repository.save(personagem);
    }
    
    public Personagem atualizarPersonagem(Long id, PersonagemAtualizarDTO dto) {
        log.info("Atualizando dados do personagem id={}", id);
        
        Personagem personagem = buscarPorId(id);
        
        if (dto.getNome() != null) {
            personagem.setNome(dto.getNome());
        }

        if (dto.getIdade() != null) {
            personagem.setIdade(dto.getIdade());
        }

        if (dto.getAldeia() != null) {
            personagem.setAldeia(dto.getAldeia());
        }

        if (dto.getChakra() != null) {
            personagem.setChakra(dto.getChakra());
        }

        if (dto.getJutsus() != null) {
            personagem.getJutsusMap().clear();
            for (String jutsu : dto.getJutsus()) {
                personagem.adicionarJutsu(jutsu, 50, 20);
            }
        }
        
        return salvar(personagem);
    }
    
    public Personagem adicionarJutsu(Long personagemId, JutsuDTO jutsuDTO) {
        log.info("Adicionando jutsu '{}' ao personagem id={}", jutsuDTO.getNome(), personagemId);
        
        Personagem personagem = buscarPorId(personagemId);
        
        if (jutsuDTO.getDano() <= 0) {
            throw new IllegalArgumentException("Dano do jutsu deve ser maior que zero");
        }
        
        if (jutsuDTO.getConsumoDeChakra() <= 0) {
            throw new IllegalArgumentException("Consumo de chakra deve ser maior que zero");
        }
        
        personagem.adicionarJutsu(jutsuDTO.getNome(), jutsuDTO.getDano(), jutsuDTO.getConsumoDeChakra());
        
        return salvar(personagem);
    }
    
    public Map<String, Object> listarJutsus(Long personagemId) {
        log.info("Listando jutsus do personagem id={}", personagemId);
        
        Personagem personagem = buscarPorId(personagemId);
        Map<String, Object> resultado = new HashMap<>();
        
        resultado.put("personagemId", personagem.getId());
        resultado.put("nome", personagem.getNome());
        
        Map<String, Map<String, Object>> jutsuDetalhes = new HashMap<>();
        
        personagem.getJutsusMap().forEach((nome, jutsu) -> {
            Map<String, Object> detalhes = new HashMap<>();
            detalhes.put("dano", jutsu.getDano());
            detalhes.put("consumoDeChakra", jutsu.getConsumoDeChakra());
            jutsuDetalhes.put(nome, detalhes);
        });
        
        resultado.put("jutsus", jutsuDetalhes);
        return resultado;
    }
}