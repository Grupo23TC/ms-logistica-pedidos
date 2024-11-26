package com.fiap.tc.logistica.dto.request.entrega;

public record CalcularEntregaRequest(
        Long pedidoId,
        Long clientId,
        double latOrig,
        double lngOrig,
        double latDest,
        double lngDest
) {
}
