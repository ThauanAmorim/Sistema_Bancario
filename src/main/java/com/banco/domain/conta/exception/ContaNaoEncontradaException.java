package com.banco.domain.conta.exception;

public class ContaNaoEncontradaException extends RuntimeException {

    private static final String MENSAGEM_NOME = "Conta do cliente %d não encontrada";
    private static final String MENSAGEM_ID = "Conta de id %d não encontrada";

    public ContaNaoEncontradaException(Long idConta) {
        super(String.format(MENSAGEM_ID, idConta));
    }

    public ContaNaoEncontradaException(String nome) {
        super(String.format(MENSAGEM_NOME, nome));
    }

}
