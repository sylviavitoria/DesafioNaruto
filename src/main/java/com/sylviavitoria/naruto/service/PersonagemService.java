// package com.sylviavitoria.naruto.service;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.stereotype.Service;

// import org.springframework.data.domain.Sort;
// import com.sylviavitoria.naruto.dto.JutsuDTO;
// import com.sylviavitoria.naruto.dto.PersonagemAtualizarDTO;
// import com.sylviavitoria.naruto.dto.PersonagemDTO;
// import com.sylviavitoria.naruto.dto.PersonagemResponseDTO;
// import com.sylviavitoria.naruto.exception.NotFoundException;
// import com.sylviavitoria.naruto.model.Jutsu;
// import com.sylviavitoria.naruto.model.NinjaDeGenjutsu;
// import com.sylviavitoria.naruto.model.NinjaDeNinjutsu;
// import com.sylviavitoria.naruto.model.NinjaDeTaijutsu;
// import com.sylviavitoria.naruto.model.Personagem;
// import com.sylviavitoria.naruto.repository.PersonagemRepository;

// import java.util.HashMap;
// import java.util.Map;

// @Service
// @RequiredArgsConstructor
// @Slf4j
// public class PersonagemService {
    
//     private final PersonagemRepository repository;

//         public Page<PersonagemResponseDTO> listarTodosDTO(int page, int size, String sort) {
//         Pageable pageable = criarPageable(page, size, sort);
//         return repository.findAll(pageable).map(this::converterParaDTO);
//     }
    
//     private Pageable criarPageable(int page, int size, String sort) {
//         if (sort != null && !sort.isEmpty()) {
//             try {
//                 return PageRequest.of(page, size, Sort.by(sort));
//             } catch (Exception e) {
//                 return PageRequest.of(page, size);
//             }
//         }
//         return PageRequest.of(page, size);
//     }

//     private PersonagemResponseDTO converterParaDTO(Personagem personagem) {
//         return PersonagemResponseDTO.builder()
//             .id(personagem.getId())
//             .nome(personagem.getNome())
//             .idade(personagem.getIdade())
//             .aldeia(personagem.getAldeia())
//             .chakra(personagem.getChakra())
//             .vida(personagem.getVida())
//             .tipoNinja(determinarTipoNinja(personagem))
//             .jutsus(personagem.getJutsus())
//             .jutsusDetalhes(converterJutsusParaDTO(personagem.getJutsusMap()))
//             .build();
//     }

//     private String determinarTipoNinja(Personagem personagem) {
//         if (personagem instanceof NinjaDeTaijutsu) return "TAIJUTSU";
//         if (personagem instanceof NinjaDeNinjutsu) return "NINJUTSU";
//         if (personagem instanceof NinjaDeGenjutsu) return "GENJUTSU";
//         return "DESCONHECIDO";
//     }

//     private Map<String, Map<String, Object>> converterJutsusParaDTO(Map<String, Jutsu> jutsusMap) {
//         Map<String, Map<String, Object>> jutsuDetalhes = new HashMap<>();
//         jutsusMap.forEach((nome, jutsu) -> {
//             Map<String, Object> detalhes = new HashMap<>();
//             detalhes.put("dano", jutsu.getDano());
//             detalhes.put("consumoDeChakra", jutsu.getConsumoDeChakra());
//             jutsuDetalhes.put(nome, detalhes);
//         });
//         return jutsuDetalhes;
//     }

//     public PersonagemResponseDTO buscarPorIdDTO(Long id) {
//         return converterParaDTO(buscarPorId(id));
//     }

//     public Personagem buscarPorId(Long id) {
//         return repository.findById(id)
//             .orElseThrow(() -> NotFoundException.personagemNaoEncontrado(id));
//     }

//     public PersonagemResponseDTO criarPersonagemDTO(PersonagemDTO dto) {
//         return converterParaDTO(criarPersonagem(dto));
//     }

//     public PersonagemResponseDTO atualizarPersonagemDTO(Long id, PersonagemAtualizarDTO dto) {
//         return converterParaDTO(atualizarPersonagem(id, dto));
//     }

//     public PersonagemResponseDTO adicionarJutsuDTO(Long id, JutsuDTO jutsuDTO) {
//         return converterParaDTO(adicionarJutsu(id, jutsuDTO));
//     }
    
//     public Page<Personagem> listarTodos(Pageable pageable) {
//         log.info("Listando todos os personagens com paginacao: page={}, size={}", 
//                 pageable.getPageNumber(), pageable.getPageSize());
//         return repository.findAll(pageable);
//     }
    

//     public Personagem salvar(Personagem personagem) {
//         log.info("Salvando personagem: {}", personagem.getNome());
//         Personagem salvo = repository.save(personagem);
//         log.info("Personagem salvo com sucesso: id={}", salvo.getId());
//         return salvo;
//     }
    
