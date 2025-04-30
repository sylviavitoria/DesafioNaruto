package com.sylviavitoria.naruto.mapper;

import com.sylviavitoria.naruto.dto.PersonagemAtualizarDTO;
import com.sylviavitoria.naruto.dto.PersonagemResponseDTO;
import com.sylviavitoria.naruto.model.Jutsu;
import com.sylviavitoria.naruto.model.Personagem;

import java.util.HashMap;
import java.util.Map;

import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonagemMapper {
    
    @Mapping(target = "tipoNinja", expression = "java(personagem.getClass().getSimpleName())")
    @Mapping(target = "jutsusDetalhes", expression = "java(converterJutsusParaDTO(personagem.getJutsusMap()))")
    PersonagemResponseDTO converterParaDTO(Personagem personagem);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizarPersonagem(PersonagemAtualizarDTO dto, @MappingTarget Personagem personagem);

    default Map<String, Map<String, Object>> converterJutsusParaDTO(Map<String, Jutsu> jutsusMap) {
        if (jutsusMap == null) {
            return null;
        }
        
        Map<String, Map<String, Object>> jutsuDetalhes = new HashMap<>();
        jutsusMap.forEach((nome, jutsu) -> {
            Map<String, Object> detalhes = new HashMap<>();
            detalhes.put("dano", jutsu.getDano());
            detalhes.put("consumoDeChakra", jutsu.getConsumoDeChakra());
            jutsuDetalhes.put(nome, detalhes);
        });
        return jutsuDetalhes;
    }
}