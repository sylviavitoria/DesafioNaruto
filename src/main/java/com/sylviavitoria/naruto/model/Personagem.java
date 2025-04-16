package com.sylviavitoria.naruto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @CollectionTable(name = "jutsus")
    private List<String> jutsus = new ArrayList<>();
    
    private int chakra;

    public Personagem(String nome, int idade, String aldeia, int chakra) {
        this.nome = nome;
        this.idade = idade;
        this.aldeia = aldeia;
        this.chakra = chakra;
    }

    public void adicionarJutsu(String jutsu) {
        jutsus.add(jutsu);
    }

    public void aumentarChakra(int quantidade) {
        this.chakra += quantidade;
    }

    public String exibirInformacoes() {
        return String.format(
            "Nome: %s\nIdade: %d\nAldeia: %s\nJutsus: %s\nChakra: %d",
            nome, idade, aldeia, jutsus.toString(), chakra
        );
    }
}