//     public void deletar(Long id) {
//         log.info("Removendo personagem com id: {}", id);
//         if (!repository.existsById(id)) {
//             log.warn("Tentativa de remover personagem inexistente: id={}", id);
//             throw new IllegalArgumentException("Personagem com ID " + id + " nao existe");
//         }
//         repository.deleteById(id);
//         log.info("Personagem removido com sucesso: id={}", id);
//     }
    
//     public Personagem atualizar(Long id, Personagem personagem) {
//         log.info("Atualizando personagem com id: {}", id);
//         Personagem existente = buscarPorId(id);
//         if (personagem.getChakra() < 0) {
//             log.warn("Tentativa de atualizar personagem com chakra negativo: {}", personagem.getChakra());
//             throw new IllegalArgumentException("Chakra nao pode ser negativo");
//         }
//         personagem.setId(existente.getId());
//         Personagem atualizado = repository.save(personagem);
//         log.info("Personagem atualizado com sucesso: id={}", id);
//         return atualizado;
//     }

//     public Personagem criarPersonagem(PersonagemDTO dto) {
//         log.info("Criando personagem do tipo: {}", dto.getTipoNinja());
        
//         validarTipoNinja(dto.getTipoNinja());
        
//         Personagem personagem = criarPersonagemPorTipo(dto.getTipoNinja());
        
//         personagem.setNome(dto.getNome());
//         personagem.setIdade(dto.getIdade());
//         personagem.setAldeia(dto.getAldeia());
//         personagem.setChakra(dto.getChakra());
        
//         if (dto.getJutsus() != null) {
//             dto.getJutsus().forEach((nomeJutsu, jutsuDTO) -> {
//                 personagem.adicionarJutsu(
//                     nomeJutsu,
//                     jutsuDTO.getDano(),
//                     jutsuDTO.getConsumoDeChakra()
//                 );
//             });
//         }
        
//         return repository.save(personagem);
//     }
//         private void validarTipoNinja(String tipoNinja) {
//         if (tipoNinja == null || tipoNinja.isBlank()) {
//             log.warn("Tentativa de criar personagem com tipo ninja nulo ou vazio");
//             throw new IllegalArgumentException("Tipo de ninja inválido");
//         }
//     }
    
//     private Personagem criarPersonagemPorTipo(String tipoNinja) {
//         return switch (tipoNinja.toUpperCase()) {
//             case "TAIJUTSU" -> new NinjaDeTaijutsu();
//             case "NINJUTSU" -> new NinjaDeNinjutsu();
//             case "GENJUTSU" -> new NinjaDeGenjutsu();
//             default -> {
//                 log.warn("Tentativa de criar personagem com tipo ninja inválido: {}", tipoNinja);
//                 throw new IllegalArgumentException("Tipo de ninja inválido");
//             }
//         };
//     }
    
//     public Personagem atualizarPersonagem(Long id, PersonagemAtualizarDTO dto) {
//         log.info("Atualizando dados do personagem id={}", id);
        
//         Personagem personagem = buscarPorId(id);
        
//         if (dto.getNome() != null) {
//             personagem.setNome(dto.getNome());
//         }

//         if (dto.getIdade() != null) {
//             personagem.setIdade(dto.getIdade());
//         }

//         if (dto.getAldeia() != null) {
//             personagem.setAldeia(dto.getAldeia());
//         }

//         if (dto.getChakra() != null) {
//             personagem.setChakra(dto.getChakra());
//         }

//         if (dto.getJutsus() != null) {
//             personagem.getJutsusMap().clear();
//             for (String jutsu : dto.getJutsus()) {
//                 personagem.adicionarJutsu(jutsu, 50, 20);
//             }
//         }
        
//         return salvar(personagem);
//     }
    
//     public Personagem adicionarJutsu(Long personagemId, JutsuDTO jutsuDTO) {
//         log.info("Adicionando jutsu '{}' ao personagem id={}", jutsuDTO.getNome(), personagemId);
        
//         Personagem personagem = buscarPorId(personagemId);
        
//         if (jutsuDTO.getDano() <= 0) {
//             throw new IllegalArgumentException("Dano do jutsu deve ser maior que zero");
//         }
        
//         if (jutsuDTO.getConsumoDeChakra() <= 0) {
//             throw new IllegalArgumentException("Consumo de chakra deve ser maior que zero");
//         }
        
//         personagem.adicionarJutsu(jutsuDTO.getNome(), jutsuDTO.getDano(), jutsuDTO.getConsumoDeChakra());
        
//         return salvar(personagem);
//     }
    
//     public Map<String, Object> listarJutsus(Long personagemId) {
//         log.info("Listando jutsus do personagem id={}", personagemId);
        
//         Personagem personagem = buscarPorId(personagemId);
//         Map<String, Object> resultado = new HashMap<>();
        
//         resultado.put("personagemId", personagem.getId());
//         resultado.put("nome", personagem.getNome());
        
//         Map<String, Map<String, Object>> jutsuDetalhes = new HashMap<>();
        
