package com.banco.application.autenticacao.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.application.autenticacao.service.AutenticacaoService;
import com.banco.presentation.auth.request.LoginRequest;
import com.banco.presentation.auth.response.LoginResponse;

@RestController
@RequestMapping("/api/autenticacao/")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(final AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = autenticacaoService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
