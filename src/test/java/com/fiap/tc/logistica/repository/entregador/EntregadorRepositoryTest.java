package com.fiap.tc.logistica.repository.entregador;

import com.fiap.tc.logistica.helper.EntregadorHelper;
import com.fiap.tc.logistica.model.Entregador;
import com.fiap.tc.logistica.repository.EntregadorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EntregadorRepositoryTest {

    @Mock
    private EntregadorRepository entregadorRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class CadastrarEntregador {

        @Test
        void devePermitirCadastrarEntregador() {
            var entregador = EntregadorHelper.gerarEntregador();
            when(entregadorRepository.save(any(Entregador.class))).thenReturn(entregador);

            var result = entregadorRepository.save(entregador);

            assertThat(result)
                    .isInstanceOf(Entregador.class)
                    .isEqualTo(entregador);
            verify(entregadorRepository, times(1)).save(any(Entregador.class));
        }
    }

    @Nested
    class BuscarEntregador {

        @Test
        void devePermitirBuscarEntregadorPorId() {
            var entregador = EntregadorHelper.gerarEntregador();
            when(entregadorRepository.findById(anyLong())).thenReturn(Optional.of(entregador));

            var result = entregadorRepository.findById(1l);

            assertThat(result)
                    .isPresent()
                    .contains(entregador);
            verify(entregadorRepository, times(1)).findById(anyLong());
        }

        @Test
        void devePermitirBuscarEntregadoresDisponiveis() {
            var entregador = EntregadorHelper.gerarEntregador();
            var entregador2 = EntregadorHelper.gerarEntregador();
            var listEntregadores = Arrays.asList(entregador,entregador2);
            when(entregadorRepository.buscarEntregadoresDisponiveis()).thenReturn(listEntregadores);

            var result = entregadorRepository.buscarEntregadoresDisponiveis();

            assertThat(result)
                    .isNotEmpty()
                    .hasSize(2)
                    .containsExactlyInAnyOrder(entregador,entregador2);
            verify(entregadorRepository, times(1)).buscarEntregadoresDisponiveis();
        }
    }

    @Nested
    class DeletarEntregador {

        @Test
        void devePermitirDeletarEntregador() {
            doNothing().when(entregadorRepository).deleteById(anyLong());
            entregadorRepository.deleteById(1l);
            verify(entregadorRepository, times(1)).deleteById(anyLong());
        }
    }
}
