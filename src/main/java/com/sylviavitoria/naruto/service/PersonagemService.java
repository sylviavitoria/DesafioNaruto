package com.sylviavitoria.naruto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sylviavitoria.naruto.model.Personagem;
import com.sylviavitoria.naruto.repository.PersonagemRepository;

@Service
@RequiredArgsConstructor
public class PersonagemService {
    
    private final PersonagemRepository repository;
    
    public Page<Personagem> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    public Personagem buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));
    }
    
    public Personagem salvar(Personagem personagem) {
        if (personagem.getChakra() < 0) {
            throw new IllegalArgumentException("Chakra não pode ser negativo");
        }
        if (personagem.getNome() == null || personagem.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        return repository.save(personagem);
    }
    
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Personagem com ID " + id + " não existe");
        }
        repository.deleteById(id);
    }
    
    public Personagem atualizar(Long id, Personagem personagem) {
        Personagem existente = buscarPorId(id);
        if (personagem.getChakra() < 0) {
            throw new IllegalArgumentException("Chakra não pode ser negativo");
        }
        personagem.setId(existente.getId());
        return repository.save(personagem);
    }
}
