package com.sylviavitoria.naruto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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
        validarNome(nome);
        validarIdade(idade);
        validarAldeia(aldeia);
        validarChakra(chakra);
        
        this.nome = nome;
        this.idade = idade;
        this.aldeia = aldeia;
        this.chakra = chakra;
        this.vida = 100;
        this.jutsusMap = new HashMap<>();
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio ou nulo");
        }
    }

    private void validarIdade(int idade) {
        if (idade < 0) {
            throw new IllegalArgumentException("Idade não pode ser negativa");
        }
    }

    private void validarAldeia(String aldeia) {
        if (aldeia == null || aldeia.trim().isEmpty()) {
            throw new IllegalArgumentException("Aldeia não pode ser vazia ou nula");
        }
    }

    private void validarChakra(int chakra) {
        if (chakra < 0) {
            throw new IllegalArgumentException("Chakra não pode ser negativo");
        }
    }

    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    public void setChakra(int chakra) {
        validarChakra(chakra);
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

    public void setVida(int vida) {
        if (vida < 0) {
            throw new IllegalArgumentException("Vida não pode ser negativa");
        }
        this.vida = vida;
    }
}
