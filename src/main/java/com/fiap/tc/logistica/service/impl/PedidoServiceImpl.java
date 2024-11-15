package com.fiap.tc.logistica.service.impl;

import com.fiap.tc.logistica.dto.request.AtualizarRastreioRequest;
import com.fiap.tc.logistica.dto.request.AtualizarStatusPedidoRequest;
import com.fiap.tc.logistica.exception.PedidoNotFoundException;
import com.fiap.tc.logistica.model.Pedido;
import com.fiap.tc.logistica.service.PedidoService;
import com.fiap.tc.logistica.service.feign.PedidoFeignClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoFeignClient pedidoFeignClient;

    @Override
    public Pedido buscarPedidoPorId(long id) {
        try {
            return pedidoFeignClient.buscarPedidoPorId(id);
        } catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                throw new PedidoNotFoundException("Pedido de id: " + id + " não encontrado.");
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public Pedido atualizarStatusPedido(Long id, AtualizarStatusPedidoRequest request) {
        return pedidoFeignClient.atualizarStatusPedido(id, request);
    }

    @Override
    public void atualizarRastreioPedido(Long idPedido, AtualizarRastreioRequest rastreioRequest) {
        pedidoFeignClient.atualizarRastreioPedido(idPedido, rastreioRequest);
    }
}
