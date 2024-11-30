package com.fiap.tc.logistica.dto.request.entrega;
import io.swagger.v3.oas.annotations.media.Schema;

public record CalcularEntregaRequest(
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long pedidoId,
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long clientId,
        @Schema(example = "-23.4984522", requiredMode = Schema.RequiredMode.REQUIRED)
        double latOrig,
        @Schema(example = "-47.4309735", requiredMode = Schema.RequiredMode.REQUIRED)
        double lngOrig,
        @Schema(example = "-23.4923629", requiredMode = Schema.RequiredMode.REQUIRED)
        double latDest,
        @Schema(example = "-47.4292845", requiredMode = Schema.RequiredMode.REQUIRED)
        double lngDest
) {
}
