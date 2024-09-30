package com.banco.infrastructure.security.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.banco.domain.cliente.model.Cliente;
import com.banco.infrastructure.repository.cliente.ClienteRepository;

@Service
public class SecurityService implements UserDetailsService {

    private ClienteRepository clienteRepository;

    public SecurityService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nome) throws UsernameNotFoundException {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        Optional<Cliente> clienteOp = clienteRepository.buscarPorNome(nome);
        if (clienteOp.isEmpty()) {
            throw new UsernameNotFoundException(nome);
        }

        Cliente cliente = clienteOp.get();
        return new User(
            cliente.getNome(),
            passwordEncoder.encode(cliente.getSenha()),
            Collections.emptyList()
        );
    }
    
}
