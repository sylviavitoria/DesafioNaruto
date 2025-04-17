package com.sylviavitoria.naruto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "Dados para criação de um personagem")
public class PersonagemDTO {
    @Schema(description = "Tipo de ninja (TAIJUTSU, NINJUTSU ou GENJUTSU)", example = "NINJUTSU", required = true)
    private String tipoNinja;
    
    @Schema(description = "Nome do personagem", example = "Naruto Uzumaki", required = true)
    private String nome;
    
    @Schema(description = "Idade do personagem", example = "17", required = true)
    private int idade;
    
    @Schema(description = "Aldeia do personagem", example = "Aldeia da Folha", required = true)
    private String aldeia;
    
    @Schema(description = "Quantidade de chakra do personagem", example = "100", required = true)
    private int chakra;
    
    @Schema(description = "Lista de jutsus do personagem", example = "[\"Rasengan\", \"Kage Bunshin no Jutsu\"]")
    private List<String> jutsus;
}