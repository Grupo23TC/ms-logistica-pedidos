package com.fiap.tc.logistica.service;

import com.fiap.tc.logistica.dto.request.CalcularEntregaRequest;
import com.fiap.tc.logistica.model.Entrega;
import com.fiap.tc.logistica.model.rota.RotaResponse;

public interface EntregaService {

    RotaResponse calcularEntrega(CalcularEntregaRequest request);
    Entrega solicitarEntrega(Long entregaId);
    void notificarEntregadores();
    Entrega atribuirEntregadorAEntrega(Long entregaId, Long entregadorId);
    Entrega finalizarEntrega(Long entregaId);
    Entrega buscarEntregaPorId(Long entregaId);

}
