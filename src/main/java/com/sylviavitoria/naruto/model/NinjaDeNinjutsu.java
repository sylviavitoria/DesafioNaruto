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

    @Override
    public String usarJutsu(String nomeJutsu, Personagem oponente) {
        if (!podeLutar()) {
            return getNome() + " não pode mais lutar!";
        }

        Jutsu jutsu = getJutsu(nomeJutsu);
        if (jutsu == null) {
            return getNome() + " não conhece o jutsu " + nomeJutsu + "!";
        }

        if (getChakra() < 10) {
            return getNome() + " não tem chakra suficiente para usar " + nomeJutsu + "!";
        }

        diminuirChakra(10);

        int danoReal = jutsu.getDano();
        oponente.receberDano(danoReal);

        StringBuilder resultado = new StringBuilder();
        resultado.append(getNome()).append(" usa ").append(nomeJutsu).append("! ");
        resultado.append("Causou ").append(danoReal).append(" de dano! ");
        resultado.append("[Vida: ").append(getVida()).append(", Chakra: ").append(getChakra()).append("]");

        return resultado.toString();
    }

    @Override
    public String desviar(String jutsuRecebido, int danoPotencial) {
        int chanceDesvio = gerarNumeroAleatorio(1, 100);
        StringBuilder resultado = new StringBuilder();

        resultado.append(getNome()).append(" tenta desviar de ").append(jutsuRecebido).append("! ");

        if (chanceDesvio <= 50) {
            resultado.append("Conseguiu desviar com um jutsu de Ninjutsu! ");
        } else {
            int danoRecebido = danoPotencial;
            receberDano(danoRecebido);
            resultado.append("Não conseguiu desviar e recebeu ").append(danoRecebido).append(" de dano! ");
        }

        resultado.append("[Vida: ").append(getVida()).append(", Chakra: ").append(getChakra()).append("]");
        return resultado.toString();
    }

    @Override
    public boolean podeLutar() {
        return getVida() > 0 && getChakra() > 0;
    }
}
