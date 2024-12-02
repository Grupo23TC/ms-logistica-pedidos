package com.fiap.tc.logistica.repository;

import com.fiap.tc.logistica.model.Entrega;
import com.fiap.tc.logistica.model.enums.StatusEntregaEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    List<Entrega> findByPedidoIdAndStatusNot(Long pedidoId, StatusEntregaEnum status);
}
