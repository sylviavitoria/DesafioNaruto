package com.sylviavitoria.naruto.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sylviavitoria.naruto.service.BatalhaService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/batalhas")
@RequiredArgsConstructor
@Tag(name = "Batalhas", description = "API para gerenciamento de batalhas entre ninjas")
public class BatalhaController {

    private final BatalhaService batalhaService;

    
    @PostMapping("/atacar/{atacanteId}/{defensorId}")
    @Operation(summary = "Realizar ataque", description = "Um personagem ataca o outro com um jutsu específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ataque realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Map<String, Object>> realizarAtaque(
            @Parameter(description = "ID do personagem atacante", required = true) @PathVariable Long atacanteId,
            @Parameter(description = "ID do personagem defensor", required = true) @PathVariable Long defensorId,
            @Parameter(description = "Nome do jutsu a ser usado", required = true) @RequestParam String nomeJutsu) {
        return ResponseEntity.ok(batalhaService.realizarAtaque(atacanteId, defensorId, nomeJutsu));
    }

}