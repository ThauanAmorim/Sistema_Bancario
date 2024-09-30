package com.banco.application.conta.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.domain.conta.exception.ContaInativaException;
import com.banco.domain.conta.exception.ContaNaoEncontradaException;
import com.banco.domain.conta.exception.SaldoInsulficienteException;
import com.banco.domain.conta.exception.ValorInvalidoException;
import com.banco.domain.conta.model.Conta;
import com.banco.domain.historico.model.Historico;
import com.banco.infrastructure.repository.Conta.ContaRepository;
import com.banco.infrastructure.utils.LogBuilder;
import com.banco.infrastructure.utils.SharUtils;

@Service
public class ContaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContaService.class);

    private static final int TAXA_JUROS_ANUAL = 24;

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    @Transactional
    public Conta cadastrar(Conta conta) {
        LOGGER.info(LogBuilder.of()
                .header("Iniciando cadastro da conta")
                .row("Conta", conta)
                .build());

        conta.ativar();
        
        Historico.cadastro(conta);

        String senha = conta.getCliente().getSenha();
        senha = SharUtils.shar(senha);
        conta.getCliente().setSenha(senha);

        conta = contaRepository.save(conta);

        LOGGER.info(LogBuilder.of()
                .header("Finalizando cadastro da conta")
                .row("Conta", conta)
                .build());

        return conta;
    }

    @Transactional
    public Conta depositar(String nome, BigDecimal valor) {
        Conta conta = buscar(nome);
        validarDeposito(conta, valor);

        Historico.deposito(conta, valor);

        conta.depositar(valor);
        return contaRepository.save(conta);
    }

    @Transactional
    public Conta sacar(long numeroConta, BigDecimal valor) {
        Conta conta = buscar(numeroConta);
        validarSaque(conta, valor);

        Historico.saque(conta, valor);

        conta.sacar(valor);
        return contaRepository.save(conta);
    }

    @Transactional
    public Conta transferir(long numeroContaRemetente, long numeroContaDestionatario, BigDecimal valor) {
        Conta remetente = buscar(numeroContaRemetente);
        Conta destinatario = buscar(numeroContaDestionatario);

        validarTransferencia(remetente, destinatario, valor);

        Historico.transferencia(remetente, destinatario, valor);

        remetente.sacar(valor);
        remetente = contaRepository.save(remetente);

        destinatario.depositar(valor);
        contaRepository.save(destinatario);

        return remetente;
    }

    @Transactional
    public Conta alterarAtivo(long numeroConta) {
        Conta conta = buscar(numeroConta);

        if (conta.isAtivo()) {
            conta.desativar();
        } else {
            conta.ativar();
        }

        Historico.alterarAtivo(conta);

        return contaRepository.save(conta);
    }

    @Transactional
    public void rendaFixa() {
        int pageSize = 2;

        PageRequest pageRequest = PageRequest.of(0, pageSize, Direction.ASC, "id");
        Page<Conta> contas = buscarTodosAtivos(pageRequest);

        if (!contas.isEmpty()) {
            do {
                LOGGER.info(LogBuilder.of()
                        .header("Processando pagina")
                        .row("Pagina", pageRequest.getPageNumber())
                        .build());

                for (Conta conta : contas) {
                    conta.adicionarValorPorPorcentagem(TAXA_JUROS_ANUAL / 12);
                }
                contaRepository.saveAll(contas);

                pageRequest = PageRequest.of(pageRequest.getPageNumber() + 1, pageSize, Direction.ASC, "id");
                contas = buscarTodosAtivos(pageRequest);
            } while (!contas.isEmpty());
        }

        LOGGER.info(LogBuilder.of()
                .header("Finalizando processamento paginado da renda fixa")
                .build());
    }

    @Transactional(readOnly = true)
    public List<Conta> buscarTodosAtivos() {
        return contaRepository.buscarTodosAtivos();
    }

    @Transactional(readOnly = true)
    public Page<Conta> buscarTodosAtivos(Pageable pageable) {
        return contaRepository.buscarTodosAtivos(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Conta> buscarTodos(Pageable pageable) {
        return contaRepository.buscarTodos(pageable);
    }

    @Transactional(readOnly = true)
    public Conta buscar(long id) {
        Optional<Conta> contaOp = contaRepository.buscarPorId(id);
        
        if (contaOp.isEmpty()) {
            throw new ContaNaoEncontradaException(id);
        }

        return contaOp.get();
    }

    @Transactional(readOnly = true)
    public Conta buscar(String nome) {
        Optional<Conta> contaOp = contaRepository.buscarPorNomeCliente(nome);
        
        if (contaOp.isEmpty()) {
            throw new ContaNaoEncontradaException(nome);
        }

        return contaOp.get();
    }

    private void validarSaque(Conta conta, BigDecimal valor) {
        if (!conta.isAtivo()) {
            throw new ContaInativaException(conta.getId());
        }
        
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException(valor);
        }

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsulficienteException(conta.getSaldo(), valor);
        }
    }

    private void validarDeposito(Conta conta, BigDecimal valor) {
        if (!conta.isAtivo()) {
            throw new ContaInativaException(conta.getId());
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException(valor);
        }
    }

    private void validarTransferencia(Conta remetente, Conta destintario, BigDecimal valor) {
        if (!remetente.isAtivo()) {
            throw new ContaInativaException(remetente.getId());
        }

        if (!destintario.isAtivo()) {
            throw new ContaInativaException(destintario.getId());
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException(valor);
        }

        if (remetente.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsulficienteException(remetente.getSaldo(), valor);
        }
    }
    
}
