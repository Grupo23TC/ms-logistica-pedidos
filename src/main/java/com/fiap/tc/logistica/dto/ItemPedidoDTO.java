package com.fiap.tc.logistica.dto;

public record ItemPedidoDTO (
        Long produtoId,
        int quantidade,
        double preco
) {
}
