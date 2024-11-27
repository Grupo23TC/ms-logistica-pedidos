package com.fiap.tc.logistica.service.entregador.integration;

import com.fiap.tc.logistica.exception.EntregadorNotFoundException;
import com.fiap.tc.logistica.helper.EntregadorHelper;
import com.fiap.tc.logistica.model.Entregador;
import com.fiap.tc.logistica.service.EntregadorService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class EntregadorServiceIT {

    @Autowired
    private EntregadorService entregadorService;

    @Nested
    class CadastrarEntregador {

        @Test
        void devePermitirCadastrarEntregador() {
            var request = EntregadorHelper.gerarCadastrarEntregadorRequest();

            var result = entregadorService.cadastraEntregador(request);

            assertThat(result)
                    .isInstanceOf(Entregador.class)
                    .isNotNull();
            assertThat(result.getEstaDisponivel()).isTrue();
        }
    }

    @Nested
    class BuscarEntregador {

        @Test
        void devePermitirBuscarEntregadorPorId() {
            var id = 1L;
            var result = entregadorService.buscarEntregadorPorId(id);

            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(Entregador.class);
            assertThat(result.getEntregadorId()).isEqualTo(id);
        }

        @Test
        void deveGerarExcecao_QuandoBuscarEntregadorPorId_IdNaoEncontrado() {
            var id = 10000000L;
            var message = "Entregador de id: " + id + " não encontrado.";

            assertThatThrownBy(() ->  entregadorService.buscarEntregadorPorId(id))
                    .isInstanceOf(EntregadorNotFoundException.class)
                    .hasMessage(message);
        }

        @Test
        void devePermitirListarEntregadoresDisponiveis() {
            var result = entregadorService.listarEntregadoresDisponiveis();

            assertThat(result)
                    .isNotEmpty()
                    .hasSize(3);
        }
    }

    @Nested
    class AtualizarEntregador {

        @Test
        void devePermitirAtualizarStatusEntregador() {
            var request = EntregadorHelper.gerarAtualizarEntregadorRequest();
            var id = 3L;

            var result = entregadorService.atualizarStatusEntregador(id, request);

            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(Entregador.class);
            assertThat(result.getEstaDisponivel()).isEqualTo(request.estaDisponivel());
        }

        @Test
        void deveGerarExcecao_QuandoAtualizarEntregador_IdNaoEncontrado () {
            var id = 1000000000000L;
            var message = "Entregador de id: " + id + " não encontrado.";
            var request = EntregadorHelper.gerarAtualizarEntregadorRequest();

            assertThatThrownBy(() -> entregadorService.atualizarStatusEntregador(id, request))
                    .isInstanceOf(EntregadorNotFoundException.class)
                    .hasMessage(message);

        }
    }

    @Nested
    class DeletarEntregador {

        @Test
        void devePermitirDeletarEntregador() {
            var id = 5l;

            var result = entregadorService.removerEntregador(id);
            assertThat(result).isTrue();
        }

        @Test
        void deveGerarExcecao_QuandoRemoverEntregador_IdNaoEncontrado() {
            var id = 1000000000000L;
            var message = "Entregador de id: " + id + " não encontrado.";

            assertThatThrownBy(() -> entregadorService.removerEntregador(id))
                    .isInstanceOf(EntregadorNotFoundException.class)
                    .hasMessage(message);
        }
    }
}
