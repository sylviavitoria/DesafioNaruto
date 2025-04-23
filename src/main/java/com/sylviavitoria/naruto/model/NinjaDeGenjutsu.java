package com.sylviavitoria.naruto.model;

import com.sylviavitoria.naruto.interfaces.Ninja;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("GENJUTSU")
@NoArgsConstructor
public class NinjaDeGenjutsu extends Personagem implements Ninja {

    public NinjaDeGenjutsu(String nome, int idade, String aldeia, int chakra) {
        super(nome, idade, aldeia, chakra);
    }

    @Override
    public void usarJutsu() {
        System.out.println(getNome() + " está usando um jutsu de Genjutsu!");
    }

    @Override
    public void desviar() {
        System.out.println(getNome() + " está desviando usando suas habilidades de Genjutsu!");
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
        
        if (getChakra() < jutsu.getConsumoDeChakra()) {
            return getNome() + " não tem chakra suficiente para usar " + nomeJutsu + "!";
        }
        
        int consumoReal = (int)(jutsu.getConsumoDeChakra() * 1.1);
        diminuirChakra(consumoReal);
        
        double modificadorDano = 1.15;
        int danoReal = jutsu.calcularDanoReal(modificadorDano);
        
        int chanceAcerto = gerarNumeroAleatorio(1, 100);
        
        int chanceCritico = gerarNumeroAleatorio(1, 100);
        boolean acertoCritico = chanceCritico <= 20;
        
        StringBuilder resultado = new StringBuilder();
        resultado.append(getNome()).append(" usa ").append(nomeJutsu).append("! ");
        
        if (chanceAcerto <= 90) {
            if (acertoCritico) {
                int danoCritico = danoReal * 2;
                oponente.receberDano(danoCritico);
                resultado.append("CRÍTICO! Causou ").append(danoCritico).append(" de dano! ");
            } else {
                oponente.receberDano(danoReal);
                resultado.append("Acertou e causou ").append(danoReal).append(" de dano! ");
            }
        } else {
            resultado.append("O jutsu ilusório falhou! ");
        }
        
        resultado.append("[Vida: ").append(getVida()).append(", Chakra: ").append(getChakra()).append("]");
        return resultado.toString();
    }
    
    @Override
    public String desviar(String jutsuRecebido, int danoPotencial) {
        int chanceDesvio = gerarNumeroAleatorio(1, 100);
        int chanceContraAtaque = gerarNumeroAleatorio(1, 100);
        boolean contraAtaque = chanceContraAtaque <= 30; 
        
        StringBuilder resultado = new StringBuilder();
        resultado.append(getNome()).append(" tenta desviar de ").append(jutsuRecebido).append("! ");
        
        if (chanceDesvio <= 40) {
            resultado.append("Conseguiu desviar com uma ilusão! ");
            
            if (contraAtaque) {
                resultado.append("E ainda contra-atacou com uma ilusão, devolvendo 30% do dano! ");
                return resultado.toString() + " [Contra-ataque causou " + (int)(danoPotencial * 0.3) + " de dano]";
            }
        } else {
            int danoRecebido = (int)(danoPotencial * 0.9);
            receberDano(danoRecebido);
            resultado.append("Não conseguiu desviar completamente e recebeu ").append(danoRecebido).append(" de dano! ");
        }
        
        resultado.append("[Vida: ").append(getVida()).append(", Chakra: ").append(getChakra()).append("]");
        return resultado.toString();
    }
    
    @Override
    public boolean podeLutar() {
        return getVida() > 0 && getChakra() > 0;
    }
}