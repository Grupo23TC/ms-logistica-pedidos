package com.fiap.tc.logistica.model;

import com.fiap.tc.logistica.dto.ItemPedidoDTO;
import com.fiap.tc.logistica.model.enums.StatusPedidoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    private Long pedidoId;
    private StatusPedidoEnum status;
    private double valorTotal;
    private Long codigoRastreio;
    private List<ItemPedidoDTO> itensPedido;
    private Long usuarioId;

}
