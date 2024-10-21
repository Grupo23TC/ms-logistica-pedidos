package com.fiap.tc.logistica.dto.request;

import com.fiap.tc.logistica.model.enums.StatusPedidoEnum;

public record AtualizarStatusPedidoRequest(
        StatusPedidoEnum novoStatus
) {
}
