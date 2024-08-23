package com.banco.domain.conta.exception;

import java.math.BigDecimal;

public class SaldoInsulficienteException extends RuntimeException {

    private static final String MENSAGEM_PADRAO = "Saldo insulficiente. Seu saldo é de %s, e você tentou subtrair %s";

    public SaldoInsulficienteException(BigDecimal saldo, BigDecimal valor) {
        super(String.format(MENSAGEM_PADRAO, saldo, valor));
    }

}
