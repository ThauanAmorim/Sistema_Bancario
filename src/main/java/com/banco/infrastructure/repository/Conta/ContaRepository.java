package com.banco.infrastructure.repository.Conta;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.banco.domain.conta.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    
    @Query("SELECT conta FROM Conta conta WHERE id = :idConta")
    public Optional<Conta> buscarPorId(@Param("idConta") long idConta);

}
