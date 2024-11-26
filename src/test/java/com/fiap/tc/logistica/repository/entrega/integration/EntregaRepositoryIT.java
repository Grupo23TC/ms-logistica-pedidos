package com.fiap.tc.logistica.repository.entrega.integration;

import com.fiap.tc.logistica.helper.EntregaHelper;
import com.fiap.tc.logistica.helper.EntregadorHelper;
import com.fiap.tc.logistica.model.Entrega;
import com.fiap.tc.logistica.model.Entregador;
import com.fiap.tc.logistica.model.enums.StatusEntregaEnum;
import com.fiap.tc.logistica.repository.EntregaRepository;
import com.fiap.tc.logistica.repository.EntregadorRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class EntregaRepositoryIT {

    @Autowired
    private EntregaRepository entregaRepository;

    @Test
    void devePermitirCriarTabela() {
        var totalDeRegistros = entregaRepository.count();
        assertThat(totalDeRegistros).isPositive();
    }

    @Nested
    class CadastrarEntrega {

        @Test
        void devePermitirCadastrarEntrega() {
            var entrega = EntregaHelper.gerarEntrega();
            var result = entregaRepository.save(entrega);

            assertThat(result)
                    .isInstanceOf(Entrega.class)
                    .isNotNull();
            assertThat(result.getEntregaId()).isPositive();
            assertThat(result.getPedidoId()).isEqualTo(entrega.getPedidoId());
            assertThat(result.getStatus()).isEqualTo(entrega.getStatus());
            assertThat(result.getInicioEstimado()).isEqualTo(entrega.getInicioEstimado());
            assertThat(result.getFimEstimado()).isEqualTo(entrega.getFimEstimado());
        }
    }

    @Nested
    class BuscarEntrega {

        @Test
        void devePermitirBuscarEntregaPorId() {
            var result = entregaRepository.findById(1l);

            assertThat(result).isPresent();
            result.ifPresent(entrega -> {
                assertThat(entrega.getEntregaId()).isEqualTo(1l);
            });
        }

        @Test
        void devePermitirBuscarPorPedidoEStatusNot() {
            var result = entregaRepository.findByPedidoIdAndStatusNot(1L, StatusEntregaEnum.CANCELADA);

            assertThat(result)
                    .isNotEmpty()
                    .hasSize(1);
        }

    }

    @Nested
    class DeletarEntrega {

        @Test
        void devePermitirDeletarEntrega() {
            var id = 5l;
            entregaRepository.deleteById(id);
            assertThat(entregaRepository.findById(id)).isNotPresent();
        }
    }
}
