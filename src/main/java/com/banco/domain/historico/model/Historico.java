package com.banco.domain.historico.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.banco.domain.conta.model.Conta;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Historico {

    private static final String MENSAGEM_CADASTRO = "Cadastrou a conta com R$ %s";
    private static final String MENSAGEM_SAQUE = "Sacou R$ %s";
    private static final String MENSAGEM_DEPOSITO = "Depositou R$ %s";
    private static final String MENSAGEM_TRANSAFERENCIA = "Transferiu R$ %s de %d para %d";
    private static final String MENSAGEM_ALTERACAO_ATIVO = "Alterou o ativo para %s";

    private Conta remetente;

    private Conta destinatario;

    private TipoOperacao tipoOperacao;

    private String descricao;

    private LocalDateTime dataCadastro;

    private Historico() {
    }

    private Historico(Conta remetente, Conta destinatario, TipoOperacao tipoOperacao, String descricao) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.tipoOperacao = tipoOperacao;
        this.descricao = descricao;
        this.dataCadastro = LocalDateTime.now();
    }

    public static Historico cadastro(Conta remetente) {
        Historico historico = new Historico(remetente, null, TipoOperacao.CADASTRO,
                String.format(MENSAGEM_CADASTRO, remetente.getSaldo()));
        remetente.addHistorico(historico);

        return historico;
    }

    public static Historico saque(Conta remetente, BigDecimal valor) {
        Historico historico = new Historico(remetente, null, TipoOperacao.SAQUE, String.format(MENSAGEM_SAQUE, valor));
        remetente.addHistorico(historico);
        return historico;
    }

    public static Historico deposito(Conta remetente, BigDecimal valor) {
        Historico historico = new Historico(remetente, null, TipoOperacao.DEPOSITO,
                String.format(MENSAGEM_DEPOSITO, valor));
        remetente.addHistorico(historico);
        return historico;
    }

    public static Historico transaferencia(Conta remetente, Conta destinatario, BigDecimal valor) {
        Historico historico = new Historico(remetente, destinatario, TipoOperacao.TRANSFERENCIA,
                String.format(MENSAGEM_TRANSAFERENCIA, valor, remetente.getNumero(), destinatario.getNumero()));
        remetente.addHistorico(historico);
        destinatario.addHistorico(historico);
        return historico;
    }

    public static Historico alterarAtivo(Conta remetente) {
        Historico historico = new Historico(remetente, null, TipoOperacao.ALTERAR_ATIVO,
                String.format(MENSAGEM_ALTERACAO_ATIVO, remetente.isAtivo()));
        remetente.addHistorico(historico);
        return historico;
    }

    @Override
    public String toString() {
        return "{\"tipoOperacao\":\"" + tipoOperacao + "\", \"descricao\":\"" + descricao + "\", \"dataCadastro\":\""
                + dataCadastro + "\"}";
    }

    public enum TipoOperacao {
        CADASTRO("Cadastro"),
        SAQUE("Saque"),
        DEPOSITO("Deposito"),
        TRANSFERENCIA("Transferencia"),
        ALTERAR_ATIVO("Alterar ativo");

        private String label;

        TipoOperacao(String label) {
            this.label = label;
        }

        public String getLabel() {
            return this.label;
        }
    }

}