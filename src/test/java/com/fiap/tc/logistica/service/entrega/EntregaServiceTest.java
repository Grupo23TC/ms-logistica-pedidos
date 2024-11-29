package com.fiap.tc.logistica.service.entrega;

import com.fiap.tc.logistica.dto.request.entregador.AtualizarEntregadorRequest;
import com.fiap.tc.logistica.dto.request.pedido.AtualizarRastreioRequest;
import com.fiap.tc.logistica.dto.request.pedido.AtualizarStatusPedidoRequest;
import com.fiap.tc.logistica.exception.EntregaNotFoundException;
import com.fiap.tc.logistica.exception.EntregadorNotFoundException;
import com.fiap.tc.logistica.exception.PedidoNotFoundException;
import com.fiap.tc.logistica.exception.RotaNotFoundException;
import com.fiap.tc.logistica.helper.EntregaHelper;
import com.fiap.tc.logistica.helper.EntregadorHelper;
import com.fiap.tc.logistica.helper.PedidoHelper;
import com.fiap.tc.logistica.helper.RotaHelper;
import com.fiap.tc.logistica.model.Entrega;
import com.fiap.tc.logistica.model.enums.StatusEntregaEnum;
import com.fiap.tc.logistica.model.rota.LocalizacaoRota;
import com.fiap.tc.logistica.model.rota.RotaResponse;
import com.fiap.tc.logistica.repository.EntregaRepository;
import com.fiap.tc.logistica.service.EntregadorService;
import com.fiap.tc.logistica.service.PedidoService;
import com.fiap.tc.logistica.service.RotaService;
import com.fiap.tc.logistica.service.impl.EntregaServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EntregaServiceTest {

    @Mock
    private EntregaRepository entregaRepository;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private EntregadorService entregadorService;

    @Mock
    private RotaService rotaService;

    @InjectMocks
    private EntregaServiceImpl entregaService;

    @Nested
    class BuscarEntrega {

        @Test
        void devePermitirBuscarEntregaPorId() {
            var entrega = EntregaHelper.gerarEntrega();
            var id = 1L;
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.of(entrega));

            var result = entregaService.buscarEntregaPorId(id);

            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(Entrega.class)
                    .isEqualTo(entrega);
            verify(entregaRepository, times(1)).findById(anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoBuscarEntregaPorId_IdNaoEncontrado() {
            var id = 1L;
            var message = "Entrega de id: " + id + " não encontrada.";
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> entregaService.buscarEntregaPorId(id))
                    .isInstanceOf(EntregaNotFoundException.class)
                    .hasMessage(message);
            verify(entregaRepository, times(1)).findById(anyLong());
        }
    }

    @Nested
    class CalcularRotaECriarEntrega {

        @Test
        void devePermitirCalcularRotaECriarEntrega() {
            var request = EntregaHelper.gerarCalcularEntregaRequest();

            when(entregaRepository.findByPedidoIdAndStatusNot(anyLong(), any(StatusEntregaEnum.class)))
                    .thenReturn(new ArrayList<>());

            var pedido = PedidoHelper.gerarPedido();
            when(pedidoService.buscarPedidoPorId(anyLong())).thenReturn(pedido);

            var rota = RotaHelper.gerarRotaResponseComplete();
            when(rotaService.calcularRota(any(LocalizacaoRota.class), any(LocalizacaoRota.class)))
                    .thenReturn(rota);

            var entrega = EntregaHelper.gerarEntrega();
            when(entregaRepository.save(any(Entrega.class))).thenReturn(entrega);

            var result = entregaService.calcularECriarEntrega(request);

            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(RotaResponse.class)
                    .isEqualTo(rota);

            verify(entregaRepository, times(1)).findByPedidoIdAndStatusNot(anyLong(), any(StatusEntregaEnum.class));
            verify(pedidoService, times(1)).buscarPedidoPorId(anyLong());
            verify(rotaService, times(1)).calcularRota(any(LocalizacaoRota.class), any(LocalizacaoRota.class));
            verify(entregaRepository, times(1)).save(any(Entrega.class));
        }

        @Test
        void deveGerarExcecao_QuandoCalcularRotaECriarEntrega_JaExisteEntregaPedido() {
            var request = EntregaHelper.gerarCalcularEntregaRequest();

            when(entregaRepository.findByPedidoIdAndStatusNot(anyLong(), any(StatusEntregaEnum.class)))
                    .thenReturn(Arrays.asList(EntregaHelper.gerarEntrega()));
            var message = "Já existe uma entrega com esse pedido";


            assertThatThrownBy(() -> entregaService.calcularECriarEntrega(request))
                    .isInstanceOf(IllegalStateException.class)
                            .hasMessage(message);

            verify(entregaRepository, times(1)).findByPedidoIdAndStatusNot(anyLong(), any(StatusEntregaEnum.class));
            verify(pedidoService, never()).buscarPedidoPorId(anyLong());
            verify(rotaService, never()).calcularRota(any(LocalizacaoRota.class), any(LocalizacaoRota.class));
            verify(entregaRepository, never()).save(any(Entrega.class));
        }

        @Test
        void deveGerarExcecao_QuandoCalcularRotaECriarEntrega_PedidoNaoEncontrado() {
            var request = EntregaHelper.gerarCalcularEntregaRequest();
            var message = "Pedido de id: " + request.pedidoId() + " não encontrado.";

            when(entregaRepository.findByPedidoIdAndStatusNot(anyLong(), any(StatusEntregaEnum.class)))
                    .thenReturn(new ArrayList<>());

            when(pedidoService.buscarPedidoPorId(anyLong()))
                    .thenThrow(new PedidoNotFoundException(message));

            assertThatThrownBy(() -> entregaService.calcularECriarEntrega(request))
                    .isInstanceOf(PedidoNotFoundException.class)
                    .hasMessage(message);

            verify(entregaRepository, times(1)).findByPedidoIdAndStatusNot(anyLong(), any(StatusEntregaEnum.class));
            verify(pedidoService, times(1)).buscarPedidoPorId(anyLong());
            verify(rotaService, never()).calcularRota(any(LocalizacaoRota.class), any(LocalizacaoRota.class));
            verify(entregaRepository, never()).save(any(Entrega.class));
        }

        @Test
        void deveGerarExcecao_QuandoCalcularRotaECriarEntrega_RotaNaoEncontrada() {
            var request = EntregaHelper.gerarCalcularEntregaRequest();
            var message = "Não foi possível calcular rota, revise as coordenadas.";
            when(entregaRepository.findByPedidoIdAndStatusNot(anyLong(), any(StatusEntregaEnum.class)))
                    .thenReturn(new ArrayList<>());

            var pedido = PedidoHelper.gerarPedido();
            when(pedidoService.buscarPedidoPorId(anyLong())).thenReturn(pedido);

            var rota = RotaHelper.gerarRotaResponseComplete();
            when(rotaService.calcularRota(any(LocalizacaoRota.class), any(LocalizacaoRota.class)))
                    .thenThrow(new RotaNotFoundException(message));

            assertThatThrownBy(() -> entregaService.calcularECriarEntrega(request))
                    .isInstanceOf(RotaNotFoundException.class)
                            .hasMessage(message);

            verify(entregaRepository, times(1)).findByPedidoIdAndStatusNot(anyLong(), any(StatusEntregaEnum.class));
            verify(pedidoService, times(1)).buscarPedidoPorId(anyLong());
            verify(rotaService, times(1)).calcularRota(any(LocalizacaoRota.class), any(LocalizacaoRota.class));
            verify(entregaRepository, never()).save(any(Entrega.class));
        }
    }

    @Nested
    class SolicitarEntrega {

        @Test
        void devePermitirSolicitarEntrega() {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.of(entrega));
            when(entregadorService.listarEntregadoresDisponiveis()).thenReturn(Arrays.asList(EntregadorHelper.gerarEntregador()));
            when(entregaRepository.save(any(Entrega.class))).thenReturn(entrega);

            var result = entregaService.solicitarEntrega(id);

            assertThat(result)
                    .isInstanceOf(Entrega.class)
                    .isEqualTo(entrega);
            assertThat(result.getEntregaId()).isEqualTo(id);
            assertThat(result.getStatus()).isEqualTo(StatusEntregaEnum.SOLICITADA);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregaRepository, times(1)).save(any(Entrega.class));
        }

        @Test
        void deveGerarExcecao_QuandoSolicitarEntrega_IdNaoEncontrado() {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            var message = "Entrega de id: " + id + " não encontrada.";
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.empty());
            when(entregaRepository.save(any(Entrega.class))).thenReturn(entrega);

            assertThatThrownBy(() -> entregaService.solicitarEntrega(id))
                    .isInstanceOf(EntregaNotFoundException.class)
                            .hasMessage(message);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregaRepository, never()).save(any(Entrega.class));
        }

        @Test
        void deveGerarExcecao_QuandoSolicitarEntrega_StatusNaoPendente() {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            entrega.setStatus(StatusEntregaEnum.SOLICITADA);
            var message = "A entrega precisa estar com status PENDENTE para ser solicitada.";
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.of(entrega));
            when(entregaRepository.save(any(Entrega.class))).thenReturn(entrega);

            assertThatThrownBy(() -> entregaService.solicitarEntrega(id))
            .isInstanceOf(IllegalStateException.class)
                    .hasMessage(message);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregaRepository, never()).save(any(Entrega.class));
        }

    }

    @Nested
    class AtribuirEntregadorParaEntrega {

        @Test
        void devePermitirAtribuirEntregadorParaEntrega() {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            entrega.setStatus(StatusEntregaEnum.SOLICITADA);
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.of(entrega));

            var entregador = EntregadorHelper.gerarEntregador();
            entregador.setEntregadorId(id);
            when(entregadorService.buscarEntregadorPorId(anyLong())).thenReturn(entregador);

            when(entregadorService.atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class)))
                    .thenReturn(entregador);
            var pedido = PedidoHelper.gerarPedido();
            when(pedidoService.atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class)))
                    .thenReturn(pedido);
            when(pedidoService.atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class)))
                    .thenReturn(pedido);
            when(entregaRepository.save(any(Entrega.class))).thenReturn(entrega);

            var result = entregaService.atribuirEntregadorAEntrega(id, id);

            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(Entrega.class);
            assertThat(result.getEntregaId()).isEqualTo(id);
            assertThat(result.getStatus()).isEqualTo(StatusEntregaEnum.ENVIADA);
            assertThat(result.getEntregador().getEntregadorId()).isEqualTo(entregador.getEntregadorId());

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregadorService, times(1)).buscarEntregadorPorId(anyLong());
            verify(entregadorService, times(1)).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
            verify(pedidoService, times(1)).atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class));
            verify(pedidoService, times(1)).atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class));
            verify(entregaRepository, times(1)).save(any(Entrega.class));
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_IdNaoEncontrado() {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            entrega.setStatus(StatusEntregaEnum.SOLICITADA);
            var message = "Entrega de id: " + id + " não encontrada.";
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> entregaService.atribuirEntregadorAEntrega(id, id))
                    .isInstanceOf(EntregaNotFoundException.class)
                    .hasMessage(message);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregadorService, never()).buscarEntregadorPorId(anyLong());
            verify(entregadorService, never()).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
            verify(pedidoService, never()).atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class));
            verify(pedidoService, never()).atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class));
            verify(entregaRepository, never()).save(any(Entrega.class));
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_StatusNaoSolicitada() {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            entrega.setStatus(StatusEntregaEnum.PENDENTE);
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.of(entrega));
            var message = "A entrega precisa estar com status SOLICITADA para ser atribuída.";

            assertThatThrownBy(() -> entregaService.atribuirEntregadorAEntrega(id, id))
            .isInstanceOf(IllegalStateException.class)
                    .hasMessage(message);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregadorService, never()).buscarEntregadorPorId(anyLong());
            verify(entregadorService, never()).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
            verify(pedidoService, never()).atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class));
            verify(pedidoService, never()).atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class));
            verify(entregaRepository, never()).save(any(Entrega.class));
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_EntregadorNaoEncontrado() {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            entrega.setStatus(StatusEntregaEnum.SOLICITADA);
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.of(entrega));

            var message = "Entregador de id: " + id + " não encontrado.";
            when(entregadorService.buscarEntregadorPorId(anyLong()))
                    .thenThrow(new EntregadorNotFoundException(message));

            assertThatThrownBy(() -> entregaService.atribuirEntregadorAEntrega(id, id))
                    .isInstanceOf(EntregadorNotFoundException.class)
                    .hasMessage(message);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregadorService, times(1)).buscarEntregadorPorId(anyLong());
            verify(entregadorService, never()).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
            verify(pedidoService, never()).atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class));
            verify(pedidoService, never()).atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class));
            verify(entregaRepository, never()).save(any(Entrega.class));
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_EntregadorNaoDisponivel() {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            entrega.setStatus(StatusEntregaEnum.SOLICITADA);
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.of(entrega));

            var entregador = EntregadorHelper.gerarEntregador();
            entregador.setEntregadorId(id);
            entregador.setEstaDisponivel(false);
            when(entregadorService.buscarEntregadorPorId(anyLong())).thenReturn(entregador);
            var message = "O Entregador: " + entregador.getNome() + " não está disponivel para realizar a entrega.";

            assertThatThrownBy(() -> entregaService.atribuirEntregadorAEntrega(id, id))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(message);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregadorService, times(1)).buscarEntregadorPorId(anyLong());
            verify(entregadorService, never()).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
            verify(pedidoService, never()).atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class));
            verify(pedidoService, never()).atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class));
            verify(entregaRepository, never()).save(any(Entrega.class));
        }

    }

    @Nested
    class FinalizarEntrega {

        @Test
        void devePermitirFinalizarEntrega() {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            entrega.setStatus(StatusEntregaEnum.ENVIADA);
            var entregador = EntregadorHelper.gerarEntregador();
            entregador.setEntregadorId(id);
            entrega.setEntregador(entregador);

            when(entregaRepository.findById(anyLong())).thenReturn(Optional.of(entrega));


            when(entregadorService.atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class)))
                    .thenReturn(entregador);

            when(entregaRepository.save(any(Entrega.class))).thenReturn(entrega);

            var result = entregaService.finalizarEntrega(id);

            assertThat(result)
                    .isInstanceOf(Entrega.class)
                    .isNotNull();
            assertThat(result.getStatus()).isEqualTo(StatusEntregaEnum.ENTREGUE);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregadorService, times(1)).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
            verify(entregaRepository, times(1)).save(any(Entrega.class));
        }

        @Test
        void deveGerarExcecao_QuandoFinalizarEntrega_IdNaoEncontrado() {
            var id = 1L;
            var message = "Entrega de id: " + id + " não encontrada.";
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> entregaService.finalizarEntrega(id))
                    .isInstanceOf(EntregaNotFoundException.class)
                            .hasMessage(message);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregadorService, never()).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
            verify(entregaRepository, never()).save(any(Entrega.class));
        }

        @Test
        void deveGerarExcecao_QuandoFinalizarEntrega_StatusNaoEnviada() {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            entrega.setStatus(StatusEntregaEnum.PENDENTE);
            var message = "A entrega precisa estar com status ENVIADA para ser finalizada.";
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.of(entrega));

            assertThatThrownBy(() -> entregaService.finalizarEntrega(id))
                    .isInstanceOf(IllegalStateException.class)
                            .hasMessage(message);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregadorService, never()).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
            verify(entregaRepository, never()).save(any(Entrega.class));
        }

    }

    @Nested
    class CancelarEntrega {

        @Test
        void devePermitirCancelarEntrega() {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            var entregador = EntregadorHelper.gerarEntregador();
            entregador.setEntregadorId(id);
            entregador.setEstaDisponivel(false);
            entrega.setEntregador(entregador);
            entrega.setStatus(StatusEntregaEnum.ENVIADA);

            when(entregaRepository.findById(anyLong())).thenReturn(Optional.of(entrega));
            when(entregadorService.atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class)))
                    .thenReturn(entregador);

            var pedido = PedidoHelper.gerarPedido();
            when(pedidoService.atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class)))
                    .thenReturn(pedido);
            when(pedidoService.atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class)))
                    .thenReturn(pedido);

            when(entregaRepository.save(any(Entrega.class))).thenReturn(entrega);

            var result = entregaService.cancelarEntrega(id);

            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(Entrega.class);
            assertThat(result.getStatus()).isEqualTo(StatusEntregaEnum.CANCELADA);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregadorService, times(1)).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
            verify(pedidoService, times(1)).atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class));
            verify(pedidoService, times(1)).atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class));
            verify(entregaRepository, times(1)).save(any(Entrega.class));
        }

        @Test
        void deveGerarExcecao_QuandoCancelarEntrega_IdNaoEncontrado() {
            var id = 1L;
            var message = "Entrega de id: " + id + " não encontrada.";
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> entregaService.cancelarEntrega(id))
                    .isInstanceOf(EntregaNotFoundException.class)
                            .hasMessage(message);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregadorService, never()).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
            verify(pedidoService, never()).atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class));
            verify(pedidoService, never()).atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class));
            verify(entregaRepository, never()).save(any(Entrega.class));
        }

        @Test
        void deveGerarExcecao_QuandoCancelarEntrega_StatusEntregue() {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setStatus(StatusEntregaEnum.ENTREGUE);
            var message = "Você não pode cancelar uma entrega já finalizada.";

            when(entregaRepository.findById(anyLong())).thenReturn(Optional.of(entrega));

            assertThatThrownBy(() -> entregaService.cancelarEntrega(id))
                    .isInstanceOf(IllegalStateException.class)
                            .hasMessage(message);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregadorService, never()).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
            verify(pedidoService, never()).atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class));
            verify(pedidoService, never()).atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class));
            verify(entregaRepository, never()).save(any(Entrega.class));
        }

        @Test
        void devePermitirCancelarEntregaSemEntregador() {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();

            entrega.setStatus(StatusEntregaEnum.SOLICITADA);

            when(entregaRepository.findById(anyLong())).thenReturn(Optional.of(entrega));

            when(entregaRepository.save(any(Entrega.class))).thenReturn(entrega);

            var result = entregaService.cancelarEntrega(id);

            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(Entrega.class);
            assertThat(result.getStatus()).isEqualTo(StatusEntregaEnum.CANCELADA);

            verify(entregaRepository, times(1)).findById(anyLong());
            verify(entregadorService, never()).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
            verify(pedidoService, never()).atualizarStatusPedido(anyLong(), any(AtualizarStatusPedidoRequest.class));
            verify(pedidoService, never()).atualizarRastreioPedido(anyLong(), any(AtualizarRastreioRequest.class));
            verify(entregaRepository, times(1)).save(any(Entrega.class));
        }

    }
}
