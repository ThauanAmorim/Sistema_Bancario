package com.banco.domain.historico.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import com.banco.domain.conta.model.Conta;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_historico")
public class Historico {

    private static final String MENSAGEM_CADASTRO = "Cadastrou a conta com R$ %s";
    private static final String MENSAGEM_SAQUE = "Sacou R$ %s";
    private static final String MENSAGEM_DEPOSITO = "Depositou R$ %s";
    private static final String MENSAGEM_RENDA_FIXA = "Recebeu R$ %s de juros";
    private static final String MENSAGEM_TRANSAFERENCIA = "Transferiu R$ %s de %d para %d";
    private static final String MENSAGEM_ALTERACAO_ATIVO = "Alterou o ativo para %s";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_remetente")
    private Conta remetente;
    
    @ManyToOne
    @JoinColumn(name = "id_destinatario")
    private Conta destinatario;

    @Enumerated(EnumType.STRING)
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

    public static Historico transferencia(Conta remetente, Conta destinatario, BigDecimal valor) {
        Historico historico = new Historico(remetente, destinatario, TipoOperacao.TRANSFERENCIA,
                String.format(MENSAGEM_TRANSAFERENCIA, valor, remetente.getId(), destinatario.getId()));
        remetente.addHistorico(historico);
        destinatario.addHistorico(historico);
        return historico;
    }

    public static Historico rendaFixa(Conta remetente, BigDecimal valor) {
        Historico historico = new Historico(remetente, null, TipoOperacao.RENDA_FIXA,
                String.format(MENSAGEM_RENDA_FIXA, valor.setScale(2, RoundingMode.HALF_UP)));
        remetente.addHistorico(historico);
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
        RENDA_FIXA("Renda fixa"),
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
