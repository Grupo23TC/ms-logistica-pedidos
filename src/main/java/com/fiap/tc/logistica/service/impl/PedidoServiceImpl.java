package com.fiap.tc.logistica.service.impl;

import com.fiap.tc.logistica.dto.request.AtualizarStatusPedidoRequest;
import com.fiap.tc.logistica.model.Pedido;
import com.fiap.tc.logistica.service.PedidoService;
import com.fiap.tc.logistica.service.feign.PedidoFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoFeignClient pedidoFeignClient;

    @Override
    public Pedido buscarPedidoPorId(long id) {
        return pedidoFeignClient.buscarPedidoPorId(id);
    }

    @Override
    public Pedido atualizarStatusPedido(Long id, AtualizarStatusPedidoRequest request) {
        return pedidoFeignClient.atualizarStatusPedido(id, request);
    }
}
