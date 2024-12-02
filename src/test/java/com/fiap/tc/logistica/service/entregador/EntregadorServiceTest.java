package com.fiap.tc.logistica.service.entregador;

import com.fiap.tc.logistica.exception.EntregadorNotFoundException;
import com.fiap.tc.logistica.helper.EntregadorHelper;
import com.fiap.tc.logistica.model.Entregador;
import com.fiap.tc.logistica.repository.EntregadorRepository;
import com.fiap.tc.logistica.service.impl.EntregadorServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EntregadorServiceTest {

    @Mock
    private EntregadorRepository entregadorRepository;

    @InjectMocks
    private EntregadorServiceImpl entregadorService;

    @Nested
    class CadastrarEntregador {

        @Test
        void devePermitirCadastrarEntregador() {
            var entregador = EntregadorHelper.gerarEntregador();
            when(entregadorRepository.save(any(Entregador.class))).thenReturn(entregador);
            var request = EntregadorHelper.gerarCadastrarEntregadorRequest();

            var result = entregadorService.cadastraEntregador(request);

            assertThat(result)
                    .isInstanceOf(Entregador.class)
                    .isNotNull();
            assertThat(result.getEstaDisponivel()).isTrue();
            verify(entregadorRepository, times(1)).save(any(Entregador.class));
        }
    }

    @Nested
    class BuscarEntregador {

        @Test
        void devePermitirBuscarEntregadorPorId() {
            var entregador = EntregadorHelper.gerarEntregador();
            when(entregadorRepository.findById(anyLong())).thenReturn(Optional.of(entregador));

            var result = entregadorService.buscarEntregadorPorId(1l);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(entregador);
            verify(entregadorRepository, times(1)).findById(anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoBuscarEntregadorPorId_IdNaoEncontrado() {
            var id = 1L;
            var message = "Entregador de id: " + id + " não encontrado.";
            when(entregadorRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThatThrownBy(() ->  entregadorService.buscarEntregadorPorId(id))
                    .isInstanceOf(EntregadorNotFoundException.class)
                            .hasMessage(message);
            verify(entregadorRepository, times(1)).findById(anyLong());
        }

        @Test
        void devePermitirListarEntregadoresDisponiveis() {
            var entregador = EntregadorHelper.gerarEntregador();
            var entregador2 = EntregadorHelper.gerarEntregador();
            var listEntregadores = Arrays.asList(entregador,entregador2);
            when(entregadorRepository.buscarEntregadoresDisponiveis()).thenReturn(listEntregadores);

            var result = entregadorService.listarEntregadoresDisponiveis();

            assertThat(result)
                    .isNotEmpty()
                    .hasSize(2)
                    .containsExactlyInAnyOrder(entregador,entregador2);
            verify(entregadorRepository, times(1)).buscarEntregadoresDisponiveis();
        }
    }

    @Nested
    class AtualizarEntregador {

        @Test
        void devePermitirAtualizarStatusEntregador() {
            var request = EntregadorHelper.gerarAtualizarEntregadorRequest();
            var id = 1L;
            var entregador = EntregadorHelper.gerarEntregador();
            when(entregadorRepository.findById(anyLong())).thenReturn(Optional.of(entregador));
            when(entregadorRepository.save(any(Entregador.class))).thenReturn(entregador);

            var result = entregadorService.atualizarStatusEntregador(id, request);

            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(Entregador.class);
            assertThat(result.getEstaDisponivel()).isEqualTo(request.estaDisponivel());

            verify(entregadorRepository, times(1)).findById(anyLong());
            verify(entregadorRepository, times(1)).save(any(Entregador.class));
        }

        @Test
        void deveGerarExcecao_QuandoAtualizarEntregador_IdNaoEncontrado () {
            var id = 1L;
            var message = "Entregador de id: " + id + " não encontrado.";
            var request = EntregadorHelper.gerarAtualizarEntregadorRequest();
            when(entregadorRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> entregadorService.atualizarStatusEntregador(id, request))
                    .isInstanceOf(EntregadorNotFoundException.class)
                    .hasMessage(message);

            verify(entregadorRepository, times(1)).findById(anyLong());
            verify(entregadorRepository, never()).save(any(Entregador.class));
        }
    }

    @Nested
    class DeletarEntregador {

        @Test
        void devePermitirDeletarEntregador() {
            var id = 1l;
            var entregador = EntregadorHelper.gerarEntregador();
            when(entregadorRepository.findById(anyLong())).thenReturn(Optional.of(entregador));
            doNothing().when(entregadorRepository).deleteById(anyLong());

            var result = entregadorService.removerEntregador(id);

            assertThat(result).isTrue();

            verify(entregadorRepository, times(1)).findById(anyLong());
            verify(entregadorRepository, times(1)).deleteById(anyLong());
        }

        @Test
        void deveGerarExcecao_QuandoRemoverEntregador_IdNaoEncontrado() {
            var id = 1L;
            var message = "Entregador de id: " + id + " não encontrado.";
            when(entregadorRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> entregadorService.removerEntregador(id))
                    .isInstanceOf(EntregadorNotFoundException.class)
                    .hasMessage(message);

            verify(entregadorRepository, times(1)).findById(anyLong());
            verify(entregadorRepository, never()).deleteById(anyLong());
        }
    }
}
