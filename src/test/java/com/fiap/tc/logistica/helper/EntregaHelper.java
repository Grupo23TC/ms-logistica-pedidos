package com.fiap.tc.logistica.helper;

import com.fiap.tc.logistica.dto.request.entrega.CalcularEntregaRequest;
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

    public static CalcularEntregaRequest gerarCalcularEntregaRequest() {
        return new CalcularEntregaRequest(
                1l,
                1l,
                -23.4984522,
                -47.4309735,
                -23.4923629,
                -47.4292845
        );
    }

    public static CalcularEntregaRequest gerarCalcularEntregaRequestPedidoNaoExistente() {
        return new CalcularEntregaRequest(
                1000000l,
                1l,
                -23.4984522,
                -47.4309735,
                -23.4923629,
                -47.4292845
        );
    }

    public static CalcularEntregaRequest gerarCalcularEntregaRequestEntregaJaExistente() {
        return new CalcularEntregaRequest(
                5l,
                1l,
                -23.4984522,
                -47.4309735,
                -23.4923629,
                -47.4292845
        );
    }

    public static CalcularEntregaRequest gerarCalcularEntregaRequestRotaNaoExistente() {
        return new CalcularEntregaRequest(
                6l,
                1l,
                -10000000.4984522,
                -10000000000.4309735,
                -2000000.4923629,
                -4755224147.4292845
        );
    }
}
