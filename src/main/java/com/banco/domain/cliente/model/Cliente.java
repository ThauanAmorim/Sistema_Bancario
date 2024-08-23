package com.banco.domain.cliente.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    
    private String nome;

    @Override
    public String toString() {
        return "{\"nome\":\"" + nome + "\"}";
    }
    
}
