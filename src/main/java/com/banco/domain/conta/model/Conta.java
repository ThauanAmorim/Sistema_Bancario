package com.banco.domain.conta.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.banco.domain.cliente.model.Cliente;
import com.banco.domain.historico.model.Historico;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Cliente cliente;

    private BigDecimal saldo;

    @Temporal(TemporalType.TIMESTAMP)
    @OrderBy(value = "dataCadastro DESC")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Historico> historicos = new ArrayList<>();

    private boolean ativo;

    public Conta(Cliente cliente, BigDecimal saldo) {
        this.cliente = cliente;
        this.saldo = saldo;
        this.historicos = new ArrayList<>();
        this.ativo = true;
    }

    public void sacar(BigDecimal valor) {
        this.saldo = saldo.subtract(valor);
    }

    public void depositar(BigDecimal valor) {
        this.saldo = saldo.add(valor);
    }

    public void adicionarValorPorPorcentagem(int porcentagem) {
        BigDecimal valorAdicional = getSaldo().multiply(BigDecimal.valueOf(porcentagem / 100f));
        setSaldo(getSaldo().add(valorAdicional));

        Historico.rendaFixa(this, valorAdicional);
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void addHistorico(Historico historico) {
        if (historicos == null) {
            this.historicos = new ArrayList<>();
        }

        this.historicos.add(historico);
    }

    @Override
    public String toString() {
        return "{\"cliente\":\"" + cliente + "\", \"saldo\":\"" + saldo
                + "\", \"historicos\":\"" + Arrays.toString(historicos.toArray()) + "\", \"ativo\":\"" + ativo + "\"}";
    }

}
