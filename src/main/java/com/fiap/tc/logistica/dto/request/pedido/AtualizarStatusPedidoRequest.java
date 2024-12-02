package com.fiap.tc.logistica.dto.request.pedido;

import com.fiap.tc.logistica.model.enums.StatusPedidoEnum;

public record AtualizarStatusPedidoRequest(
        StatusPedidoEnum novoStatus
) {
}
