package com.fiap.tc.logistica.repository.entrega;

import com.fiap.tc.logistica.helper.EntregaHelper;
import com.fiap.tc.logistica.model.Entrega;
import com.fiap.tc.logistica.model.enums.StatusEntregaEnum;
import com.fiap.tc.logistica.repository.EntregaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EntregaRepositoryTest {

    @Mock
    private EntregaRepository entregaRepository;

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
    class CadastrarEntrega {

        @Test
        void devePermitirCadastrarEntrega() {
            var entrega = EntregaHelper.gerarEntrega();
            when(entregaRepository.save(any(Entrega.class))).thenReturn(entrega);

            var result = entregaRepository.save(entrega);

            assertThat(result)
                    .isInstanceOf(Entrega.class)
                    .isEqualTo(entrega);
            verify(entregaRepository, times(1)).save(any(Entrega.class));
        }
    }

    @Nested
    class BuscarEntrega {

        @Test
        void devePermitirBuscarEntregaPorId() {
            var entrega = EntregaHelper.gerarEntrega();
            when(entregaRepository.findById(anyLong())).thenReturn(Optional.of(entrega));

            var result = entregaRepository.findById(1l);

            assertThat(result)
                    .isPresent()
                    .contains(entrega);
            verify(entregaRepository, times(1)).findById(anyLong());
        }

        @Test
        void devePermitirBuscarPorPedidoEStatusNot() {
            var entrega = EntregaHelper.gerarEntrega();
            var entrega2 = EntregaHelper.gerarEntrega();
            List<Entrega> entregaList = Arrays.asList(entrega, entrega2);
            when(entregaRepository.findByPedidoIdAndStatusNot(anyLong(), any(StatusEntregaEnum.class))).thenReturn(entregaList);

            var result = entregaRepository.findByPedidoIdAndStatusNot(1L, StatusEntregaEnum.CANCELADA);

            assertThat(result)
                    .isNotEmpty()
                    .hasSize(2)
                    .containsExactlyInAnyOrder(entrega, entrega2);
            verify(entregaRepository, times(1)).findByPedidoIdAndStatusNot(anyLong(), any(StatusEntregaEnum.class));
        }

    }

    @Nested
    class DeletarEntrega {

        @Test
        void devePermitirDeletarEntrega() {
            doNothing().when(entregaRepository).deleteById(anyLong());
            entregaRepository.deleteById(1l);
            verify(entregaRepository, times(1)).deleteById(anyLong());
        }
    }
}
