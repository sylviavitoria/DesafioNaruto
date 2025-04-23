package com.sylviavitoria.naruto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_ninja")
public abstract class Personagem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private int idade;
    private String aldeia;
    
    
    @ElementCollection
    @CollectionTable(name = "jutsus_map")
    @MapKeyColumn(name = "nome_jutsu")
    private Map<String, Jutsu> jutsusMap = new HashMap<>();
    
    private int chakra = 100;
    private int vida = 100;

    public Personagem(String nome, int idade, String aldeia, int chakra) {
        this.nome = nome;
        this.idade = idade;
        this.aldeia = aldeia;
        this.chakra = chakra;
    }

    public void adicionarJutsu(String nomeJutsu, int dano, int consumoDeChakra) {
        Jutsu jutsu = new Jutsu(dano, consumoDeChakra);
        jutsusMap.put(nomeJutsu, jutsu);
    }
    
    public List<String> getJutsus() {
        return new ArrayList<>(jutsusMap.keySet());
    }

    @JsonIgnore
    public Jutsu getJutsu(String nomeJutsu) {
        return jutsusMap.get(nomeJutsu);
    }

    public void aumentarChakra(int quantidade) {
        this.chakra += quantidade;
    }

    public void diminuirChakra(int quantidade) {
        this.chakra -= quantidade;
        if (this.chakra < 0) {
            this.chakra = 0;
        }
    }

    public void receberDano(int dano) {
        this.vida -= dano;
        if (this.vida < 0) {
            this.vida = 0;
        }
    }

    protected int gerarNumeroAleatorio(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    
    @JsonIgnore
    public boolean podeLutar() {
        return vida > 0 && chakra > 0;
    }

    public String exibirInformacoes() {
        StringBuilder info = new StringBuilder();
        info.append(String.format("Nome: %s\nIdade: %d\nAldeia: %s\nVida: %d\nChakra: %d\n", 
                nome, idade, aldeia, vida, chakra));
        
        info.append("Jutsus:\n");
        for (Map.Entry<String, Jutsu> entry : jutsusMap.entrySet()) {
            info.append(String.format("- %s (Dano: %d, Consumo: %d)\n", 
                    entry.getKey(), entry.getValue().getDano(), entry.getValue().getConsumoDeChakra()));
        }
        
        return info.toString();
    }
}
