package com.sylviavitoria.naruto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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