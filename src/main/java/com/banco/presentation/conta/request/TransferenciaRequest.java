package com.banco.presentation.conta.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaRequest {

    private Long idContaRemetente;

    private Long idContaDestinatario;

    private BigDecimal valor;
    
}
