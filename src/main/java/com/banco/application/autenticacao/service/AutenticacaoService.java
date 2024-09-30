package com.banco.application.autenticacao.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.banco.domain.cliente.model.Cliente;
import com.banco.infrastructure.repository.cliente.ClienteRepository;
import com.banco.infrastructure.security.service.JwtService;
import com.banco.infrastructure.security.service.SecurityService;
import com.banco.infrastructure.utils.SharUtils;
import com.banco.presentation.auth.request.LoginRequest;
import com.banco.presentation.auth.response.LoginResponse;

@Service
public class AutenticacaoService {

    private final AuthenticationManager authenticationManager;

    private final SecurityService securityService;

    private final JwtService jwtService;

    private final ClienteRepository clienteRepository;

    public AutenticacaoService(
            AuthenticationManager authenticationManager,
            SecurityService securityService,
            JwtService jwtService,
            ClienteRepository clienteRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.securityService = securityService;
        this.jwtService = jwtService;
        this.clienteRepository = clienteRepository;
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getNome(),
                SharUtils.shar(request.getSenha())));

        Optional<Cliente> clienteOp = clienteRepository.buscarPorNome(request.getNome());
        if (clienteOp.isEmpty()) {
            throw new RuntimeException("Cliente não encontrado");
        }

        UserDetails userDetails = securityService.loadUserByUsername(request.getNome());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token);
    }

}
