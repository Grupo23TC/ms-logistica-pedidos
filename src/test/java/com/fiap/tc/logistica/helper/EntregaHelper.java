package com.fiap.tc.logistica.helper;

import com.fiap.tc.logistica.model.Entrega;
import com.fiap.tc.logistica.model.enums.StatusEntregaEnum;

import java.time.ZonedDateTime;

public class EntregaHelper {

    public static Entrega gerarEntrega() {
        Entrega entrega = new Entrega();

        entrega.setPedidoId(1L);
        entrega.setInicioEstimado(ZonedDateTime.now());
        entrega.setFimEstimado(ZonedDateTime.now().plusHours(1));
        entrega.setStatus(StatusEntregaEnum.PENDENTE);

        return entrega;
    }
}
