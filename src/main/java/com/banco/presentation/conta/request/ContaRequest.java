package com.banco.presentation.conta.request;

import java.math.BigDecimal;

import com.banco.presentation.cliente.request.ClienteRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaRequest {

    private ClienteRequest cliente;
    
    private BigDecimal saldo;

}
