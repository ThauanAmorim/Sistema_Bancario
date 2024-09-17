package com.banco.presentation.conta.response;

import java.math.BigDecimal;
import java.util.List;

import com.banco.presentation.cliente.response.ClienteResponse;
import com.banco.presentation.historico.response.HistoricoResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaResponse {

    private Long id;

    private ClienteResponse cliente;
    
    private BigDecimal saldo;

    private List<HistoricoResponse> historicos;

}
