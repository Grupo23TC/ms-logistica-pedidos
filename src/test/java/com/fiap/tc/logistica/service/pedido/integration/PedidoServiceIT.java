package com.fiap.tc.logistica.service.pedido.integration;

import com.fiap.tc.logistica.exception.PedidoNotFoundException;
import com.fiap.tc.logistica.helper.PedidoHelper;
import com.fiap.tc.logistica.model.Pedido;
import com.fiap.tc.logistica.service.PedidoService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
public class PedidoServiceIT {

    @Autowired
    private PedidoService pedidoService;

    @Nested
    class BuscarPedido {

        @Test
        void devePermitirBuscarPedidoPorId() {
            var id = 1L;

            var result = pedidoService.buscarPedidoPorId(id);

            assertThat(result)
                    .isInstanceOf(Pedido.class)
                    .isNotNull();
        }

        @Test
        void deveGerarExcecao_QuandoBuscarPedidoPorId_IdNaoEncontrado() {
            var id = 1000000000l;
            var mensagem = "Pedido de id: " + id + " não encontrado.";

            assertThatThrownBy(() -> pedidoService.buscarPedidoPorId(id))
                    .isInstanceOf(PedidoNotFoundException.class)
                    .hasMessage(mensagem);

        }
    }

    @Nested
    class AtualizarPedido {

        @Test
        void devePermitirAtualizarStatusPedido() {
            var id = 1L;
            var request = PedidoHelper.gerarAtualizarStatusPedidoRequest();

            var result = pedidoService.atualizarStatusPedido(id, request);
            assertThat(result)
                    .isInstanceOf(Pedido.class)
                    .isNotNull();

        }

        @Test
        void deveGerarExcecao_QuandoAtualizarStatusPedido_IdNaoEncontrado() {
            var id = 1000000l;
            var mensagem = "Pedido de id: " + id + " não encontrado.";
            var request = PedidoHelper.gerarAtualizarStatusPedidoRequest();

            assertThatThrownBy(() -> pedidoService.atualizarStatusPedido(id, request))
                    .isInstanceOf(PedidoNotFoundException.class)
                    .hasMessage(mensagem);


        }

        @Test
        void devePermitirAtualizarCodigoRastreioPedido() {
            var id = 1L;
            var request = PedidoHelper.gerarAtualizarRastreioRequest();

            var result = pedidoService.atualizarRastreioPedido(id, request);
            assertThat(result)
                    .isInstanceOf(Pedido.class)
                    .isNotNull();

        }

        @Test
        void deveGerarExcecao_QuandoAtualizarCodigoRastreioPedido_IdNaoEncontrado() {
            var id = 1000000000l;
            var request = PedidoHelper.gerarAtualizarRastreioRequest();
            var mensagem = "Pedido de id: " + id + " não encontrado.";

            assertThatThrownBy(() -> pedidoService.atualizarRastreioPedido(id, request))
                    .isInstanceOf(PedidoNotFoundException.class)
                    .hasMessage(mensagem);

        }
    }
}
