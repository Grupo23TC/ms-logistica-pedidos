package com.fiap.tc.logistica.controller.entrega;

import com.fiap.tc.logistica.controller.EntregaController;
import com.fiap.tc.logistica.dto.request.entrega.CalcularEntregaRequest;
import com.fiap.tc.logistica.exception.EntregaNotFoundException;
import com.fiap.tc.logistica.exception.EntregadorNotFoundException;
import com.fiap.tc.logistica.exception.PedidoNotFoundException;
import com.fiap.tc.logistica.exception.RotaNotFoundException;
import com.fiap.tc.logistica.handlers.ControllerExceptionHandler;
import com.fiap.tc.logistica.helper.*;
import com.fiap.tc.logistica.service.EntregaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class EntregaControllerTest {

    @Mock
    private EntregaService service;

    @InjectMocks
    private EntregaController controller;

    private MockMvc mockMvc;

    AutoCloseable mock;

    @BeforeEach
    public void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class BuscarEntrega {

        @Test
        void devePermitirBuscarEntregaPorId() throws Exception {
            var entrega = EntregaHelper.gerarEntrega();
            var id = 1L;
            entrega.setEntregaId(id);
            when(service.buscarEntregaPorId(anyLong())).thenReturn(entrega);

            mockMvc.perform(get("/entregas/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.entregaId").value(entrega.getEntregaId()));
            verify(service, times(1)).buscarEntregaPorId(anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoBuscarEntregaPorId_IdNaoEncontrado() throws Exception {
            var id = 1L;
            var message = "Entrega de id: " + id + " não encontrada.";
            when(service.buscarEntregaPorId(anyLong())).thenThrow(new EntregadorNotFoundException(message));

            mockMvc.perform(get("/entregas/{id}", id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/" + id));

            verify(service, times(1)).buscarEntregaPorId(anyLong());
        }
    }

    @Nested
    class CalcularRotaECriarEntrega {

        @Test
        void devePermitirCalcularRotaECriarEntrega() throws Exception {
            var request = EntregaHelper.gerarCalcularEntregaRequest();
            var rota = RotaHelper.gerarRotaResponseComplete();
            when(service.calcularECriarEntrega(any(CalcularEntregaRequest.class))).thenReturn(rota);

            mockMvc.perform(post("/entregas/calcularECriar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonStringHelper.asJsonString(request)))
                    .andExpect(status().isCreated());

            verify(service, times(1)).calcularECriarEntrega(any(CalcularEntregaRequest.class));
        }

        @Test
        void deveGerarExcecao_QuandoCalcularRotaECriarEntrega_JaExisteEntregaPedido() throws Exception {
            var request = EntregaHelper.gerarCalcularEntregaRequest();
            var message = "Já existe uma entrega com esse pedido";
            when(service.calcularECriarEntrega(any(CalcularEntregaRequest.class))).thenThrow(new IllegalStateException(message));

            mockMvc.perform(post("/entregas/calcularECriar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonStringHelper.asJsonString(request)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/calcularECriar"));

            verify(service, times(1)).calcularECriarEntrega(any(CalcularEntregaRequest.class));
        }

        @Test
        void deveGerarExcecao_QuandoCalcularRotaECriarEntrega_PedidoNaoEncontrado() throws Exception {
            var request = EntregaHelper.gerarCalcularEntregaRequest();
            var message = "Pedido de id: " + request.pedidoId() + " não encontrado.";

            when(service.calcularECriarEntrega(any(CalcularEntregaRequest.class))).thenThrow(new PedidoNotFoundException(message));

            mockMvc.perform(post("/entregas/calcularECriar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonStringHelper.asJsonString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/calcularECriar"));

            verify(service, times(1)).calcularECriarEntrega(any(CalcularEntregaRequest.class));
        }

        @Test
        void deveGerarExcecao_QuandoCalcularRotaECriarEntrega_RotaNaoEncontrada() throws Exception {
            var request = EntregaHelper.gerarCalcularEntregaRequest();
            var message = "Não foi possível calcular rota, revise as coordenadas.";
            when(service.calcularECriarEntrega(any(CalcularEntregaRequest.class))).thenThrow(new RotaNotFoundException(message));

            mockMvc.perform(post("/entregas/calcularECriar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonStringHelper.asJsonString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/calcularECriar"));

            verify(service, times(1)).calcularECriarEntrega(any(CalcularEntregaRequest.class));
        }
    }

    @Nested
    class SolicitarEntrega {

        @Test
        void devePermitirSolicitarEntrega() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            when(service.solicitarEntrega(anyLong())).thenReturn(entrega);

            mockMvc.perform(put("/entregas/solicitar/{id}", id))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.entregaId").value(entrega.getEntregaId()));

            verify(service, times(1)).solicitarEntrega(anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoSolicitarEntrega_IdNaoEncontrado() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            var message = "Entrega de id: " + id + " não encontrada.";
            when(service.solicitarEntrega(anyLong())).thenThrow(new EntregaNotFoundException(message));
            mockMvc.perform(put("/entregas/solicitar/{id}", id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/solicitar/" + id));

            verify(service, times(1)).solicitarEntrega(anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoSolicitarEntrega_StatusNaoPendente() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            var message = "A entrega precisa estar com status PENDENTE para ser solicitada.";
            when(service.solicitarEntrega(anyLong())).thenThrow(new IllegalStateException(message));
            mockMvc.perform(put("/entregas/solicitar/{id}", id))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/solicitar/" + id));

            verify(service, times(1)).solicitarEntrega(anyLong());
        }

    }

    @Nested
    class AtribuirEntregadorParaEntrega {

        @Test
        void devePermitirAtribuirEntregadorParaEntrega() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);

            when(service.atribuirEntregadorAEntrega(anyLong(), anyLong())).thenReturn(entrega);

            mockMvc.perform(put("/entregas/atribuirEntregador/{entregaId}/{entregadorId}", id, id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.entregaId").value(entrega.getEntregaId()));

            verify(service, times(1)).atribuirEntregadorAEntrega(anyLong(), anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_IdNaoEncontrado() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            var message = "Entrega de id: " + id + " não encontrada.";
            when(service.atribuirEntregadorAEntrega(anyLong(), anyLong())).thenThrow(new EntregaNotFoundException(message));

            mockMvc.perform(put("/entregas/atribuirEntregador/{entregaId}/{entregadorId}", id, id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/atribuirEntregador/" + id + "/" + id));

            verify(service, times(1)).atribuirEntregadorAEntrega(anyLong(), anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_StatusNaoSolicitada() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            var message = "A entrega precisa estar com status SOLICITADA para ser atribuída.";
            when(service.atribuirEntregadorAEntrega(anyLong(), anyLong())).thenThrow(new IllegalStateException(message));

            mockMvc.perform(put("/entregas/atribuirEntregador/{entregaId}/{entregadorId}", id, id))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/atribuirEntregador/" + id + "/" + id));

            verify(service, times(1)).atribuirEntregadorAEntrega(anyLong(), anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_EntregadorNaoEncontrado() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            var message = "Entregador de id: " + id + " não encontrado.";
            when(service.atribuirEntregadorAEntrega(anyLong(), anyLong())).thenThrow(new EntregadorNotFoundException(message));

            mockMvc.perform(put("/entregas/atribuirEntregador/{entregaId}/{entregadorId}", id, id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/atribuirEntregador/" + id + "/" + id));

            verify(service, times(1)).atribuirEntregadorAEntrega(anyLong(), anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_EntregadorNaoDisponivel() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            var message = "O Entregador: Teste não está disponivel para realizar a entrega.";
            when(service.atribuirEntregadorAEntrega(anyLong(), anyLong())).thenThrow(new IllegalStateException(message));

            mockMvc.perform(put("/entregas/atribuirEntregador/{entregaId}/{entregadorId}", id, id))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/atribuirEntregador/" + id + "/" + id));

            verify(service, times(1)).atribuirEntregadorAEntrega(anyLong(), anyLong());
        }

    }

    @Nested
    class FinalizarEntrega {

        @Test
        void devePermitirFinalizarEntrega() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            when(service.finalizarEntrega(anyLong())).thenReturn(entrega);

            mockMvc.perform(put("/entregas/finalizar/{id}", id))
                            .andExpect(status().isOk())
                                    .andExpect(jsonPath("$.entregaId").value(entrega.getEntregaId()));

            verify(service, times(1)).finalizarEntrega(anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoFinalizarEntrega_IdNaoEncontrado() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            var message = "Entrega de id: " + id + " não encontrada.";
            when(service.finalizarEntrega(anyLong())).thenThrow(new EntregaNotFoundException(message));

            mockMvc.perform(put("/entregas/finalizar/{id}", id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/finalizar/" + id));

            verify(service, times(1)).finalizarEntrega(anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoFinalizarEntrega_StatusNaoEnviada() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            var message = "A entrega precisa estar com status ENVIADA para ser finalizada.";
            when(service.finalizarEntrega(anyLong())).thenThrow(new IllegalStateException(message));

            mockMvc.perform(put("/entregas/finalizar/{id}", id))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/finalizar/" + id));

            verify(service, times(1)).finalizarEntrega(anyLong());
        }

    }

    @Nested
    class CancelarEntrega {

        @Test
        void devePermitirCancelarEntrega() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            when(service.cancelarEntrega(anyLong())).thenReturn(entrega);

            mockMvc.perform(put("/entregas/cancelar/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.entregaId").value(entrega.getEntregaId()));

            verify(service, times(1)).cancelarEntrega(anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoCancelarEntrega_IdNaoEncontrado() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            var message = "Entrega de id: " + id + " não encontrada.";
            when(service.cancelarEntrega(anyLong())).thenThrow(new EntregaNotFoundException(message));

            mockMvc.perform(put("/entregas/cancelar/{id}", id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/cancelar/" + id));

            verify(service, times(1)).cancelarEntrega(anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoCancelarEntrega_StatusEntregue() throws Exception {
            var id = 1L;
            var entrega = EntregaHelper.gerarEntrega();
            entrega.setEntregaId(id);
            var message = "Você não pode cancelar uma entrega já finalizada.";
            when(service.cancelarEntrega(anyLong())).thenThrow(new IllegalStateException(message));

            mockMvc.perform(put("/entregas/cancelar/{id}", id))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregas/cancelar/" + id));

            verify(service, times(1)).cancelarEntrega(anyLong());
        }
    }
}
