package com.fiap.tc.logistica.helper;

import com.fiap.tc.logistica.dto.request.pedido.AtualizarRastreioRequest;
import com.fiap.tc.logistica.dto.request.pedido.AtualizarStatusPedidoRequest;
import com.fiap.tc.logistica.model.Pedido;
import com.fiap.tc.logistica.model.enums.StatusPedidoEnum;

public class PedidoHelper {

    public static Pedido gerarPedido() {
        Pedido pedido = new Pedido();
        pedido.setPedidoId(1L);
        pedido.setStatus(StatusPedidoEnum.CRIADO);
        pedido.setUsuarioId(1L);
        pedido.setValorTotal(5000.0);

        return pedido;
    }

    public static AtualizarStatusPedidoRequest gerarAtualizarStatusPedidoRequest() {
        return new AtualizarStatusPedidoRequest(StatusPedidoEnum.ENVIADO);
    }

    public static AtualizarRastreioRequest gerarAtualizarRastreioRequest() {
        return new AtualizarRastreioRequest(1L);
    }
}
