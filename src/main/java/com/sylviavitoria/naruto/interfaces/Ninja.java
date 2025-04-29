package com.sylviavitoria.naruto.interfaces;

import com.sylviavitoria.naruto.model.Personagem;

public interface Ninja {

    void usarJutsu();
    void desviar();
    
    String usarJutsu(String nomeJutsu, Personagem oponente);
    
    String desviar(String jutsuRecebido, int danoPotencial);
    
    boolean podeLutar();
}