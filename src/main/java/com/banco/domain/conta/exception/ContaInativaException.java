package com.banco.domain.conta.exception;

public class ContaInativaException extends RuntimeException {

    private static final String MENSAGEM_PADRAO = "Conta de número %d não está ativa para receber operações";

    public ContaInativaException(long numeroConta) {
        super(String.format(MENSAGEM_PADRAO, numeroConta));
    }

}
