package com.fiap.tc.logistica.repository;

import com.fiap.tc.logistica.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    List<Entrega> findByPedidoId(Long pedidoId);
}
