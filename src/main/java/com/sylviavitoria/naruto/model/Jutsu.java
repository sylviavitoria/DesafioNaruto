package com.sylviavitoria.naruto.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Jutsu {
    private int dano;
    private int consumoDeChakra;
    
    public int calcularDanoReal(double modificador) {
        return (int) (dano * modificador);
    }
}