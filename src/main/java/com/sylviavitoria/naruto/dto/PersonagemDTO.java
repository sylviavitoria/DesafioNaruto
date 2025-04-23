package com.sylviavitoria.naruto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
        @Schema(
        description = "Map de jutsus com seus atributos",
        example = """
            {
                "Rasengan": {
                    "dano": 70,
                    "consumoDeChakra": 30
                },
                "Kage Bunshin": {
                    "dano": 40,
                    "consumoDeChakra": 20
                }
            }
            """
    )
    private Map<String, JutsuDTO> jutsus = new HashMap<>();

}