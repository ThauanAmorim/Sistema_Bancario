package com.banco.presentation.historico.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoResponse {

   private String tipoOperacao;

    private String descricao;

    private LocalDateTime dataCadastro;

}
