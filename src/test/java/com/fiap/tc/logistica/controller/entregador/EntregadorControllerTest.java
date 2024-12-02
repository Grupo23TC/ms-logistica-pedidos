package com.fiap.tc.logistica.controller.entregador;

import com.fiap.tc.logistica.controller.EntregadorController;
import com.fiap.tc.logistica.dto.request.entregador.AtualizarEntregadorRequest;
import com.fiap.tc.logistica.dto.request.entregador.CadastrarEntregadorRequest;
import com.fiap.tc.logistica.exception.EntregadorNotFoundException;
import com.fiap.tc.logistica.handlers.ControllerExceptionHandler;
import com.fiap.tc.logistica.helper.EntregadorHelper;
import com.fiap.tc.logistica.helper.JsonStringHelper;
import com.fiap.tc.logistica.service.EntregadorService;
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

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class EntregadorControllerTest {

    @Mock
    private EntregadorService service;

    @InjectMocks
    private EntregadorController controller;

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
    class CadastrarEntregador {

        @Test
        void devePermitirCadastrarEntregador() throws Exception {
            var entregador = EntregadorHelper.gerarEntregador();
            when(service.cadastraEntregador(any(CadastrarEntregadorRequest.class))).thenReturn(entregador);
            var request = EntregadorHelper.gerarCadastrarEntregadorRequest();

            mockMvc.perform(post("/entregadores")
                    .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonStringHelper.asJsonString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.entregadorId").value(entregador.getEntregadorId()))
                    .andExpect(jsonPath("$.nome").value(entregador.getNome()))
                    .andExpect(jsonPath("$.cpf").value(entregador.getCpf()))
                    .andExpect(jsonPath("$.email").value(entregador.getEmail()))
                    .andExpect(jsonPath("$.telefone").value(entregador.getTelefone()))
                    .andExpect(jsonPath("$.estaDisponivel").value(entregador.getEstaDisponivel()));

            verify(service, times(1)).cadastraEntregador(any(CadastrarEntregadorRequest.class));
        }
    }

    @Nested
    class BuscarEntregador {

        @Test
        void devePermitirBuscarEntregadorPorId() throws Exception {
            var id = 1L;
            var entregador = EntregadorHelper.gerarEntregador();
            entregador.setEntregadorId(id);
            when(service.buscarEntregadorPorId(anyLong())).thenReturn(entregador);

            mockMvc.perform(get("/entregadores/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.entregadorId").value(entregador.getEntregadorId()));

            verify(service, times(1)).buscarEntregadorPorId(anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoBuscarEntregadorPorId_IdNaoEncontrado() throws Exception {
            var id = 1L;
            var message = "Entregador de id: " + id + " não encontrado.";
            when(service.buscarEntregadorPorId(anyLong())).thenThrow(new EntregadorNotFoundException(message));
            mockMvc.perform(get("/entregadores/{id}", id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregadores/" + id));

            verify(service, times(1)).buscarEntregadorPorId(anyLong());
        }

        @Test
        void devePermitirListarEntregadoresDisponiveis() throws Exception {
            var entregador = EntregadorHelper.gerarEntregador();
            var entregador2 = EntregadorHelper.gerarEntregador();
            var listEntregadores = Arrays.asList(entregador,entregador2);
            when(service.listarEntregadoresDisponiveis()).thenReturn(listEntregadores);

            mockMvc.perform(get("/entregadores/disponiveis"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(listEntregadores.size()));

            verify(service, times(1)).listarEntregadoresDisponiveis();
        }
    }

    @Nested
    class AtualizarEntregador {

        @Test
        void devePermitirAtualizarStatusEntregador() throws Exception {
            var request = EntregadorHelper.gerarAtualizarEntregadorRequest();
            var id = 1L;
            var entregador = EntregadorHelper.gerarEntregador();
            when(service.atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class))).thenReturn(entregador);

            mockMvc.perform(put("/entregadores/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonStringHelper.asJsonString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.entregadorId").value(entregador.getEntregadorId()))
                    .andExpect(jsonPath("$.nome").value(entregador.getNome()))
                    .andExpect(jsonPath("$.cpf").value(entregador.getCpf()))
                    .andExpect(jsonPath("$.email").value(entregador.getEmail()))
                    .andExpect(jsonPath("$.telefone").value(entregador.getTelefone()))
                    .andExpect(jsonPath("$.estaDisponivel").value(entregador.getEstaDisponivel()));

            verify(service, times(1)).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
        }

        @Test
        void deveGerarExcecao_QuandoAtualizarEntregador_IdNaoEncontrado () throws Exception {
            var id = 1L;
            var message = "Entregador de id: " + id + " não encontrado.";
            var request = EntregadorHelper.gerarAtualizarEntregadorRequest();
            when(service.atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class)))
                    .thenThrow(new EntregadorNotFoundException(message));

            mockMvc.perform(put("/entregadores/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonStringHelper.asJsonString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregadores/" + id));

            verify(service, times(1)).atualizarStatusEntregador(anyLong(), any(AtualizarEntregadorRequest.class));
        }
    }

    @Nested
    class DeletarEntregador {

        @Test
        void devePermitirDeletarEntregador() throws Exception {
            var id = 1l;
            when(service.removerEntregador(anyLong())).thenReturn(true);

            mockMvc.perform(delete("/entregadores/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));

            verify(service, times(1)).removerEntregador(anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoRemoverEntregador_IdNaoEncontrado() throws Exception {
            var id = 1L;
            var message = "Entregador de id: " + id + " não encontrado.";
            when(service.removerEntregador(anyLong())).thenThrow(new EntregadorNotFoundException(message));

            mockMvc.perform(delete("/entregadores/{id}", id))

                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.erro").value(message))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.horario").exists())
                    .andExpect(jsonPath("$.rota").value("/entregadores/" + id));

            verify(service, times(1)).removerEntregador(anyLong());
        }
    }
}
