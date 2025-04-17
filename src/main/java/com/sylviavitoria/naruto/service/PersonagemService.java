package com.sylviavitoria.naruto.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sylviavitoria.naruto.model.Personagem;
import com.sylviavitoria.naruto.repository.PersonagemRepository;

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
        return repository.save(personagem);
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
        log.info("Personagem atualizado com sucesso: id={}", id);
        return repository.save(personagem);
    }
}
