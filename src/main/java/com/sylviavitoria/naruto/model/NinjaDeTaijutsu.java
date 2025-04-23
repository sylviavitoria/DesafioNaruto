package com.sylviavitoria.naruto.model;

import com.sylviavitoria.naruto.interfaces.Ninja;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("TAIJUTSU")
@NoArgsConstructor
public class NinjaDeTaijutsu extends Personagem implements Ninja {

    public NinjaDeTaijutsu(String nome, int idade, String aldeia, int chakra) {
        super(nome, idade, aldeia, chakra);
    }

    @Override
    public void usarJutsu() {
        System.out.println(getNome() + " está usando um golpe de Taijutsu!");
    }

    @Override
    public void desviar() {
        System.out.println(getNome() + " está desviando usando suas habilidades de Taijutsu!");
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
        
        diminuirChakra(jutsu.getConsumoDeChakra());
        
        double modificadorDano = 1.2;
        int danoReal = jutsu.calcularDanoReal(modificadorDano);
        
        int chanceAcerto = gerarNumeroAleatorio(1, 100);
        
        StringBuilder resultado = new StringBuilder();
        resultado.append(getNome()).append(" usa ").append(nomeJutsu).append("! ");
        
        if (chanceAcerto <= 80) {
            oponente.receberDano(danoReal);
            resultado.append("Acertou e causou ").append(danoReal).append(" de dano! ");
        } else {
            resultado.append("Errou o golpe! ");
        }
        
        resultado.append("[Vida: ").append(getVida()).append(", Chakra: ").append(getChakra()).append("]");
        return resultado.toString();
    }
    
    @Override
    public String desviar(String jutsuRecebido, int danoPotencial) {
        int chanceDesvio = gerarNumeroAleatorio(1, 100);
        StringBuilder resultado = new StringBuilder();
        
        resultado.append(getNome()).append(" tenta desviar de ").append(jutsuRecebido).append("! ");
        
        if (chanceDesvio <= 70) {
            resultado.append("Conseguiu desviar com suas habilidades de Taijutsu! ");
        } else {
            int danoRecebido = (int)(danoPotencial * 0.8); 
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