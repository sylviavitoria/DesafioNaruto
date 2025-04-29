package com.sylviavitoria.naruto.service;

import com.sylviavitoria.naruto.interfaces.Ninja;
import com.sylviavitoria.naruto.model.Jutsu;
import com.sylviavitoria.naruto.model.Personagem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatalhaService {
    
    private final PersonagemService personagemService;

    public Map<String, Object> realizarAtaque(Long atacanteId, Long defensorId, String nomeJutsu) {
        log.info("Realizando ataque: atacante={}, defensor={}, jutsu={}", atacanteId, defensorId, nomeJutsu);
        Map<String, Object> resultado = new HashMap<>();
        List<String> batalha = new ArrayList<>();
        
        try {
            Personagem atacante = personagemService.buscarPorId(atacanteId);
            Personagem defensor = personagemService.buscarPorId(defensorId);
            
            if (!(atacante instanceof Ninja)) {
                throw new IllegalArgumentException("Atacante não é um ninja");
            }
            
            if (!(defensor instanceof Ninja)) {
                throw new IllegalArgumentException("Defensor não é um ninja");
            }
            
            Ninja ninjaAtacante = (Ninja) atacante;
            Ninja ninjaDefensor = (Ninja) defensor;
            
            if (!ninjaAtacante.podeLutar()) {
                batalha.add(atacante.getNome() + " não pode mais lutar!");
                resultado.put("vencedor", defensor.getNome());
                resultado.put("log", batalha);
                return resultado;
            }
            
            if (!ninjaDefensor.podeLutar()) {
                batalha.add(defensor.getNome() + " não pode mais lutar!");
                resultado.put("vencedor", atacante.getNome());
                resultado.put("log", batalha);
                return resultado;
            }
            
            Jutsu jutsu = atacante.getJutsu(nomeJutsu);
            if (jutsu == null) {
                batalha.add(atacante.getNome() + " não conhece o jutsu " + nomeJutsu);
                resultado.put("erro", "Jutsu não encontrado");
                resultado.put("log", batalha);
                return resultado;
            }
            
            String resultadoAtaque = ninjaAtacante.usarJutsu(nomeJutsu, defensor);
            batalha.add(resultadoAtaque);
            
            if (defensor.getVida() > 0 && defensor.getChakra() > 0) {
                String resultadoDefesa = ninjaDefensor.desviar(nomeJutsu, jutsu.getDano());
                batalha.add(resultadoDefesa);
            }
            
            personagemService.salvar(atacante);
            personagemService.salvar(defensor);
            
            if (!ninjaAtacante.podeLutar()) {
                batalha.add(atacante.getNome() + " não pode mais lutar!");
                resultado.put("vencedor", defensor.getNome());
            } else if (!ninjaDefensor.podeLutar()) {
                batalha.add(defensor.getNome() + " não pode mais lutar!");
                resultado.put("vencedor", atacante.getNome());
            } else {
                resultado.put("statusAtacante", 
                    Map.of("vida", atacante.getVida(), "chakra", atacante.getChakra()));
                resultado.put("statusDefensor", 
                    Map.of("vida", defensor.getVida(), "chakra", defensor.getChakra()));
            }
            
            resultado.put("log", batalha);
            return resultado;
            
        } catch (Exception e) {
            log.error("Erro ao realizar batalha: {}", e.getMessage(), e);
            resultado.put("erro", e.getMessage());
            return resultado;
        }
    }
    
}