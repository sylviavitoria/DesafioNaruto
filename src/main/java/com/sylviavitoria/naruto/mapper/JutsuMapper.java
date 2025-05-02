package com.sylviavitoria.naruto.mapper;

import com.sylviavitoria.naruto.dto.JutsuDTO;
import com.sylviavitoria.naruto.model.Jutsu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JutsuMapper {
    
    Jutsu converterParaJutsu(JutsuDTO dto);
    
    @Mapping(target = "nome", ignore = true)
    JutsuDTO converterParaDTO(Jutsu jutsu);
}