//         personagem.getJutsusMap().forEach((nome, jutsu) -> {
//             Map<String, Object> detalhes = new HashMap<>();
//             detalhes.put("dano", jutsu.getDano());
//             detalhes.put("consumoDeChakra", jutsu.getConsumoDeChakra());
//             jutsuDetalhes.put(nome, detalhes);
//         });
        
//         resultado.put("jutsus", jutsuDetalhes);
//         return resultado;
//     }
// }

package com.sylviavitoria.naruto.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Sort;
import com.sylviavitoria.naruto.dto.JutsuDTO;
import com.sylviavitoria.naruto.dto.PersonagemAtualizarDTO;
import com.sylviavitoria.naruto.dto.PersonagemDTO;
import com.sylviavitoria.naruto.dto.PersonagemResponseDTO;
import com.sylviavitoria.naruto.exception.NotFoundException;
import com.sylviavitoria.naruto.mapper.JutsuMapper;
import com.sylviavitoria.naruto.mapper.PersonagemMapper;
import com.sylviavitoria.naruto.model.Jutsu;
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
    private final PersonagemMapper personagemMapper;
    private final JutsuMapper jutsuMapper;

    public Page<PersonagemResponseDTO> listarTodosDTO(int page, int size, String sort) {
        Pageable pageable = criarPageable(page, size, sort);
        return repository.findAll(pageable).map(personagemMapper::converterParaDTO);
    }
    
    private Pageable criarPageable(int page, int size, String sort) {
        if (sort != null && !sort.isEmpty()) {
            try {
                return PageRequest.of(page, size, Sort.by(sort));
            } catch (Exception e) {
                return PageRequest.of(page, size);
            }
        }
        return PageRequest.of(page, size);
    }

    public PersonagemResponseDTO buscarPorIdDTO(Long id) {
        return personagemMapper.converterParaDTO(buscarPorId(id));
    }

    public PersonagemResponseDTO criarPersonagemDTO(PersonagemDTO dto) {
        return personagemMapper.converterParaDTO(criarPersonagem(dto));
    }

    public PersonagemResponseDTO atualizarPersonagemDTO(Long id, PersonagemAtualizarDTO dto) {
        Personagem personagem = buscarPorId(id);
        personagemMapper.atualizarPersonagem(dto, personagem);
        return personagemMapper.converterParaDTO(salvar(personagem));
    }

    public PersonagemResponseDTO adicionarJutsuDTO(Long id, JutsuDTO jutsuDTO) {
        return personagemMapper.converterParaDTO(adicionarJutsu(id, jutsuDTO));
    }
    
    public Personagem buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> NotFoundException.personagemNaoEncontrado(id));
    }

    public Page<Personagem> listarTodos(Pageable pageable) {
        log.info("Listando todos os personagens com paginacao: page={}, size={}", 
                pageable.getPageNumber(), pageable.getPageSize());
        return repository.findAll(pageable);
    }
    
    public Personagem salvar(Personagem personagem) {
        log.info("Salvando personagem: {}", personagem.getNome());
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
    
    public Personagem criarPersonagem(PersonagemDTO dto) {
        log.info("Criando personagem do tipo: {}", dto.getTipoNinja());
        
        validarTipoNinja(dto.getTipoNinja());
        
        Personagem personagem = criarPersonagemPorTipo(dto.getTipoNinja());
        
        personagem.setNome(dto.getNome());
        personagem.setIdade(dto.getIdade());
        personagem.setAldeia(dto.getAldeia());
        personagem.setChakra(dto.getChakra());
        
        if (dto.getJutsus() != null) {
            dto.getJutsus().forEach((nomeJutsu, jutsuDTO) -> {
                Jutsu jutsu = jutsuMapper.converterParaJutsu(jutsuDTO);
                personagem.adicionarJutsu(nomeJutsu, jutsu.getDano(), jutsu.getConsumoDeChakra());
            });
        }
        
        return repository.save(personagem);
    }
        public void validarTipoNinja(String tipoNinja) {
        if (tipoNinja == null || tipoNinja.isBlank()) {
            log.warn("Tentativa de criar personagem com tipo ninja nulo ou vazio");
            throw new IllegalArgumentException("Tipo de ninja inválido");
        }
    }
    
    public Personagem criarPersonagemPorTipo(String tipoNinja) {
        return switch (tipoNinja.toUpperCase()) {
            case "TAIJUTSU" -> new NinjaDeTaijutsu();
            case "NINJUTSU" -> new NinjaDeNinjutsu();
            case "GENJUTSU" -> new NinjaDeGenjutsu();
            default -> {
                log.warn("Tentativa de criar personagem com tipo ninja inválido: {}", tipoNinja);
                throw new IllegalArgumentException("Tipo de ninja inválido");
            }
        };
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
                personagem.adicionarJutsu(jutsu, 50, 10);
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
        resultado.put("jutsus", personagemMapper.converterJutsusParaDTO(personagem.getJutsusMap()));
        
        return resultado;
    }
}