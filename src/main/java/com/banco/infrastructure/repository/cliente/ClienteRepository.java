package com.banco.infrastructure.repository.cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banco.domain.cliente.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT cliente FROM Cliente cliente WHERE cliente.nome = :nome")
    public Optional<Cliente> buscarPorNome(@Param("nome") String nome);
    
}
