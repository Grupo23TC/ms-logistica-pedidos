package com.fiap.tc.logistica.repository;

import com.fiap.tc.logistica.model.Entregador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregadorRepository extends JpaRepository<Entregador, Long> {

    @Query("SELECT e FROM Entregador e WHERE e.estaDisponivel is true")
    List<Entregador> buscarEntregadoresDisponiveis();
}
