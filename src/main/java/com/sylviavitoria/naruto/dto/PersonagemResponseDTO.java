package com.sylviavitoria.naruto.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class PersonagemResponseDTO {
    private Long id;
    private String nome;
    private int idade;
    private String aldeia;
    private int chakra;
    private int vida;
    private String tipoNinja;
    private List<String> jutsus;
    private Map<String, Map<String, Object>> jutsusDetalhes;
}