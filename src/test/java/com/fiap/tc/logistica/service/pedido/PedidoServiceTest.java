package com.fiap.tc.logistica.service.pedido;

import com.fiap.tc.logistica.dto.request.pedido.AtualizarRastreioRequest;
import com.fiap.tc.logistica.dto.request.pedido.AtualizarStatusPedidoRequest;
import com.fiap.tc.logistica.exception.PedidoNotFoundException;
import com.fiap.tc.logistica.helper.FeignExceptionHelper;
import com.fiap.tc.logistica.helper.PedidoHelper;
import com.fiap.tc.logistica.model.Pedido;
import com.fiap.tc.logistica.service.feign.PedidoFeignClient;
import com.fiap.tc.logistica.service.impl.PedidoServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PedidoServiceTest {

    @Mock
    private PedidoFeignClient client;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Nested
    class BuscarPedido {

        @Test
        void devePermitirBuscarPedidoPorId() {
            var id = 1L;
            var pedido = PedidoHelper.gerarPedido();
            when(client.buscarPedidoPorId(anyLong())).thenReturn(pedido);

            var result = pedidoService.buscarPedidoPorId(id);

            assertThat(result)
                    .isInstanceOf(Pedido.class)
                    .isEqualTo(pedido);
            verify(client, times(1)).buscarPedidoPorId(anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoBuscarPedidoPorId_IdNaoEncontrado() {
            var id = 1l;
            var mensagem = "Pedido de id: " + id + " não encontrado.";
            when(client.buscarPedidoPorId(anyLong()))
                    .thenThrow(FeignExceptionHelper.gerarFeignExceptionNotFound());

            assertThatThrownBy(() -> pedidoService.buscarPedidoPorId(id))
                    .isInstanceOf(PedidoNotFoundException.class)
                    .hasMessage(mensagem);
            verify(client, times(1)).buscarPedidoPorId(anyLong());

        }

        @Test
        void deveGerarExcecao_QuandoBuscarPedidoPorId_ErroGenerico() {
            var id = 1l;
            when(client.buscarPedidoPorId(anyLong()))
                    .thenThrow(FeignExceptionHelper.gerarFeignExceptionInternalServerError());

            assertThatThrownBy(() -> pedidoService.buscarPedidoPorId(id))
                    .isInstanceOf(RuntimeException.class);
            verify(client, times(1)).buscarPedidoPorId(anyLong());
        }

    }

    @Nested
    class AtualizarPedido {

        @Test
        void devePermitirAtualizarStatusPedido() {
            var id = 1L;
            var request = PedidoHelper.gerarAtualizarStatusPedidoRequest();
            var pedido = PedidoHelper.gerarPedido();
            when(client.atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class))).thenReturn(pedido);

            var result = pedidoService.atualizarStatusPedido(id, request);
            assertThat(result)
                    .isInstanceOf(Pedido.class)
                    .isEqualTo(pedido);
            verify(client, times(1)).atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class));
        }

        @Test
        void deveGerarExcecao_QuandoAtualizarStatusPedido_IdNaoEncontrado() {
            var id = 1l;
            var mensagem = "Pedido de id: " + id + " não encontrado.";
            var request = PedidoHelper.gerarAtualizarStatusPedidoRequest();
            when(client.atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class)))
                    .thenThrow(FeignExceptionHelper.gerarFeignExceptionNotFound());

            assertThatThrownBy(() -> pedidoService.atualizarStatusPedido(id, request))
                    .isInstanceOf(PedidoNotFoundException.class)
                    .hasMessage(mensagem);
            verify(client, times(1)).atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class));

        }

        @Test
        void deveGerarExcecao_QuandoAtualizarStatusPedido_ErroGenerico() {
            var id = 1l;
            var request = PedidoHelper.gerarAtualizarStatusPedidoRequest();
            when(client.atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class)))
                    .thenThrow(FeignExceptionHelper.gerarFeignExceptionInternalServerError());

            assertThatThrownBy(() -> pedidoService.atualizarStatusPedido(id, request))
                    .isInstanceOf(RuntimeException.class);
            verify(client, times(1)).atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class));
        }


        @Test
        void devePermitirAtualizarCodigoRastreioPedido() {
            var id = 1L;
            var request = PedidoHelper.gerarAtualizarRastreioRequest();
            var pedido = PedidoHelper.gerarPedido();
            when(client.atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class))).thenReturn(pedido);

            var result = pedidoService.atualizarRastreioPedido(id, request);
            assertThat(result)
                    .isInstanceOf(Pedido.class)
                    .isEqualTo(pedido);
            verify(client, times(1)).atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class));
        }

        @Test
        void deveGerarExcecao_QuandoAtualizarCodigoRastreioPedido_IdNaoEncontrado() {
            var id = 1l;
            var request = PedidoHelper.gerarAtualizarRastreioRequest();
            var mensagem = "Pedido de id: " + id + " não encontrado.";
            when(client.atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class)))
                    .thenThrow(FeignExceptionHelper.gerarFeignExceptionNotFound());

            assertThatThrownBy(() -> pedidoService.atualizarRastreioPedido(id, request))
                    .isInstanceOf(PedidoNotFoundException.class)
                    .hasMessage(mensagem);
            verify(client, times(1)).atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class));

        }

        @Test
        void deveGerarExcecao_QuandoAtualizarCodigoRastreioPedido_ErroGenerico() {
            var id = 1l;
            var request = PedidoHelper.gerarAtualizarRastreioRequest();
            when(client.atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class)))
                    .thenThrow(FeignExceptionHelper.gerarFeignExceptionInternalServerError());

            assertThatThrownBy(() -> pedidoService.atualizarRastreioPedido(id, request))
                    .isInstanceOf(RuntimeException.class);
            verify(client, times(1)).atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class));
        }
    }
}
