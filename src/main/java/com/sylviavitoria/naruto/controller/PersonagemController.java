package com.sylviavitoria.naruto.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sylviavitoria.naruto.dto.JutsuDTO;
import com.sylviavitoria.naruto.dto.PersonagemAtualizarDTO;
import com.sylviavitoria.naruto.dto.PersonagemDTO;
import com.sylviavitoria.naruto.dto.PersonagemResponseDTO;
import com.sylviavitoria.naruto.model.Personagem;
import com.sylviavitoria.naruto.service.PersonagemService;

import java.util.Map;
@RestController
@RequestMapping("/api/v1/personagens")
@RequiredArgsConstructor
@Tag(name = "Personagens", description = "API para gerenciamento de personagens do Naruto")
public class PersonagemController {

    private final PersonagemService service;

    @GetMapping
    @Operation(summary = "Lista todos os personagens", description = "Retorna uma lista paginada de todos os personagens cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<PersonagemResponseDTO>> listarTodos(
            @Parameter(description = "Número da página (começa em 0)", example = "0") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10") 
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação (ex: nome, idade)", example = "nome") 
            @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(service.listarTodosDTO(page, size, sort));
    }

    
    @GetMapping("/{id}")
    @Operation(summary = "Busca um personagem por ID", description = "Retorna um personagem específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personagem encontrado"),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PersonagemResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorIdDTO(id));
    }

    @PostMapping
    @Operation(summary = "Cria um novo personagem", description = "Cria um novo personagem com base nos dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personagem criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PersonagemResponseDTO> criar(@RequestBody PersonagemDTO dto) {
        return ResponseEntity.ok(service.criarPersonagemDTO(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um personagem existente", description = "Atualiza os dados de um personagem existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personagem atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PersonagemResponseDTO> atualizar(
            @PathVariable Long id, 
            @RequestBody PersonagemAtualizarDTO dto) {
        return ResponseEntity.ok(service.atualizarPersonagemDTO(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um personagem", description = "Remove um personagem pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Personagem removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/jutsus")
    @Operation(summary = "Adiciona um novo jutsu ao personagem", description = "Adiciona um novo jutsu com nome, dano e consumo de chakra")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jutsu adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PersonagemResponseDTO> adicionarJutsu(
        @Parameter(description = "ID do personagem", required = true) @PathVariable Long id,
        @Parameter(description = "Dados do jutsu", required = true, schema = @Schema(example = """
                {
                  "nome": "Sabio",
                  "dano": 70,
                  "consumoDeChakra": 10
                }
                """)) @RequestBody JutsuDTO jutsuDTO) {
        return ResponseEntity.ok(service.adicionarJutsuDTO(id, jutsuDTO));
    }

    @GetMapping("/{id}/jutsus")
    @Operation(summary = "Lista todos os jutsus de um personagem", description = "Retorna a lista de jutsus com detalhes de dano e consumo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Map<String, Object>> listarJutsus(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarJutsus(id));
    }
}