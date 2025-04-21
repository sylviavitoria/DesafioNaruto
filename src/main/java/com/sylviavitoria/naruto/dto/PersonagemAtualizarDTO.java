package com.sylviavitoria.naruto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "Dados para atualização de um personagem")
public class PersonagemAtualizarDTO {
    
    @Schema(description = "Nome do personagem", example = "Naruto Uzumaki")
    private String nome;
    
    @Schema(description = "Idade do personagem", example = "17")
    private Integer idade;
    
    @Schema(description = "Aldeia do personagem", example = "Aldeia da Folha")
    private String aldeia;
    
    @Schema(description = "Quantidade de chakra do personagem", example = "100")
    private Integer chakra;
    
    @Schema(description = "Lista de jutsus do personagem", example = "[\"Rasengan\", \"Kage Bunshin no Jutsu\"]")
    private List<String> jutsus;
}
