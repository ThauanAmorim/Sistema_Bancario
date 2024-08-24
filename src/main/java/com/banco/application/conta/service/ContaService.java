package com.banco.application.conta.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.banco.domain.conta.exception.ContaInativaException;
import com.banco.domain.conta.exception.ContaNaoEncontradaException;
import com.banco.domain.conta.exception.SaldoInsulficienteException;
import com.banco.domain.conta.exception.ValorInvalidoException;
import com.banco.domain.conta.model.Conta;
import com.banco.domain.historico.model.Historico;

@Service
public class ContaService {

    private List<Conta> contas;

    public ContaService() {
        this.contas = new ArrayList<>();
    }

    public Conta cadastrar(Conta conta) {
        conta.setNumero(contas.size() + 1);

        Historico.cadastro(conta);

        contas.add(conta);
        return conta;
    }

    public Conta depositar(int numeroConta, BigDecimal valor) {
        Conta conta = buscar(numeroConta);
        validarDeposito(conta, valor);

        Historico.deposito(conta, valor);

        conta.depositar(valor);
        return conta;
    }

    public Conta sacar(int numeroConta, BigDecimal valor) {
        Conta conta = buscar(numeroConta);
        validarSaque(conta, valor);

        Historico.saque(conta, valor);

        conta.sacar(valor);
        return conta;
    }

    public Conta transferir(int numeroContaRemetente, int numeroContaDestionatario, BigDecimal valor) {
        Conta remetente = buscar(numeroContaRemetente);
        Conta destinatario = buscar(numeroContaDestionatario);

        validarTransferencia(remetente, destinatario, valor);

        Historico.transferencia(remetente, destinatario, valor);

        remetente.sacar(valor);
        destinatario.depositar(valor);

        return remetente;
    }

    public Conta alterarAtivo(int numeroConta) {
        Conta conta = buscar(numeroConta);

        if (conta.isAtivo()) {
            conta.desativar();
        } else {
            conta.ativar();
        }

        Historico.alterarAtivo(conta);

        return conta;
    }

    public List<Conta> buscarTodos() {
        return this.contas;
    }

    public Conta buscar(int numeroConta) {
        for (Conta conta : contas) {
            if (conta.getNumero() == numeroConta) {
                return conta;
            }
        }
        throw new ContaNaoEncontradaException(numeroConta);
    }

    private void validarSaque(Conta conta, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException(valor);
        }

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsulficienteException(conta.getSaldo(), valor);
        }
    }

    private void validarDeposito(Conta conta, BigDecimal valor) {
        if (!conta.isAtivo()) {
            throw new ContaInativaException(conta.getNumero());
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException(valor);
        }
    }

    private void validarTransferencia(Conta remetente, Conta destintario, BigDecimal valor) {
        if (!remetente.isAtivo()) {
            throw new ContaInativaException(remetente.getNumero());
        }

        if (!destintario.isAtivo()) {
            throw new ContaInativaException(destintario.getNumero());
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException(valor);
        }

        if (remetente.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsulficienteException(remetente.getSaldo(), valor);
        }
    }
    
}
