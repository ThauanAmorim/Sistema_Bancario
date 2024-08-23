package com.banco.domain.conta.exception;

import java.math.BigDecimal;

public class ValorInvalidoException extends RuntimeException {

    private static final String MENSAGEM_PADRAO = "O valor de %s não é válido";

    public ValorInvalidoException(BigDecimal valor) {
        super(String.format(MENSAGEM_PADRAO, valor));
    }

}
