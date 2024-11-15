package com.fiap.tc.logistica.service.feign;

import com.fiap.tc.logistica.config.feign.FeignConfiguration;
import com.fiap.tc.logistica.dto.request.AtualizarRastreioRequest;
import com.fiap.tc.logistica.dto.request.AtualizarStatusPedidoRequest;
import com.fiap.tc.logistica.model.Pedido;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pedidos", primary = false, contextId = "pedidoClient", url = "${application.client.pedidos.api.host}",
        configuration = FeignConfiguration.class)
public interface PedidoFeignClient {

    @GetMapping(value = "/pedidos/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    Pedido buscarPedidoPorId(@PathVariable("id") Long id);

    @PutMapping("/pedidos/atualizar-status/{id}")
    Pedido atualizarStatusPedido(@PathVariable("id") Long id, @RequestBody AtualizarStatusPedidoRequest status);

    @PutMapping("/pedidos/atualizar-rastreio/{id}")
    Pedido atualizarRastreioPedido(@PathVariable("id") Long id, @RequestBody AtualizarRastreioRequest body);

}
