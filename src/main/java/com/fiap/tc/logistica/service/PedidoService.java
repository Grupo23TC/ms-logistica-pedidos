package com.fiap.tc.logistica.service;

import com.fiap.tc.logistica.dto.request.AtualizarStatusPedidoRequest;
import com.fiap.tc.logistica.model.Pedido;

public interface PedidoService {

    Pedido buscarPedidoPorId(long id);

    Pedido atualizarStatusPedido(Long id, AtualizarStatusPedidoRequest request);
}