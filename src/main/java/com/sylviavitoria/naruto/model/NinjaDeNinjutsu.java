package com.sylviavitoria.naruto.model;

import com.sylviavitoria.naruto.interfaces.Ninja;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("NINJUTSU")
@NoArgsConstructor
public class NinjaDeNinjutsu extends Personagem implements Ninja {

    public NinjaDeNinjutsu(String nome, int idade, String aldeia, int chakra) {
        super(nome, idade, aldeia, chakra);
    }

    @Override
    public void usarJutsu() {
        System.out.println(getNome() + " está usando um jutsu de Ninjutsu!");
    }

    @Override
    public void desviar() {
        System.out.println(getNome() + " está desviando usando suas habilidades de Ninjutsu!");
    }
}
