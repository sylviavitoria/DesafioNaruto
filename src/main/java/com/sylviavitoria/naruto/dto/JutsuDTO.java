package com.sylviavitoria.naruto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dados para criação de um jutsu")
public class JutsuDTO {
    @Schema(description = "Nome do jutsu", example = "Rasengan", required = true)
    private String nome;
    
    @Schema(description = "Dano que o jutsu causa", example = "50", required = true)
    private int dano;
    
    @Schema(description = "Consumo de chakra do jutsu", example = "20", required = true)
    private int consumoDeChakra;
}