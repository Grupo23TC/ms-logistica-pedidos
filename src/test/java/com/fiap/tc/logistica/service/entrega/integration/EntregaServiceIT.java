package com.fiap.tc.logistica.service.entrega.integration;

import com.fiap.tc.logistica.exception.EntregaNotFoundException;
import com.fiap.tc.logistica.exception.EntregadorNotFoundException;
import com.fiap.tc.logistica.exception.PedidoNotFoundException;
import com.fiap.tc.logistica.exception.RotaNotFoundException;
import com.fiap.tc.logistica.helper.EntregaHelper;
import com.fiap.tc.logistica.model.Entrega;
import com.fiap.tc.logistica.model.enums.StatusEntregaEnum;
import com.fiap.tc.logistica.model.rota.RotaResponse;
import com.fiap.tc.logistica.service.EntregaService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class EntregaServiceIT {

    @Autowired
    private EntregaService entregaService;

    @Nested
    class BuscarEntrega {

        @Test
        void devePermitirBuscarEntregaPorId() {
            var id = 1L;

            var result = entregaService.buscarEntregaPorId(id);

            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(Entrega.class);
            assertThat(result.getEntregaId()).isEqualTo(id);
        }

        @Test
        void deveGerarExcecao_QuandoBuscarEntregaPorId_IdNaoEncontrado() {
            var id = 10000000L;
            var message = "Entrega de id: " + id + " não encontrada.";

            assertThatThrownBy(() -> entregaService.buscarEntregaPorId(id))
                    .isInstanceOf(EntregaNotFoundException.class)
                    .hasMessage(message);
        }
    }

    @Nested
    class CalcularRotaECriarEntrega {

        @Test
        void devePermitirCalcularRotaECriarEntrega() {
            var request = EntregaHelper.gerarCalcularEntregaRequest();

            var result = entregaService.calcularECriarEntrega(request);

            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(RotaResponse.class);
            assertThat(result.getRoutes())
                    .isNotEmpty();
        }

        @Test
        void deveGerarExcecao_QuandoCalcularRotaECriarEntrega_JaExisteEntregaPedido() {
            var request = EntregaHelper.gerarCalcularEntregaRequestEntregaJaExistente();
            var message = "Já existe uma entrega com esse pedido";

            assertThatThrownBy(() -> entregaService.calcularECriarEntrega(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(message);
        }

        @Test
        void deveGerarExcecao_QuandoCalcularRotaECriarEntrega_PedidoNaoEncontrado() {
            var request = EntregaHelper.gerarCalcularEntregaRequestPedidoNaoExistente();
            var message = "Pedido de id: " + request.pedidoId() + " não encontrado.";

            assertThatThrownBy(() -> entregaService.calcularECriarEntrega(request))
                    .isInstanceOf(PedidoNotFoundException.class)
                    .hasMessage(message);
        }

        @Test
        void deveGerarExcecao_QuandoCalcularRotaECriarEntrega_RotaNaoEncontrada() {
            var request = EntregaHelper.gerarCalcularEntregaRequestRotaNaoExistente();
            var message = "Não foi possível calcular rota, revise as coordenadas.";

            assertThatThrownBy(() -> entregaService.calcularECriarEntrega(request))
                    .isInstanceOf(RotaNotFoundException.class)
                    .hasMessage(message);
        }
    }

    @Nested
    class SolicitarEntrega {

        @Test
        void devePermitirSolicitarEntrega() {
            var id = 1L;

            var result = entregaService.solicitarEntrega(id);

            assertThat(result)
                    .isInstanceOf(Entrega.class)
                    .isNotNull();
            assertThat(result.getEntregaId()).isEqualTo(id);
            assertThat(result.getStatus()).isEqualTo(StatusEntregaEnum.SOLICITADA);
        }

        @Test
        void deveGerarExcecao_QuandoSolicitarEntrega_IdNaoEncontrado() {
            var id = 100000L;
            var message = "Entrega de id: " + id + " não encontrada.";

            assertThatThrownBy(() -> entregaService.solicitarEntrega(id))
                    .isInstanceOf(EntregaNotFoundException.class)
                    .hasMessage(message);
        }

        @Test
        void deveGerarExcecao_QuandoSolicitarEntrega_StatusNaoPendente() {
            var id = 2L;
            var message = "A entrega precisa estar com status PENDENTE para ser solicitada.";

            assertThatThrownBy(() -> entregaService.solicitarEntrega(id))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(message);
        }

    }

    @Nested
    class AtribuirEntregadorParaEntrega {

        @Test
        void devePermitirAtribuirEntregadorParaEntrega() {
            var id = 2L;

            var result = entregaService.atribuirEntregadorAEntrega(id, id);

            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(Entrega.class);
            assertThat(result.getEntregaId()).isEqualTo(id);
            assertThat(result.getStatus()).isEqualTo(StatusEntregaEnum.ENVIADA);
            assertThat(result.getEntregador().getEntregadorId()).isEqualTo(id);
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_IdNaoEncontrado() {
            var id = 100000L;
            var message = "Entrega de id: " + id + " não encontrada.";

            assertThatThrownBy(() -> entregaService.atribuirEntregadorAEntrega(id, id))
                    .isInstanceOf(EntregaNotFoundException.class)
                    .hasMessage(message);
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_StatusNaoSolicitada() {
            var id = 5L;
            var message = "A entrega precisa estar com status SOLICITADA para ser atribuída.";

            assertThatThrownBy(() -> entregaService.atribuirEntregadorAEntrega(id, id))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(message);

        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_EntregadorNaoEncontrado() {
            var id = 6L;
            var entregadorid = 1000000l;
            var message = "Entregador de id: " + entregadorid + " não encontrado.";

            assertThatThrownBy(() -> entregaService.atribuirEntregadorAEntrega(id, entregadorid))
                    .isInstanceOf(EntregadorNotFoundException.class)
                    .hasMessage(message);
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_EntregadorNaoDisponivel() {
            var id = 6L;
            var entregadorid = 3l;
            var message = "O Entregador: Rafael não está disponivel para realizar a entrega.";

            assertThatThrownBy(() -> entregaService.atribuirEntregadorAEntrega(id, entregadorid))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(message);

        }

    }

    @Nested
    class FinalizarEntrega {

        @Test
        void devePermitirFinalizarEntrega() {
            var id = 3L;

            var result = entregaService.finalizarEntrega(id);

            assertThat(result)
                    .isInstanceOf(Entrega.class)
                    .isNotNull();
            assertThat(result.getStatus()).isEqualTo(StatusEntregaEnum.ENTREGUE);
        }

        @Test
        void deveGerarExcecao_QuandoFinalizarEntrega_IdNaoEncontrado() {
            var id = 1000000L;
            var message = "Entrega de id: " + id + " não encontrada.";

            assertThatThrownBy(() -> entregaService.finalizarEntrega(id))
                    .isInstanceOf(EntregaNotFoundException.class)
                    .hasMessage(message);
        }

        @Test
        void deveGerarExcecao_QuandoFinalizarEntrega_StatusNaoEnviada() {
            var id = 5L;
            var message = "A entrega precisa estar com status ENVIADA para ser finalizada.";

            assertThatThrownBy(() -> entregaService.finalizarEntrega(id))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(message);

        }

    }

    @Nested
    class CancelarEntrega {

        @Test
        void devePermitirCancelarEntrega() {
            var id = 7L;

            var result = entregaService.cancelarEntrega(id);

            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(Entrega.class);
            assertThat(result.getStatus()).isEqualTo(StatusEntregaEnum.CANCELADA);
        }

        @Test
        void deveGerarExcecao_QuandoCancelarEntrega_IdNaoEncontrado() {
            var id = 1000000L;
            var message = "Entrega de id: " + id + " não encontrada.";

            assertThatThrownBy(() -> entregaService.cancelarEntrega(id))
                    .isInstanceOf(EntregaNotFoundException.class)
                    .hasMessage(message);

        }

        @Test
        void deveGerarExcecao_QuandoCancelarEntrega_StatusEntregue() {
            var id = 4L;
            var message = "Você não pode cancelar uma entrega já finalizada.";

            assertThatThrownBy(() -> entregaService.cancelarEntrega(id))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(message);

        }

        @Test
        void devePermitirCancelarEntregaSemEntregador() {
            var id = 1L;

            var result = entregaService.cancelarEntrega(id);

            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(Entrega.class);
            assertThat(result.getStatus()).isEqualTo(StatusEntregaEnum.CANCELADA);
        }

    }
}
