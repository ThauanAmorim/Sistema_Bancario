package com.banco.application.conta.controller;

import org.modelmapper.ModelMapper;
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
import com.banco.presentation.conta.response.ContaResponse;

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

}
