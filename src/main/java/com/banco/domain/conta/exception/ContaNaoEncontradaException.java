package com.banco.domain.conta.exception;

public class ContaNaoEncontradaException extends RuntimeException {

    private static final String MENSAGEM_PADRAO = "Conta de número %d não encontrada";

    public ContaNaoEncontradaException(long numeroConta) {
        super(String.format(MENSAGEM_PADRAO, numeroConta));
    }

}
