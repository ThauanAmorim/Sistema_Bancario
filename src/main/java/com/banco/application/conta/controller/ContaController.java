package com.banco.application.conta.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.application.conta.service.ContaService;
import com.banco.domain.conta.exception.ContaNaoEncontradaException;
import com.banco.domain.conta.model.Conta;
import com.banco.presentation.conta.request.ContaRequest;
import com.banco.presentation.conta.request.DepositoRequest;
import com.banco.presentation.conta.request.SaqueRequest;
import com.banco.presentation.conta.request.TransferenciaRequest;
import com.banco.presentation.conta.response.ContaResponse;
import com.banco.presentation.conta.response.ContaResponseReduzido;

@RestController
@RequestMapping("/api/conta/")
public class ContaController {

    private final ContaService contaService;
    private final ModelMapper modelMapper;

    public ContaController(final ContaService contaService, final ModelMapper modelMapper) {
        this.contaService = contaService;
        this.modelMapper = modelMapper;
    }
    
    @PostMapping("cadastrar")
    public ResponseEntity<ContaResponse> cadastrar(@RequestBody ContaRequest contaRequest) {
        Conta conta = modelMapper.map(contaRequest, Conta.class);
        conta = contaService.cadastrar(conta);

        ContaResponse response = modelMapper.map(conta, ContaResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("depositar")
    public ResponseEntity<?> depositar(@RequestBody DepositoRequest depositoRequest) {
        Conta conta = null;

        try {
            conta = contaService.depositar(depositoRequest.getIdConta(), depositoRequest.getValor());
        } catch (ContaNaoEncontradaException contaNaoEncontradaException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(contaNaoEncontradaException.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }

        ContaResponse response = modelMapper.map(conta, ContaResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("sacar")
    public ResponseEntity<?> sacar(@RequestBody SaqueRequest saqueRequest) {
        Conta conta = null;

        try {
            conta = contaService.sacar(saqueRequest.getIdConta(), saqueRequest.getValor());
        } catch (ContaNaoEncontradaException contaNaoEncontradaException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(contaNaoEncontradaException.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }

        ContaResponse response = modelMapper.map(conta, ContaResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("transferir")
    public ResponseEntity<?> transferir(@RequestBody TransferenciaRequest transferenciaRequest) {
        Conta conta = null;

        try {
            conta = contaService.transferir(transferenciaRequest.getIdContaRemetente(), transferenciaRequest.getIdContaDestinatario(), transferenciaRequest.getValor());
        } catch (ContaNaoEncontradaException contaNaoEncontradaException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(contaNaoEncontradaException.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }

        ContaResponse response = modelMapper.map(conta, ContaResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("alterar-ativo/{id}")
    public ResponseEntity<?> alterarAtivo(@PathVariable("id") Long idConta) {
        Conta conta = null;

        try {
            conta = contaService.alterarAtivo(idConta);
        } catch (ContaNaoEncontradaException contaNaoEncontradaException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(contaNaoEncontradaException.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }

        ContaResponse response = modelMapper.map(conta, ContaResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("buscar/{id}")
    public ResponseEntity<?> buscarConta(@PathVariable("id") long idConta) {
        Conta conta = null;

        try {
            conta = contaService.buscar(idConta);
        } catch (ContaNaoEncontradaException contaNaoEncontradaException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(contaNaoEncontradaException.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }

        ContaResponse response = modelMapper.map(conta, ContaResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("buscar")
    public ResponseEntity<Page<?>> buscarTodos(Pageable pageable) {
        Page<Conta> contas = null;

        Page<?> response;

        try {
            contas = contaService.buscarTodos(pageable);
        } catch (ContaNaoEncontradaException contaNaoEncontradaException) {
            response = new PageImpl<>(Arrays.asList(contaNaoEncontradaException.getMessage()), pageable, 1);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception exception) {
            response = new PageImpl<>(Arrays.asList(exception.getMessage()), pageable, 1);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        List<ContaResponseReduzido> contaResponseReduzidos = new ArrayList<>(contas.getSize());

        for (Conta conta : contas) {
            contaResponseReduzidos.add(modelMapper.map(conta, ContaResponseReduzido.class));
        }

        response = new PageImpl<>(contaResponseReduzidos, pageable, contaResponseReduzidos.size());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
