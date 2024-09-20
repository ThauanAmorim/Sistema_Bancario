package com.banco.presentation.conta.response;

import java.math.BigDecimal;

import com.banco.presentation.cliente.response.ClienteResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaResponseReduzido {

    private Long id;

    private ClienteResponse cliente;
    
    private BigDecimal saldo;

    private boolean ativo;

}
