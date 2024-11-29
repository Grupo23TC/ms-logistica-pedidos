package com.fiap.tc.logistica.service;

import com.fiap.tc.logistica.dto.request.entrega.CalcularEntregaRequest;
import com.fiap.tc.logistica.model.Entrega;
import com.fiap.tc.logistica.model.rota.RotaResponse;

public interface EntregaService {

    RotaResponse calcularECriarEntrega(CalcularEntregaRequest request);
    Entrega solicitarEntrega(Long entregaId);
    Entrega atribuirEntregadorAEntrega(Long entregaId, Long entregadorId);
    Entrega finalizarEntrega(Long entregaId);
    Entrega buscarEntregaPorId(Long entregaId);
    Entrega cancelarEntrega(Long entregaId);

}
