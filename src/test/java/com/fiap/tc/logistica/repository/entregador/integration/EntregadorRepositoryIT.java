package com.fiap.tc.logistica.repository.entregador.integration;

import com.fiap.tc.logistica.helper.EntregadorHelper;
import com.fiap.tc.logistica.model.Entregador;
import com.fiap.tc.logistica.repository.EntregadorRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class EntregadorRepositoryIT {

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Test
    void devePermitirCriarTabela() {
        var totalDeRegistros = entregadorRepository.count();
        assertThat(totalDeRegistros).isPositive();
    }

    @Nested
    class CadastrarEntregador {

        @Test
        void devePermitirCadastrarEntregador() {
            var entregador = EntregadorHelper.gerarEntregador();
            var result = entregadorRepository.save(entregador);
            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(Entregador.class);
            assertThat(result.getEntregadorId()).isPositive();
            assertThat(result.getNome()).isEqualTo(entregador.getNome());
            assertThat(result.getEmail()).isEqualTo(entregador.getEmail());
            assertThat(result.getTelefone()).isEqualTo(entregador.getTelefone());
            assertThat(result.getCpf()).isEqualTo(entregador.getCpf());
            assertThat(result.getEstaDisponivel()).isEqualTo(entregador.getEstaDisponivel());
        }
    }

    @Nested
    class BuscarEntregador {

        @Test
        void devePermitirBuscarEntregadorPorId() {
            var entregadorOpt = entregadorRepository.findById(1L);
            assertThat(entregadorOpt).isPresent();
            entregadorOpt.ifPresent(entregador -> {
                assertThat(entregador.getEntregadorId()).isEqualTo(1L);
            });
        }

        @Test
        void devePermitirBuscarEntregadoresDisponiveis() {
            var entregadoresList = entregadorRepository.buscarEntregadoresDisponiveis();
            assertThat(entregadoresList)
                    .isNotEmpty()
                    .hasSize(3);
        }
    }

    @Nested
    class DeletarEntregador {

        @Test
        void devePermitirDeletarEntregador() {
            var id = 5l;
            entregadorRepository.deleteById(id);
            assertThat(entregadorRepository.findById(id)).isNotPresent();
        }
    }
}
